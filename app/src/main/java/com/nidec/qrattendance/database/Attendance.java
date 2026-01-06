package com.nidec.qrattendance.database;

import java.util.Date;

public class Attendance {
    private int id;
    private int employeeId;
    private Date timestamp;
    private String attendanceStatus;
    private String registrationPreference;
    private Integer score;
    private String notes;
    private Date createdAt;

    // Transient fields for display
    private String employeeName;
    private String employeeCode;
    private String department;

    public Attendance() {
    }

    public Attendance(int employeeId, String attendanceStatus, String registrationPreference, String notes) {
        this.employeeId = employeeId;
        this.attendanceStatus = attendanceStatus;
        this.registrationPreference = registrationPreference;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getRegistrationPreference() {
        return registrationPreference;
    }

    public void setRegistrationPreference(String registrationPreference) {
        this.registrationPreference = registrationPreference;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
