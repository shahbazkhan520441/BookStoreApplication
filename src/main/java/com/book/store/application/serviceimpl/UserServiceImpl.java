package com.book.store.application.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.book.store.application.exception.*;
import com.book.store.application.jwt.AccessToken;
import com.book.store.application.jwt.JwtService;
import com.book.store.application.jwt.RefreshToken;
import com.book.store.application.repository.AccessTokenRepository;
import com.book.store.application.repository.RefreshTokenRepository;
import com.book.store.application.requestdto.UserAuthRequest;
import com.book.store.application.responsedto.AuthResponse;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.book.store.application.email.MailService;
import com.book.store.application.email.MessageData;
import com.book.store.application.entity.Admin;
import com.book.store.application.entity.Customer;
import com.book.store.application.entity.Seller;
import com.book.store.application.entity.User;
import com.book.store.application.enums.UserRole;
import com.book.store.application.mapper.UserMapper;
import com.book.store.application.repository.UserRepository;
import com.book.store.application.requestdto.MainUserRequest;
import com.book.store.application.requestdto.OtpVerificationRequest;
import com.book.store.application.requestdto.UserRequest;
import com.book.store.application.responsedto.UserResponse;
import com.book.store.application.service.UserService;
import com.book.store.application.util.ResponseStructure;
import com.google.common.cache.Cache;

@Service
public class UserServiceImpl implements UserService {

	@Value("${application.jwt.access_expiry_seconds}")
	private long accessExpirySeconds;

	@Value("${application.jwt.refresh_expiry_seconds}")
	private long refreshExpireSeconds;

	@Value("${application.cookie.domain}")
	private String domain;

	@Value("${application.cookie.same-site}")
	private String sameSite;

	@Value("${application.cookie.secure}")
	private boolean secure;

//	@Autowired
    private final UserRepository userRepository;
    private final Cache<String, String> otpCache;
    private final Cache<String, User> userCache;
    private final UserMapper userMapper;
    private final Random random;
    private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final AccessTokenRepository accessTokenRepository;
	private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository,
						   Cache<String, String> otpCache,
						   Cache<String, User> userCache,
						   UserMapper userMapper,
						   Random random,
						   MailService mailService,
						   PasswordEncoder passwordEncoder,
						   AuthenticationManager authenticationManager,
						   JwtService jwtService,
						   AccessTokenRepository accessTokenRepository,
						   RefreshTokenRepository refreshTokenRepository) {
		super();
		this.userRepository = userRepository;
		this.otpCache = otpCache;
		this.userCache = userCache;
		this.userMapper = userMapper;
		this.random = random;
		this.mailService = mailService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.accessTokenRepository = accessTokenRepository;
		this.refreshTokenRepository = refreshTokenRepository;
	}

//----------------------------------------------------------------------------------------------------------------------------------------------------------
@Override
public ResponseEntity<ResponseStructure<AuthResponse>> login(UserAuthRequest authRequest) {
	try {
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (authenticate.isAuthenticated()) {
			return userRepository.findByUsername(authRequest.getUsername()).map(existUser -> {
				HttpHeaders httpHeaders = new HttpHeaders();
				grantAccessToken(httpHeaders, existUser);
				grantRefreshToken(httpHeaders, existUser);

				return ResponseEntity.status(HttpStatus.OK)
						.headers(httpHeaders)
						.body(new ResponseStructure<AuthResponse>()
								.setStatus(HttpStatus.OK.value())
								.setMessage("User Verified")
								.setData(AuthResponse.builder()
										.userId(existUser.getUserid())
										.username(existUser.getUsername())
										.userRole(existUser.getUserRole())
										.accessExpiration(accessExpirySeconds)
										.refreshExpiration(refreshExpireSeconds)
										.build()));
			}).orElseThrow(() -> new UserNotExistException("Username : " + authRequest.getUsername() + ", is not found"));
		} else
			throw new BadCredentialsException("Invalid Credentials");
	} catch (AuthenticationException e) {
		throw new BadCredentialsException("Invalid Credentials", e);
	}
}
	//----------------------------------------------------------------------------------------------------------------------------------------------------------
		@Override
		public  void grantAccessToken(HttpHeaders httpHeaders, User user) {
			String token = jwtService.createJwtToken(user.getUsername(), user.getUserRole(), (accessExpirySeconds * 1000)); // 1 hour in ms

			AccessToken accessToken = AccessToken.builder()
					.accessToken(token)
					.expiration(LocalDateTime.now().plusSeconds(accessExpirySeconds))
					.user(user)
					.build();
			accessTokenRepository.save(accessToken);

			httpHeaders.add(HttpHeaders.SET_COOKIE, generateCookie("at", token, accessExpirySeconds));
		}
	//----------------------------------------------------------------------------------------------------------------------------------------------------------

		@Override
		public void grantRefreshToken(HttpHeaders httpHeaders, User user) {

			String token = jwtService.createJwtToken(user.getUsername(), user.getUserRole(), (refreshExpireSeconds * 1000));

			RefreshToken refreshToken = RefreshToken.builder()
					.refreshToken(token)
					.expiration(LocalDateTime.now().plusSeconds(refreshExpireSeconds))
					.user(user)
					.build();
			refreshTokenRepository.save(refreshToken);

			httpHeaders.add(HttpHeaders.SET_COOKIE, generateCookie("rt", token, refreshExpireSeconds));
		}

	//----------------------------------------------------------------------------------------------------------------------------------------------------------

	public String generateCookie(String name, String tokenValue, long maxAge) {
		return ResponseCookie.from(name, tokenValue)
				.httpOnly(true)
				.secure(secure)
				.path("/")
				.maxAge(maxAge)
//                .domain(domain)
				.sameSite(sameSite) // how can issue  cookie to particular browser
				.build()
				.toString();
	}

	//----------------------------------------------------------------------------------------------------------------------------------------------------------
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(MainUserRequest userRequest, UserRole userRole) {
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
	                user = userMapper.mapUserRequestToUser(user ,userRequest);
	                user.setUserRole(userRole);
					user.setRegisteredDate(LocalDate.now());

	                userCache.put(userRequest.getEmail(), user);
	                int otp = random.nextInt(100000, 999999);
	                otpCache.put(userRequest.getEmail(), otp + "");

	                String otpExpired = otpExpirationTimeCalculate(5);
//	                Send otp in mail
	                mailSend(user.getEmail(), "OTP verification for BookStoreShoppingApp", "<h3>Welcome to Book Store Shopping Application</h3></br><h4>Otp : " + otp + "</h4></br><p>" + otpExpired + "</p>");
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
	@Override
	    public void mailSend(String email, String subject, String text) {
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
	User user = userCache.getIfPresent(otpVerificationRequest.getEmail());
	String otp = otpCache.getIfPresent(otpVerificationRequest.getEmail());
	if (user == null && otp == null) {
		throw new IllegalOperationException("Please Enter correct information");
	} else if (otp == null && user.getEmail().equals(otpVerificationRequest.getEmail())) {
//            if user otp will be expired
		throw new OtpExpiredException("Otp is expired");
	} else if (!otp.equals(otpVerificationRequest.getOtp())) {
//            oto mismatch with existing otp   or   invalid otp
		throw new InvalidOtpException("Invalid otp");
	} else if (otp.equals(otpVerificationRequest.getOtp()) && user != null) {
//            If user otp and cache otp
//           Create Dynamic username
		if (user.getUsername() == null) {
			String userGen = usernameGenerate(user.getEmail());
			user.setUsername(userGen);
			user.setEmailVerified(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user = userRepository.save(user);
			//            Send mail to user for confirmation
			mailSend(user.getEmail(), "Email Verification done", "<h3>Your account is created in Book Store Application</h3></br><h4>Your username is : " + userGen + " and UserRole is : " + user.getUserRole() + "</h4>");
		} else if (user.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user = userRepository.save(user);
			mailSend(user.getEmail(), "Profile successfully updated", "<h3>Your account is updated in Book Store Application</h3></br><h4>Your username is : " + user.getUsername() + " and UserRole is : " + user.getUserRole() + "</h4>");
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<UserResponse>()
					.setStatus(HttpStatus.OK.value())
					.setMessage("User verified")
					.setData(userMapper.mapUserToUserResponse(user)));
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<UserResponse>()
				.setStatus(HttpStatus.CREATED.value())
				.setMessage(user.getUserRole() + " Created or Updated")
				.setData(userMapper.mapUserToUserResponse(user)));
	} else {
		throw new OtpExpiredException("Otp is expired");
	}
}
//	    -----------------------------------------------------
	   @Override
	    public String usernameGenerate(String email) {
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
	        if (user != null) {
	            int otp = random.nextInt(100000, 999999);
	            otpCache.put(userRequest.getEmail(), otp + "");

	            String otpExpired = otpExpirationTimeCalculate(5);
//	          Re-Send otp in mail
	            mailSend(user.getEmail(), "OTP verification for Book Store Application", "<h3>Welcome to BookStore Shopping Application</h3></br><h4>Regenerated Otp : " + otp + "</h4></br><p>" + otpExpired + "</p>");

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
	                user = userMapper.mapUserRequestToUser(user, userRequest);
	                userCache.put(userRequest.getEmail(), user);
	                int otp = random.nextInt(100000, 999999);
	                otpCache.put(userRequest.getEmail(), otp + "");

	                String otpExpired = otpExpirationTimeCalculate(5);
//	                Send otp in mail
	                mailSend(user.getEmail(), "OTP verification for BookStoreApplication", "<h3>Welcome to BookStore Shopping Application</h3></br><h4>Otp : " + otp + "</h4></br><p>" + otpExpired + "</p>");
	            } else {
	                boolean existEmail = userRepository.existsByEmail(userRequest.getEmail());
	                if (existEmail)
	                    throw new UserAlreadyExistException("Email : " + userRequest.getEmail() + ", is already exist with the given email use different email id ");
	                else {
	                    user = userMapper.mapUserRequestToUser(user, userRequest);
	                    userCache.put(userRequest.getEmail(), user);
	                    int otp = random.nextInt(100000, 999999);
	                    otpCache.put(userRequest.getEmail(), otp + "");

	                    String otpExpired = otpExpirationTimeCalculate(5);
//	                Send otp in  previous mail id
	                    mailSend(user.getEmail(), "OTP verification for BookStoreApplication", "<h3>Welcome to BookStore Shopping Application</h3></br><h4>Otp : " + otp + "</h4></br><p>" + otpExpired + "</p>");
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
	            existUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
				existUser.setUpdatedDate(LocalDate.now());
	            existUser = userRepository.save(existUser);
	            mailSend(existUser.getEmail(), "Password reset done at BookStoreApplication",
	                    "<h3>Welcome to BookStore Shopping Application</h3></br><p>Your password reset successfully done</p></br><h4>Your Username is : " + existUser.getUsername() + "</h4>");
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

//		--------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(String refreshToken) {
		Date expiryDate = jwtService.extractExpirationDate(refreshToken);
		if (expiryDate.getTime() < new Date().getTime()) {
			throw new TokenExpiredException("Refresh token was expired, Please make a new SignIn request");
		} else {
			String username = jwtService.extractUserName(refreshToken);
//            UserRole userRole = jwtService.extractUserRole(refreshToken);
			User user = userRepository.findByUsername(username).get();

			HttpHeaders httpHeaders = new HttpHeaders();
			grantAccessToken(httpHeaders, user);

			return ResponseEntity.status(HttpStatus.OK)
					.headers(httpHeaders)
					.body(new ResponseStructure<AuthResponse>()
							.setStatus(HttpStatus.OK.value())
							.setMessage("Access Toke renewed")
							.setData(AuthResponse.builder()
									.userId(user.getUserid())
									.username(user.getUsername())
									.userRole(user.getUserRole())
									.accessExpiration(accessExpirySeconds)
									.refreshExpiration((expiryDate.getTime() - new Date().getTime()) / 1000)
									.build()));
		}
	}


}