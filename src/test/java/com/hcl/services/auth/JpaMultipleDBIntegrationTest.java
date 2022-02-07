package com.hcl.services.auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.services.auth.model.employee.LoginEmployee;
import com.hcl.services.auth.model.user.LoginUser;
import com.hcl.services.auth.repo.employee.LoginEmployeeRepository;
import com.hcl.services.auth.repo.user.LoginUserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableTransactionManagement
class JpaMultipleDBIntegrationTest {
	
	@Autowired
	private LoginUserRepository userRepository;
	
	@Autowired
	private LoginEmployeeRepository employeeRepository;
	
	@Test
	@Transactional("userTransactionManager")
	void whenCreatingUser_thenCreated() {
		LoginUser user = new LoginUser();
		user.setUsername("vishithamanu");
		user.setPassword("password123");
		user.setActive(true);
		user.setCustomerId(10009l);
		user.setRoles("CUSTOMER");
		user = userRepository.save(user);
		
		assertNotNull(userRepository.findById(user.getLoginUserId()));
	}
	
	@Test
	@Transactional("employeeTransactionManager")
	void whenCreatingEmployee_thenCreated() {
		LoginEmployee emp = new LoginEmployee();
		emp.setUsername("prasadt");
		emp.setPassword("prasadpass123");
		emp.setMailId("prasad@dbs.com");
		emp.setPhoneNo("9876543211");
		emp.setActive(true);
		emp.setRoles("EMPLOYEE");
		emp = employeeRepository.save(emp);
		
		assertNotNull(userRepository.findById(emp.getLoginEmployeeId()));
	}

}
