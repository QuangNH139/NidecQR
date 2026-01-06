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

public class AttendanceDAO {
    private DatabaseHelper dbHelper;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public AttendanceDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public long insertAttendance(Attendance attendance) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EMPLOYEE_ID, attendance.getEmployeeId());
        values.put(DatabaseHelper.COLUMN_ATTENDANCE_STATUS, attendance.getAttendanceStatus());
        values.put(DatabaseHelper.COLUMN_REGISTRATION_PREFERENCE, attendance.getRegistrationPreference());
        values.put(DatabaseHelper.COLUMN_SCORE, attendance.getScore());
        values.put(DatabaseHelper.COLUMN_NOTES, attendance.getNotes());
        
        if (attendance.getTimestamp() != null) {
            values.put(DatabaseHelper.COLUMN_TIMESTAMP, dateFormat.format(attendance.getTimestamp()));
        }
        
        return db.insert(DatabaseHelper.TABLE_ATTENDANCE, null, values);
    }

    public Attendance getAttendanceById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT a.*, e." + DatabaseHelper.COLUMN_EMPLOYEE_CODE + ", e." +
                      DatabaseHelper.COLUMN_FULL_NAME + ", e." + DatabaseHelper.COLUMN_DEPARTMENT +
                      " FROM " + DatabaseHelper.TABLE_ATTENDANCE + " a" +
                      " INNER JOIN " + DatabaseHelper.TABLE_EMPLOYEES + " e ON a." +
                      DatabaseHelper.COLUMN_EMPLOYEE_ID + " = e." + DatabaseHelper.COLUMN_EMPLOYEE_ID +
                      " WHERE a." + DatabaseHelper.COLUMN_ID + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        Attendance attendance = null;
        if (cursor != null && cursor.moveToFirst()) {
            attendance = cursorToAttendance(cursor);
            cursor.close();
        }
        return attendance;
    }

    public List<Attendance> getAllAttendance() {
        return getAttendanceWithFilter(null, null, null, null);
    }

    public List<Attendance> getRecentAttendance(int limit) {
        List<Attendance> attendances = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT a.*, e." + DatabaseHelper.COLUMN_EMPLOYEE_CODE + ", e." +
                      DatabaseHelper.COLUMN_FULL_NAME + ", e." + DatabaseHelper.COLUMN_DEPARTMENT +
                      " FROM " + DatabaseHelper.TABLE_ATTENDANCE + " a" +
                      " INNER JOIN " + DatabaseHelper.TABLE_EMPLOYEES + " e ON a." +
                      DatabaseHelper.COLUMN_EMPLOYEE_ID + " = e." + DatabaseHelper.COLUMN_EMPLOYEE_ID +
                      " ORDER BY a." + DatabaseHelper.COLUMN_TIMESTAMP + " DESC LIMIT ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(limit)});
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                attendances.add(cursorToAttendance(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return attendances;
    }

    public List<Attendance> getAttendanceByStatus(String status) {
        return getAttendanceWithFilter(status, null, null, null);
    }

    public List<Attendance> getAttendanceWithFilter(String status, String department, Date startDate, Date endDate) {
        List<Attendance> attendances = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        StringBuilder query = new StringBuilder("SELECT a.*, e." + DatabaseHelper.COLUMN_EMPLOYEE_CODE + ", e." +
                DatabaseHelper.COLUMN_FULL_NAME + ", e." + DatabaseHelper.COLUMN_DEPARTMENT +
                " FROM " + DatabaseHelper.TABLE_ATTENDANCE + " a" +
                " INNER JOIN " + DatabaseHelper.TABLE_EMPLOYEES + " e ON a." +
                DatabaseHelper.COLUMN_EMPLOYEE_ID + " = e." + DatabaseHelper.COLUMN_EMPLOYEE_ID);
        
        List<String> conditions = new ArrayList<>();
        List<String> args = new ArrayList<>();
        
        if (status != null && !status.isEmpty()) {
            conditions.add("a." + DatabaseHelper.COLUMN_ATTENDANCE_STATUS + " = ?");
            args.add(status);
        }
        
        if (department != null && !department.isEmpty()) {
            conditions.add("e." + DatabaseHelper.COLUMN_DEPARTMENT + " = ?");
            args.add(department);
        }
        
        if (startDate != null) {
            conditions.add("a." + DatabaseHelper.COLUMN_TIMESTAMP + " >= ?");
            args.add(dateFormat.format(startDate));
        }
        
        if (endDate != null) {
            conditions.add("a." + DatabaseHelper.COLUMN_TIMESTAMP + " <= ?");
            args.add(dateFormat.format(endDate));
        }
        
        if (!conditions.isEmpty()) {
            query.append(" WHERE ");
            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0) query.append(" AND ");
                query.append(conditions.get(i));
            }
        }
        
        query.append(" ORDER BY a." + DatabaseHelper.COLUMN_TIMESTAMP + " DESC");
        
        Cursor cursor = db.rawQuery(query.toString(), args.toArray(new String[0]));
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                attendances.add(cursorToAttendance(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return attendances;
    }

    public int getAttendanceCount(String status, String department, Date startDate, Date endDate) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_ATTENDANCE + " a" +
                " INNER JOIN " + DatabaseHelper.TABLE_EMPLOYEES + " e ON a." +
                DatabaseHelper.COLUMN_EMPLOYEE_ID + " = e." + DatabaseHelper.COLUMN_EMPLOYEE_ID);
        
        List<String> conditions = new ArrayList<>();
        List<String> args = new ArrayList<>();
        
        if (status != null && !status.isEmpty()) {
            conditions.add("a." + DatabaseHelper.COLUMN_ATTENDANCE_STATUS + " = ?");
            args.add(status);
        }
        
        if (department != null && !department.isEmpty()) {
            conditions.add("e." + DatabaseHelper.COLUMN_DEPARTMENT + " = ?");
            args.add(department);
        }
        
        if (startDate != null) {
            conditions.add("a." + DatabaseHelper.COLUMN_TIMESTAMP + " >= ?");
            args.add(dateFormat.format(startDate));
        }
        
        if (endDate != null) {
            conditions.add("a." + DatabaseHelper.COLUMN_TIMESTAMP + " <= ?");
            args.add(dateFormat.format(endDate));
        }
        
        if (!conditions.isEmpty()) {
            query.append(" WHERE ");
            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0) query.append(" AND ");
                query.append(conditions.get(i));
            }
        }
        
        Cursor cursor = db.rawQuery(query.toString(), args.toArray(new String[0]));
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    public int updateAttendance(Attendance attendance) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EMPLOYEE_ID, attendance.getEmployeeId());
        values.put(DatabaseHelper.COLUMN_ATTENDANCE_STATUS, attendance.getAttendanceStatus());
        values.put(DatabaseHelper.COLUMN_REGISTRATION_PREFERENCE, attendance.getRegistrationPreference());
        values.put(DatabaseHelper.COLUMN_SCORE, attendance.getScore());
        values.put(DatabaseHelper.COLUMN_NOTES, attendance.getNotes());
        
        return db.update(DatabaseHelper.TABLE_ATTENDANCE,
                values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(attendance.getId())});
    }

    public int deleteAttendance(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_ATTENDANCE,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    private Attendance cursorToAttendance(Cursor cursor) {
        Attendance attendance = new Attendance();
        attendance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
        attendance.setEmployeeId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLOYEE_ID)));
        attendance.setAttendanceStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ATTENDANCE_STATUS)));
        
        int prefIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_REGISTRATION_PREFERENCE);
        if (prefIndex >= 0 && !cursor.isNull(prefIndex)) {
            attendance.setRegistrationPreference(cursor.getString(prefIndex));
        }
        
        int scoreIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SCORE);
        if (scoreIndex >= 0 && !cursor.isNull(scoreIndex)) {
            attendance.setScore(cursor.getInt(scoreIndex));
        }
        
        int notesIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTES);
        if (notesIndex >= 0 && !cursor.isNull(notesIndex)) {
            attendance.setNotes(cursor.getString(notesIndex));
        }
        
        String timestampStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIMESTAMP));
        try {
            attendance.setTimestamp(dateFormat.parse(timestampStr));
        } catch (Exception e) {
            attendance.setTimestamp(new Date());
        }
        
        String createdAtStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_AT));
        try {
            attendance.setCreatedAt(dateFormat.parse(createdAtStr));
        } catch (Exception e) {
            attendance.setCreatedAt(new Date());
        }
        
        // Set employee info if joined
        int codeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EMPLOYEE_CODE);
        if (codeIndex >= 0) {
            attendance.setEmployeeCode(cursor.getString(codeIndex));
        }
        
        int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_FULL_NAME);
        if (nameIndex >= 0) {
            attendance.setEmployeeName(cursor.getString(nameIndex));
        }
        
        int deptIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DEPARTMENT);
        if (deptIndex >= 0) {
            attendance.setDepartment(cursor.getString(deptIndex));
        }
        
        return attendance;
    }
}
