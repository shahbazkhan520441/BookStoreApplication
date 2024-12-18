package com.book.store.application.serviceimpl;

import com.book.store.application.entity.*;
import com.book.store.application.exception.*;
import com.book.store.application.mapper.BookMapper;
import com.book.store.application.mapper.OrderMapper;
import com.book.store.application.repository.*;
import com.book.store.application.requestdto.OrderRequest;
import com.book.store.application.responsedto.OrderResponseDto;
import com.book.store.application.service.OrderService;
import com.book.store.application.service.UserService;
import com.book.store.application.util.ResponseStructure;
import org.springframework.http.ResponseEntity;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final BookRepository bookRepository; // Assuming this exists
    private final OrderMapper orderMapper;
    private final BookMapper bookMapper;
    private final UserService userService;
    private final CartRepository cartRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository, AddressRepository addressRepository, BookRepository bookRepository, OrderMapper orderMapper, UserService userService,CartRepository cartRepository,BookMapper bookMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.bookRepository = bookRepository;
        this.orderMapper = orderMapper;
        this.userService = userService;
        this.cartRepository = cartRepository;
        this.bookMapper = bookMapper;
    }

    // Remove WebClient

//    ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
@Override
public ResponseEntity<ResponseStructure<List<OrderResponseDto>>> findPurchaseOrders(Long customerId) {
    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotExistException("Customer Id : " + customerId + ", does not exist"));
    List<OrderResponseDto> listOrders = orderRepository
            .findByCustomer(customer)
            .stream()
            .map(order -> orderMapper.mapOrderToOrderResponseDto(order))
            .toList();

    if (listOrders.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                new ResponseStructure<List<OrderResponseDto>>()
                        .setStatus(HttpStatus.NO_CONTENT.value())
                        .setMessage("No Purchase Orders Found")
                        .setData(listOrders) // Empty list in this case
        );
    }

    return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseStructure<List<OrderResponseDto>>()
                    .setStatus(HttpStatus.OK.value())
                    .setMessage("Purchase Orders Found")
                    .setData(listOrders)
    );
}

    @Override
    public ResponseEntity<ResponseStructure<OrderResponseDto>> findPurchaseOrder(Long orderId) {
        return orderRepository.findById(orderId).map(order -> {
            // Creating and setting response structure details for a successful response
            ResponseStructure<OrderResponseDto> responseStructure = new ResponseStructure<>();
            responseStructure.setStatus(HttpStatus.OK.value());
            OrderResponseDto purchaseOrder = orderMapper.mapOrderToOrderResponseDto(order);
//            purchaseOrder.setBooks(order.getBooks().stream().map(bookMapper::mapBookToBookResponse).toList());
//            // Using OrderMapper to map Order entity to OrderResponseDto
//            responseStructure.setData(purchaseOrder);

            // Returning the response entity with the response structure
            return ResponseEntity.status(HttpStatus.OK).body(responseStructure);

        }).orElseThrow(() ->
                // Throwing an exception if the order is not found
                new OrderNotExistException("OrderId: " + orderId + ", does not exist")
        );
    }







    @Override
    public ResponseEntity<ResponseStructure<OrderResponseDto>> generatePurchaseOrder(OrderRequest orderRequest, Long customerId, Long addressId) {

        // Retrieve customer and address, handle non-existence
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotExistException("Customer Id: " + customerId + " does not exist"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotExistException("Address Id: " + addressId + " does not exist"));

        // Retrieve customer's cart and check if empty
        List<Cart> carts = customer.getCarts();
        if (carts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseStructure<OrderResponseDto>()
                            .setStatus(HttpStatus.OK.value())
                            .setMessage("No items in the cart")
                            .setData(null));
        }

        // Check stock and update quantities
        List<Book> booksToUpdate = new ArrayList<>();
        for (Cart cart : carts) {
            Book book = cart.getBook();
            if (book.getBookQuantity() < 1) {
                throw new IllegalArgumentException("Insufficient stock for Book Id: " + book.getBookid());
            }
            book.setBookQuantity(book.getBookQuantity() - 1);
            booksToUpdate.add(book);
        }
        // Save all updated books in a batch
        bookRepository.saveAll(booksToUpdate);

        // Clear carts from customer entity first
        customer.getCarts().clear();
        customerRepository.save(customer); // Ensure this change is persisted in the database

        // Remove carts from the repository
        cartRepository.deleteAll(carts);

        // Create and save the order
        Order order = orderMapper.mapOrderRequestToOrder(
                orderRequest,
                customer,
                address,
                carts.stream().map(Cart::getBook).toList()
        );
        order = orderRepository.save(order);

        // Prepare the response DTO
        OrderResponseDto responseDto = orderMapper.mapOrderToOrderResponseDto(order);

        // Send order confirmation email asynchronously
        userService.mailSend(
                customer.getEmail(),
                "Order Placed Successfully",
                "<h3>Your Order Id: " + order.getOrderId() + "</h3><p>Track your order using the provided orderId. or contact:6362520441 to know the status of you order</p>"
        );

        // Return the response
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseStructure<OrderResponseDto>()
                        .setStatus(HttpStatus.CREATED.value())
                        .setMessage("Order Created Successfully and Cart Cleared")
                        .setData(responseDto));
    }


//    to handel race condition
//    need to implement RabbitMQ

//    @Transactional
//    @Override
//    public ResponseEntity<ResponseStructure<OrderResponseDto>> generatePurchaseOrder(OrderRequest orderRequest, Long customerId, Long addressId) {
//
//        // Retrieve customer and address with error handling
//        Customer customer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new CustomerNotExistException("Customer Id: " + customerId + " does not exist"));
//
//        Address address = addressRepository.findById(addressId)
//                .orElseThrow(() -> new AddressNotExistException("Address Id: " + addressId + " does not exist"));
//
//        // Retrieve customer's cart and check if empty
//        List<Cart> carts = customer.getCarts();
//        if (carts.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new ResponseStructure<OrderResponseDto>()
//                            .setStatus(HttpStatus.OK.value())
//                            .setMessage("No items in the cart")
//                            .setData(null));
//        }
//
//        // Initialize a list to hold books for batch updates
//        List<Book> booksToUpdate = new ArrayList<>();
//
//        // Check stock and apply locking for concurrency
//        for (Cart cart : carts) {
//            Book book = bookRepository.findBookWithLock(cart.getBook().getBookid())
//                    .orElseThrow(() -> new BookNotExistException("Book Id: " + cart.getBook().getBookid() + " does not exist"));
//
//            if (book.getBookQuantity() < 1) {
//                throw new IllegalArgumentException("Insufficient stock for Book Id: " + book.getBookid());
//            }
//
//            // Deduct stock
//            book.setBookQuantity(book.getBookQuantity() - 1);
//            booksToUpdate.add(book);
//        }
//
//        // Save all updated books in a batch
//        bookRepository.saveAll(booksToUpdate);
//
//        // Clear carts from the customer and persist changes
//        customer.getCarts().clear();
//        customerRepository.save(customer);
//
//        // Remove carts from the repository in a batch
//        cartRepository.deleteAllInBatch(carts);
//
//        // Create and save the order
//        Order order = orderMapper.mapOrderRequestToOrder(
//                orderRequest,
//                customer,
//                address,
//                carts.stream().map(Cart::getBook).toList()
//        );
//        order = orderRepository.save(order);
//
//        // Prepare the response DTO
//        OrderResponseDto responseDto = orderMapper.mapOrderToOrderResponseDto(order);
//
//        // Offload email sending to a queue for asynchronous processing
//        emailQueue.send(new EmailDetails(
//                customer.getEmail(),
//                "Order Placed Successfully",
//                "<h3>Your Order Id: " + order.getOrderId() + "</h3><p>Track your order using the provided orderId. For inquiries, contact: 6362520441</p>"
//        ));
//
//        // Return the response
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new ResponseStructure<OrderResponseDto>()
//                        .setStatus(HttpStatus.CREATED.value())
//                        .setMessage("Order Created Successfully and Cart Cleared")
//                        .setData(responseDto));
//    }







}


