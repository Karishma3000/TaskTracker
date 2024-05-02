package com.tasktracker.todolist.service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tasktracker.todolist.entity.Task;
import com.tasktracker.todolist.entity.TaskResponse;

@Service
public interface TaskService {

	public TaskResponse<Task> getByUserId(Long userId);

	public TaskResponse<Task> addTask(Task task);

	public TaskResponse<List<Task>> getAllTask(Pageable paging);

	public TaskResponse<Task> getTaskById(Integer id);

	public TaskResponse<List<Task>> getTaskByTitle(String title);

	public TaskResponse<List<Task>> getTaskByCompletionDate(LocalDateTime completionDate);

	public TaskResponse<List<Task>> getTaskByCreationDate(LocalDateTime creationDate);

	public TaskResponse<List<Task>> getAllRemainingTask();

	public TaskResponse<List<Task>> getIncompleteTask();

	public boolean deleteTaskById(Integer id);

	public TaskResponse<Task> updateTask(Task task, Integer id);

	public String ScheduleNotification();

}
