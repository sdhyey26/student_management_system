package com.sms.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.sms.database.DBConnection;
import com.sms.model.Subject;

public class SubjectDAO {

	private Connection connection = null;
	
	public SubjectDAO() throws SQLException {
		this.connection = DBConnection.connect();
	}
	
	

    public List<Subject> getAllSubjects() throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects WHERE is_active = 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubject_id(rs.getInt("subject_id"));
                subject.setSubject_name(rs.getString("subject_name"));
                subject.setSubject_type(rs.getString("subject_type"));
                subject.setActive(rs.getInt("is_active"));
                subjects.add(subject);
            }
        }
        return subjects;
    }

    public int addSubject(Subject subject) {
        String sql = "INSERT INTO subjects (subject_name, subject_type, is_active) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, subject.getSubject_name());
            pstmt.setString(2, subject.getSubject_type());
            pstmt.setInt(3, subject.getActive());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding subject: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }


    public Subject getSubjectById(int id) {
        String query = "SELECT * FROM subjects WHERE subject_id = ? AND is_active = 1";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubject_id(rs.getInt("subject_id"));
                    subject.setSubject_name(rs.getString("subject_name"));
                    subject.setSubject_type(rs.getString("subject_type"));
                    subject.setActive(rs.getInt("is_active"));
                    return subject;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting subject by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateSubject(Subject subject) {
        String sql = "UPDATE subjects SET subject_name = ?, subject_type = ? WHERE subject_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, subject.getSubject_name());
            stmt.setString(2, subject.getSubject_type());
            stmt.setInt(3, subject.getSubject_id());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating subject: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean softDeleteSubject(int id) {
        String sql = "UPDATE subjects SET is_active = 0 WHERE subject_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting subject: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
