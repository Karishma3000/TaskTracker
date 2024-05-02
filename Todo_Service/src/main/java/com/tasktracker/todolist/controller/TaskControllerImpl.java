package com.tasktracker.todolist.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tasktracker.todolist.entity.Task;
import com.tasktracker.todolist.entity.TaskResponse;
import com.tasktracker.todolist.service.TaskService;

@RestController
@RequestMapping("/task")
public class TaskControllerImpl implements TaskController {

	@Autowired
	private TaskService taskService;

	static final Logger log = LogManager.getLogger(TaskControllerImpl.class.getName());

	@Override
	public ResponseEntity<TaskResponse<Task>> getTaskByUserId(Long userId) {
		try {
			TaskResponse<Task> taskResponse = taskService.getByUserId(userId);
			 if(taskResponse.getMessage().equals("success"))
				return new ResponseEntity<TaskResponse<Task>>(taskResponse, HttpStatus.OK);
			 else 
				 return new ResponseEntity<TaskResponse<Task>>(taskResponse, HttpStatus.OK);
			
		} catch (Exception e) {
			log.info("exception : {}", e);
		}

		return new ResponseEntity<TaskResponse<Task>>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<String> addTask(Task task) {
		try {
			if (task.getTitle() == null || task.getDescription() == null || task.getCompletionDate() == null
					|| task.getUserId() == null) {
				return new ResponseEntity<String>("task not added", HttpStatus.BAD_REQUEST);
			}
			log.info("adding new task : {}", task);
			Task addedTask = taskService.addTask(task);
			if (addedTask != null) {
				return new ResponseEntity<String>("task added", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<String>("task not added", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			log.info("exception : {}", e);
		}
		return new ResponseEntity<String>("task not added", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<List<Task>> findAllTask(Integer pageSize, Integer page) {
		try {
			log.info("fetching all tasks");
			Pageable paging = PageRequest.of(page, pageSize);
			List<Task> taskList = taskService.getAllTask(paging);
			log.info("taskList :{} ", taskList);
			if (!taskList.isEmpty()) {
				return new ResponseEntity<List<Task>>(taskList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<Task>>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.info("exception : {}", e);
		}
		return new ResponseEntity<List<Task>>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<Task> getTaskById(Integer id) {
		try {
			if (id != 0 && id != null) {
				log.info("get Task By Id : {}", id);
				Task task = taskService.getTaskById(id);
				log.info("task :{}", task);
				if (task != null) {
					return new ResponseEntity<Task>(task, HttpStatus.FOUND);
				} else {
					return new ResponseEntity<Task>(HttpStatus.NOT_FOUND);
				}
			}
		} catch (Exception e) {
			log.error("exception : {}", e);
		}
		return new ResponseEntity<Task>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<List<Task>> getTaskByTitle(String title) {

		try {
			List<Task> task = taskService.getTaskByTitle(title);
			return new ResponseEntity<List<Task>>(task, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
		}
		return new ResponseEntity<List<Task>>(HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<List<Task>> getTaskByCompletionDate(LocalDateTime completionDate) {
		try {
			List<Task> taskList = taskService.getTaskByCompletionDate(completionDate);
			log.info("taskList:{} ", taskList);
			if (!taskList.isEmpty()) {
				return new ResponseEntity<List<Task>>(taskList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<Task>>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			log.error("exception : {}", e);
		}
		return new ResponseEntity<List<Task>>(HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<List<Task>> getTaskByCreationDate(LocalDateTime creationDate) {
		try {
			List<Task> taskList = taskService.getTaskByCreationDate(creationDate);
			log.info("taskList :{} ", taskList);
			if (!taskList.isEmpty()) {
				return new ResponseEntity<List<Task>>(taskList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<Task>>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			log.error("exception : {}", e);
		}
		return new ResponseEntity<List<Task>>(HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<List<Task>> getRemainingTask() {
		try {
			List<Task> taskList = taskService.getAllRemainingTask();
			log.info("taskList :{}", taskList);
			if (!taskList.isEmpty()) {
				return new ResponseEntity<List<Task>>(taskList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<Task>>(HttpStatus.NO_CONTENT);
			}

		} catch (Exception e) {
			log.error("exception : {}", e);
		}
		return new ResponseEntity<List<Task>>(HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<List<Task>> getIncompleteTask() {

		try {
			List<Task> list = taskService.getIncompleteTask();
			return new ResponseEntity<List<Task>>(list, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
		}
		return new ResponseEntity<List<Task>>(HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<String> deleteTaskById(Integer id) {
		try {
			log.info("deleting task by id : {} ", id);
			if (taskService.deleteTaskById(id)) {
				return new ResponseEntity<String>("Task deleted successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Task not with given id not exist ", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.info("exception : {}", e);
		}
		return new ResponseEntity<String>("Task  not exist  with given id", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<Task> updateTask(Task task, Integer id) {
		try {

			log.info("updating the task :{}", task);
			Task updatedTask = taskService.getTaskById(id);
			if (updatedTask != null) {
				return new ResponseEntity<Task>(taskService.updateTask(task, id), HttpStatus.OK);
			} else {
				return new ResponseEntity<Task>(task, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			log.info("exception : {}", e);
		}
		return new ResponseEntity<Task>(HttpStatus.NOT_FOUND);
	}

	@Override
	public String schedularNotification() {
		try {
			String string = taskService.ScheduleNotification();
			return string;
		} catch (Exception e) {
			log.error(e.toString());
		}
		return "scheduler not working";
	}

}
