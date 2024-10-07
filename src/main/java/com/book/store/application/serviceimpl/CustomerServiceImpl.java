package com.book.store.application.serviceimpl;

import com.book.store.application.entity.Cart;
import com.book.store.application.exception.UserNotExistException;
import com.book.store.application.mapper.UserMapper;
import com.book.store.application.repository.CustomerRepository;
import com.book.store.application.responsedto.CartBookResponse;
import com.book.store.application.responsedto.UserResponse;
import com.book.store.application.service.CustomerService;
import com.book.store.application.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final UserMapper userMapper;
    //------------------------------------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity<ResponseStructure<UserResponse>> findCustomer(Long customerId) {
        return customerRepository.findById(customerId).map(customer -> {
            return ResponseEntity.status(HttpStatus.FOUND).body(new ResponseStructure<UserResponse>()
                    .setStatus(HttpStatus.FOUND.value())
                    .setMessage("Customer Founded")
                    .setData(userMapper.mapUserToUserResponse(customer)));
        }).orElseThrow(() -> new UserNotExistException("CustomerId : " + customerId + ", is not exist"));
    }
    //------------------------------------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity<ResponseStructure<List<UserResponse>>> findCustomers() {
        List<UserResponse> customerResponseList = customerRepository.findAll()
                .stream()
                .map(userMapper::mapUserToUserResponse)
                .toList();
        return ResponseEntity.status(HttpStatus.FOUND).body(new ResponseStructure<List<UserResponse>>()
                .setMessage("Customers are Founded")
                .setData(customerResponseList));
    }
    //------------------------------------------------------------------------------------------------------------------------


}