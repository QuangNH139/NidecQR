package com.nidec.qrattendance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.nidec.qrattendance.adapter.AttendanceAdapter;
import com.nidec.qrattendance.database.Attendance;
import com.nidec.qrattendance.database.AttendanceDAO;
import com.nidec.qrattendance.scanner.QRScannerManager;
import com.nidec.qrattendance.scanner.ZebraDataWedgeHelper;
import com.nidec.qrattendance.utils.Constants;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements AttendanceAdapter.OnItemClickListener {
    
    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private AttendanceDAO attendanceDAO;
    private FloatingActionButton fabScan;
    private TabLayout tabLayout;
    private ProgressBar progressBar;
    private ZebraDataWedgeHelper zebraHelper;
    private ExecutorService executorService;
    
    private int currentTab = Constants.TAB_ALL;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        setupRecyclerView();
        setupTabs();
        setupZebraScanner();
        checkPermissions();
        loadAttendances();
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        fabScan = findViewById(R.id.fab_scan);
        tabLayout = findViewById(R.id.tab_layout);
        progressBar = findViewById(R.id.progress_bar);
        
        attendanceDAO = new AttendanceDAO(this);
        executorService = Executors.newSingleThreadExecutor();
        
        fabScan.setOnClickListener(v -> startQRScan());
    }
    
    private void setupRecyclerView() {
        adapter = new AttendanceAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    
    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_all));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_present));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_absent));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                loadAttendances();
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    
    private void setupZebraScanner() {
        if (ZebraDataWedgeHelper.isZebraDevice()) {
            zebraHelper = new ZebraDataWedgeHelper(this, data -> {
                if (QRScannerManager.isValidEmployeeCode(data)) {
                    handleScanResult(data);
                } else {
                    Toast.makeText(MainActivity.this, 
                        R.string.error_invalid_qr, 
                        Toast.LENGTH_SHORT).show();
                }
            });
            zebraHelper.createDataWedgeProfile();
        }
    }
    
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.CAMERA}, 
                Constants.REQUEST_CODE_CAMERA_PERMISSION);
        }
    }
    
    private void startQRScan() {
        QRScannerManager.startScan(this);
    }
    
    private void loadAttendances() {
        progressBar.setVisibility(View.VISIBLE);
        executorService.execute(() -> {
            List<Attendance> attendances;
            
            switch (currentTab) {
                case Constants.TAB_PRESENT:
                    attendances = attendanceDAO.getAttendanceByStatus(Constants.STATUS_PRESENT);
                    break;
                case Constants.TAB_ABSENT:
                    attendances = attendanceDAO.getAttendanceByStatus(Constants.STATUS_ABSENT);
                    break;
                default:
                    attendances = attendanceDAO.getRecentAttendance(100);
                    break;
            }
            
            runOnUiThread(() -> {
                adapter.setAttendances(attendances);
                progressBar.setVisibility(View.GONE);
            });
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        String scanResult = QRScannerManager.getScanResult(requestCode, resultCode, data);
        if (scanResult != null) {
            handleScanResult(scanResult);
        }
    }
    
    private void handleScanResult(String code) {
        if (QRScannerManager.isValidEmployeeCode(code)) {
            Intent intent = new Intent(this, AttendanceDetailActivity.class);
            intent.putExtra(Constants.KEY_EMPLOYEE_CODE, code);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.error_invalid_qr, Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onItemClick(Attendance attendance) {
        Intent intent = new Intent(this, AttendanceDetailActivity.class);
        intent.putExtra(Constants.KEY_ATTENDANCE_ID, attendance.getId());
        startActivity(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO: Implement search functionality
                return true;
            }
        });
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_employees) {
            startActivity(new Intent(this, EmployeeListActivity.class));
            return true;
        } else if (id == R.id.action_report) {
            startActivity(new Intent(this, ReportActivity.class));
            return true;
        } else if (id == R.id.action_refresh) {
            loadAttendances();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (zebraHelper != null) {
            zebraHelper.register();
        }
        loadAttendances();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (zebraHelper != null) {
            zebraHelper.unregister();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
