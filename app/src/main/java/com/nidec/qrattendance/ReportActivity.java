package com.nidec.qrattendance;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.nidec.qrattendance.adapter.AttendanceAdapter;
import com.nidec.qrattendance.database.Attendance;
import com.nidec.qrattendance.database.AttendanceDAO;
import com.nidec.qrattendance.database.EmployeeDAO;
import com.nidec.qrattendance.utils.Constants;
import com.nidec.qrattendance.utils.DateTimeUtils;
import com.nidec.qrattendance.utils.ExcelExporter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportActivity extends AppCompatActivity implements AttendanceAdapter.OnItemClickListener {
    
    private Spinner spinnerPeriod;
    private Spinner spinnerDepartment;
    private TextView tvTotalCount;
    private TextView tvPresentCount;
    private TextView tvAbsentCount;
    private TextView tvDateRange;
    private RecyclerView recyclerView;
    private Button btnExport;
    private ProgressBar progressBar;
    
    private AttendanceDAO attendanceDAO;
    private EmployeeDAO employeeDAO;
    private AttendanceAdapter adapter;
    private ExecutorService executorService;
    
    private Date startDate;
    private Date endDate;
    private String selectedDepartment = null;
    
    private static final String PERIOD_TODAY = "Hôm nay";
    private static final String PERIOD_WEEK = "Tuần này";
    private static final String PERIOD_MONTH = "Tháng này";
    private static final String PERIOD_CUSTOM = "Tùy chỉnh";
    private static final String DEPT_ALL = "Tất cả";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initViews();
        setupSpinners();
        setupRecyclerView();
        loadReport();
    }
    
    private void initViews() {
        spinnerPeriod = findViewById(R.id.spinner_period);
        spinnerDepartment = findViewById(R.id.spinner_department);
        tvTotalCount = findViewById(R.id.tv_total_count);
        tvPresentCount = findViewById(R.id.tv_present_count);
        tvAbsentCount = findViewById(R.id.tv_absent_count);
        tvDateRange = findViewById(R.id.tv_date_range);
        recyclerView = findViewById(R.id.recycler_view);
        btnExport = findViewById(R.id.btn_export);
        progressBar = findViewById(R.id.progress_bar);
        
        attendanceDAO = new AttendanceDAO(this);
        employeeDAO = new EmployeeDAO(this);
        executorService = Executors.newSingleThreadExecutor();
        
        btnExport.setOnClickListener(v -> exportToExcel());
    }
    
    private void setupSpinners() {
        // Period spinner
        String[] periods = {PERIOD_TODAY, PERIOD_WEEK, PERIOD_MONTH, PERIOD_CUSTOM};
        ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item, periods);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriod.setAdapter(periodAdapter);
        
        spinnerPeriod.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                handlePeriodSelection(selected);
            }
            
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
        
        // Department spinner - load from database
        loadDepartments();
    }
    
    private void loadDepartments() {
        executorService.execute(() -> {
            List<String> departments = employeeDAO.getAllDepartments();
            departments.add(0, DEPT_ALL);
            
            runOnUiThread(() -> {
                ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, departments);
                deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDepartment.setAdapter(deptAdapter);
                
                spinnerDepartment.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                        String selected = (String) parent.getItemAtPosition(position);
                        selectedDepartment = selected.equals(DEPT_ALL) ? null : selected;
                        loadReport();
                    }
                    
                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {}
                });
            });
        });
    }
    
    private void handlePeriodSelection(String period) {
        switch (period) {
            case PERIOD_TODAY:
                startDate = DateTimeUtils.getTodayStart();
                endDate = DateTimeUtils.getTodayEnd();
                loadReport();
                break;
            case PERIOD_WEEK:
                startDate = DateTimeUtils.getStartOfWeek();
                endDate = DateTimeUtils.getTodayEnd();
                loadReport();
                break;
            case PERIOD_MONTH:
                startDate = DateTimeUtils.getStartOfMonth();
                endDate = DateTimeUtils.getTodayEnd();
                loadReport();
                break;
            case PERIOD_CUSTOM:
                showDateRangePicker();
                break;
        }
    }
    
    private void showDateRangePicker() {
        Calendar calendar = Calendar.getInstance();
        
        DatePickerDialog startDatePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar startCal = Calendar.getInstance();
            startCal.set(year, month, dayOfMonth);
            startDate = DateTimeUtils.getStartOfDay(startCal.getTime());
            
            // Show end date picker
            DatePickerDialog endDatePicker = new DatePickerDialog(this, (v2, year2, month2, dayOfMonth2) -> {
                Calendar endCal = Calendar.getInstance();
                endCal.set(year2, month2, dayOfMonth2);
                endDate = DateTimeUtils.getEndOfDay(endCal.getTime());
                loadReport();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            
            endDatePicker.setTitle("Chọn ngày kết thúc");
            endDatePicker.show();
            
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        
        startDatePicker.setTitle("Chọn ngày bắt đầu");
        startDatePicker.show();
    }
    
    private void setupRecyclerView() {
        adapter = new AttendanceAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    
    private void loadReport() {
        if (startDate == null || endDate == null) {
            return;
        }
        
        progressBar.setVisibility(View.VISIBLE);
        
        // Update date range display
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvDateRange.setText(sdf.format(startDate) + " - " + sdf.format(endDate));
        
        executorService.execute(() -> {
            List<Attendance> allAttendances = attendanceDAO.getAttendanceWithFilter(
                null, selectedDepartment, startDate, endDate);
            
            int totalCount = allAttendances.size();
            int presentCount = attendanceDAO.getAttendanceCount(
                Constants.STATUS_PRESENT, selectedDepartment, startDate, endDate);
            int absentCount = attendanceDAO.getAttendanceCount(
                Constants.STATUS_ABSENT, selectedDepartment, startDate, endDate);
            
            runOnUiThread(() -> {
                tvTotalCount.setText(String.valueOf(totalCount));
                tvPresentCount.setText(String.valueOf(presentCount));
                tvAbsentCount.setText(String.valueOf(absentCount));
                adapter.setAttendances(allAttendances);
                progressBar.setVisibility(View.GONE);
            });
        });
    }
    
    private void exportToExcel() {
        // Check storage permission for Android 10 and below
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 
                    Constants.REQUEST_CODE_STORAGE_PERMISSION);
                return;
            }
        }
        
        progressBar.setVisibility(View.VISIBLE);
        btnExport.setEnabled(false);
        
        executorService.execute(() -> {
            try {
                List<Attendance> attendances = attendanceDAO.getAttendanceWithFilter(
                    null, selectedDepartment, startDate, endDate);
                
                File file = ExcelExporter.exportAttendanceToExcel(this, attendances);
                
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnExport.setEnabled(true);
                    
                    Snackbar.make(btnExport, 
                        getString(R.string.success_exported, file.getName()), 
                        Snackbar.LENGTH_LONG).show();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnExport.setEnabled(true);
                    Toast.makeText(this, R.string.error_export_failed + ": " + e.getMessage(), 
                        Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    @Override
    public void onItemClick(Attendance attendance) {
        // Show details or do nothing
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
