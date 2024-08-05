package com.person;

import jakarta.servlet.ServletConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@WebServlet("/person")
public class person extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection con;

    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "tiger");
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Error initializing servlet", e);
        }
    }

    public void destroy() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        long phoneNumber = Long.parseLong(request.getParameter("PHONE_NO"));
        String gender = request.getParameter("Gender");
        String[] hobbiesArray = request.getParameterValues("Hobbies");
        String hobb ="";
        if(hobbiesArray !=null) {
        	for(String hobby : hobbiesArray ) {
        		hobb = hobb+" , "+hobby;
        	}
        }
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO person  VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setLong(3, phoneNumber);
            ps.setString(4, gender);
            PrintWriter pw = response.getWriter();
           
            ps.setString(5,hobb );
            int rowsAffected = ps.executeUpdate();
             pw.println("<html><body bgcolor=pink text=black padding=30px margin=20px ><center><h1>");
             pw.println(" "+hobb+" ");

             if (rowsAffected > 0) {
                pw.println("<h1>Successful</h1>");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to insert data");
            }
            pw.println("</h1></center></body></html>");
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}
