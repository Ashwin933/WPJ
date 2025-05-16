package com.cdac.dao;

import java.sql.SQLException;
import java.util.List;

import com.cdac.pojos.User;

public interface UserDao {
	// ya ha se ham database connectivity karte hai
//sign in using layer
	// ....... signIn(em,pass) throws SE
	List<User> getAllUser() throws Exception;
	User signIn(String email,String pwd) throws SQLException;
	public String voterRegistration(User user) throws SQLException;
	public String changePassword(String email,String oldPwd,String newPwd) throws Exception;
	public String deleteUser(int id) throws Exception;
}
