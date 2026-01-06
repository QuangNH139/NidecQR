package com.nidec.qrattendance.utils;

import android.content.Context;
import android.os.Environment;

import com.nidec.qrattendance.database.Attendance;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

public class ExcelExporter {
    
    public static File exportAttendanceToExcel(Context context, List<Attendance> attendances) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Attendance Report");
        
        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        
        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "Timestamp", 
            "Mã số nhân viên", 
            "Họ và Tên", 
            "Bộ phận", 
            "Xác nhận Tham gia", 
            "Bạn muốn đăng ký Phần ăn gì?", 
            "Score",
            "Ghi chú"
        };
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Create data rows
        int rowNum = 1;
        for (Attendance attendance : attendances) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(DateTimeUtils.formatDateTime(attendance.getTimestamp()));
            row.createCell(1).setCellValue(attendance.getEmployeeCode() != null ? attendance.getEmployeeCode() : "");
            row.createCell(2).setCellValue(attendance.getEmployeeName() != null ? attendance.getEmployeeName() : "");
            row.createCell(3).setCellValue(attendance.getDepartment() != null ? attendance.getDepartment() : "");
            row.createCell(4).setCellValue(attendance.getAttendanceStatus() != null ? attendance.getAttendanceStatus() : "");
            row.createCell(5).setCellValue(attendance.getRegistrationPreference() != null ? attendance.getRegistrationPreference() : "");
            
            if (attendance.getScore() != null) {
                row.createCell(6).setCellValue(attendance.getScore());
            } else {
                row.createCell(6).setCellValue("");
            }
            
            row.createCell(7).setCellValue(attendance.getNotes() != null ? attendance.getNotes() : "");
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // Create file
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs();
        }
        
        String fileName = "NidecQR_Report_" + DateTimeUtils.formatDateTimeForFile(new Date()) + ".xlsx";
        File file = new File(downloadsDir, fileName);
        
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
        
        return file;
    }
}
