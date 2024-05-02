package com.tasktracker.userservice.Controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tasktracker.userservice.Entity.User;
import com.tasktracker.userservice.Service.UserService;

@RestController
@RequestMapping("/user")
public class UserControllerImpl implements UserController {

	static final Logger logger = LogManager.getLogger(UserControllerImpl.class.getName());

	@Autowired
	private UserService userService;

	@Value("${project.image}")
	private String path;

	@Override
	public String createUser(User user) {
		String existEmployee = null;
		try {
			if (user != null) {
				logger.info("Creating User: {}", user.getName());
				existEmployee = userService.createUser(user);
			} else {
				logger.info("User is null");
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return existEmployee;
	}

	@Override
	public ResponseEntity<List<User>> getAllUsers(Integer pageSize, Integer page) {
		Pageable paging = PageRequest.of(page, pageSize);
		List<User> userList = userService.getAllUsers(paging);
		if(!userList.isEmpty()) {
			return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<List<User>>(userList, HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<?> getUserById(Long id) {
		if (id != 0) {
			logger.info("get user by {}", id);
			Object existEmployee = userService.getUserById(id);
			return ResponseEntity.status(HttpStatus.CREATED).body(existEmployee);
		} else {
			return ResponseEntity.status(HttpStatus.CREATED).body("please enter the user id");
		}
	}

	@Override
	public ResponseEntity<List<User>> getUsersBetweenDates(Date startDate, Date endDate) {
		List<User> userList= new ArrayList<User>();
		if (startDate != null & endDate != null) {
			logger.info("get users present between dates");
			userList= userService.getUsersBetweenDates(startDate, endDate);
			return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
		} else {
			logger.info("start and end dates must not be null");
			return new ResponseEntity<List<User>>(userList, HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<String> updateUser(Long id, User updateUser) throws Exception {
		if (id != 0 && updateUser != null) {
			logger.info("User updation called");
			User user = userService.updateUser(id, updateUser);
			if (user != null) {
				return new ResponseEntity<String>("user updated", HttpStatus.OK);
			}
		}
		return new ResponseEntity<String>("user not updated", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<String> deleteUser(Long id) {
		try {
			if (id != 0) {
				logger.info("Deleting User with id: {}", id);
				String existEmployee = userService.deleteUser(id);
				return ResponseEntity.status(HttpStatus.OK).body(existEmployee);
			} else {
				logger.warn("please enter the id");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return new ResponseEntity<String>("user not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<User> fileUpload(MultipartFile image) {
		try {
			userService.uploadImage(path, image);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@Override
	public ResponseEntity<String> sendEmail(String email, String password) {
		try {
			logger.info("usercontroller : sendEmail {}", email);
			userService.sendSimpleMessage(email, password);
			return new ResponseEntity<String>("mail send", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("usercontroller : sendEmail {}", e);
		}
		return new ResponseEntity<String>("mail could not send", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<String> verifyOtp(String email, Long otp) {
		try {
			logger.info("usercontroller : verifyotp eamail : {} otp : {} ", email, otp);
			if (userService.checkOtp(email, otp)) {
				return new ResponseEntity<String>("otp matched ", HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.info("usercontroller : verifyOyp {}", e);
		}

		return new ResponseEntity<String>("invalid otp ", HttpStatus.NOT_FOUND);
	}

}
