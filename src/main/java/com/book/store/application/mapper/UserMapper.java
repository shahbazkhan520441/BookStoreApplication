package com.book.store.application.mapper;


import com.book.store.application.requestdto.MainUserRequest;
import org.springframework.stereotype.Component;


import com.book.store.application.entity.User;
import com.book.store.application.responsedto.UserResponse;
@Component
public class UserMapper {

    // Map UserRequest to User entity
    public User mapUserRequestToUser(User user, MainUserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setDob(userRequest.getDob());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        return user;
    }

    // Map User entity to UserResponse DTO
    public UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserid());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setDob(user.getDob());
        userResponse.setEmail(user.getEmail());
        userResponse.setUserRole(user.getUserRole());
        userResponse.setRegisteredDate(user.getRegisteredDate());
        userResponse.setUpdatedDate(user.getUpdatedDate());
        userResponse.setUsername(user.getUsername());
        return userResponse;
    }
}
