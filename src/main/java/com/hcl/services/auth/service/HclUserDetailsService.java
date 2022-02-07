package com.hcl.services.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hcl.services.auth.model.HclUserDetails;
import com.hcl.services.auth.model.user.LoginUser;
import com.hcl.services.auth.repo.user.LoginUserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Qualifier("hclUserDetailsService")
@Service
public class HclUserDetailsService implements UserDetailsService{
	
	@Autowired
	private LoginUserRepository repositoryLogin;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("HUDS: loadUserByUsername(" + username + ")");
		Optional<LoginUser> user = repositoryLogin.findByUsername(username);		
		user.orElseThrow(()-> new UsernameNotFoundException("Not found " +username));		
		return user.map(HclUserDetails::new).get();
	}

}
