package com.book.store.application.mapper;

import com.book.store.application.entity.*;
import com.book.store.application.enums.Priority;
import com.book.store.application.repository.AddressRepository;
import com.book.store.application.repository.CustomerRepository;
import com.book.store.application.requestdto.AddressDto;
import com.book.store.application.requestdto.OrderRequest;
import com.book.store.application.requestdto.OrderRequestDto;
import com.book.store.application.responsedto.OrderResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class OrderMapper {

    @Autowired
    public BookMapper bookMapper;

 private final AddressRepository addressRepository;
    // Mapping OrderRequest to Order entity
    public Order mapOrderRequestToOrder(OrderRequest orderRequest, Customer customer, Address address, List<Book> books) {
        Order order = new Order();
        order.setTotalQuantity(orderRequest.getTotalQuantity());
        order.setTotalPrice(orderRequest.getTotalPrice());
        order.setDiscount(orderRequest.getDiscount());
        order.setDiscountPrice(orderRequest.getDiscountPrice());
        order.setTotalPayableAmount(orderRequest.getTotalPayableAmount());
        order.setCustomer(customer);
        order.setAddress(address);
        order.setBooks(books);
        order.setOrderDate(LocalDate.now()); // Assuming you want to set the current date
        return order;
    }


    // Mapping Order entity to OrderResponseDto
    public OrderResponseDto mapOrderToOrderResponseDto(Order order) {
        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setOrderId(order.getOrderId());
        responseDto.setCustomerId(order.getCustomer().getUserid()); // Assuming customer has a method getUserid()
        responseDto.setTotalQuantity(order.getTotalQuantity());
        responseDto.setTotalPrice(order.getTotalPrice());
        responseDto.setDiscount(order.getDiscount());
        responseDto.setDiscountPrice(order.getDiscountPrice());
        responseDto.setTotalPayableAmount(order.getTotalPayableAmount());
        responseDto.setBooks(order.getBooks().stream().map(bookMapper::mapBookToBookResponse).toList());

        // Map Address to AddressDto
        responseDto.setAddressDto(mapAddressToAddressDto(order.getAddress()));

        return responseDto;
    }



    // Mapping Address entity to AddressDto
    private AddressDto mapAddressToAddressDto(Address address) {

        System.out.println(address.getAddressId());
       Optional<Address> optionalAddress= addressRepository.findById(address.getAddressId());
        List<Contact> contacts=null;
       if(optionalAddress.isPresent()){
           Address address1 = optionalAddress.get();
           System.out.println(address1);
          contacts= address1.getContacts();
           System.out.println(contacts);
       }
        // Initializing the AddressDto
        // Iterating over contacts and setting based on priority (PRIMARY or SECONDARY)

            AddressDto addressDto = new AddressDto();

            addressDto.setStreetAddress(address.getStreetAddress());
            addressDto.setStreetAddressAdditional(address.getStreetAddressAdditional());
            addressDto.setCity(address.getCity());
            addressDto.setState(address.getState());
            addressDto.setCountry(address.getCountry());
            addressDto.setPincode(address.getPincode());
            addressDto.setAddressType(address.getAddressType());
        for (Contact contact : contacts) {
            System.out.println(contact);

            if (contact.getPriority() == Priority.PRIMARY) {
                addressDto.setContactNumber1(contact.getContactNumber().toString());  // Setting primary contact
            } else if (contact.getPriority() == Priority.SECONDARY) {
                addressDto.setContactNumber2(contact.getContactNumber().toString());  // Setting secondary contact
            }
        }



            // Return the mapped AddressDto
            return addressDto;
        }







}
