package com.admin.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.admin.usermanagement.bean.User;
public class UserDao {
	
	private String jdbcURL = "jdbc:mysql://localhost:3306/userdb?useSSL=false";
	private String jdbcUserName="root";
	private String jdbcPassword="mohammed@#$882001";
	private String jdbcDriver="com.mysql.jdbc.Driver";
	
	private static final String  INSERT_USERS ="INSERT INTO users"+"(name,email,country) VALUES"+" (?, ?, ?)";
	private static final String  SELECT_USER_BY_ID ="select id,name,email,country from users where id=?;";
	private static final String  SELECT_ALL_USERS ="select * from users;";
	private static final String  DELETE_USER ="delete from users where id = ?;";
	private static final String  UPDATE_USER ="update users set name =?,email=?,country=? where id = ?;";
	
	
	public UserDao() {
		
	}
	
	protected Connection getConnection() {
		Connection connection =null;
		try {
			Class.forName(jdbcDriver);
			connection=DriverManager.getConnection(jdbcURL,jdbcUserName,jdbcPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	//insert user 
	public void insertUser(User user)throws SQLException {
		System.out.println(INSERT_USERS);
		try (Connection connection =getConnection();
				PreparedStatement preparedStatement=connection.prepareStatement(INSERT_USERS)){
			
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}
	
	//select user by id 
	public User selectUserById(int id) {
		User user =null;
		// 1. Establish a connection "USING TRY WITH RESOURCE" to avoid self closing resource .
		try (Connection connection =getConnection();
				// 2. Create a statement using connection object .
				PreparedStatement preparedStatement=connection.prepareStatement(SELECT_USER_BY_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			// 3. Execute the query.
			ResultSet rs = preparedStatement.executeQuery();
			// 4. Process the ResultSet object.
			while(rs.next()) {
				String name=rs.getString("name");
				String emil=rs.getString("email");
				String country=rs.getString("country");
				user=new User(id,name,emil,country);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return user;
	}
	
	// select all  users 
	public List<User> selectAllUsers(){
		List<User> users=new ArrayList<>();
		// 1. Establish a connection "USING TRY WITH RESOURCE" to avoid self closing resource .
		try (Connection connection =getConnection();
				
		// 2. Create a statement using connection object .
				
				PreparedStatement preparedStatement=connection.prepareStatement(SELECT_ALL_USERS);) {
			
			System.out.println(preparedStatement);
		// 3. Execute the query.
			ResultSet rs =preparedStatement.executeQuery();
		// 4. Process the ResultSet object.
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id,name,email,country));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return users;
	}
	
	
	//update user 
	public boolean updateUser(User user)throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement= connection.prepareStatement(UPDATE_USER);) {
			System.out.println("updated User :"+preparedStatement);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			preparedStatement.setInt(4, user.getId());
			
			rowUpdated = preparedStatement.executeUpdate()>0;
		} 
		return rowUpdated;
	}
	
	
	//delete user 
	public boolean deleteUser(int id)throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement= connection.prepareStatement(DELETE_USER);) {
			System.out.println("Deleted User :"+preparedStatement);
			preparedStatement.setInt(1, id);
			rowDeleted = preparedStatement.executeUpdate()>0;
		} 
		return rowDeleted;
	}
	
	
	
	//implement SQL exception Function
	private void printSQLException(SQLException ex) {
		for(Throwable e:ex) {
			if(e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQL State: "+ ((SQLException)e).getSQLState());
				System.err.println("Error Code: "+ ((SQLException)e).getErrorCode());
				System.err.println("Message: "+ e.getMessage());
				Throwable t =ex.getCause();
				while(t!=null) {
					System.out.println("Cause: "+t);
					t = t.getCause();
				}
			}
		}
	}
	
}
