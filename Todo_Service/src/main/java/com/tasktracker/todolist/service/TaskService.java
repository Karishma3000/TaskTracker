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

	public Task addTask(Task task);

	public List<Task> getAllTask(Pageable paging);

	public Task getTaskById(Integer id);

	public List<Task> getTaskByTitle(String title);

	public List<Task> getTaskByCompletionDate(LocalDateTime completionDate);

	public List<Task> getTaskByCreationDate(LocalDateTime creationDate);

	public List<Task> getAllRemainingTask();

	public List<Task> getIncompleteTask();

	public boolean deleteTaskById(Integer id);

	public Task updateTask(Task task, Integer id);

	public String ScheduleNotification();

}
