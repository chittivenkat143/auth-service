package com.hcl.services.auth.model.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loginemployee")
public class LoginEmployee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loginEmployeeId")
	private Long loginEmployeeId;
	
	@Column(name = "username", unique = true)
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "active")
	private boolean active;
	@Column(name = "roles")
	private String roles;
	
	@Column(name = "emailId", unique = true)
	private String mailId;
	
	@Column(name = "phoneNumber", unique = true)
	private String phoneNo;
	
}
