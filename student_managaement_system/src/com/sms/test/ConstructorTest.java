package com.sms.test;

import java.sql.SQLException;

import com.sms.controller.CourseController;
import com.sms.controller.DashboardController;
import com.sms.controller.FeeController;
import com.sms.controller.StudentController;
import com.sms.controller.SubjectController;
import com.sms.controller.TeacherController;
import com.sms.service.CourseService;
import com.sms.service.DashboardService;
import com.sms.service.FeeService;
import com.sms.service.StudentService;
import com.sms.service.SubjectService;
import com.sms.service.TeacherService;

public class ConstructorTest {

	public static void main(String[] args) {
		System.out.println("Testing Constructor Exception Handling...");

		try {
			// Test 1: Service Classes
			System.out.println("1. Testing Service Classes...");

			new CourseService();
			System.out.println("✓ CourseService created successfully");

			new SubjectService();
			System.out.println("✓ SubjectService created successfully");

			new StudentService();
			System.out.println("✓ StudentService created successfully");

			new FeeService();
			System.out.println("✓ FeeService created successfully");

			new TeacherService();
			System.out.println("✓ TeacherService created successfully");

			new DashboardService();
			System.out.println("✓ DashboardService created successfully");

			// Test 2: Controller Classes
			System.out.println("\n2. Testing Controller Classes...");

			new CourseController();
			System.out.println("✓ CourseController created successfully");

			new SubjectController();
			System.out.println("✓ SubjectController created successfully");

			new StudentController();
			System.out.println("✓ StudentController created successfully");

			new FeeController();
			System.out.println("✓ FeeController created successfully");

			new TeacherController();
			System.out.println("✓ TeacherController created successfully");

			new DashboardController();
			System.out.println("✓ DashboardController created successfully");

			System.out.println("\n✓ All constructors working properly! No SQLException issues.");

		} catch (SQLException e) {
			System.err.println("✗ Test failed with SQLException: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("✗ Test failed with Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}