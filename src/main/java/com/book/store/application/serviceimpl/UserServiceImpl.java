package com.book.store.application.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.hibernate.query.IllegalQueryOperationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.book.store.application.email.MailService;
import com.book.store.application.email.MessageData;
import com.book.store.application.entity.Admin;
import com.book.store.application.entity.Customer;
import com.book.store.application.entity.Seller;
import com.book.store.application.entity.User;
import com.book.store.application.enums.UserRole;
import com.book.store.application.exception.IllegalOperationException;
import com.book.store.application.exception.InvalidOtpException;
import com.book.store.application.exception.OtpExpiredException;
import com.book.store.application.exception.UserAlreadyExistException;
import com.book.store.application.exception.UserNotExistException;
import com.book.store.application.mapper.UserMapper;
import com.book.store.application.repository.UserRepository;
import com.book.store.application.requestdto.MainUserRequest;
import com.book.store.application.requestdto.OtpVerificationRequest;
import com.book.store.application.requestdto.UserRequest;
import com.book.store.application.responsedto.UserResponse;
import com.book.store.application.service.UserService;
import com.book.store.application.util.ResponseStructure;
import com.google.common.cache.Cache;

import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements UserService {
	
//	@Autowired
    private final UserRepository userRepository;
    private final Cache<String, String> otpCache;
    private final Cache<String, User> userCache;
    private final UserMapper userMapper;
    private final Random random;
    private final MailService mailService;
    
//    private final PasswordEncoder passwordEncoder;
	
    public UserServiceImpl(UserRepository userRepository, Cache<String, String> otpCache,
			Cache<String, User> userCache, UserMapper userMapper, Random random, MailService mailService
			) {
		super();
		this.userRepository = userRepository;
		this.otpCache = otpCache;
		this.userCache = userCache;
		this.userMapper = userMapper;
		this.random = random;
		this.mailService = mailService;
//		this.passwordEncoder = passwordEncoder;
	}
    
    
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(MainUserRequest userRequest, UserRole userRole) {
		System.out.println("in save user ");
		boolean emailExist = userRepository.existsByEmail(userRequest.getEmail());
		
		   if (emailExist)
	            throw new UserAlreadyExistException("Email : " + userRequest.getEmail() + ", is already exist");
	        else {
	            User user = null;
	            switch (userRole) {
	                case UserRole.SELLER -> user = new Seller();
	                case UserRole.CUSTOMER -> user = new Customer();
	                case UserRole.ADMIN -> user = new Admin();
	            }
	            
	            if (user != null) {
	                user = userMapper.mapUserRequestToUser(userRequest);
	                user.setUserRole(userRole);
	                userCache.put(userRequest.getEmail(), user);
	                int otp = random.nextInt(100000, 999999);
	                otpCache.put(userRequest.getEmail(), otp + "");

	                String otpExpired = otpExpirationTimeCalculate(5);
	                
//	                Send otp in mail
	                mailSend(user.getEmail(), "OTP verification for BookStoreShoppingApp", "<h3>Welcome to BookStore Shopping Application</h3></br><h4>Otp : " + otp + "</h4></br><p>" + otpExpired + "</p>");
	                System.out.println("out save user ");
	                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseStructure<UserResponse>()
	                        .setStatus(HttpStatus.ACCEPTED.value())
	                        .setMessage("Otp sended")
	                        .setData(userMapper.mapUserToUserResponse(user)));
	            } else throw new UserAlreadyExistException("Bad Request");

	}
		   
		 

	}
//	-------------------------------------------------------------------------------------
	
	   private String otpExpirationTimeCalculate(int expired) {
	        // Get the current time
	        LocalDateTime currentTime = LocalDateTime.now();

	        // Add 5 minutes to the current time
	        LocalDateTime timeAfterFiveMinutes = currentTime.plusMinutes(expired);

	        // Convert LocalDateTime to Date
	        ZonedDateTime zonedDateTime = timeAfterFiveMinutes.atZone(ZoneId.systemDefault());
	        Date dateAfterFiveMinutes = Date.from(zonedDateTime.toInstant());

	        return "Otp will be expired after 5 minutes: " + dateAfterFiveMinutes;
	    }
	   
//	   --------------------------------------------------------------------------------------
	   
	   //    Logic for mail generation
	    private void mailSend(String email, String subject, String text) {
	        MessageData messageData = new MessageData();
	        messageData.setTo(email);
	        messageData.setSubject(subject);
	        messageData.setText(text);
	        messageData.setSendDate(new Date());
	        try {
	            mailService.sendMail(messageData);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
//	    -------------------------------------------------------
	    
	    @Override
	    public ResponseEntity<ResponseStructure<UserResponse>> verifyUserOtp(OtpVerificationRequest otpVerificationRequest) {
	        System.out.println("In OTP verification request");

	        User user = userCache.getIfPresent(otpVerificationRequest.getEmail());
	        String otp = otpCache.getIfPresent(otpVerificationRequest.getEmail());

	        if (user == null) {
	            throw new IllegalQueryOperationException("Please enter correct information");
	        }

	        if (otp == null) {
	            throw new OtpExpiredException("OTP is expired");
	        }

	        if (!otp.equals(otpVerificationRequest.getOtp())) {
	            throw new InvalidOtpException("Invalid OTP");
	        }

	        // User verified, proceed with creating or updating user details
	        String userGen = usernameGenerate(user.getEmail());
	        boolean isNewUser = user.getUsername() == null;

	        if (isNewUser) {
	            user.setUsername(userGen);
	            user.setEmailVerified(true);
	            user.setRegisteredDate(LocalDate.now());
	            mailSend(user.getEmail(), "Email Verification Done", 
	                    "<h3>Your account is created in BookStoreApplication</h3></br><h4>Your username is: " + userGen + " and UserRole is: " + user.getUserRole() + "</h4>");
	        } else {
	            mailSend(user.getEmail(), "Profile Successfully Updated", 
	                    "<h3>Your account is updated in BookStoreApplication</h3></br><h4>Your username is: " + user.getUsername() + " and UserRole is: " + user.getUserRole() + "</h4>");
	        }

	        // Save user and return response
	        user.setPassword(user.getPassword()); // Ensure password is set (consider hashing if not already done)
	        user = userRepository.save(user);

	        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<UserResponse>()
	                .setStatus(HttpStatus.CREATED.value())
	                .setMessage(user.getUserRole() + (isNewUser ? " Created" : " Updated"))
	                .setData(userMapper.mapUserToUserResponse(user)));
	    }

//	    -----------------------------------------------------
	    private String usernameGenerate(String email) {
	        String[] str = email.split("@");
	        String username = str[0];
	        int temp = 0;
	        while (true) {
	            if (userRepository.existsByUsername(username)) {
	                username += temp;
	                temp++;
	                continue;
	            } else
	                break;
	        }
	        if (temp != 0) {
	            return username;
	        } else {
	            return str[0];
	        }
	    }
//	        -----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	        
	    @Override
	    public ResponseEntity<ResponseStructure<UserResponse>> resendOtp(OtpVerificationRequest userRequest) {
	        User user = userCache.getIfPresent(userRequest.getEmail());
	        System.out.println(user);
	        if (user != null) {
	            int otp = random.nextInt(100000, 999999);
	            otpCache.put(userRequest.getEmail(), otp + "");

	            String otpExpired = otpExpirationTimeCalculate(5);
//	          Re-Send otp in mail
	            mailSend(user.getEmail(), "OTP verification for BookStoreApplication", "<h3>Welcome to BookStore Shopping Applicationa</h3></br><h4>Regenerated Otp : " + otp + "</h4></br><p>" + otpExpired + "</p>");

	            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseStructure<UserResponse>()
	                    .setStatus(HttpStatus.ACCEPTED.value())
	                    .setMessage("Otp sended")
	                    .setData(userMapper.mapUserToUserResponse(user)));
	        } else throw new UserNotExistException("Email : " + userRequest.getEmail() + ", is not exist");
	    } 
	        
//	    --------------------------------------------------------------------------------------------------------
	    
	    @Override
	    public ResponseEntity<ResponseStructure<UserResponse>> updateUser(MainUserRequest userRequest, Long userId) {
	    	
	       return  userRepository.findById(userId).map(user -> {
	            if (user.getEmail().equals(userRequest.getEmail())) {
	                user = userMapper.mapUserRequestToUser(userRequest);
	                userCache.put(userRequest.getEmail(), user);
	                int otp = random.nextInt(100000, 999999);
	                otpCache.put(userRequest.getEmail(), otp + "");

	                String otpExpired = otpExpirationTimeCalculate(5);
//	                Send otp in mail
	                mailSend(user.getEmail(), "OTP verification for BookStoreApplication", "<h3>Welcome to BookStore Shopping Applicationa</h3></br><h4>Otp : " + otp + "</h4></br><p>" + otpExpired + "</p>");
	            } else {
	                boolean existEmail = userRepository.existsByEmail(userRequest.getEmail());
	                if (existEmail)
	                    throw new UserAlreadyExistException("Email : " + userRequest.getEmail() + ", is already exist with the given email use different email id ");
	                else {
	                    user = userMapper.mapUserRequestToUser(userRequest);
	                    userCache.put(userRequest.getEmail(), user);
	                    int otp = random.nextInt(100000, 999999);
	                    otpCache.put(userRequest.getEmail(), otp + "");

	                    String otpExpired = otpExpirationTimeCalculate(5);
//	                Send otp in  previous mail id
	                    mailSend(user.getEmail(), "OTP verification for BookStoreApplication", "<h3>Welcome to BookStore Shopping Applicationa</h3></br><h4>Otp : " + otp + "</h4></br><p>" + otpExpired + "</p>");
	                }
	            }
	          
	         return    ResponseEntity.status(HttpStatus.OK)
	                    .body(new ResponseStructure<UserResponse>()
	                    .setStatus(HttpStatus.OK.value())
	                    .setMessage("Otp sended")
	                    .setData(userMapper.mapUserToUserResponse(user)));
	        }).orElseThrow(() -> new UserNotExistException("UserId : " + userId + ", is not exist"));
	    }
	    

//	------------------------------------------------------------------------------------------------------------
	    
	    
	    @Override
	    public ResponseEntity<ResponseStructure<UserResponse>> passwordResetByEmail(String email) {
	        User user = userRepository.findByEmail(email)
	                .orElseThrow(() -> new UserNotExistException("Email Id : " + email + ", is not exist"));
	        user.setPassword(null);
	        userCache.put(user.getEmail(), user);
	        int otp = random.nextInt(100000, 999999);
	        otpCache.put(user.getEmail(), otp + "");

	        String otpExpired = otpExpirationTimeCalculate(5);
//	                Send otp in mail
	        mailSend(user.getEmail(), "OTP verification for BookStoreApplication",
	                "<h3>Welcome to BookStore Shopping Application</h3></br><h4>Otp : " + otp + "</h4>");
	        return ResponseEntity.status(HttpStatus.OK)
	                .body(new ResponseStructure<UserResponse>()
	                        .setStatus(HttpStatus.OK.value())
	                        .setMessage("Otp sended")
	                        .setData(userMapper.mapUserToUserResponse(user)));
	    }
	    
//	    ----------------------------------------------------------------------------------------------------------------------- 
	    @Override
	    public ResponseEntity<ResponseStructure<UserResponse>> passwordResetByEmailVerification(UserRequest userRequest, String otp) {
	    	
	        User cacheUser = userCache.getIfPresent(userRequest.getEmail());
	        String exotp   = otpCache.getIfPresent(userRequest.getEmail());
	        User existUser = userRepository.findByEmail(userRequest.getEmail())
	                .orElseThrow(() -> new UserNotExistException("Email Id : " + userRequest.getEmail() + ", is not exist"));
	        
	        if (cacheUser != null && exotp.equals(otp)) {
	            existUser.setPassword(userRequest.getPassword());
				existUser.setUpdatedDate(LocalDate.now());
	            existUser = userRepository.save(existUser);
	            mailSend(existUser.getEmail(), "Password reset done at BookStoreApplication",
	                    "<h3>Welcome to BookStore Shopping Applicationa</h3></br><p>Your password reset successfully done</p></br><h4>Your Username is : " + existUser.getUsername() + "</h4>");
	            return ResponseEntity.status(HttpStatus.OK)
	                    .body(new ResponseStructure<UserResponse>()
	                            .setStatus(HttpStatus.OK.value())
	                            .setMessage("Password Reset done")
	                            .setData(userMapper.mapUserToUserResponse(existUser)));
	        } else {
	            throw new IllegalOperationException("Session expired please try again...!!!");
	        }
	    }
	    
//	    ----------------------------------------------
	    
	    
	    @Override
	    public ResponseEntity<ResponseStructure<UserResponse>> findUser(Long userId) {
	        return userRepository.findById(userId).map(user -> {
	            return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<UserResponse>()
	                    .setStatus(HttpStatus.OK.value())
	                    .setMessage("User Founded")
	                    .setData(userMapper.mapUserToUserResponse(user)));
	        }).orElseThrow(() -> new UserNotExistException("User with the given userid : " + userId + ", is not exist"));
	    }
	    
//	    ---------------------------------------------------------
	    
	    @Override
	    public ResponseEntity<ResponseStructure<List<UserResponse>>> findUsers() {
	        List<UserResponse> userResponseList = userRepository.findAll()
	                .stream()
	                .map(userMapper::mapUserToUserResponse)
	                .toList();
	        return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<List<UserResponse>>().setStatus(HttpStatus.OK.value())
	                .setMessage("Users are Founded")
	                .setData(userResponseList));
	    }




	  


	
	
}