# HỆ THỐNG QUẢN LÝ DỊCH VỤ GARA Ô TÔ (GARAOTO)

## 1. TỔNG QUAN DỰ ÁN
GaraOto là hệ thống Client-Server giúp gara ô tô số hóa quy trình vận hành, gồm hai mảng nghiệp vụ chính:
- **Dịch vụ Sửa Chữa (Maintenance & Repair):** đặt lịch hẹn, tiếp nhận xe, lập phiếu sửa chữa (chẩn đoán + chi tiết dịch vụ), phân công kỹ thuật viên, tính tổng tiền, thanh toán.
- **Dịch vụ Cho Thuê Xe Tự Lái (Car Rental):** quản lý danh sách xe cho thuê, đặt thuê, duyệt đơn, lập hợp đồng thuê, giao xe và thu hồi xe.
- **Đánh giá dịch vụ (Rating):** khách hàng chấm 1–5 sao sau khi hoàn thành một lần sửa chữa hoặc thuê xe.

### Vai trò người dùng
| Vai trò | Giá trị `vaiTro` | Chức năng chính |
|---|---|---|
| Quản trị viên | `Admin` | Dashboard thống kê doanh thu (Chart.js), quản lý khách hàng, dịch vụ, xe cho thuê, đơn thuê, duyệt lịch hẹn, phiếu sửa chữa, phân công, đánh giá |
| Khách hàng | `KhachHang` | Quản lý tài khoản, xe của tôi, đặt lịch sửa chữa, thuê xe, theo dõi lịch hẹn/đơn thuê, đánh giá |
| Kỹ thuật viên | `NhanVienKyThuat` | Xem công việc được phân công và cập nhật tiến độ |

---

## 2. KIẾN TRÚC VÀ CÔNG NGHỆ (TECH STACK)
Kiến trúc Monolithic, tách biệt Frontend tĩnh và Backend REST API.

### Backend (`backend/`)
| Hạng mục | Công nghệ |
|---|---|
| Ngôn ngữ | Java 17 |
| Framework | Spring Boot 4.0.4 (Spring Web MVC, Spring Data JPA/Hibernate, Spring Security, Validation) |
| Xác thực | JWT (jjwt 0.12.7, hết hạn sau 24h) + BCrypt; stateless session |
| Database | PostgreSQL (`jdbc:postgresql://localhost:5432/garaoto`) |
| Tài liệu API | springdoc-openapi — Swagger UI tại `http://localhost:8080/swagger-ui.html` |
| Khác | Lombok, Maven Wrapper (`mvnw`), Flyway (đã khai báo nhưng đang tắt) |

**Luồng xử lý:** `Controller` (nhận/trả DTO, bọc trong `ApiResponse`) → `Service` (interface + `impl`) → `Repository` (Spring Data JPA) → `Entity`. Lỗi được xử lý tập trung tại `GlobalExceptionHandler` (`BadRequest`, `ResourceNotFound`, `DuplicateResource`, `Forbidden`, `Unauthorized`).

**Bảo mật:** `JwtAuthenticationFilter` đọc header `Authorization: Bearer <token>`, xác thực và nạp user vào `SecurityContext`.
- Không cần đăng nhập: `/api/auth/**`, `GET /api/xe-cho-thue/**`, `GET /api/dich-vu-sua-chua/**`, Swagger.
- Mọi endpoint khác yêu cầu đã đăng nhập. Phân quyền theo role bằng `@PreAuthorize` hiện mới áp dụng ở `DanhGiaController`, `DichVuSuaChuaController`, `NguoiDungController`; phần còn lại do Frontend (`guard.js`) giới hạn.

### Frontend (`frontend/`)
- **HTML/CSS/JavaScript thuần** (Vanilla, ES6 Modules), không framework, không bước build.
- Module JS dùng chung:
  - `config.js` — `API_BASE_URL = http://localhost:8080/api`
  - `api.js` — wrapper `fetch` gắn JWT
  - `auth.js` — lưu token/user trong `localStorage`
  - `guard.js` — chặn truy cập trang theo vai trò
  - `components.js`, `toast.js`, `utils.js` — modal, thông báo, định dạng tiền/ngày
- CSS tự viết (Flexbox/Grid): `base.css`, `layout.css`, `components.css` + CSS theo trang trong `css/pages/`.
- Thư viện qua CDN: Font Awesome 6.5.1, Chart.js (dashboard Admin).
- Cấu trúc trang (multi-page): `pages/auth`, `pages/tai-khoan`, `pages/dat-lich`, `pages/dich-vu`, `pages/xe-cho-thue`, `pages/dat-thue`, `pages/admin`, `pages/nhan-vien`.

---

## 3. CƠ SỞ DỮ LIỆU (DATABASE SCHEMA)

**Người dùng** — kế thừa JPA kiểu `JOINED`:
1. `nguoi_dung`: thông tin chung (họ tên, email, SĐT, mật khẩu, địa chỉ, `vai_tro`, `trang_thai`).
2. `admin`: mở rộng từ `nguoi_dung` (chức vụ).
3. `khach_hang`: mở rộng từ `nguoi_dung` (CCCD, số/hạng GPLX).
4. `nhan_vien_ky_thuat`: mở rộng từ `nguoi_dung` (chuyên môn, ca làm việc).

**Xe** — dùng chung lớp cha `XeBase` (biển số, hãng, dòng xe, năm sản xuất):

5. `xe_khach_hang`: xe cá nhân của khách (màu, số khung, số máy).
6. `xe_cho_thue`: xe của gara cho thuê (số chỗ, hộp số, nhiên liệu, giá theo ngày, hình ảnh, `tinh_trang`: `SanSang` / `DangThue` / `BaoTri`).

**Sửa chữa:**

7. `dich_vu_sua_chua`: bảng giá dịch vụ/phụ tùng (tên, mô tả, giá cơ bản).
8. `lich_hen_sua_chua`: lịch hẹn của khách (xe, ngày/giờ hẹn, mô tả lỗi, trạng thái).
9. `phieu_sua_chua`: phiếu sửa sinh ra từ lịch hẹn (ngày nhận xe, chẩn đoán, tổng tiền, trạng thái).
10. `chi_tiet_sua_chua`: các dịch vụ dùng trong phiếu (số lượng, đơn giá, thành tiền).
11. `phan_cong_sua_chua`: phiếu sửa được Admin giao cho kỹ thuật viên nào.

**Thuê xe:**

12. `don_thue_xe`: đơn thuê (ngày nhận/trả, địa điểm nhận/trả, tiền cọc, tổng tiền, trạng thái).
13. `hop_dong_thue`: hợp đồng 1-1 với đơn thuê (điều khoản, ghi chú).

**Khác:**

14. `thanh_toan`: thanh toán cho phiếu sửa **hoặc** đơn thuê (`loai_thanh_toan`, số tiền, phương thức, trạng thái).
15. `danh_gia`: đánh giá 1–5 sao, xác định dịch vụ qua cặp `loai_dich_vu` + `ma_tham_chieu`; mỗi dịch vụ chỉ được đánh giá một lần.

> **Lưu ý:** `spring.jpa.hibernate.ddl-auto=none` và Flyway đang tắt, nên Hibernate **không** tự tạo bảng. Cần tạo schema thủ công bằng `schema-update.sql`.

---

## 4. QUY TRÌNH NGHIỆP VỤ (WORKFLOWS)

### Quy trình 1: Sửa chữa ô tô
1. Khách hàng đăng nhập → chọn xe của mình → đặt lịch hẹn (ngày, giờ, mô tả lỗi). Lịch hẹn có trạng thái `ChoXacNhan`.
2. Admin (trang **Duyệt lịch**) xác nhận → `DaXacNhan`, hoặc hủy → `DaHuy`.
3. Khi khách mang xe tới, Admin bấm tiếp nhận: lịch hẹn → `DangSuaChua`, đồng thời tạo **phiếu sửa chữa** trạng thái `TiepNhan`.
4. Tại trang **Phiếu sửa chữa**, Admin nhập chẩn đoán và thêm các dịch vụ vào phiếu. Mỗi lần thêm/xóa chi tiết, Backend tự tính lại `tong_tien` = tổng `thanh_tien`.
5. Admin phân công kỹ thuật viên: tạo bản ghi phân công (`DaPhanCong`), phiếu → `DaPhanCong`.
6. Khi bắt đầu làm, phân công → `DangThucHien` và phiếu → `DangSuaChua`. Kỹ thuật viên theo dõi việc của mình ở trang `nhan-vien`.
7. Admin xác nhận hoàn thành: phiếu → `HoanThanh`; Backend tự đưa lịch hẹn liên quan về `HoanThanh`.
8. Khách hàng thấy nút **Đánh giá** trên lịch hẹn đã hoàn thành.

### Quy trình 2: Thuê xe tự lái
1. Khách hàng xem danh sách xe cho thuê (không cần đăng nhập) → xem chi tiết → đặt xe.
2. Backend kiểm tra `ngayTra >= ngayNhan` rồi tính **tổng tiền = giá theo ngày × số ngày** và **tiền cọc = 30% tổng tiền**. Đơn có trạng thái `ChoDuyet`.
3. Admin duyệt → `DaXacNhan` (hoặc từ chối → `DaHuy`). Khi đơn được xác nhận, Backend tự chuyển xe sang `DangThue`.
4. Giao xe cho khách → đơn `DangThue`.
5. Khách trả xe → Admin chuyển đơn sang `DaTra`, Backend tự đưa xe về `SanSang` (khi đơn bị hủy cũng vậy).
6. Khách hàng thấy nút **Đánh giá** chuyến thuê.

> **Lưu ý:** Hiện Backend **chưa** chặn chuyển trạng thái sai thứ tự; endpoint `PATCH .../{id}/trang-thai` chấp nhận mọi giá trị. Thứ tự các bước do giao diện Admin quy định. Việc kiểm tra xe phải đang `SanSang` khi tạo đơn thuê cũng đang tạm tắt (`DonThueXeServiceImpl`).

---

## 5. CÀI ĐẶT VÀ CHẠY

### Yêu cầu
- JDK 17+
- PostgreSQL
- Trình duyệt, cùng một static server bất kỳ (VS Code Live Server, `npx serve`, ...)

### Backend
1. Tạo database `garaoto` trong PostgreSQL rồi chạy `schema-update.sql`.
2. Copy `backend/src/main/resources/application-local.properties.example` thành `application-local.properties` (đã `.gitignore`) rồi điền mật khẩu Postgres và JWT secret.
3. Chạy:
   ```bash
   cd backend
   ./mvnw spring-boot:run      # Windows: mvnw.cmd spring-boot:run
   ```
4. API chạy tại `http://localhost:8080/api`, Swagger tại `http://localhost:8080/swagger-ui.html`.

### Frontend
Mở thư mục `frontend/` bằng static server tại gốc domain (các đường dẫn đang dùng dạng tuyệt đối `/css/...`, `/pages/...`) và truy cập `index.html`. Khi chạy trên `localhost`, Frontend tự gọi `http://localhost:8080/api`; trên domain khác sẽ gọi `PRODUCTION_API_URL` trong `frontend/js/config.js`.

---

## 6. DEPLOY (PRODUCTION)
Backend có sẵn `backend/Dockerfile` (dùng cho Render, Railway, Fly.io hoặc VPS). Cấu hình qua biến môi trường:

| Biến | Bắt buộc | Ý nghĩa |
|---|---|---|
| `DB_URL` | ✔ | JDBC URL, ví dụ `jdbc:postgresql://<host>/<db>?sslmode=require` |
| `DB_USERNAME` | ✔ | User Postgres |
| `DB_PASSWORD` | ✔ | Mật khẩu Postgres |
| `JWT_SECRET` | ✔ | Chuỗi **Base64**, tối thiểu 32 byte (`openssl rand -base64 48`) |
| `CORS_ORIGINS` | ✔ | Domain Frontend, nhiều giá trị cách nhau bởi dấu phẩy |
| `PORT` | | Cổng server (mặc định 8080; Render tự đặt) |
| `SHOW_SQL`, `LOG_LEVEL_SECURITY`, `LOG_LEVEL_SQL`, `LOG_LEVEL_SQL_BIND` | | Bật log debug khi cần |

Frontend là file tĩnh: deploy thư mục `frontend/` lên Netlify / Vercel / Cloudflare Pages, sau khi sửa `PRODUCTION_API_URL` trong `frontend/js/config.js`.

---

## 7. TÀI LIỆU API
Danh sách endpoint kèm payload mẫu cho Postman: xem [`backend/API_README.md`](backend/API_README.md).

Các nhóm API chính (prefix `/api`): `auth`, `nguoi-dung`, `khach-hang`, `nhan-vien-ky-thuat`, `admin`, `xe-khach-hang`, `xe-cho-thue`, `dich-vu-sua-chua`, `lich-hen-sua-chua`, `phieu-sua-chua`, `chi-tiet-sua-chua`, `phan-cong-sua-chua`, `don-thue-xe`, `hop-dong-thue`, `thanh-toan`, `danh-gia`.
