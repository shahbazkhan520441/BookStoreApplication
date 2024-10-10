package com.book.store.application.serviceimpl;

import com.book.store.application.entity.Address;
import com.book.store.application.entity.Book;
import com.book.store.application.entity.Customer;
import com.book.store.application.entity.Order;
import com.book.store.application.exception.AddressNotExistException;
import com.book.store.application.exception.BookNotExistException;
import com.book.store.application.exception.CustomerNotExistException;
import com.book.store.application.exception.OrderNotExistException;
import com.book.store.application.mapper.OrderMapper;
import com.book.store.application.repository.AddressRepository;
import com.book.store.application.repository.BookRepository;
import com.book.store.application.repository.CustomerRepository;
import com.book.store.application.repository.OrderRepository;
import com.book.store.application.requestdto.OrderRequest;
import com.book.store.application.responsedto.OrderResponseDto;
import com.book.store.application.service.OrderService;
import com.book.store.application.service.UserService;
import com.book.store.application.util.ResponseStructure;
import org.springframework.http.ResponseEntity;



import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final BookRepository bookRepository; // Assuming this exists
    private final OrderMapper orderMapper;
    private final UserService userService; // Remove WebClient

    @Override
    public ResponseEntity<ResponseStructure<OrderResponseDto>> generatePurchaseOrder(
            OrderRequest orderRequest,
            Long bookId, // Changed from productId to bookId
            Long customerId,
            Long addressId) {

        // Retrieve customer, address, and book
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotExistException("Customer Id : " + customerId + ", does not exist"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotExistException("Address Id : " + addressId + ", does not exist"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotExistException("Book id : " + bookId + ", does not exist"));

        // Check if there's enough quantity of the book
        if (book.getBookQuantity() < orderRequest.getTotalQuantity()) {
            throw new IllegalArgumentException("Insufficient quantity for Book Id: " + bookId);
        }

        // Update book quantity
        book.setBookQuantity(book.getBookQuantity() - orderRequest.getTotalQuantity());
        bookRepository.save(book); // Save updated book entity

        // Create order entity
        Order order = orderMapper.mapOrderRequestToOrder(orderRequest, customer, address, book);
        order = orderRepository.save(order); // Save order

        // Prepare response DTO using mapper
        OrderResponseDto orderResponseDto = orderMapper.mapOrderToOrderResponseDto(order);

        // Send email to customer
        userService.mailSend(customer.getEmail(), "Order Placed Successfully in BookStoreApplicationApp",
                "<h3>Your Order Id : " + order.getOrderId() + "</h3></br><p>Track Your Order in below link</p></br><p>Track order</p></br></br><p></p>");

        // Return response entity
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseStructure<OrderResponseDto>()
                        .setStatus(HttpStatus.CREATED.value())
                        .setMessage("Purchase Order Created")
                        .setData(orderResponseDto)); // Return orderResponseDto directly
    }

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
            responseStructure.setMessage("Purchase Order Found");

            // Using OrderMapper to map Order entity to OrderResponseDto
            responseStructure.setData(orderMapper.mapOrderToOrderResponseDto(order));

            // Returning the response entity with the response structure
            return ResponseEntity.status(HttpStatus.OK).body(responseStructure);

        }).orElseThrow(() ->
                // Throwing an exception if the order is not found
                new OrderNotExistException("OrderId: " + orderId + ", does not exist")
        );
    }



}


