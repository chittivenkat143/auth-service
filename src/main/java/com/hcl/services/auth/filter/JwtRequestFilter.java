package com.hcl.services.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hcl.services.auth.service.HclEmployeeDetailsService;
import com.hcl.services.auth.service.HclUserDetailsService;
import com.hcl.services.auth.utils.JwtUtil;
import com.hcl.services.auth.utils.UserRole;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	
	@Autowired
	@Qualifier("hclUserDetailsService")
	private HclUserDetailsService hclUserDetailsService;
	
	@Autowired
	@Qualifier("hclEmployeeDetailsService")		
	private HclEmployeeDetailsService hclEmployeeDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		log.info("JRF:doFilterInternal {}", request.toString());
		
		final String authorizationHeader = request.getHeader("Authorization");
		final String userType = request.getHeader("UserType");
		
		log.info("JRF: Auth-Service UserType: {0} | Authorization: {1}", userType, authorizationHeader);
		
		String username = null;
		String jwt = null;
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			username = jwtUtil.extractUsername(jwt);
			log.info("JRF:Condition {0} | {1}", jwt, username);
		}
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			log.info("JRF: Auth-Service SecurityContextHolder");
			
			UserDetails userDetails = null;
			if(userType.equals(UserRole.CUSTOMER.toString())) {
				log.info("JRF: SecurityContextHolder:CUSTOMER {}", request.toString());
				userDetails = this.hclUserDetailsService.loadUserByUsername(username);
			}else {
				log.info("JRF: SecurityContextHolder:EMPLOYEE {}", request.toString());
				userDetails = this.hclEmployeeDetailsService.loadUserByUsername(username);
			}
			
			if(Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
				log.info("JRF: Auth-Service-UserDetails {}", userDetails.toString());
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(jwt, null, userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}		
		filterChain.doFilter(request, response);
	}

}
