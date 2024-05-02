package com.tasktracker.todolist.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tasktracker.todolist.entity.Task;
import com.tasktracker.todolist.entity.TaskResponse;
import com.tasktracker.todolist.repository.TaskDao;
import com.tasktracker.todolist.repository.TaskRepository;
import com.tasktracker.todolist.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	TaskDao taskDao;

	private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

	@Override
	public TaskResponse<Task> getByUserId(Long userId) {
		TaskResponse<Task> taskResponse =  new TaskResponse<Task>();
		try {
			List<Task> taskList = taskRepository.findByuserId(userId);
			if (!taskList.isEmpty()) {
				
				taskResponse.setData( taskList.stream().sorted((task1, task2) -> task2.getPriority().compareTo(task1.getPriority()))
						.collect(Collectors.toList()));
				taskResponse.setMessage("success");
				taskResponse.setStatusCode(HttpStatus.OK.value());
				return taskResponse;
			}
		} catch (Exception e) {
			log.error("exception " + e.toString());
		}
		taskResponse.setMessage("no data found");
		return taskResponse;

	}

	@Override
	public Task addTask(Task task) {
		Task newTask = null;
		log.info("task :{}", task);
		try {
			if (task.getTitle() != null && task.getUserId() != null) {
				Optional<Task> existingTask = taskRepository.findTaskByTitleAndUserId(task.getTitle().toLowerCase(),
						task.getUserId());

				log.info("existingTask :{}", existingTask);
				if (existingTask.isEmpty()) {
					log.info("existingTask is empty we can add new task");

					newTask.setTitle(task.getTitle().toLowerCase());
					newTask.setUserId(task.getUserId());
					newTask.setDescription(task.getDescription());
					newTask.setCompletionDate(task.getCompletionDate());
					newTask.setPriority(task.getPriority());
					newTask.setStatus(task.getStatus());
					newTask.setRating(task.getRating());
					newTask.setTodoType(task.getTodoType());
					newTask.setTags(task.getTags());
					return taskRepository.save(newTask);
				}
			}

		} catch (Exception e) {
			log.error("exception in addTask : {}", e);
		}
		log.info("task already exist");
		return newTask;
	}

	@Override
	public List<Task> getAllTask(Pageable paging) {
		List<Task> list = new ArrayList<Task>();
		try {
			Page<Task> taskList = taskRepository.findAll(paging);
			list = taskList.getContent();
			log.info("list of tasks:{} ", taskList);
			if (!list.isEmpty()) {
				log.info("taskList is not empty");
				return list.stream().sorted((task1, task2) -> task2.getPriority().compareTo(task1.getPriority()))
						.collect(Collectors.toList());
			}

		} catch (Exception e) {
			log.error("exception in getAllTask :{} ", e);
		}

		return list;

	}

	@Override
	public Task getTaskById(Integer id) {
		log.info("Id : {}", id);
		Task taskObj = null;
		try {
			Optional<Task> task = taskRepository.findById(id);
			if (!task.isEmpty()) {
				taskObj = task.get();
				log.info("task is not empty");
				return taskObj;
			}
		} catch (Exception e) {
			log.error("exception in getTaskById : {}", e);
		}

		return taskObj;
	}

	@Override
	public List<Task> getTaskByTitle(String title) {
		List<Task> list = new ArrayList<Task>();
		try {
			list = taskDao.findTaskByTitle(title);
			if (!list.isEmpty())
				return list;
		} catch (Exception e) {
			log.error("exception {}", e);
		}
		return list;

	}

	@Override
	public List<Task> getTaskByCompletionDate(LocalDateTime completionDate) {
		List<Task> task = new ArrayList<Task>();
		try {
			task = taskRepository.findTaskByCompletionDate(completionDate);
			log.info("taskList:{} ", task);
			if (!task.isEmpty()) {

				log.info("taskList:{} ", task);

				return task;

			}
		} catch (Exception e) {
			log.error("exception in getTaskByCompletionDate:{} ", e);
		}
		log.info("task not found");
		return task;
	}

	@Override
	public List<Task> getTaskByCreationDate(LocalDateTime creationDate) {
		List<Task> task = new ArrayList<Task>();
		try {
			task = taskRepository.findTaskByCreationDate(creationDate);
			log.info("taskList :{}", task);
			if (!task.isEmpty()) {
				return task;
			}
		} catch (Exception e) {
			log.error("exception in getTaskByCreationDate : {}", e);
		}
		log.info("task not found");
		return task;
	}

	@Override
	public List<Task> getAllRemainingTask() {
		List<Task> taskList = new ArrayList<Task>();
		try {
			LocalDate date = LocalDate.now();
			taskList = taskRepository.getAllRemainingTask(date);
			log.info("taskList " + taskList);
			if (!taskList.isEmpty()) {
				log.info("taskList is not empty :{}", taskList);
				return taskList;
			}

		} catch (Exception e) {
			log.error("exception in updateTask :{} ", e);
		}
		log.info("tasks not found");
		return taskList;
	}

	@Override
	public List<Task> getIncompleteTask() {
		List<Task> taskList = new ArrayList<Task>();
		try {
			taskList = taskRepository.findIncomplteTask();
			if (!taskList.isEmpty())
				return taskList;
		} catch (Exception e) {
			log.error("exception {}", e);
		}

		return taskList;
	}

	@Override
	public boolean deleteTaskById(Integer id) {

		try {
			log.info("id : {}", id);
			Optional<Task> task = taskRepository.findById(id);
			if (task.isPresent()) {
				log.info("task is not empty ; {}", task);
				taskRepository.deleteById(id);
				return true;
			}
		} catch (Exception e) {
			log.error("exception :{} ", e);
		}
		log.info("task not found with id : {}", id);
		return false;

	}

	@Override
	public Task updateTask(Task newTask, Integer id) {
		Task taskObj = new Task();
		try {
			if (id != null && newTask != null) {
				Optional<Task> task = taskRepository.findById(id);
				log.info("update task :{}", task);

				if (task.isPresent()) {
					log.info(" task is not null :{}", task);
					taskObj = task.get();
					if (newTask.getDescription() != null) {
						log.info(" description is not null :{}", newTask.getDescription());
						taskObj.setDescription(newTask.getDescription());
					}
					if (newTask.getCompletionDate() != null) {
						log.info(" completionDate is not null :{}", newTask.getCompletionDate());
						taskObj.getCompletionDateHistory().add(task.get().getCompletionDate());
						taskObj.setCompletionDate(newTask.getCompletionDate());
					}

					return taskRepository.save(taskObj);
				}
			}

		} catch (Exception e) {
			log.error("exception in update task :{} ", e);
		}
		log.info("task not found with id : {}", id);
		return taskObj;
	}

	@Override
	// @Scheduled(fixedRate = 6000)
	public String ScheduleNotification() {
		System.out.println("scheduler");
		LocalDateTime currentTime = LocalDateTime.now();

		List<Task> taskList = taskRepository.findAll();
		for (Task task : taskList) {

			System.out.println("cirretnt time " + currentTime);
			LocalDateTime complitiontime = task.getCompletionDate();
			System.out.println(complitiontime);
			System.out.println(task.getCompletionDate());
//			if (task.getCompletionDate().isAfter(currentTime) && task.getCompletionDate().isBefore(complitiontime)) {
			if (currentTime.withSecond(0).withNano(0).equals(complitiontime.minusHours(1).withSecond(0).withNano(0))) {
				System.out.println("your task" + task.getTitle() + " is near complition date");
				return "your task" + task.getTitle() + " is near complition date";
			}
		}
		return "scheduler";
	}

}
