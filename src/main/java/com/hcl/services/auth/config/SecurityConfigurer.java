package com.hcl.services.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hcl.services.auth.filter.JwtRequestFilter;
import com.hcl.services.auth.service.HclEmployeeDetailsService;
import com.hcl.services.auth.service.HclUserDetailsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter{
	
	@Autowired
	@Qualifier("hclUserDetailsService")
	private HclUserDetailsService hclUserDetailsService;
	
	@Autowired
	@Qualifier("hclEmployeeDetailsService")		
	private HclEmployeeDetailsService hclEmployeeDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("SC: configure(AuthenticationManager)");
		auth.userDetailsService(hclUserDetailsService)
			.and()
			.userDetailsService(hclEmployeeDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("SC: configure(HttpSecurity)");
		http.csrf().disable()
			.authorizeHttpRequests()
			.antMatchers("/customer/login").permitAll()
			.antMatchers("/employee/login").permitAll()
			.anyRequest().authenticated()
			.and().sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		log.info("SC: authenticationManager()");
		return super.authenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
}
