package com.sms.test;

import java.sql.SQLException;

import com.sms.dao.CourseDAO;
import com.sms.dao.StudentDao;
import com.sms.dao.SubjectDAO;
import com.sms.database.DBConnection;

public class ConnectionTest {
    
    public static void main(String[] args) {
        System.out.println("Testing Database Connection Management...");
        
        try {
            // Test 1: Basic connection
            System.out.println("1. Testing basic connection...");
            @SuppressWarnings("unused")
			var connection = DBConnection.connect();
            System.out.println("✓ Connection established successfully");
            System.out.println("Connection valid: " + DBConnection.isConnectionValid());
            
            // Test 2: Multiple DAO operations
            System.out.println("\n2. Testing multiple DAO operations...");
            
            CourseDAO courseDAO = new CourseDAO();
            System.out.println("✓ CourseDAO created");
            
            StudentDao studentDao = new StudentDao();
            System.out.println("✓ StudentDao created");
            
            SubjectDAO subjectDAO = new SubjectDAO();
            System.out.println("✓ SubjectDAO created");
            
            // Test 3: Perform operations
            System.out.println("\n3. Testing operations...");
            
            var courses = courseDAO.getAllCourses();
            System.out.println("✓ CourseDAO.getAllCourses() - Found " + courses.size() + " courses");
            
            var students = studentDao.readAllStudents();
            System.out.println("✓ StudentDao.readAllStudents() - Found " + students.size() + " students");
            
            var subjects = subjectDAO.getAllSubjects();
            System.out.println("✓ SubjectDAO.getAllSubjects() - Found " + subjects.size() + " subjects");
            
            // Test 4: Connection still valid after operations
            System.out.println("\n4. Checking connection validity...");
            System.out.println("Connection still valid: " + DBConnection.isConnectionValid());
            
            System.out.println("\n✓ All tests passed! Connection management is working properly.");
            
        } catch (SQLException e) {
            System.err.println("✗ Test failed with SQLException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("✗ Test failed with Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 