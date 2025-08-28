package com.sms.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.sms.dao.CourseDAO;
import com.sms.dao.FeeDao;
import com.sms.model.Course;
import com.sms.model.Fee;
import com.sms.model.FeeNotifier;
import com.sms.payment.notifier.EmailFeeNotifier;
import com.sms.payment.notifier.FeeAlert;
import com.sms.payment.notifier.SmsFeeNotifier;
import com.sms.payment.notifier.WhatsAppFeeNotifier;

public class FeeService {
	private FeeDao feeDao;
	private CourseDAO courseDao;
	private FeeNotifierService feeNotifierService;

	public FeeService() throws SQLException {
		this.feeDao = new FeeDao();
		this.courseDao = new CourseDAO();
		this.feeNotifierService = new FeeNotifierService();
	}

	// View Total Paid Fees
	public BigDecimal getTotalPaidFees() {
		return feeDao.getTotalPaidFees();
	}

	public List<Fee> getPaidFeesByStudents() {
		return feeDao.getPaidFeesByStudents();
	}

	// View Total Pending Fees
	public BigDecimal getTotalPendingFees() {
		return feeDao.getTotalPendingFees();
	}

	public List<Fee> getPendingFeesByStudents() {
		return feeDao.getPendingFeesByStudents();
	}

	public String getFeesByStudent(int studentId) {
		if (studentId <= 0) {
			return "Invalid student ID.";
		}
		List<Fee> fees = feeDao.getFeesByStudent(studentId);
		if (fees.isEmpty()) {
			return "No fees found for Student ID: " + studentId;
		}
		return "SUCCESS";
	}

	public String getFeesByCourse(int courseId) {
		if (courseId <= 0) {
			return "Invalid course ID.";
		}
		List<Fee> fees = feeDao.getFeesByCourse(courseId);
		if (fees.isEmpty()) {
			return "No fees found for Course ID: " + courseId;
		}
		return "SUCCESS";
	}

	public String getCourseFeeSummary(int courseId) {
		Course course;
		try {
			course = courseDao.getCourseById(courseId);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error retrieving course.";
		}

		if (course == null) {
			return "Invalid Course ID.";
		}

		List<Fee> fees = feeDao.getFeesByCourse(courseId);
		int studentCount = fees.size();

		BigDecimal courseFee = course.getTotal_fee();
		BigDecimal totalExpectedFees = courseFee.multiply(BigDecimal.valueOf(studentCount));

		StringBuilder summary = new StringBuilder();
		summary.append("\nCourse: ").append(course.getCourse_name());
		summary.append("\nStudents Enrolled: ").append(studentCount);
		summary.append(String.format("\nTotal Expected Fees: ₹%.2f\n", totalExpectedFees));

		return summary.toString();
	}

	// Update Fees Of A Course
	public String updateCourseFees(int courseId, BigDecimal newTotalFee) {
		if (courseId <= 0) {
			return "Invalid course ID.";
		}
		if (newTotalFee == null || newTotalFee.compareTo(BigDecimal.ZERO) < 0) {
			return "Fee amount cannot be negative or null.";
		}
		boolean success = feeDao.updateCourseFees(courseId, newTotalFee);
		if (!success) {
			return "Failed to update course fees. Please check the Course ID.";
		}
		return "Course fees updated successfully to ₹" + newTotalFee;
	}

	// Get Total Earning
	public BigDecimal getTotalEarning() {
		return feeDao.getTotalEarning();
	}

	// Get all fees
	public List<Fee> getAllFees() {
		return feeDao.getAllFees();
	}

	// Get all courses for selection
	public List<com.sms.model.Course> getAllCourses() {
		return feeDao.getAllCourses();
	}

	// Get all students for selection
	public List<com.sms.model.Student> getAllStudents() {
		return feeDao.getAllStudents();
	}

	// Update fee payment
	public String updateFeePayment(int studentId, BigDecimal paymentAmount, int courseId) {
		if (studentId <= 0) {
			return "Invalid student ID.";
		}
		if (paymentAmount == null || paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
			return "Payment amount must be positive.";
		}
		if (courseId <= 0) {
			return "Invalid course ID.";
		}
		boolean success = feeDao.updateFeePayment(studentId, paymentAmount, courseId);
		if (!success) {
			return "Failed to update fee payment. Please check the Student ID or Course ID.";
		}

		FeeNotifier prefs = feeNotifierService.getPreferences(studentId);
		if (prefs == null) {
			feeNotifierService.createDefaultPreferences(studentId);
			prefs = feeNotifierService.getPreferences(studentId);
		}

		FeeAlert alert = new FeeAlert();

		if (prefs.isSmsEnabled())
			alert.registerNotifier(new SmsFeeNotifier());
		if (prefs.isEmailEnabled())
			alert.registerNotifier(new EmailFeeNotifier());
		if (prefs.isWhatsappEnabled())
			alert.registerNotifier(new WhatsAppFeeNotifier());

		alert.notifyAll(studentId, paymentAmount);

		return "Fee payment updated successfully.";
	}

	// Helper method to get fees for display purposes
	public List<Fee> getFeesListByStudent(int studentId) {
		return feeDao.getFeesByStudent(studentId);
	}

	// Helper method to get fees for display purposes
	public List<Fee> getFeesListByCourse(int courseId) {
		return feeDao.getFeesByCourse(courseId);
	}

	public int getStudentCourseId(int studentId, int courseId) {
		try {
			return feeDao.getStudentCourseId(studentId, courseId);
		} catch (SQLException e) {
			return -1; // Indicate failure
		}
	}

	public String createFeeRecord(int studentCourseId, BigDecimal totalFee) {
		try {
			feeDao.createFeeRecord(studentCourseId, totalFee);
			return "SUCCESS";
		} catch (SQLException e) {
			return "Failed to create fee record: " + e.getMessage();
		}
	}

	public Course getCourseById(int courseId) {
		return courseDao.getCourseById(courseId);
	}

}