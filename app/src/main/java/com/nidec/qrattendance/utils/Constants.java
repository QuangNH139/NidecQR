package com.nidec.qrattendance.utils;

public class Constants {
    // Attendance Status
    public static final String STATUS_PRESENT = "Có tham gia";
    public static final String STATUS_ABSENT = "Không tham gia";
    
    // Registration Preferences
    public static final String PREF_SALTY = "Mặn";
    public static final String PREF_VEGETARIAN = "Chay";
    public static final String PREF_NONE = "Không";
    
    // Intent Keys
    public static final String KEY_EMPLOYEE_CODE = "employee_code";
    public static final String KEY_EMPLOYEE_ID = "employee_id";
    public static final String KEY_ATTENDANCE_ID = "attendance_id";
    
    // DataWedge
    public static final String DATAWEDGE_INTENT_ACTION = "com.nidec.qrattendance.SCAN";
    public static final String DATAWEDGE_INTENT_KEY_DATA = "com.symbol.datawedge.data_string";
    public static final String DATAWEDGE_PROFILE_NAME = "NidecQR";
    
    // Shared Preferences
    public static final String PREF_NAME = "NidecQRPreferences";
    public static final String PREF_USE_ZEBRA_SCANNER = "use_zebra_scanner";
    
    // Date Formats
    public static final String DATE_FORMAT_DISPLAY = "dd/MM/yyyy HH:mm";
    public static final String DATE_FORMAT_FILE = "yyyyMMdd_HHmmss";
    
    // Request Codes
    public static final int REQUEST_CODE_SCAN = 1001;
    public static final int REQUEST_CODE_ADD_EMPLOYEE = 1002;
    public static final int REQUEST_CODE_EDIT_EMPLOYEE = 1003;
    public static final int REQUEST_CODE_CAMERA_PERMISSION = 1004;
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 1005;
    
    // Tab indices
    public static final int TAB_ALL = 0;
    public static final int TAB_PRESENT = 1;
    public static final int TAB_ABSENT = 2;
}
