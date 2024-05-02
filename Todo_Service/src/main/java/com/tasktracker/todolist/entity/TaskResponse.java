package com.tasktracker.todolist.entity;

import java.util.List;

import lombok.Data;

@Data
public class TaskResponse<T> {
	
	private T data;
	
	private String message;
	
	private boolean status;

}
