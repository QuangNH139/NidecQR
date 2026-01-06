package com.nidec.qrattendance;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nidec.qrattendance.adapter.EmployeeAdapter;
import com.nidec.qrattendance.database.Employee;
import com.nidec.qrattendance.database.EmployeeDAO;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmployeeListActivity extends AppCompatActivity implements EmployeeAdapter.OnItemClickListener {
    
    private RecyclerView recyclerView;
    private EmployeeAdapter adapter;
    private FloatingActionButton fabAdd;
    private ProgressBar progressBar;
    
    private EmployeeDAO employeeDAO;
    private ExecutorService executorService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initViews();
        setupRecyclerView();
        loadEmployees();
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        fabAdd = findViewById(R.id.fab_add);
        progressBar = findViewById(R.id.progress_bar);
        
        employeeDAO = new EmployeeDAO(this);
        executorService = Executors.newSingleThreadExecutor();
        
        fabAdd.setOnClickListener(v -> showAddEmployeeDialog());
    }
    
    private void setupRecyclerView() {
        adapter = new EmployeeAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    
    private void loadEmployees() {
        progressBar.setVisibility(View.VISIBLE);
        executorService.execute(() -> {
            List<Employee> employees = employeeDAO.getAllEmployees();
            
            runOnUiThread(() -> {
                adapter.setEmployees(employees);
                progressBar.setVisibility(View.GONE);
            });
        });
    }
    
    private void showAddEmployeeDialog() {
        showEmployeeDialog(null);
    }
    
    private void showEditEmployeeDialog(Employee employee) {
        showEmployeeDialog(employee);
    }
    
    private void showEmployeeDialog(Employee employee) {
        boolean isEdit = (employee != null);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isEdit ? R.string.edit_employee : R.string.add_employee);
        
        // Create input fields
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);
        
        final EditText etCode = new EditText(this);
        etCode.setHint(R.string.hint_employee_code);
        etCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (isEdit) {
            etCode.setText(employee.getEmployeeCode());
            etCode.setEnabled(false); // Don't allow editing code
        }
        layout.addView(etCode);
        
        final EditText etName = new EditText(this);
        etName.setHint(R.string.hint_full_name);
        if (isEdit) etName.setText(employee.getFullName());
        layout.addView(etName);
        
        final EditText etDept = new EditText(this);
        etDept.setHint(R.string.hint_department);
        if (isEdit) etDept.setText(employee.getDepartment());
        layout.addView(etDept);
        
        builder.setView(layout);
        
        builder.setPositiveButton(R.string.save, (dialog, which) -> {
            String code = etCode.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String dept = etDept.getText().toString().trim();
            
            if (code.isEmpty() || name.isEmpty() || dept.isEmpty()) {
                Toast.makeText(this, R.string.error_fill_all_fields, 
                    Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (!code.matches("^\\d{5}$")) {
                Toast.makeText(this, R.string.error_invalid_employee_code, 
                    Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (isEdit) {
                employee.setFullName(name);
                employee.setDepartment(dept);
                updateEmployee(employee);
            } else {
                Employee newEmployee = new Employee(code, name, dept);
                addEmployee(newEmployee);
            }
        });
        
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }
    
    private void addEmployee(Employee employee) {
        progressBar.setVisibility(View.VISIBLE);
        executorService.execute(() -> {
            long result = employeeDAO.insertEmployee(employee);
            
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                
                if (result > 0) {
                    employee.setEmployeeId((int) result);
                    adapter.addEmployee(employee);
                    Toast.makeText(this, R.string.success_employee_added, 
                        Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.error_employee_exists, 
                        Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    
    private void updateEmployee(Employee employee) {
        progressBar.setVisibility(View.VISIBLE);
        executorService.execute(() -> {
            int result = employeeDAO.updateEmployee(employee);
            
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                
                if (result > 0) {
                    adapter.updateEmployee(employee);
                    Toast.makeText(this, R.string.success_employee_updated, 
                        Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.error_update_failed, 
                        Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    
    private void showDeleteConfirmDialog(Employee employee) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.delete_employee)
            .setMessage(getString(R.string.confirm_delete_employee, employee.getFullName()))
            .setPositiveButton(R.string.delete, (dialog, which) -> deleteEmployee(employee))
            .setNegativeButton(R.string.cancel, null)
            .show();
    }
    
    private void deleteEmployee(Employee employee) {
        progressBar.setVisibility(View.VISIBLE);
        executorService.execute(() -> {
            int result = employeeDAO.deleteEmployee(employee.getEmployeeId());
            
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                
                if (result > 0) {
                    adapter.removeEmployee(employee.getEmployeeId());
                    Toast.makeText(this, R.string.success_employee_deleted, 
                        Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.error_delete_failed, 
                        Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    
    @Override
    public void onItemClick(Employee employee) {
        showEditEmployeeDialog(employee);
    }
    
    @Override
    public void onItemLongClick(Employee employee) {
        showDeleteConfirmDialog(employee);
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
