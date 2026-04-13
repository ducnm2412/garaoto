# Tài liệu API GaraOto Backend (Có Mẫu Payload)

Đây là danh sách tổng hợp các điểm cuối (Endpoints) API của hệ thống GaraOto Backend kèm theo **mẫu chuỗi JSON (Request Body)** dùng cho việc kiểm thử trên Postman.

---

## Cấu hình Postman Cơ Bản
- **Base URL:** `http://localhost:8080/api`
- **Xác thực (Authentication):** Sau khi có Token từ API `/api/auth/login`, ở các API khác bạn chuyển sang Tab **Authorization** -> Chọn loại **Bearer Token** -> Dán Token vào.

---

## 1. Phân hệ Xác thực & Đăng nhập (Auth)

### 1.1 Đăng ký tài khoản (Register)
* `POST /api/auth/register`
* Gửi tùy thuộc vào `vaiTro` (KhachHang / Admin / NhanVienKyThuat). Các trường không thuộc vai trò đó có thể bỏ trống.
```json
{
    "hoTen": "Nguyễn Văn A",
    "email": "nguyenvana@gmail.com",
    "soDienThoai": "0987654321",
    "matKhau": "123456",
    "diaChi": "123 Đường Nam Kỳ Khởi Nghĩa, HCM",
    "vaiTro": "KhachHang",
    
    "cccd": "079012345678",
    "soGplx": "790123456",
    "hangGplx": "B2"
}
```

### 1.2 Đăng nhập (Login)
* `POST /api/auth/login`
```json
{
    "email": "nguyenvana@gmail.com",
    "matKhau": "123456"
}
```

---

## 2. Quản lý Người Dùng & Quyền (User Management)
*(Tất cả API từ bước này trở đi đều yêu cầu truyền Bearer Token)*

### Cập nhật Thông tin Cơ bản
* `PUT /api/nguoi-dung/{id}`
```json
{
    "hoTen": "Nguyễn Văn B",
    "soDienThoai": "0912345678",
    "diaChi": "456 Đường Lê Lợi, Quận 1, HCM"
}
```

### Khách Hàng (Tạo / Cập Nhật)
> Lưu ý: Việc tạo mới bằng `POST /api/khach-hang` hiện tại không được dùng vì tạo chung nguyên khối ở API Register bên trên. API `PUT` được dùng để sửa thuộc tính riêng biệt của khách theo ID.
* `PUT /api/khach-hang/{id}`
```json
{
    "cccd": "079012345699",
    "soGplx": "790123499",
    "hangGplx": "C"
}
```

### Thợ Kỹ Thuật (Tạo / Cập Nhật)
* `PUT /api/nhan-vien-ky-thuat/{id}`
```json
{
    "chuyenMon": "Thợ gầm máy",
    "caLamViec": "Sáng (08:00 - 17:00)"
}
```

---

## 3. Quản lý Xe Ô tô (Vehicles)

### 3.1 Xe Của Khách Hàng
* `POST /api/xe-khach-hang` (Tạo mới)
* `PUT /api/xe-khach-hang/{id}` (Cập nhật)
```json
{
    "maNguoiDung": 1,
    "bienSo": "51G-12345",
    "hangXe": "Toyota",
    "dongXe": "Camry",
    "namSanXuat": 2021,
    "mauSac": "Trắng",
    "soKhung": "1T234HDFSD23",
    "soMay": "M92323-A"
}
```

### 3.2 Xe Cho Thuê do Gara cung cấp
* `POST /api/xe-cho-thue` (Thêm xe mới vào hệ thống Gara)
* `PUT /api/xe-cho-thue/{id}` (Sửa tình trạng xe)
```json
{
    "bienSo": "51A-99999",
    "hangXe": "Mazda",
    "dongXe": "CX-5",
    "soCho": 5,
    "hopSo": "Tự động",
    "nhienLieu": "Xăng",
    "giaTheoNgay": 1500000.0,
    "tinhTrang": "SanSang",
    "hinhAnh": "https://link-anh.com/mazda.jpg",
    "namSanXuat": 2022
}
```

---

## 4. Dịch Vụ - Lịch Hẹn - Sửa Chữa

### 4.1 Quản trị Gói Dịch Vụ Sửa Chữa
* `POST /api/dich-vu-sua-chua`
* `PUT /api/dich-vu-sua-chua/{id}`
```json
{
    "tenDichVu": "Thay nhớt động cơ",
    "moTa": "Sử dụng nhớt Castrol Edge chuẩn tổng hợp",
    "giaCoBan": 850000.0,
    "trangThai": "DangKinhDoanh"
}
```

### 4.2 Lịch Hẹn Đặt Trước
* `POST /api/lich-hen-sua-chua`
```json
{
    "maNguoiDung": 1,
    "maXeKh": 1,
    "ngayHen": "2026-08-15",
    "gioHen": "09:30:00",
    "moTaLoi": "Động cơ kêu to khi khởi động buổi sáng"
}
```

### 4.3 Phiếu Tiếp Nhận (Phiếu Sửa Chữa)
* `POST /api/phieu-sua-chua`
```json
{
    "maLichHen": 1,
    "maXeKh": 1,
    "maNguoiDung": 1,
    "chanDoan": "Hỏng roan động cơ, thiếu nhớt bôi trơn",
    "tongTien": 0.0,
    "trangThai": "DangXuLy"
}
```

### 4.4 Phân Công Cho Thợ
* `POST /api/phan-cong-sua-chua`
```json
{
    "maPhieuSua": 1,
    "maNguoiDungNv": 3, 
    "maNguoiDungAdmin": 2,
    "ghiChu": "Ưu tiên làm gấp vì khách vội",
    "trangThai": "DaPhanCong"
}
```

### 4.5 Chi Tiết Các Món Phụ Tùng / Dịch Vụ (Của 1 Phiếu Sửa Cụ Thể)
* `POST /api/chi-tiet-sua-chua`
```json
{
    "maPhieuSua": 1,
    "maDichVu": 2,
    "soLuong": 2,
    "donGia": 150000.0,
    "thanhTien": 300000.0,
    "ghiChu": "Thay 2 ron cao su"
}
```

---

## 5. Dịch Vụ Thuê Xe Độc Lập

### 5.1 Đơn Yêu Cầu Mướn Xe
* `POST /api/don-thue-xe`
```json
{
    "maNguoiDung": 1,
    "maXeThue": 1,
    "ngayNhan": "2026-10-01T08:00:00",
    "ngayTra": "2026-10-03T18:00:00",
    "diaDiemNhan": "Q1, TP.HCM",
    "diaDiemTra": "Q1, TP.HCM"
}
```

### 5.2 Lập Hợp Đồng Thuê
* `POST /api/hop-dong-thue`
```json
{
    "maDonThue": 1,
    "dieuKhoan": "Phạt 500k nếu trả trễ 1 ngày",
    "ghiChu": "Khách đã để lại hộ chiếu làm tin",
    "trangThai": "DaKyKet"
}
```

---

## 6. Thanh Toán & Đánh Giá

### 6.1 Biên Lai Thanh Toán (Ghi Nợ / Trả Tiền / Đặt Cọc)
* `POST /api/thanh-toan`
```json
{
    "maNguoiDung": 1,
    "loaiThanhToan": "ThueXe",
    "maPhieuSua": null,
    "maDonThue": 1,
    "soTien": 3000000.0,
    "phuongThuc": "ChuyenKhoan",
    "trangThai": "HoanThanh"
}
```

### 6.2 Review Của Khách Hàng (Feedback)
* `POST /api/danh-gia`
```json
{
    "maNguoiDung": 1,
    "loaiDanhGia": "SuaChua",
    "maPhieuSua": 1,
    "maDonThue": null,
    "soSao": 5,
    "noiDung": "Dịch vụ quá tuyệt vời, chẩn đoán bệnh xe chuẩn không cần chỉnh!!"
}
```
