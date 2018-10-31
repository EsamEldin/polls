package com.example.polls.controller;

import java.net.URI;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * @author <egadEldin.ext@orange.com> Essam Eldin
 *
 */

import com.example.polls.exception.AppException;
import com.example.polls.model.Role;
import com.example.polls.model.RoleName;
import com.example.polls.model.User;
import com.example.polls.model.VerificationToken;
import com.example.polls.payload.ApiResponse;
import com.example.polls.payload.JwtAuthenticationResponse;
import com.example.polls.payload.LoginRequest;
import com.example.polls.payload.SignUpRequest;
import com.example.polls.repository.RoleRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.security.JwtTokenProvider;
import com.example.polls.service.IUserService;
import com.example.polls.util.OnRegistrationCompleteEvent;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;
    
    //  email verification
    @Autowired
    ApplicationEventPublisher eventPublisher;
    
    @Autowired
    private IUserService service;
    
    Logger log=LoggerFactory.getLogger(this.getClass());

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        
        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail())
            .orElseThrow(() -> 
                    new UsernameNotFoundException("User not found with username or email : " + loginRequest.getUsernameOrEmail()));
        
        if(!user.isEnabled()) {
            return new ResponseEntity(new ApiResponse(false, "This account is not activated yet !"),
                    HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest,WebRequest request) {
       
        
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);
        
      
           if (result == null) {
               return new ResponseEntity(new ApiResponse(false, "User cannot be registered !"),
                   HttpStatus.BAD_REQUEST);
    }
        
           URI location = ServletUriComponentsBuilder
               .fromCurrentContextPath().path("/api/auth")
               .buildAndExpand().toUri();
        
        try {
            String appUrl = location.toString();
            log.info("appUrl_____________="+appUrl);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(result, request.getLocale(), appUrl));
        } catch (Exception me) {
            me.printStackTrace();
            return new ResponseEntity(new ApiResponse(false, "cannot trigger mail server !"),
                HttpStatus.BAD_REQUEST);
        }
        


        return  ResponseEntity.ok().body(result);
    }
    
    @RequestMapping(value = "/regitrationConfirm", method = RequestMethod.GET)
    public ResponseEntity<?> confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
      
        Locale locale = request.getLocale();
         
        VerificationToken verificationToken = service.getVerificationToken(token);
        if (verificationToken == null) {
           // String message = "invalid token";//messages.getMessage("auth.message.invalidToken", null, locale);
            return new ResponseEntity(new ApiResponse(false, "invalid token"),
                HttpStatus.BAD_REQUEST);
           
        }
         
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return new ResponseEntity(new ApiResponse(false, "token is expired"),
                HttpStatus.BAD_REQUEST);
        } 
         
        user.setEnabled(true); 
        service.saveRegisteredUser(user); 
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/api/users/{username}")
            .buildAndExpand(user.getUsername()).toUri();

       return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
