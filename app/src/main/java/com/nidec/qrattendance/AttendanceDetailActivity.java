package com.nidec.qrattendance;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.nidec.qrattendance.database.Attendance;
import com.nidec.qrattendance.database.AttendanceDAO;
import com.nidec.qrattendance.database.Employee;
import com.nidec.qrattendance.database.EmployeeDAO;
import com.nidec.qrattendance.utils.Constants;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AttendanceDetailActivity extends AppCompatActivity {
    
    private TextView tvEmployeeCode;
    private TextView tvEmployeeName;
    private TextView tvDepartment;
    private RadioGroup rgStatus;
    private RadioButton rbPresent;
    private RadioButton rbAbsent;
    private Spinner spinnerPreference;
    private EditText etNotes;
    private Button btnSave;
    private ProgressBar progressBar;
    private View layoutEmployeeInfo;
    
    private EmployeeDAO employeeDAO;
    private AttendanceDAO attendanceDAO;
    private Employee currentEmployee;
    private Attendance currentAttendance;
    private ExecutorService executorService;
    
    private boolean isEditMode = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_detail);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initViews();
        setupSpinner();
        loadData();
    }
    
    private void initViews() {
        tvEmployeeCode = findViewById(R.id.tv_employee_code);
        tvEmployeeName = findViewById(R.id.tv_employee_name);
        tvDepartment = findViewById(R.id.tv_department);
        rgStatus = findViewById(R.id.rg_status);
        rbPresent = findViewById(R.id.rb_present);
        rbAbsent = findViewById(R.id.rb_absent);
        spinnerPreference = findViewById(R.id.spinner_preference);
        etNotes = findViewById(R.id.et_notes);
        btnSave = findViewById(R.id.btn_save);
        progressBar = findViewById(R.id.progress_bar);
        layoutEmployeeInfo = findViewById(R.id.layout_employee_info);
        
        employeeDAO = new EmployeeDAO(this);
        attendanceDAO = new AttendanceDAO(this);
        executorService = Executors.newSingleThreadExecutor();
        
        btnSave.setOnClickListener(v -> saveAttendance());
    }
    
    private void setupSpinner() {
        String[] preferences = {
            Constants.PREF_NONE,
            Constants.PREF_SALTY,
            Constants.PREF_VEGETARIAN
        };
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this, 
            android.R.layout.simple_spinner_item, 
            preferences
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPreference.setAdapter(adapter);
    }
    
    private void loadData() {
        String employeeCode = getIntent().getStringExtra(Constants.KEY_EMPLOYEE_CODE);
        int attendanceId = getIntent().getIntExtra(Constants.KEY_ATTENDANCE_ID, -1);
        
        if (attendanceId != -1) {
            // Edit mode
            isEditMode = true;
            loadAttendance(attendanceId);
        } else if (employeeCode != null) {
            // New attendance
            isEditMode = false;
            loadEmployee(employeeCode);
        } else {
            Toast.makeText(this, R.string.error_no_data, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void loadEmployee(String employeeCode) {
        progressBar.setVisibility(View.VISIBLE);
        executorService.execute(() -> {
            Employee employee = employeeDAO.getEmployeeByCode(employeeCode);
            
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                
                if (employee != null) {
                    currentEmployee = employee;
                    displayEmployeeInfo();
                } else {
                    Toast.makeText(this, R.string.error_employee_not_found, 
                        Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        });
    }
    
    private void loadAttendance(int attendanceId) {
        progressBar.setVisibility(View.VISIBLE);
        executorService.execute(() -> {
            Attendance attendance = attendanceDAO.getAttendanceById(attendanceId);
            
            if (attendance != null) {
                Employee employee = employeeDAO.getEmployeeById(attendance.getEmployeeId());
                
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    
                    if (employee != null) {
                        currentEmployee = employee;
                        currentAttendance = attendance;
                        displayEmployeeInfo();
                        displayAttendanceData();
                    } else {
                        Toast.makeText(this, R.string.error_no_data, 
                            Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            } else {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, R.string.error_no_data, 
                        Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }
    
    private void displayEmployeeInfo() {
        layoutEmployeeInfo.setVisibility(View.VISIBLE);
        tvEmployeeCode.setText(currentEmployee.getEmployeeCode());
        tvEmployeeName.setText(currentEmployee.getFullName());
        tvDepartment.setText(currentEmployee.getDepartment());
    }
    
    private void displayAttendanceData() {
        if (currentAttendance.getAttendanceStatus().equals(Constants.STATUS_PRESENT)) {
            rbPresent.setChecked(true);
        } else {
            rbAbsent.setChecked(true);
        }
        
        String pref = currentAttendance.getRegistrationPreference();
        if (pref != null) {
            if (pref.equals(Constants.PREF_SALTY)) {
                spinnerPreference.setSelection(1);
            } else if (pref.equals(Constants.PREF_VEGETARIAN)) {
                spinnerPreference.setSelection(2);
            }
        }
        
        if (currentAttendance.getNotes() != null) {
            etNotes.setText(currentAttendance.getNotes());
        }
    }
    
    private void saveAttendance() {
        if (currentEmployee == null) {
            Toast.makeText(this, R.string.error_no_employee, Toast.LENGTH_SHORT).show();
            return;
        }
        
        int selectedStatusId = rgStatus.getCheckedRadioButtonId();
        if (selectedStatusId == -1) {
            Toast.makeText(this, R.string.error_select_status, Toast.LENGTH_SHORT).show();
            return;
        }
        
        String status = selectedStatusId == R.id.rb_present ? 
            Constants.STATUS_PRESENT : Constants.STATUS_ABSENT;
        String preference = spinnerPreference.getSelectedItem().toString();
        String notes = etNotes.getText().toString().trim();
        
        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);
        
        executorService.execute(() -> {
            long result;
            
            if (isEditMode && currentAttendance != null) {
                currentAttendance.setAttendanceStatus(status);
                currentAttendance.setRegistrationPreference(preference);
                currentAttendance.setNotes(notes);
                result = attendanceDAO.updateAttendance(currentAttendance);
            } else {
                Attendance attendance = new Attendance();
                attendance.setEmployeeId(currentEmployee.getEmployeeId());
                attendance.setAttendanceStatus(status);
                attendance.setRegistrationPreference(preference);
                attendance.setNotes(notes);
                attendance.setTimestamp(new Date());
                result = attendanceDAO.insertAttendance(attendance);
            }
            
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                btnSave.setEnabled(true);
                
                if (result > 0) {
                    Snackbar.make(btnSave, R.string.success_saved, Snackbar.LENGTH_SHORT).show();
                    // Delay finish to show snackbar
                    btnSave.postDelayed(() -> finish(), 1000);
                } else {
                    Toast.makeText(this, R.string.error_save_failed, 
                        Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
