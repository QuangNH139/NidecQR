package com.nidec.qrattendance.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nidec.qrattendance.R;
import com.nidec.qrattendance.database.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    
    private List<Employee> employees;
    private OnItemClickListener listener;
    
    public interface OnItemClickListener {
        void onItemClick(Employee employee);
        void onItemLongClick(Employee employee);
    }
    
    public EmployeeAdapter(OnItemClickListener listener) {
        this.employees = new ArrayList<>();
        this.listener = listener;
    }
    
    public void setEmployees(List<Employee> employees) {
        this.employees = employees != null ? employees : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    public void addEmployee(Employee employee) {
        employees.add(employee);
        notifyItemInserted(employees.size() - 1);
    }
    
    public void updateEmployee(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeId() == employee.getEmployeeId()) {
                employees.set(i, employee);
                notifyItemChanged(i);
                break;
            }
        }
    }
    
    public void removeEmployee(int employeeId) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeId() == employeeId) {
                employees.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }
    
    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employees.get(position);
        holder.bind(employee);
    }
    
    @Override
    public int getItemCount() {
        return employees.size();
    }
    
    class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEmployeeCode;
        private TextView tvEmployeeName;
        private TextView tvDepartment;
        private CardView cardView;
        
        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmployeeCode = itemView.findViewById(R.id.tv_employee_code);
            tvEmployeeName = itemView.findViewById(R.id.tv_employee_name);
            tvDepartment = itemView.findViewById(R.id.tv_department);
            cardView = itemView.findViewById(R.id.card_view);
        }
        
        public void bind(final Employee employee) {
            tvEmployeeCode.setText(employee.getEmployeeCode());
            tvEmployeeName.setText(employee.getFullName());
            tvDepartment.setText(employee.getDepartment());
            
            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(employee);
                }
            });
            
            cardView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onItemLongClick(employee);
                }
                return true;
            });
        }
    }
}
