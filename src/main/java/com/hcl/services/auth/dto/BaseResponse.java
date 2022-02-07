package com.hcl.services.auth.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class BaseResponse extends ResponseEntity<Object>{
	
	private HttpStatus status;
	private MultiValueMap<String, String> headersValues;
	private Object response;
	private int rawStatus;

	public BaseResponse(HttpStatus status) {
		this(status,  null);
	}

	public BaseResponse(HttpStatus status, Object body) {
		this(body, null, status);
	}

	public BaseResponse(Object body, MultiValueMap<String, String> headers, HttpStatus status) {
		this(body, headers, status.value());
	}

	public BaseResponse(Object body, MultiValueMap<String, String> headers, int rawStatus) {
		super(body, headers, rawStatus);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public MultiValueMap<String, String> getHeadersValues() {
		return headersValues;
	}

	public void setHeadersValues(MultiValueMap<String, String> headersValues) {
		this.headersValues = headersValues;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public int getRawStatus() {
		return rawStatus;
	}

	public void setRawStatus(int rawStatus) {
		this.rawStatus = rawStatus;
	}
	
}
