package com.tasktracker.userservice.ServiceImpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tasktracker.userservice.Entity.Task;
import com.tasktracker.userservice.Entity.User;
import com.tasktracker.userservice.Repository.UserRepository;
import com.tasktracker.userservice.Service.UserService;
import com.tasktracker.userservice.feignclient.TaskClient;

@Service
public class UserSeviceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TaskClient taskClient;

	@Autowired
	JavaMailSender javaMailSender;

	private static final Logger logger = LoggerFactory.getLogger(UserSeviceImpl.class);

	@Override
	public String createUser(User user) {
		try {
			if (!Objects.equals(user.getEmail(), "") && !Objects.equals(user.getName(), "")) {
				if (user.getEmail() != null && user.getName() != null) {
					Optional<User> existUser = userRepository.findByemail(user.getEmail());
					if (existUser.isPresent()) {
						logger.warn("User already exist");
						return "User already exist";
					} else {
						if (user.getPassword().matches("[a-z A-Z 0-9 @$]{8,}+")) {
							String encoded = new BCryptPasswordEncoder().encode(user.getPassword());
							user.setPassword(encoded);
							userRepository.save(user);
							logger.info("User saved successfully");
							return "User saved successfully";
						} else {
							return "password should contain uppercase, lowercase, digit and special character and length must be 8 character";
						}
					}
				} else {
					logger.warn("User can not be  null");
					return "User can not be  null";
				}
			} else {
				logger.warn("User can not be  Empty");
				return "User can not be  Empty";
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return "something went wrong";
	}

	@Override
	public List<User> getAllUsers(Pageable paging) {
		try {
			Page<User> userlist = userRepository.findAll(paging);
			List<User> userList = userlist.getContent();

			List<User> newUserList = userList.stream().map(user -> {
				user.setTask(taskClient.getTasksOfUser(user.getId()));
				user.setOtp(null);
				return user;
			}).collect(Collectors.toList());
			return newUserList;
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return null;
	}

	@Override
	public Object getUserById(Long id) {
		try {
			Optional<User> getUser = userRepository.findById(id);
			if (getUser.isPresent()) {
				User user = getUser.get();
				user.setOtp(null);
				// fetch task by userId
				List<Task> taskList = taskClient.getTasksOfUser(user.getId()).stream()
						.sorted((task1, task2) -> task2.getPriority().compareTo(task1.getPriority()))
						.collect(Collectors.toList());
				// set list of fetched tasks to user
				user.setTask(taskList);
				logger.info("succesfully get the user");
				return user;
			} else {
				logger.warn("invaliad user id");
				return "invaliad user id";
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return "something went wrong";
	}

	@Override
	public List<User> getUsersBetweenDates(Date startDate, Date endDate) {
		try {
			List<User> userList = userRepository.findByCreationDateBetween(startDate, endDate).get();
			if (!userList.isEmpty())
				logger.info("user List is not empty");
			return userList;
		} catch (Exception e) {
			logger.error("exception :{}", e);
		}
		return null;
	}

	@Override
	public User updateUser(Long id, User updateUser) {

		try {
			if (id == null || updateUser == null) {
				throw new IllegalArgumentException("User id and updated user cannot be null");
			}

			User user = userRepository.findById(id).orElse(null);
			if (user == null) {
				throw new IllegalArgumentException("User not found with id: " + id);
			}

			if (updateUser.getName() != null && !updateUser.getName().isEmpty()) {
				user.setName(updateUser.getName());
			}
			if (updateUser.getEmail() != null && !updateUser.getEmail().isEmpty()) {
				user.setEmail(updateUser.getEmail());
			}

			userRepository.save(user);
			return user;
		} catch (Exception e) {
			throw new RuntimeException("Error updating user: " + e.getMessage());
		}

	}

	@Override
	public String deleteUser(Long id) {
		logger.info("finding the id of the User for getUserById");
		Optional<User> userOptional = userRepository.findById(id);
		if (!userOptional.isPresent()) {
			logger.error("User ID is  not present=" + id);
			return "user is not present";
		} else {
			userRepository.deleteById(id);
			logger.info("Uesr with ID " + id + " deleted successfully.");
			return "user deleted sucessfully";
		}
	}

	@Override
	public String uploadImage(String path, MultipartFile file) {
		// TODO Auto-generated method stub

		// File name

		String name = file.getOriginalFilename();
		// File Path

		String filePath = path + File.separator + name;

		// create folder if not created

		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}

		// file copy

		try {

			File files = new File("images/" + file.getOriginalFilename());
			if (files.exists()) {
				System.out.println("file already exist");
			} else {
				Files.copy(file.getInputStream(), Paths.get(filePath));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}

	public void sendSimpleMessage(String email, String password) {
		try {
			Random random = new Random();
			long otp = random.nextInt(1000, 9999);
			Optional<User> user = userRepository.findByemail(email);
			if (user.isPresent() && user.get().getPassword().equals(password)) {
				user.get().setOtp(otp);
				userRepository.save(user.get());
				String body = "your OTP is " + otp;
				String subject = "otp verification";
				SimpleMailMessage message = new SimpleMailMessage();
				message.setFrom("harshit.vyas@intelliatech.com");
				message.setTo(email);
				message.setSubject(subject);
				message.setText(body);
				javaMailSender.send(message);

			}

		} catch (Exception e) {
			logger.error("UserServiceImpl {}", e);
		}
	}

	@Override
	public Boolean checkOtp(String email, Long otp) {

		Optional<User> user = userRepository.findByemail(email);
		if (user.get().getOtp().equals(otp)) {
			user.get().setOtp(null);
			userRepository.save(user.get());
			return true;
		}
		user.get().setOtp(null);
		userRepository.save(user.get());
		return false;

	}

}
