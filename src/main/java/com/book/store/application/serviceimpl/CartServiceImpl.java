package com.book.store.application.serviceimpl;

import com.book.store.application.entity.Book;
import com.book.store.application.entity.Cart;
import com.book.store.application.entity.Customer;
import com.book.store.application.exception.BookNotExistException;
import com.book.store.application.exception.CartNotExistException;
import com.book.store.application.exception.CustomerNotExistException;
import com.book.store.application.mapper.BookMapper;
import com.book.store.application.mapper.CartMapper;
import com.book.store.application.repository.BookRepository;
import com.book.store.application.repository.CartRepository;
import com.book.store.application.repository.CustomerRepository;
import com.book.store.application.repository.UserRepository;
import com.book.store.application.requestdto.CartRequest;
import com.book.store.application.responsedto.CartBookResponse;
import com.book.store.application.service.CartService;
import com.book.store.application.util.ResponseStructure;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {


    private final BookMapper bookMapper;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final BookRepository bookRepository;

//    ------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public ResponseEntity<ResponseStructure<CartBookResponse>> addBookInCart(CartRequest cartRequest, Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotExistException("Customer Id: " + customerId + ", does not exist"));

        List<Cart> carts = customer.getCarts();
        Cart existingCart = null;
        Cart newCart = null;

        // Check if the product is already in the customer's cart
        if (carts != null) {
            for (Cart cart : carts) {
                if (cart.getBook().getBookid().equals(cartRequest.getBook().getBookid())) {
                    existingCart = cart;
                    break;
                }
            }
        }

        if (existingCart != null) {
            // Update the quantity of the existing cart
            existingCart.setSelectedQuantity(existingCart.getSelectedQuantity() + cartRequest.getSelectedQuantity());
            cartRepository.save(existingCart);
        } else {
            // Map the cart request to a new cart
            newCart = cartMapper.mapCartRequestToCart(cartRequest, new Cart());

            // Fetch the book by ID instead of saving it directly (better practice)
            System.out.println(cartRequest.getBook().getBookid() + " ---------------------------------------");
            Book book = bookRepository.findById(cartRequest.getBook().getBookid())
                    .orElseThrow(() -> new BookNotExistException("Book with ID: " + cartRequest.getBook().getBookid() + " does not exist"));

            newCart.setBook(book);
            newCart = cartRepository.save(newCart);

            // If the customer has no carts, initialize the list; otherwise, add the new cart to the existing list
            if (carts == null) {
                customer.setCarts(List.of(newCart));
            } else {
                carts.add(newCart);
            }
        }

        // Save the updated customer with the modified cart
        customerRepository.save(customer);

        // Map the cart to a response DTO
        CartBookResponse cartBookResponse = cartMapper.mapCartToCartResponse(existingCart != null ? existingCart : newCart);

        // Return the response with the appropriate HTTP status
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseStructure<CartBookResponse>()
                        .setStatus(HttpStatus.CREATED.value())
                        .setMessage("Cart is created or updated successfully")
                        .setData(cartBookResponse));
    }


//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity<ResponseStructure<CartBookResponse>> updateCart(int selectedQuantity, Long cartId) {

        // Find the cart by ID or throw an exception if not found
        Cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                new CartNotExistException("Cart Id : " + cartId + ", does not exist"));

        // Validate the selectedQuantity (e.g., it should be a positive number)
        if (selectedQuantity <= 0) {
            throw new IllegalArgumentException("Selected quantity must be greater than 0");
        }

        // Update the cart quantity and save it
        cart.setSelectedQuantity(selectedQuantity);
        cart = cartRepository.save(cart);

        // Create a response structure with the updated cart
        CartBookResponse cartBookResponse = cartMapper.mapCartToCartResponse(cart);

        // Return the response with HTTP status OK (200)
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseStructure<CartBookResponse>()
                        .setStatus(HttpStatus.OK.value())
                        .setMessage("Cart book is updated successfully")
                        .setData(cartBookResponse));
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity<ResponseStructure<CartBookResponse>> removeCartProduct(Long customerId, Long cartId) {
        // Find customer and cart by their IDs
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotExistException(
                        "Customer Id: " + customerId + ", does not exist"));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotExistException(
                        "Cart Id: " + cartId + ", does not exist"));

        // Map the cart to CartBookResponse before deletion
        CartBookResponse cartBookResponse = cartMapper.mapCartToCartResponse(cart);

        // Remove the cart from the customer's list of carts
        customer.getCarts().remove(cart);
        customerRepository.save(customer);

        // Delete the cart from the repository
        cartRepository.delete(cart);

        // Return response after deletion
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<CartBookResponse>()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Cart book is deleted")
                .setData(cartBookResponse));
    }

    //----------------------------------------------------------------------------------------------------------------------------
    @Override
    public ResponseEntity<ResponseStructure<String>> removeAllCartProduct(Long customerId) {
        // Find the customer by ID or throw an exception if not found
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotExistException(
                        "Customer Id: " + customerId + ", does not exist"));

        // Fetch all carts linked to the customer
        List<Cart> carts = customer.getCarts();

        // Clear the cart list from the customer
        customer.getCarts().clear();
        customerRepository.save(customer);

        // Delete all carts in one operation
        cartRepository.deleteAll(carts);

        // Return a success response
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<String>()
                .setStatus(HttpStatus.OK.value())
                .setMessage("All Books have been removed from Cart")
                .setData("Successfully removed all books"));
    }

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity<ResponseStructure<List<CartBookResponse>>> getCartProducts(Long customerId) {
        // Fetch customer and validate existence
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotExistException("Customer Id : " + customerId + " does not exist"));

        // Get the list of carts
        List<Cart> carts = customer.getCarts();

        // Return a proper response if no carts are found
        if (carts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<List<CartBookResponse>>()
                    .setStatus(HttpStatus.OK.value())
                    .setMessage("No cart found")
                    .setData(Collections.emptyList()));
        }

        // Validate each cart's book existence in the book repository
        List<CartBookResponse> cartResponses = carts.stream()
                .map(cart -> {
                    Book book = bookRepository.findById(cart.getBook().getBookid())
                            .orElseThrow(() -> new BookNotExistException("Book Id : " + cart.getBook().getBookid() + " does not exist"));

                    // Set the validated book (if book is updated, you can save cart if needed)
                    cart.setBook(book);

                    return cartMapper.mapCartToCartResponse(cart);
                }).toList();

        // Return the list of cart book responses
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<List<CartBookResponse>>()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Cart books found")
                .setData(cartResponses));
    }

}
