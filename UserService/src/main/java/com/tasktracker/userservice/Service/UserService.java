package com.tasktracker.userservice.Service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tasktracker.userservice.Entity.User;

@Service
public interface UserService {

	public String createUser(User user);

	public List<User> getAllUsers(Pageable paging);

	public Object getUserById(Long id);

	public List<User> getUsersBetweenDates(Date startDate, Date endDate);

	public User updateUser(Long id, User updateUser) throws Exception;

	public String deleteUser(Long id);

	public String uploadImage(String path, MultipartFile file);

	public void sendSimpleMessage(String email, String password);

	public Boolean checkOtp(String email, Long otp);

}
