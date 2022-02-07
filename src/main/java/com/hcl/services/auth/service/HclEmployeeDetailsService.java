package com.hcl.services.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hcl.services.auth.model.HclEmployeeDetails;
import com.hcl.services.auth.model.employee.LoginEmployee;
import com.hcl.services.auth.repo.employee.LoginEmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Qualifier("hclEmployeeDetailsService")
@Service
public class HclEmployeeDetailsService implements UserDetailsService{
	
	@Autowired
	private LoginEmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("HUDS: Employees Login by username: " + username);
		Optional<LoginEmployee> employee = employeeRepository.findByUsername(username);
		employee.orElseThrow(()-> new UsernameNotFoundException("Employee Not found " +username));
		return employee.map(HclEmployeeDetails::new).get();
	}

}
