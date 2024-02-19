package com.admin.usermanagement.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.admin.usermanagement.bean.User;
import com.admin.usermanagement.dao.UserDao;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     private UserDao userDao;  
	public void init() throws ServletException {
		userDao=new UserDao();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action =request.getServletPath();
		switch(action) {
			case "/new":
				showNewForm(request, response);
				break;
			case "/insert":
			try {
				insertUser(request, response);
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
				break;
				
			case "/delete":
			try {
				deleteUser(request, response);
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
				break;
				
			case "/edit":
				showEditForm(request, response);
				break;
				
			case "/update":
			try {
				updateUser(request, response);
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
				break;
				
			default:
			try {
				listUser(request, response);
			} catch (SQLException | ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				break;
		}
		
	}

	private void showNewForm(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher= request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);
	}
	
	// insert function

	private void insertUser(HttpServletRequest request , HttpServletResponse response) throws SQLException, IOException {
		String name =request.getParameter("name");
		String email= request.getParameter("email");
		String country= request.getParameter("country");
		User newUser=new User(name,email,country);
		
		userDao.insertUser(newUser);
		response.sendRedirect("list");
	}
	
	// delete function

	private void deleteUser(HttpServletRequest request , HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		try {
			userDao.deleteUser(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("list");
	}

	// edit function
	private void showEditForm(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		User existingUser;
		try {
			existingUser= userDao.selectUserById(id);
			RequestDispatcher dispatcher=request.getRequestDispatcher("user-form.jsp");
			request.setAttribute("user", existingUser);
			dispatcher.forward(request, response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	// update function 
	private void updateUser(HttpServletRequest request , HttpServletResponse response) throws SQLException, IOException {
		int id= Integer.parseInt(request.getParameter("id"));
		String name =request.getParameter("name");
		String email= request.getParameter("email");
		String country= request.getParameter("country");
		User updatedUser=new User(id,name,email,country);
		
		userDao.updateUser(updatedUser);
		response.sendRedirect("list");
	}
	
	
	// default function 
	
	private void listUser (HttpServletRequest request , HttpServletResponse response) throws SQLException,ServletException, IOException{
		
		try {
			List<User> listUser = userDao.selectAllUsers();
			request.setAttribute("listUser", listUser);
			RequestDispatcher dispatcher=request.getRequestDispatcher("user-list.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
