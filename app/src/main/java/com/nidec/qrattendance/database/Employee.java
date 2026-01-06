package com.nidec.qrattendance.database;

import java.util.Date;

public class Employee {
    private int employeeId;
    private String employeeCode;
    private String fullName;
    private String department;
    private Date createdAt;

    public Employee() {
    }

    public Employee(String employeeCode, String fullName, String department) {
        this.employeeCode = employeeCode;
        this.fullName = fullName;
        this.department = department;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
