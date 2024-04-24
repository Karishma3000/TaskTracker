package com.example.UserService.Service;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.UserService.Entity.User;

@Service
public interface UserService {

	String createUser(User user);

	String deleteUser(Long id);

	Object getUserById(Long id);

	List<User> getAllUsers();

	User updateUser(Long id, User updateUser) throws Exception;

	List<User> getUsersBetweenDates(Date startDate, Date endDate);
	
	String uploadImage(String path,MultipartFile file); 

}
