package com.fileoperations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fileoperations.dto.JWTAuthResponse;
import com.fileoperations.dto.LoginDto;
import com.fileoperations.dto.SignUpDto;
import com.fileoperations.entity.User;
import com.fileoperations.repository.UserRepository;
import com.fileoperations.security.JwtTokenProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	  	@Autowired
	    private AuthenticationManager authenticationManager;
	    @Autowired
	    private UserRepository userRepository;
	    @Autowired
	    private PasswordEncoder passwordEncoder;
	    @Autowired
	    private JwtTokenProvider jwtTokenProvider;

		@Operation(summary = "Signin")
		@ApiResponses(value = {
				@ApiResponse(responseCode = "200", description = "File Recieved Succesfully"),
				@ApiResponse(responseCode = "400", description = "Bad Request"),
		})
	    @PostMapping("/signin")
	    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
	        Authentication authentication= authenticationManager.authenticate
	                (new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),loginDto.getPassword()));
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        String token=jwtTokenProvider.generateToken(authentication);
	        return  ResponseEntity.ok(new JWTAuthResponse(token));

	    }
	    
		
		@Operation(summary = "Save new user")
		@ApiResponses(value = {
				@ApiResponse(responseCode = "200", description = "New User Saved Successfuly"),
				@ApiResponse(responseCode = "400", description = "Bad Request"),
		})
	    @PostMapping("/signup")
		@Transactional
	    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
	        if(userRepository.existsByUsername(signUpDto.getUsername())){
	            return new ResponseEntity<>("Username is already taken!.",HttpStatus.BAD_REQUEST);
	        }
	        if(userRepository.existsByEmail(signUpDto.getEmail())){
	            return new ResponseEntity<>("Email is already taken!.",HttpStatus.BAD_REQUEST);
	        }
	        User user = new User();
	        user.setEmail(signUpDto.getEmail());
	        user.setUsername(signUpDto.getUsername());
	        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

	        userRepository.save(user);

	        return new ResponseEntity<>("User registered succesfully",HttpStatus.OK);
	    }


}
