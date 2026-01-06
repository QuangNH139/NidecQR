package com.nidec.qrattendance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EmployeeDAO {
    private DatabaseHelper dbHelper;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public EmployeeDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public long insertEmployee(Employee employee) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EMPLOYEE_CODE, employee.getEmployeeCode());
        values.put(DatabaseHelper.COLUMN_FULL_NAME, employee.getFullName());
        values.put(DatabaseHelper.COLUMN_DEPARTMENT, employee.getDepartment());
        return db.insert(DatabaseHelper.TABLE_EMPLOYEES, null, values);
    }

    public Employee getEmployeeById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_EMPLOYEES,
                null,
                DatabaseHelper.COLUMN_EMPLOYEE_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        
        Employee employee = null;
        if (cursor != null && cursor.moveToFirst()) {
            employee = cursorToEmployee(cursor);
            cursor.close();
        }
        return employee;
    }

    public Employee getEmployeeByCode(String code) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_EMPLOYEES,
                null,
                DatabaseHelper.COLUMN_EMPLOYEE_CODE + " = ?",
                new String[]{code},
                null, null, null);
        
        Employee employee = null;
        if (cursor != null && cursor.moveToFirst()) {
            employee = cursorToEmployee(cursor);
            cursor.close();
        }
        return employee;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_EMPLOYEES,
                null, null, null, null, null,
                DatabaseHelper.COLUMN_FULL_NAME + " ASC");
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                employees.add(cursorToEmployee(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return employees;
    }

    public List<Employee> searchEmployees(String query) {
        List<Employee> employees = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DatabaseHelper.COLUMN_EMPLOYEE_CODE + " LIKE ? OR " +
                          DatabaseHelper.COLUMN_FULL_NAME + " LIKE ? OR " +
                          DatabaseHelper.COLUMN_DEPARTMENT + " LIKE ?";
        String searchPattern = "%" + query + "%";
        String[] selectionArgs = {searchPattern, searchPattern, searchPattern};
        
        Cursor cursor = db.query(DatabaseHelper.TABLE_EMPLOYEES,
                null, selection, selectionArgs, null, null,
                DatabaseHelper.COLUMN_FULL_NAME + " ASC");
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                employees.add(cursorToEmployee(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return employees;
    }

    public int updateEmployee(Employee employee) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EMPLOYEE_CODE, employee.getEmployeeCode());
        values.put(DatabaseHelper.COLUMN_FULL_NAME, employee.getFullName());
        values.put(DatabaseHelper.COLUMN_DEPARTMENT, employee.getDepartment());
        
        return db.update(DatabaseHelper.TABLE_EMPLOYEES,
                values,
                DatabaseHelper.COLUMN_EMPLOYEE_ID + " = ?",
                new String[]{String.valueOf(employee.getEmployeeId())});
    }

    public int deleteEmployee(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_EMPLOYEES,
                DatabaseHelper.COLUMN_EMPLOYEE_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public List<String> getAllDepartments() {
        List<String> departments = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(true, DatabaseHelper.TABLE_EMPLOYEES,
                new String[]{DatabaseHelper.COLUMN_DEPARTMENT},
                null, null, null, null,
                DatabaseHelper.COLUMN_DEPARTMENT + " ASC", null);
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                departments.add(cursor.getString(0));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return departments;
    }

    private Employee cursorToEmployee(Cursor cursor) {
        Employee employee = new Employee();
        employee.setEmployeeId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLOYEE_ID)));
        employee.setEmployeeCode(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLOYEE_CODE)));
        employee.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULL_NAME)));
        employee.setDepartment(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTMENT)));
        
        String createdAtStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_AT));
        try {
            employee.setCreatedAt(dateFormat.parse(createdAtStr));
        } catch (Exception e) {
            employee.setCreatedAt(new Date());
        }
        
        return employee;
    }
}
