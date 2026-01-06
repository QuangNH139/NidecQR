# Hướng dẫn Build APK

## Yêu cầu

### Phần mềm cần thiết:
- **Android Studio:** Arctic Fox (2020.3.1) trở lên
- **JDK:** Java Development Kit 8 hoặc 11
- **Android SDK:** API Level 24 trở lên
- **Gradle:** 7.0+ (tự động cài qua Android Studio)

### Kiến thức cơ bản:
- Biết cách sử dụng Android Studio
- Hiểu cơ bản về Git

## Phương pháp 1: Build từ Android Studio (Khuyến nghị)

### Bước 1: Clone Repository

```bash
git clone https://github.com/QuangNH139/NidecQR.git
cd NidecQR
```

### Bước 2: Mở Project

1. Mở **Android Studio**
2. Chọn **File** → **Open**
3. Duyệt đến thư mục **NidecQR**
4. Nhấn **OK**

### Bước 3: Sync Gradle

1. Android Studio sẽ tự động bắt đầu sync
2. Nếu không, chọn **File** → **Sync Project with Gradle Files**
3. Đợi quá trình sync hoàn tất (có thể mất vài phút)

### Bước 4: Build Debug APK

#### Cách 1: Qua Menu
1. Chọn **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
2. Đợi build hoàn tất
3. Thông báo sẽ hiển thị: "APK(s) generated successfully"
4. Nhấn **locate** để mở thư mục chứa APK

#### Cách 2: Qua Gradle Task
1. Mở cửa sổ **Gradle** (bên phải)
2. Expand: **NidecQR** → **app** → **Tasks** → **build**
3. Double-click vào **assembleDebug**
4. Đợi build hoàn tất

**Vị trí APK:**
```
app/build/outputs/apk/debug/app-debug.apk
```

### Bước 5: Build Release APK

#### Cấu hình Signing (lần đầu)

1. Chọn **Build** → **Generate Signed Bundle / APK**
2. Chọn **APK** → **Next**
3. Nhấn **Create new...** để tạo keystore mới

**Tạo Keystore:**
- **Key store path:** Chọn vị trí lưu (ví dụ: `nidec-keystore.jks`)
- **Password:** Tạo mật khẩu (lưu lại cẩn thận!)
- **Alias:** nidec-key
- **Alias password:** Tạo mật khẩu cho alias
- **Validity (years):** 25
- **Certificate:**
  - First and Last Name: Nidec
  - Organizational Unit: IT
  - Organization: Nidec Corporation
  - City or Locality: [Your City]
  - State or Province: [Your State]
  - Country Code: VN
4. Nhấn **OK**

#### Build Release APK:

1. Chọn keystore vừa tạo
2. Nhập passwords
3. Nhấn **Next**
4. Chọn **release** build variant
5. Chọn signature versions: **V1** và **V2**
6. Nhấn **Finish**

**Vị trí APK:**
```
app/build/outputs/apk/release/app-release.apk
```

## Phương pháp 2: Build từ Command Line

### Bước 1: Cài đặt môi trường

#### Windows:
```bash
# Set JAVA_HOME
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_XXX
set PATH=%PATH%;%JAVA_HOME%\bin
```

#### Linux/Mac:
```bash
# Set JAVA_HOME
export JAVA_HOME=/path/to/jdk
export PATH=$PATH:$JAVA_HOME/bin
```

### Bước 2: Build Debug APK

```bash
cd NidecQR
./gradlew assembleDebug
```

**Windows:**
```bash
gradlew.bat assembleDebug
```

**Vị trí APK:**
```
app/build/outputs/apk/debug/app-debug.apk
```

### Bước 3: Build Release APK

Tạo file `keystore.properties` trong thư mục root:

```properties
storePassword=YOUR_STORE_PASSWORD
keyPassword=YOUR_KEY_PASSWORD
keyAlias=nidec-key
storeFile=path/to/nidec-keystore.jks
```

Cập nhật `app/build.gradle`:

```gradle
android {
    signingConfigs {
        release {
            def keystorePropertiesFile = rootProject.file("keystore.properties")
            def keystoreProperties = new Properties()
            keystoreProperties.load(new FileInputStream(keystorePropertiesFile))
            
            storeFile file(keystoreProperties['storeFile'])
            storePassword keystoreProperties['storePassword']
            keyAlias keystoreProperties['keyAlias']
            keyPassword keystoreProperties['keyPassword']
        }
    }
    
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

Build:
```bash
./gradlew assembleRelease
```

## Cài đặt APK lên thiết bị

### Phương pháp 1: Qua USB (ADB)

1. Bật **Developer Options** trên thiết bị Android
2. Bật **USB Debugging**
3. Kết nối thiết bị với máy tính qua USB
4. Chạy lệnh:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

Hoặc cho release:
```bash
adb install app/build/outputs/apk/release/app-release.apk
```

### Phương pháp 2: Qua File Manager

1. Sao chép APK vào thiết bị (qua USB hoặc email)
2. Mở **File Manager** trên thiết bị
3. Tìm file APK
4. Nhấn vào file APK
5. Cho phép cài đặt từ nguồn không xác định (nếu được hỏi)
6. Nhấn **Install**

### Phương pháp 3: Qua Android Studio

1. Kết nối thiết bị qua USB
2. Trong Android Studio, chọn thiết bị từ dropdown
3. Nhấn **Run** (▶️) hoặc Shift+F10
4. Ứng dụng sẽ tự động build và cài đặt

## Tùy chỉnh Build

### Thay đổi Version

Trong `app/build.gradle`:

```gradle
android {
    defaultConfig {
        versionCode 2        // Tăng lên mỗi lần build
        versionName "1.1"    // Hiển thị cho người dùng
    }
}
```

### Thay đổi Package Name

Trong `app/build.gradle`:

```gradle
android {
    defaultConfig {
        applicationId "com.yourcompany.qrattendance"
    }
}
```

**Lưu ý:** Cần refactor code nếu thay đổi package name

### Giảm kích thước APK

Trong `app/build.gradle`:

```gradle
android {
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### Build cho nhiều CPU architectures

```gradle
android {
    splits {
        abi {
            enable true
            reset()
            include 'armeabi-v7a', 'arm64-v8a', 'x86', 'x86_64'
            universalApk true
        }
    }
}
```

## Build Bundle (AAB)

Để upload lên Google Play Store:

### Qua Android Studio:

1. **Build** → **Generate Signed Bundle / APK**
2. Chọn **Android App Bundle**
3. Chọn keystore
4. Nhấn **Finish**

### Qua Command Line:

```bash
./gradlew bundleRelease
```

**Vị trí AAB:**
```
app/build/outputs/bundle/release/app-release.aab
```

## Troubleshooting

### Lỗi: "SDK location not found"

**Giải pháp:**
Tạo file `local.properties`:
```properties
sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk
```
(Thay đường dẫn cho đúng với máy bạn)

### Lỗi: "Gradle sync failed"

**Giải pháp:**
1. File → Invalidate Caches / Restart
2. Xóa thư mục `.gradle` trong project
3. Sync lại

### Lỗi: "Build failed" với POI libraries

**Giải pháp:**
Thêm vào `app/build.gradle`:
```gradle
android {
    packagingOptions {
        exclude 'META-INF/DEPENDENCIES'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/NOTICE.txt'
    }
}
```

### APK quá lớn

**Giải pháp:**
- Bật minifyEnabled
- Bật shrinkResources
- Remove unused resources
- Build separate APKs cho từng architecture

## Kiểm tra APK

### Kiểm tra thông tin APK:

```bash
aapt dump badging app-release.apk
```

### Kiểm tra kích thước:

```bash
ls -lh app/build/outputs/apk/release/app-release.apk
```

### Analyze APK trong Android Studio:

1. **Build** → **Analyze APK**
2. Chọn file APK
3. Xem chi tiết size của từng component

## Best Practices

1. **Luôn tăng versionCode** khi build version mới
2. **Lưu keystore** ở nơi an toàn (backup!)
3. **Không commit keystore** vào Git
4. **Test kỹ** trước khi release
5. **Giữ passwords** cẩn thận
6. **Build release** cho production
7. **Build debug** chỉ để test

## Automation (CI/CD)

Có thể tự động hóa build với:
- **GitHub Actions**
- **GitLab CI**
- **Jenkins**
- **Bitrise**

Example GitHub Actions workflow:

```yaml
name: Android CI

on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 11
      uses: actions/setup-java@v2
      with:
        java-version: '11'
    - name: Build with Gradle
      run: ./gradlew assembleDebug
    - name: Upload APK
      uses: actions/upload-artifact@v2
      with:
        name: app-debug
        path: app/build/outputs/apk/debug/app-debug.apk
```

---

**Lưu ý:** Hướng dẫn này dành cho build từ source code. Để sử dụng ứng dụng, chỉ cần cài đặt APK đã build sẵn.

**Hỗ trợ:** Nếu gặp vấn đề khi build, vui lòng tạo issue trên GitHub.
