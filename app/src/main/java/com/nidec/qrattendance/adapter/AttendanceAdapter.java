package com.nidec.qrattendance.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nidec.qrattendance.R;
import com.nidec.qrattendance.database.Attendance;
import com.nidec.qrattendance.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {
    
    private List<Attendance> attendances;
    private OnItemClickListener listener;
    
    public interface OnItemClickListener {
        void onItemClick(Attendance attendance);
    }
    
    public AttendanceAdapter(OnItemClickListener listener) {
        this.attendances = new ArrayList<>();
        this.listener = listener;
    }
    
    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances != null ? attendances : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    public void addAttendance(Attendance attendance) {
        attendances.add(0, attendance);
        notifyItemInserted(0);
    }
    
    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Attendance attendance = attendances.get(position);
        holder.bind(attendance);
    }
    
    @Override
    public int getItemCount() {
        return attendances.size();
    }
    
    class AttendanceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEmployeeCode;
        private TextView tvEmployeeName;
        private TextView tvDepartment;
        private TextView tvStatus;
        private TextView tvTimestamp;
        private TextView tvPreference;
        private CardView cardView;
        
        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmployeeCode = itemView.findViewById(R.id.tv_employee_code);
            tvEmployeeName = itemView.findViewById(R.id.tv_employee_name);
            tvDepartment = itemView.findViewById(R.id.tv_department);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
            tvPreference = itemView.findViewById(R.id.tv_preference);
            cardView = itemView.findViewById(R.id.card_view);
        }
        
        public void bind(final Attendance attendance) {
            tvEmployeeCode.setText(attendance.getEmployeeCode());
            tvEmployeeName.setText(attendance.getEmployeeName());
            tvDepartment.setText(attendance.getDepartment());
            tvStatus.setText(attendance.getAttendanceStatus());
            tvTimestamp.setText(DateTimeUtils.formatDateTime(attendance.getTimestamp()));
            
            if (attendance.getRegistrationPreference() != null && !attendance.getRegistrationPreference().isEmpty()) {
                tvPreference.setVisibility(View.VISIBLE);
                tvPreference.setText("Phần ăn: " + attendance.getRegistrationPreference());
            } else {
                tvPreference.setVisibility(View.GONE);
            }
            
            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(attendance);
                }
            });
        }
    }
}
