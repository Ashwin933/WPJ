package com.cdac.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.cdac.pojos.User;
import static com.cdac.utils.DBUtils.*;

public class UserDaoImpl implements UserDao {
	// dependency - DB connection - DBUtils
	private Connection connection;
	private PreparedStatement pst1,pst2,pst3,pst4,pst5;

	// def ctor - will be invoked by layer above - Tester (dyn web comp)
	public UserDaoImpl() throws SQLException {
		// establish db cn
		openConnection();
		connection = getConnection();
		// creates PreparedStament
		String sql = "select * from users where email=? and password=?";
		pst1 = connection.prepareStatement(sql);
		//registration
		pst2 = connection.prepareStatement("Insert INTO users(first_name, last_name, email, password, dob, status, role) VALUES (?, ?, ?, ?, ?, ?, ?)");
		//change password
		pst3 = connection.prepareStatement("UPDATE users SET password=? WHERE email=? AND password=?");
		// delete user
		pst4 = connection.prepareStatement("DELETE FROM users WHERE id=?");
		//getAllUser
		pst5 = connection.prepareStatement("SELECT * FROM users");
		System.out.println("user dao created !");
	}

	@Override
	public User signIn(String email, String pwd) throws SQLException {
		// 1. set IN params
		pst1.setString(1, email);
		pst1.setString(2, pwd);
		// 2. execute query n process RST
		try (ResultSet rst = pst1.executeQuery()) {
			if (rst.next()) {
				//success
				return new User(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5),
						rst.getDate(6), rst.getBoolean(7), rst.getString(8));
			}
			return null;
		}
	}
	
	@Override
	public String voterRegistration(User user) throws SQLException {
		int age = java.time.Period.between(user.getDob().toLocalDate(),java.time.LocalDate.now()).getYears();
		if(age<21) {
			return "Registration failed: Age must be greater than 21";
		}
		
		pst2.setString(1,user.getFirstName());
		pst2.setString(2,user.getLastName());
		pst2.setString(3, user.getEmail());
		pst2.setString(4,user.getPassword());
		pst2.setDate(5, user.getDob());
		pst2.setBoolean(6, user.isStatus());
		pst2.setString(7, user.getUserRole());
		
		int rows = pst2.executeUpdate();
		return rows == 1 ? "Registration successful!!!" : "Registration failed!";
	}
	
	@Override
	public String changePassword(String email, String oldPwd, String newPwd) throws Exception {
		pst3.setString(1,newPwd);
		pst3.setString(2,email);
		pst3.setString(3,oldPwd);
		
		int rows = pst3.executeUpdate();
		return rows == 1 ? "Password updated successfully!" : "Invalid credentials!";
	}
	
	@Override
	public String deleteUser(int id) throws Exception {
		pst4.setInt(1, id);
		int rows = pst4.executeUpdate();
		return rows == 1 ? "User deleted Successfully!!!" : "User not found!";
	}
	
	@Override
	public List<User> getAllUser() throws Exception {
		List<User> ul = new ArrayList<>();
		try(ResultSet rs = pst5.executeQuery()){
			while(rs.next()) {
				User user = new User(
						rs.getInt(1),      // id
						rs.getString(2),   // first_name
						rs.getString(3),   // last_name
						rs.getString(4),   // email
						rs.getString(5),   // password
						rs.getDate(6),     // dob
						rs.getBoolean(7),  // status
						rs.getString(8)    // role
					
				);
				ul.add(user);
			}
		}
		return ul;
	}
	
	// clean up - close db resources (PST , DB cn)
	public void cleanUp() throws SQLException {
		if (pst1 != null) pst1.close();
		if (pst2 != null) pst2.close();
		if (pst3 != null) pst3.close();
		if (pst4 != null) pst4.close();

		closeConnection();
	}




	

	

	

	

}
