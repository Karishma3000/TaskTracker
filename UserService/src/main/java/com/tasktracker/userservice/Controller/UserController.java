package com.tasktracker.userservice.Controller;

import java.sql.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tasktracker.userservice.Entity.User;

public interface UserController {

	@PostMapping("/createUser")
	public  String createUser(@RequestBody User user);

	@GetMapping("/getUsers")
	public ResponseEntity<List<User>> getAllUsers(@RequestParam(defaultValue = "5", required = false) Integer pageSize,
			@RequestParam(defaultValue = "0", required = false) Integer page);

	@GetMapping("/getUsersById")
	public ResponseEntity<?> getUserById(@RequestParam Long id);

	@GetMapping("/getUserByDate")
	public ResponseEntity<List<User>> getUsersBetweenDates(@RequestParam Date startDate, @RequestParam Date endDate);

	@PostMapping("/updateUser")
	public ResponseEntity<String> updateUser(@RequestParam Long id, @RequestBody User updateUser) throws Exception;

	@PostMapping("/deleteUser")
	public ResponseEntity<String> deleteUser(@RequestParam Long id);

	@PostMapping("/upload")
	public ResponseEntity<User> fileUpload(@RequestParam("image") MultipartFile image);

	@GetMapping("/login")
	public ResponseEntity<String> sendEmail(@RequestParam("email") String email, @RequestParam("password") String password);

	@GetMapping("/verifyOtp")
	public ResponseEntity<String> verifyOtp(@RequestParam("email") String email, @RequestParam("otp") Long otp);
}
