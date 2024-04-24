package com.task.todolist.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.task.todolist.entity.Task;




@Service
public interface TaskService {
	
	public List<Task>  getAllTask();
	
	public Task getTaskById(Integer id);
	
	public Task addTask(Task task);
	
	public Task updateTask(Task task,Integer id);
	
	public boolean deleteTaskById(Integer id);
	
	public List<Task> getAllRemainningTask();
	
	public List<Task> getTaskByCreationDate(LocalDate creationDate);

	public List<Task> getTaskByCompletionDate(LocalDateTime completionDate);
	
	public List<Task> getByUserId(Long userId);

	String ScheduleNotification();

}
