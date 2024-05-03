package com.tasktracker.todolist.entity;

import java.util.List;

import lombok.Data;

@Data
public class TaskResponse<T> {
	
	private List<Task> data;
	
	private String message;
	
	private boolean status;

}
