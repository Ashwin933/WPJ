package com.cdac.tester;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import com.cdac.dao.UserDaoImpl;
import com.cdac.pojos.User;

public class TestLayeredApplication {

	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			// 1.create dao instace - create dependency
			UserDaoImpl dao = new UserDaoImpl();
			boolean exit = false;
			// accept em n password
			while(!exit) {
				System.out.println("\nOptions:");
				System.out.println("1. Sign In");
				System.out.println("2. Register");
				System.out.println("3. Get All Users");
				System.out.println("4. Delete User");
				System.out.println("5. Exit");
				System.out.print("Enter choice: ");
				
				switch(sc.nextInt()) {
				case 1:
					System.out.print("Enter email and password: ");
					User user = dao.signIn(sc.next(),sc.next());
					if(user != null) {
						System.out.println("Login success");
						System.out.println("Hello, " + user.getFirstName() + " " + user.getLastName());
					}else {
						System.out.println("Invalid email or password!");
					}
					break;
					
				case 2:
					System.out.println("Enter firstName, lastName, email, password, dob(yyyy-mm-dd), status(true/false), role");
					User newUser = new User(
						sc.nextInt(),sc.next(),sc.next(),sc.next(),sc.next(),
						Date.valueOf(sc.next()),sc.nextBoolean(),sc.next());
					String result = dao.voterRegistration(newUser);
					System.out.println(result);
					break;
				
				case 3:
					List<User> users = dao.getAllUser();
					users.forEach(System.out::println);
					break;
				
				case 4:
					System.out.print("Enter user ID to delete: ");
					System.out.println(dao.deleteUser(sc.nextInt()));
					break;
					
				case 5:
					dao.cleanUp();
					exit = true;
					System.out.println("Exiting...");
					break;
					
				default :
					System.out.println("Invalid choice!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
