package com.nidec.qrattendance.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nidec_attendance.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_EMPLOYEES = "employees";
    public static final String TABLE_ATTENDANCE = "attendance";

    // Common columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATED_AT = "created_at";

    // Employee columns
    public static final String COLUMN_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_EMPLOYEE_CODE = "employee_code";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_DEPARTMENT = "department";

    // Attendance columns
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_ATTENDANCE_STATUS = "attendance_status";
    public static final String COLUMN_REGISTRATION_PREFERENCE = "registration_preference";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_NOTES = "notes";

    private static final String CREATE_TABLE_EMPLOYEES = 
        "CREATE TABLE " + TABLE_EMPLOYEES + " (" +
        COLUMN_EMPLOYEE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_EMPLOYEE_CODE + " TEXT UNIQUE NOT NULL, " +
        COLUMN_FULL_NAME + " TEXT NOT NULL, " +
        COLUMN_DEPARTMENT + " TEXT NOT NULL, " +
        COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ")";

    private static final String CREATE_TABLE_ATTENDANCE = 
        "CREATE TABLE " + TABLE_ATTENDANCE + " (" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_EMPLOYEE_ID + " INTEGER NOT NULL, " +
        COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
        COLUMN_ATTENDANCE_STATUS + " TEXT NOT NULL, " +
        COLUMN_REGISTRATION_PREFERENCE + " TEXT, " +
        COLUMN_SCORE + " INTEGER, " +
        COLUMN_NOTES + " TEXT, " +
        COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
        "FOREIGN KEY(" + COLUMN_EMPLOYEE_ID + ") REFERENCES " + 
        TABLE_EMPLOYEES + "(" + COLUMN_EMPLOYEE_ID + ")" +
        ")";

    private static DatabaseHelper instance;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EMPLOYEES);
        db.execSQL(CREATE_TABLE_ATTENDANCE);
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    private void insertSampleData(SQLiteDatabase db) {
        String[] sampleData = {
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('44825', 'Nguyễn Hoàng Huy', 'Pro#1/FAN CAR')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('44727', 'An Như Thành', 'QA/SQE')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('44822', 'An Thị Thanh Phương', 'Pro#1/FAN CAR')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('45400', 'An Xuân Thanh', 'Pro#1/FAN')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('45428', 'Ánh Thy', 'Pro#2/Mold_Pro')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('43717', 'Âu Thị Bích Trâm', 'Pro#1/FAN')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('45490', 'Bá Nữ Ngọc Toái', 'Pro#2/Mold_Eng')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('43127', 'BAN VĂN TRỊNH', 'Pro#2/Press_Pro')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('44371', 'Biện Thị Nhớ', 'QA/QA')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('40898', 'Biên thụy kiều', 'Pro#1/DCM')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('44302', 'Bùi Ái Phi', 'Pro#1/DCM')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('42300', 'Bùi Ánh Kiệt', 'Pro#2/Mold_Eng')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('42421', 'BÙI CHÚC MÀI', 'Pro#2/Press_Pro')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('41578', 'Bùi Đình Kiên', 'Pro#2/Press_Eng')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('43500', 'Bùi Hoàng Nam', 'QA/SQE')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('44100', 'Bùi Thị Hoa', 'Pro#1/FAN CAR')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('45200', 'Cao Minh Tuấn', 'Pro#2/Mold_Pro')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('44900', 'Đặng Thu Hà', 'QA/QA')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('43800', 'Đinh Văn Long', 'Pro#1/DCM')",
            "INSERT INTO " + TABLE_EMPLOYEES + " (" + COLUMN_EMPLOYEE_CODE + ", " + COLUMN_FULL_NAME + ", " + COLUMN_DEPARTMENT + ") VALUES ('45100', 'Đỗ Thị Mai', 'Pro#2/Press_Pro')"
        };

        for (String sql : sampleData) {
            try {
                db.execSQL(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
