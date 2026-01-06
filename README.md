# NidecQR - Ứng dụng Quản lý Tham gia qua QR Code

Ứng dụng Android (100% JAVA) để quản lý việc tham gia/không tham gia của nhân viên thông qua quét mã QR, hỗ trợ thiết bị Zebra.

## ✨ Tính năng

- ✅ **Quét QR Code** - Sử dụng ZXing và hỗ trợ Zebra DataWedge
- ✅ **Quản lý điểm danh** - Ghi nhận tham gia/không tham gia với thông tin chi tiết
- ✅ **Database SQLite** - Lưu trữ offline hoàn toàn
- ✅ **Quản lý nhân viên** - Thêm, sửa, xóa thông tin nhân viên
- ✅ **Báo cáo thống kê** - Thống kê theo thời gian và bộ phận
- ✅ **Export Excel** - Xuất dữ liệu báo cáo ra file Excel (.xlsx)
- ✅ **Hỗ trợ đa ngôn ngữ** - Tiếng Việt và English
- ✅ **Material Design** - Giao diện đẹp, hiện đại

## 🚀 Yêu cầu hệ thống

- **Android:** 7.0 (API 24) trở lên
- **Camera:** Hỗ trợ camera để quét QR (hoặc Zebra scanner)
- **Storage:** Quyền ghi file để export Excel

## 📦 Cài đặt

### Yêu cầu môi trường phát triển

- **Android Studio:** Arctic Fox (2020.3.1) trở lên
- **JDK:** 8 trở lên
- **Gradle:** 7.0+

### Các bước build

1. **Clone repository:**
   ```bash
   git clone https://github.com/QuangNH139/NidecQR.git
   cd NidecQR
   ```

2. **Mở project trong Android Studio:**
   - File → Open → Chọn thư mục NidecQR

3. **Sync Gradle:**
   - Android Studio sẽ tự động sync
   - Hoặc File → Sync Project with Gradle Files

4. **Build APK:**
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - APK sẽ được tạo tại: `app/build/outputs/apk/debug/app-debug.apk`

5. **Cài đặt trên thiết bị:**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

## 📱 Sử dụng

### Quét QR Code nhân viên

1. Mở ứng dụng NidecQR
2. Nhấn nút **Quét QR Code** (biểu tượng camera)
3. Đưa camera vào mã QR của nhân viên (mã 5 số)
4. Ứng dụng sẽ tự động nhận diện và chuyển đến màn hình điểm danh

### Ghi nhận điểm danh

1. Sau khi quét QR, màn hình chi tiết điểm danh sẽ hiển thị
2. Chọn trạng thái: **Có tham gia** hoặc **Không tham gia**
3. Chọn phần ăn: **Mặn**, **Chay**, hoặc **Không**
4. Thêm ghi chú (nếu cần)
5. Nhấn **Lưu**

### Xem danh sách điểm danh

- Màn hình chính hiển thị danh sách điểm danh gần đây
- Sử dụng Tab để lọc: **Tất cả**, **Có tham gia**, **Không tham gia**
- Nhấn vào item để xem chi tiết

### Quản lý nhân viên

1. Menu → **Quản lý nhân viên**
2. Nhấn nút **+** để thêm nhân viên mới
3. Nhấn vào nhân viên để sửa thông tin
4. Giữ lâu (long press) để xóa nhân viên

### Xem báo cáo và Export Excel

1. Menu → **Báo cáo**
2. Chọn khoảng thời gian: Hôm nay, Tuần này, Tháng này, hoặc Tùy chỉnh
3. Chọn bộ phận (hoặc Tất cả)
4. Xem thống kê và danh sách chi tiết
5. Nhấn **Xuất Excel** để tạo file báo cáo
6. File sẽ được lưu tại: `Downloads/NidecQR_Report_[datetime].xlsx`

## 🔧 Cấu hình Zebra DataWedge

Chi tiết xem file [ZEBRA_SETUP.md](ZEBRA_SETUP.md)

### Tóm tắt:

1. Mở ứng dụng **DataWedge** trên thiết bị Zebra
2. Tạo Profile mới với tên: **NidecQR**
3. Cấu hình:
   - **Associated Apps:** com.nidec.qrattendance
   - **Intent Output:**
     - Intent action: `com.nidec.qrattendance.SCAN`
     - Intent category: `android.intent.category.DEFAULT`
     - Intent delivery: Broadcast

## 📂 Cấu trúc project

```
app/
├── src/main/
│   ├── java/com/nidec/qrattendance/
│   │   ├── MainActivity.java
│   │   ├── AttendanceDetailActivity.java
│   │   ├── EmployeeListActivity.java
│   │   ├── ReportActivity.java
│   │   ├── database/
│   │   │   ├── DatabaseHelper.java
│   │   │   ├── Employee.java
│   │   │   ├── Attendance.java
│   │   │   ├── EmployeeDAO.java
│   │   │   └── AttendanceDAO.java
│   │   ├── scanner/
│   │   │   ├── QRScannerManager.java
│   │   │   └── ZebraDataWedgeHelper.java
│   │   ├── adapter/
│   │   │   ├── AttendanceAdapter.java
│   │   │   └── EmployeeAdapter.java
│   │   └── utils/
│   │       ├── Constants.java
│   │       ├── DateTimeUtils.java
│   │       └── ExcelExporter.java
│   ├── res/
│   │   ├── layout/
│   │   ├── values/
│   │   ├── values-en/
│   │   ├── menu/
│   │   └── xml/
│   └── AndroidManifest.xml
└── build.gradle
```

## 🗃️ Database Schema

### Bảng `employees`
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| employee_id | INTEGER | Primary Key, Auto Increment |
| employee_code | TEXT | Mã nhân viên (5 số), UNIQUE |
| full_name | TEXT | Họ và tên |
| department | TEXT | Bộ phận |
| created_at | TIMESTAMP | Thời gian tạo |

### Bảng `attendance`
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| id | INTEGER | Primary Key, Auto Increment |
| employee_id | INTEGER | Foreign Key → employees |
| timestamp | DATETIME | Thời gian điểm danh |
| attendance_status | TEXT | Trạng thái (Có/Không tham gia) |
| registration_preference | TEXT | Phần ăn (Mặn/Chay/Không) |
| score | INTEGER | Điểm số (nếu có) |
| notes | TEXT | Ghi chú |
| created_at | TIMESTAMP | Thời gian tạo record |

## 📚 Thư viện sử dụng

- **ZXing** - QR Code scanning
- **Apache POI** - Excel export
- **Material Components** - UI components
- **RecyclerView** - List display

## 🛠️ Troubleshooting

### App không quét được QR
- Kiểm tra quyền Camera
- Đảm bảo mã QR chứa 5 số
- Thử làm sạch camera

### Không xuất được Excel
- Kiểm tra quyền Storage
- Đảm bảo có dữ liệu để export
- Kiểm tra dung lượng thiết bị

### Zebra scanner không hoạt động
- Kiểm tra DataWedge Profile đã được tạo
- Xem hướng dẫn chi tiết trong ZEBRA_SETUP.md

## 📄 License

Copyright © 2024 Nidec. All rights reserved.

## 👥 Tác giả

- QuangNH139

## 📞 Hỗ trợ

Nếu gặp vấn đề, vui lòng tạo issue trên GitHub repository.