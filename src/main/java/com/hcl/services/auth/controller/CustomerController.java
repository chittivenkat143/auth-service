package com.hcl.services.auth.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.services.auth.dto.AuthenticationRequest;
import com.hcl.services.auth.dto.AuthenticationResponse;
import com.hcl.services.auth.model.user.LoginUser;
import com.hcl.services.auth.repo.user.LoginUserRepository;
import com.hcl.services.auth.service.HclUserDetailsService;
import com.hcl.services.auth.utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	@Qualifier("hclUserDetailsService")
	private HclUserDetailsService hclUserDetailsService;
	
	@Autowired
	private LoginUserRepository repository;
	
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception{
		log.info("GC: creating authentication");
		try {
			log.info("GC: AuthenticationManager\t" + request.getUsername() + "|" + request.getPassword());
			authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (AuthenticationException e) {
			log.error("GC: " + e.getMessage());
			throw new Exception("Incorrect username and password ", e);
		}
		
		final UserDetails userDetails = hclUserDetailsService.loadUserByUsername(request.getUsername());
		final String jwtString = jwtUtil.generateToken(userDetails);
		log.info("GC: JWT \t Bearer " + jwtString);
		return ResponseEntity.ok(new AuthenticationResponse(jwtString));
	}
	
	@PostMapping("/loginuser")
	public ResponseEntity<LoginUser> getLoginUserDetailsByUsername(@RequestBody AuthenticationRequest request) throws Exception{
		Optional<LoginUser> loginUser = repository.findByUsername(request.getUsername());
		if(!loginUser.isPresent()) {
			throw new Exception("Incorrect username and password ");
		}
		return ResponseEntity.ok(loginUser.get());
	}
	
}
