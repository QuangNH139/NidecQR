# Hướng dẫn cấu hình Zebra DataWedge

## Giới thiệu

DataWedge là ứng dụng có sẵn trên các thiết bị Zebra, cho phép cấu hình và sử dụng barcode scanner tích hợp. Tài liệu này hướng dẫn cách cấu hình DataWedge để làm việc với ứng dụng NidecQR.

## Yêu cầu

- Thiết bị Zebra (mobile computer hoặc smartphone)
- DataWedge version 6.5 trở lên
- Ứng dụng NidecQR đã được cài đặt

## Cách 1: Tự động cấu hình (Khuyến nghị)

Ứng dụng NidecQR sẽ tự động tạo profile DataWedge khi chạy lần đầu trên thiết bị Zebra.

### Kiểm tra profile đã được tạo:

1. Mở ứng dụng **DataWedge**
2. Tìm profile có tên: **NidecQR**
3. Nếu đã có, bỏ qua Cách 2

## Cách 2: Cấu hình thủ công

### Bước 1: Mở DataWedge

1. Từ màn hình Home, mở App Drawer
2. Tìm và mở ứng dụng **DataWedge**

### Bước 2: Tạo Profile mới

1. Nhấn vào menu (⋮) ở góc trên bên phải
2. Chọn **New Profile**
3. Nhập tên: `NidecQR`
4. Nhấn **OK**

### Bước 3: Cấu hình Associated Apps

1. Trong profile **NidecQR**, tìm mục **Associated apps**
2. Nhấn vào **Associated apps**
3. Nhấn menu (⋮) và chọn **New app/activity**
4. Điền thông tin:
   ```
   Application: com.nidec.qrattendance
   Activity: * (dấu sao)
   ```
5. Nhấn **OK**

### Bước 4: Cấu hình Barcode Input

1. Quay lại màn hình profile **NidecQR**
2. Tìm và nhấn vào **Barcode Input**
3. Đảm bảo **Enabled** được bật (ON)
4. Trong mục **Decoders**, bật các decoder:
   - **QR Code** - BẬT (quan trọng!)
   - Code 128, Code 39, Data Matrix (tùy chọn)

### Bước 5: Cấu hình Intent Output

**Quan trọng:** Đây là bước quan trọng nhất!

1. Quay lại màn hình profile **NidecQR**
2. Tìm và nhấn vào **Intent Output**
3. Bật **Enabled** (ON)
4. Cấu hình các thông số:

#### Intent action:
```
com.nidec.qrattendance.SCAN
```

#### Intent category:
```
android.intent.category.DEFAULT
```

#### Intent delivery:
- Chọn: **Broadcast Intent** (hoặc **Send via startActivity**)
- Khuyến nghị: **Broadcast Intent**

### Bước 6: Cấu hình Keystroke Output (Tắt)

1. Quay lại màn hình profile **NidecQR**
2. Tìm và nhấn vào **Keystroke Output**
3. **TẮT** Enabled (OFF)

*Lý do: Chúng ta dùng Intent Output thay vì Keystroke*

### Bước 7: Lưu và Kích hoạt

1. Nhấn nút **Back** để quay lại danh sách profiles
2. Đảm bảo profile **NidecQR** đã được tạo
3. Profile sẽ tự động kích hoạt khi mở ứng dụng NidecQR

## Kiểm tra cấu hình

### Test Scanner

1. Mở ứng dụng **NidecQR**
2. Nhấn nút **Quét QR Code**
3. Thay vì dùng camera, nhấn nút **scan** vật lý trên thiết bị Zebra
4. Quét một mã QR code nhân viên
5. Nếu thành công, ứng dụng sẽ chuyển đến màn hình điểm danh

### Nếu không hoạt động:

1. Kiểm tra lại Intent Action và Category
2. Đảm bảo Associated Apps đã đúng package name
3. Khởi động lại ứng dụng NidecQR
4. Khởi động lại thiết bị

## Cấu hình chi tiết (Advanced)

### Data Formatting

Nếu cần format dữ liệu trước khi gửi:

1. Vào **Intent Output** → **Advanced data formatting**
2. Thêm rule nếu cần (thường không cần thiết)

### Scanner Parameters

Tối ưu hóa scanner:

1. Vào **Barcode Input** → **Reader Params**
2. Cấu hình:
   - **Illumination Mode**: Auto
   - **Picklist Mode**: Disabled
   - **Beam Timer**: 5000ms (5 giây)

### Multi-Barcode

Nếu cần quét nhiều mã cùng lúc:

1. Vào **Barcode Input** → **Multi-barcode**
2. Cấu hình theo nhu cầu (thường không cần)

## Troubleshooting

### Scanner không phản hồi

**Nguyên nhân:** Profile chưa được kích hoạt

**Giải pháp:**
1. Mở DataWedge
2. Kiểm tra profile **NidecQR** có dấu tick xanh không
3. Nếu không, nhấn vào profile để kích hoạt

### Quét được nhưng không có dữ liệu

**Nguyên nhân:** Intent Output cấu hình sai

**Giải pháp:**
1. Kiểm tra lại Intent action: `com.nidec.qrattendance.SCAN`
2. Đảm bảo Intent delivery là **Broadcast**
3. Khởi động lại ứng dụng

### QR Code không được nhận diện

**Nguyên nhân:** QR Code decoder chưa được bật

**Giải pháp:**
1. Vào **Barcode Input** → **Decoders**
2. Bật **QR Code**
3. Lưu cấu hình

### Dữ liệu bị trùng lặp

**Nguyên nhân:** Cả Intent và Keystroke đều bật

**Giải pháp:**
1. Tắt **Keystroke Output**
2. Chỉ giữ **Intent Output** bật

## Cấu hình mẫu (JSON)

Dưới đây là cấu hình mẫu dưới dạng JSON (có thể import vào DataWedge):

```json
{
  "PROFILE_NAME": "NidecQR",
  "PROFILE_ENABLED": "true",
  "CONFIG_MODE": "UPDATE",
  "APP_LIST": [
    {
      "PACKAGE_NAME": "com.nidec.qrattendance",
      "ACTIVITY_LIST": ["*"]
    }
  ],
  "PLUGIN_CONFIG": {
    "PLUGIN_NAME": "BARCODE",
    "PARAM_LIST": {
      "scanner_input_enabled": "true",
      "decoder_qrcode": "true"
    }
  },
  "INTENT_OUTPUT": {
    "intent_output_enabled": "true",
    "intent_action": "com.nidec.qrattendance.SCAN",
    "intent_category": "android.intent.category.DEFAULT",
    "intent_delivery": "2"
  }
}
```

## Export/Import Profile

### Export Profile

1. Mở DataWedge
2. Chọn profile **NidecQR**
3. Menu → **Export**
4. File sẽ được lưu tại: `/sdcard/datawedge_profiles/`

### Import Profile

1. Sao chép file profile vào thiết bị
2. Mở DataWedge
3. Menu → **Import**
4. Chọn file profile
5. Profile sẽ được tạo tự động

## Sử dụng DataWedge API

Ứng dụng NidecQR sử dụng DataWedge Intent API để tự động tạo profile. Code được implement trong file:
```
ZebraDataWedgeHelper.java
```

Các Intent actions sử dụng:
- `com.symbol.datawedge.api.CREATE_PROFILE`
- `com.symbol.datawedge.api.SET_CONFIG`

## Thông tin thêm

### Tài liệu chính thức Zebra

- [DataWedge User Guide](https://techdocs.zebra.com/datawedge/)
- [DataWedge API](https://techdocs.zebra.com/datawedge/latest/guide/api/)

### Các thiết bị Zebra được hỗ trợ

- TC21/TC26
- TC52/TC57
- TC72/TC77
- MC33
- MC93
- và hầu hết thiết bị Android của Zebra

---

**Lưu ý:** Hướng dẫn này dựa trên DataWedge version 11.x. Giao diện có thể khác nhau tùy phiên bản nhưng các bước cơ bản tương tự.

**Hỗ trợ:** Nếu gặp vấn đề, vui lòng liên hệ bộ phận IT hoặc tham khảo tài liệu Zebra chính thức.
