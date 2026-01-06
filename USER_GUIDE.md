# Hướng dẫn sử dụng NidecQR

## Mục lục
1. [Giới thiệu](#giới-thiệu)
2. [Bắt đầu sử dụng](#bắt-đầu-sử-dụng)
3. [Quét QR Code](#quét-qr-code)
4. [Ghi nhận điểm danh](#ghi-nhận-điểm-danh)
5. [Quản lý nhân viên](#quản-lý-nhân-viên)
6. [Xem báo cáo](#xem-báo-cáo)
7. [Xuất Excel](#xuất-excel)

## Giới thiệu

NidecQR là ứng dụng Android giúp quản lý việc tham gia/không tham gia của nhân viên thông qua quét mã QR. Ứng dụng hoạt động hoàn toàn offline và hỗ trợ cả điện thoại thông thường lẫn thiết bị Zebra.

### Tính năng chính:
- Quét QR Code nhanh chóng
- Ghi nhận điểm danh với thông tin chi tiết
- Quản lý danh sách nhân viên
- Thống kê và báo cáo
- Xuất dữ liệu ra Excel

## Bắt đầu sử dụng

### Màn hình chính

Khi mở ứng dụng, bạn sẽ thấy:
- **Danh sách điểm danh** gần đây ở giữa màn hình
- **3 Tab** ở phía trên: Tất cả, Có tham gia, Không tham gia
- **Nút camera** (tròn màu cam) ở góc dưới bên phải - dùng để quét QR
- **Menu** (3 chấm) ở góc trên bên phải

### Các tab

- **Tất cả**: Hiển thị toàn bộ lịch sử điểm danh
- **Có tham gia**: Chỉ hiển thị những người đã xác nhận tham gia
- **Không tham gia**: Chỉ hiển thị những người không tham gia

## Quét QR Code

### Bước 1: Mở camera quét
1. Nhấn vào **nút camera** (biểu tượng máy ảnh tròn màu cam)
2. Ứng dụng sẽ yêu cầu quyền Camera (lần đầu tiên)
3. Nhấn **Cho phép** để tiếp tục

### Bước 2: Quét mã QR
1. Camera sẽ mở tự động
2. Đưa camera vào mã QR của nhân viên
3. Đảm bảo mã QR nằm trong khung quét
4. Ứng dụng sẽ tự động nhận diện và quét

### Lưu ý:
- Mã QR phải chứa **mã nhân viên 5 số** (ví dụ: 44825)
- Nếu mã QR không hợp lệ, sẽ có thông báo lỗi
- Thiết bị Zebra có thể dùng scanner tích hợp (xem hướng dẫn DataWedge)

## Ghi nhận điểm danh

Sau khi quét QR thành công:

### Bước 1: Xem thông tin nhân viên
Màn hình sẽ hiển thị:
- **Mã nhân viên**
- **Họ và tên**
- **Bộ phận**

### Bước 2: Chọn trạng thái tham gia
Chọn một trong hai:
- ⭕ **Có tham gia**
- ⭕ **Không tham gia**

### Bước 3: Đăng ký phần ăn
Chọn từ danh sách thả xuống:
- **Không** - Không đăng ký
- **Mặn** - Đăng ký phần ăn mặn
- **Chay** - Đăng ký phần ăn chay

### Bước 4: Thêm ghi chú (tùy chọn)
- Nhập thông tin bổ sung nếu cần
- Ví dụ: "Đến muộn 15 phút", "Xin về sớm"

### Bước 5: Lưu
1. Nhấn nút **Lưu**
2. Đợi thông báo "Đã lưu thành công!"
3. Ứng dụng sẽ tự động quay về màn hình chính

## Quản lý nhân viên

### Mở danh sách nhân viên
1. Nhấn **Menu** (3 chấm) ở góc trên
2. Chọn **Quản lý nhân viên**

### Thêm nhân viên mới
1. Nhấn nút **+** (tròn màu cam) ở góc dưới
2. Điền thông tin:
   - **Mã nhân viên**: 5 số (ví dụ: 44825)
   - **Họ và tên**: Họ tên đầy đủ
   - **Bộ phận**: Tên bộ phận (ví dụ: Pro#1/FAN CAR)
3. Nhấn **Lưu**

### Sửa thông tin nhân viên
1. **Nhấn vào** nhân viên muốn sửa
2. Cửa sổ sửa thông tin sẽ hiển thị
3. Cập nhật **Họ và tên** hoặc **Bộ phận**
4. Nhấn **Lưu**

**Lưu ý:** Không thể thay đổi mã nhân viên sau khi đã tạo

### Xóa nhân viên
1. **Giữ lâu** (long press) vào nhân viên muốn xóa
2. Hộp thoại xác nhận sẽ hiển thị
3. Nhấn **Xóa** để xác nhận

**⚠️ Cảnh báo:** Xóa nhân viên sẽ không xóa lịch sử điểm danh của họ

## Xem báo cáo

### Mở màn hình báo cáo
1. Nhấn **Menu** (3 chấm) ở góc trên
2. Chọn **Báo cáo**

### Chọn khoảng thời gian

Từ danh sách "Khoảng thời gian":
- **Hôm nay**: Chỉ dữ liệu hôm nay
- **Tuần này**: Từ đầu tuần đến hôm nay
- **Tháng này**: Từ đầu tháng đến hôm nay
- **Tùy chỉnh**: Chọn ngày bắt đầu và kết thúc tùy ý

#### Chọn ngày tùy chỉnh:
1. Chọn **Tùy chỉnh**
2. Chọn **ngày bắt đầu** từ lịch
3. Chọn **ngày kết thúc** từ lịch

### Lọc theo bộ phận

1. Từ danh sách "Bộ phận", chọn:
   - **Tất cả**: Xem tất cả bộ phận
   - Hoặc chọn một bộ phận cụ thể

### Xem thống kê

Báo cáo hiển thị:
- **Tổng số lượt**: Tổng số lần điểm danh
- **Số người tham gia**: Số lượt chọn "Có tham gia"
- **Số người không tham gia**: Số lượt chọn "Không tham gia"

### Xem chi tiết

Cuộn xuống để xem danh sách chi tiết từng bản ghi điểm danh.

## Xuất Excel

### Tạo file Excel

1. Trong màn hình **Báo cáo**
2. Chọn khoảng thời gian và bộ phận cần xuất
3. Nhấn nút **Xuất Excel**
4. Ứng dụng sẽ yêu cầu quyền Storage (lần đầu)
5. Nhấn **Cho phép**
6. Đợi thông báo "Đã xuất file thành công"

### Tìm file Excel

File sẽ được lưu tại:
```
Bộ nhớ thiết bị/Downloads/NidecQR_Report_[ngày_giờ].xlsx
```

Ví dụ: `NidecQR_Report_20240106_140530.xlsx`

### Mở file Excel

1. Mở ứng dụng **Files** hoặc **My Files**
2. Vào thư mục **Downloads**
3. Tìm file có tên bắt đầu bằng `NidecQR_Report_`
4. Nhấn vào file để mở (cần ứng dụng Excel hoặc tương tự)

### Nội dung file Excel

File chứa các cột:
1. **Timestamp**: Ngày giờ điểm danh
2. **Mã số nhân viên**: Mã 5 số
3. **Họ và Tên**: Tên đầy đủ
4. **Bộ phận**: Bộ phận làm việc
5. **Xác nhận Tham gia**: Có/Không
6. **Phần ăn**: Mặn/Chay/Không
7. **Score**: Điểm số (nếu có)
8. **Ghi chú**: Ghi chú thêm

## Mẹo sử dụng

### Quét nhanh liên tục
- Sau khi lưu điểm danh, nhấn lại nút camera để quét người tiếp theo
- Không cần quay về màn hình chính

### Tìm kiếm nhanh
- Ở màn hình chính, nhấn biểu tượng **kính lúp**
- Gõ tên, mã nhân viên hoặc bộ phận để tìm

### Làm mới dữ liệu
- Nhấn biểu tượng **làm mới** (mũi tên tròn) để cập nhật danh sách

### Sao lưu dữ liệu
- Thường xuyên xuất Excel để có bản sao lưu
- Lưu file Excel vào Google Drive hoặc máy tính

## Xử lý lỗi

### "Mã QR không hợp lệ"
- Đảm bảo QR code chứa đúng 5 số
- Kiểm tra QR code không bị hỏng

### "Không tìm thấy nhân viên"
- Nhân viên chưa được thêm vào hệ thống
- Vào **Quản lý nhân viên** để thêm mới

### "Lưu thất bại"
- Kiểm tra kết nối database
- Thử khởi động lại ứng dụng

### "Xuất file thất bại"
- Kiểm tra quyền Storage
- Kiểm tra dung lượng còn trống
- Thử khởi động lại ứng dụng

## Câu hỏi thường gặp

**Q: Ứng dụng có cần internet không?**  
A: Không. Ứng dụng hoạt động hoàn toàn offline.

**Q: Dữ liệu có bị mất khi tắt ứng dụng không?**  
A: Không. Tất cả được lưu trong database SQLite trên thiết bị.

**Q: Có thể chỉnh sửa điểm danh đã lưu không?**  
A: Có. Nhấn vào bản ghi trong danh sách để xem và chỉnh sửa.

**Q: Tối đa bao nhiêu nhân viên?**  
A: Không giới hạn (phụ thuộc vào bộ nhớ thiết bị).

**Q: File Excel có mở được trên máy tính không?**  
A: Có. File .xlsx tương thích với Excel, Google Sheets, LibreOffice.

---

**Hỗ trợ kỹ thuật:**  
Nếu gặp vấn đề, vui lòng liên hệ bộ phận IT hoặc tạo issue trên GitHub.
