package com.book.store.application.serviceimpl;

import com.book.store.application.entity.Address;
import com.book.store.application.entity.Customer;
import com.book.store.application.entity.Seller;
import com.book.store.application.entity.User;
import com.book.store.application.enums.UserRole;
import com.book.store.application.exception.AddressNotExistException;
import com.book.store.application.exception.AlreadyAddressExistException;
import com.book.store.application.exception.UserNotExistException;
import com.book.store.application.mapper.AddressMapper;
import com.book.store.application.repository.AddressRepository;
import com.book.store.application.repository.UserRepository;
import com.book.store.application.requestdto.AddressRequest;
import com.book.store.application.responsedto.AddressResponse;
import com.book.store.application.service.AddressService;
import com.book.store.application.service.CustomerService;
import com.book.store.application.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final SellerRepositor sellerRepository;
    private final AddressMapper addressMapper;

    //----------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public ResponseEntity<ResponseStructure<AddressResponse>> addAddress(AddressRequest addressRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException("UserId : " + userId + ", is not exist"));
        Address address = addressMapper.mapAddressRequestToAddress(addressRequest, new Address());

        if (user.getUserRole().equals(UserRole.CUSTOMER)) {
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                if (customer.getAddresses().size() >= 4) {
                    throw new AlreadyAddressExistException("Customer cannot have more than 4 addresses");
                }
                address.setCustomer(customer);
                address = addressRepository.save(address);
            } else {
                throw new ClassCastException("User is not a Customer");
            }
        } else if (user.getUserRole().equals(UserRole.SELLER)) {
            if (user instanceof Seller) {
                Seller seller = (Seller) user;
                if (seller.getAddress() != null) {
                    throw new AlreadyAddressExistException("Address already present, modify existing address");
                }
                address = addressRepository.save(address);
                seller.setAddress(address);
                sellerRepository.save(seller);
            } else {
                throw new ClassCastException("User is not a Seller");
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<AddressResponse>()
                .setStatus(HttpStatus.CREATED.value())
                .setMessage("Address created")
                .setData(addressMapper.mapAddressToAddressResponse(address)));
    }

    //----------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public ResponseEntity<ResponseStructure<List<AddressResponse>>> getAddress(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException("UserId : " + userId + ", is not exist"));

        if (user.getUserRole().equals(UserRole.CUSTOMER)) {
            List<Address> addresses = addressRepository.findByCustomer((Customer) user);
            List<AddressResponse> addressResponses = addresses.stream().map(addressMapper::mapAddressToAddressResponse).toList();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<List<AddressResponse>>()
                    .setStatus(HttpStatus.OK.value())
                    .setMessage("Addresses are founded")
                    .setData(addressResponses));
        } else if (user.getUserRole().equals(UserRole.SELLER)) {
            Seller seller = (Seller) user;
            Address address = seller.getAddress();
            if (address == null) {
                throw new AddressNotExistException("Address not found for seller");
            }
            AddressResponse addressResponse = addressMapper.mapAddressToAddressResponse(address);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<List<AddressResponse>>()
                    .setStatus(HttpStatus.OK.value())
                    .setMessage("Address is founded")
                    .setData(List.of(addressResponse)));
        } else {
            throw new UserNotExistException("User role not supported");
        }
    }

    @Override
    public ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(Long addressId, AddressRequest addressRequest) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new AddressNotExistException("Address Id : " + addressId + ", is not exist"));
        address = addressMapper.mapAddressRequestToAddress(addressRequest, address);
        address = addressRepository.save(address);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<AddressResponse>()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Address updated done")
                .setData(addressMapper.mapAddressToAddressResponse(address)));
    }
//----------------------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------------------

}