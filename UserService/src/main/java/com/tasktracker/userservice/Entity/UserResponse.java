package com.tasktracker.userservice.Entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse<T> {
	private String message;
	private List<User> data;
	private boolean Status;
	public UserResponse(String message, List<User> data, boolean status) {
		super();
		this.message = message;
		this.data = data;
		Status = status;
	}
	

	
}
