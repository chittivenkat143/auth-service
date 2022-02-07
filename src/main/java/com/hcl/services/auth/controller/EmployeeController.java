package com.hcl.services.auth.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.services.auth.dto.AuthenticationRequest;
import com.hcl.services.auth.dto.AuthenticationResponse;
import com.hcl.services.auth.model.employee.LoginEmployee;
import com.hcl.services.auth.repo.employee.LoginEmployeeRepository;
import com.hcl.services.auth.service.HclEmployeeDetailsService;
import com.hcl.services.auth.utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	@Qualifier("hclEmployeeDetailsService")
	private HclEmployeeDetailsService hclEmployeeDetailsService;
	
	@Autowired
	private LoginEmployeeRepository repository;
	
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception{
		log.info("EC: creating authentication");
		try {
			log.info("EC: AuthenticationManager\t" + request.getUsername() + "|" + request.getPassword());
			authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (AuthenticationException e) {
			log.error("EC: " + e.getMessage());
			throw new Exception("Incorrect username and password ", e);
		}
		
		final UserDetails userDetails = hclEmployeeDetailsService.loadUserByUsername(request.getUsername());
		final String jwtString = jwtUtil.generateToken(userDetails);
		log.info("EC: JWT \t Bearer " + jwtString);
		return ResponseEntity.ok(new AuthenticationResponse(jwtString));
	}
	
	@PostMapping("/loginuser")
	public ResponseEntity<LoginEmployee> getLoginUserDetailsByUsername(@RequestBody AuthenticationRequest request) throws Exception{
		Optional<LoginEmployee> loginEmployee = repository.findByUsername(request.getUsername());
		if(!loginEmployee.isPresent()) {
			throw new Exception("Incorrect username and password ");
		}
		return ResponseEntity.ok(loginEmployee.get());
	}

}
