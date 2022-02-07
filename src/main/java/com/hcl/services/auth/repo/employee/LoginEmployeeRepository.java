package com.hcl.services.auth.repo.employee;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.services.auth.model.employee.LoginEmployee;

//@Repository
public interface LoginEmployeeRepository extends JpaRepository<LoginEmployee, Long>{
	Optional<LoginEmployee> findByUsername(String username);			
}
