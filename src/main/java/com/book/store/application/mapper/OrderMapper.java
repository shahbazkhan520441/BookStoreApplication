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
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class OrderMapper {

 private final AddressRepository addressRepository;
    // Mapping OrderRequest to Order entity
    public Order mapOrderRequestToOrder(OrderRequest orderRequest, Customer customer, Address address, Book book) {
        Order order = new Order();
        order.setTotalQuantity(orderRequest.getTotalQuantity());
        order.setTotalPrice(orderRequest.getTotalPrice());
        order.setDiscount(orderRequest.getDiscount());
        order.setDiscountPrice(orderRequest.getDiscountPrice());
        order.setTotalPayableAmount(orderRequest.getTotalPayableAmount());
        order.setCustomer(customer);
        order.setAddress(address);
        order.setBook(book);
        order.setOrderDate(LocalDate.now()); // Assuming you want to set the current date
        return order;
    }

    // Updating existing Order with OrderRequest data
    public Order mapOrderRequestDtoToOrder(OrderRequest orderRequest, Order order) {
        order.setTotalQuantity(orderRequest.getTotalQuantity());
        order.setTotalPrice(orderRequest.getTotalPrice());
        order.setDiscount(orderRequest.getDiscount());
        order.setDiscountPrice(orderRequest.getDiscountPrice());
        order.setTotalPayableAmount(orderRequest.getTotalPayableAmount());
        return order;
    }

    // Mapping Order entity to OrderResponseDto
    public OrderResponseDto mapOrderToOrderResponseDto(Order order) {
        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setOrderId(order.getOrderId());
        responseDto.setCustomerId(order.getCustomer().getUserid()); // Assuming customer has a method getUserid()
        responseDto.setBookId(order.getBook().getBookid()); // Assuming book has a method getBookid()
        responseDto.setTotalQuantity(order.getTotalQuantity());
        responseDto.setTotalPrice(order.getTotalPrice());
        responseDto.setDiscount(order.getDiscount());
        responseDto.setDiscountPrice(order.getDiscountPrice());
        responseDto.setTotalPayableAmount(order.getTotalPayableAmount());

        // Map Address to AddressDto
        responseDto.setAddressDto(mapAddressToAddressDto(order.getAddress()));

        return responseDto;
    }

    // Mapping AddressDto to Address entity
    private Address mapAddressDtoToAddress(AddressDto addressDto) {
        Address address = new Address();
        address.setStreetAddress(addressDto.getStreetAddress());
        address.setStreetAddressAdditional(addressDto.getStreetAddressAdditional());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setCountry(addressDto.getCountry());
        address.setPincode(addressDto.getPincode());
        address.setAddressType(addressDto.getAddressType());
        // You may need to map other fields if needed, like customer or contacts
        return address;
    }

    // Mapping Address entity to AddressDto
    private AddressDto mapAddressToAddressDto(Address address) {

        System.out.println("in order mapper ");
        System.out.println(address.getAddressId());
       Optional<Address> optionalAddress= addressRepository.findById(address.getAddressId());
        System.out.println("in order mapper ");
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

            System.out.println(contact.getPriority()+"------------------------------------------------------");
            if (contact.getPriority() == Priority.PRIMARY) {
                addressDto.setContactNumber1(contact.getContactNumber().toString());  // Setting primary contact
            } else if (contact.getPriority() == Priority.SECONDARY) {
                addressDto.setContactNumber2(contact.getContactNumber().toString());  // Setting secondary contact
            }
        }



            // Return the mapped AddressDto
            return addressDto;
        }





    // Mapping OrderRequest to OrderRequestDto
    public OrderRequestDto mapOrderRequestToOrderRequestDto(OrderRequest orderRequest, Long orderId, AddressDto addressDto) {
        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setTotalQuantity(orderRequest.getTotalQuantity());
        requestDto.setTotalPrice(orderRequest.getTotalPrice());
        requestDto.setDiscount(orderRequest.getDiscount());
        requestDto.setDiscountPrice(orderRequest.getDiscountPrice());
        requestDto.setTotalPayableAmount(orderRequest.getTotalPayableAmount());
        requestDto.setOrderId(orderId);
        requestDto.setAddressDto(addressDto);
        return requestDto;
    }


}
