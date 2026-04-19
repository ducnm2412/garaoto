# HỆ THỐNG QUẢN LÝ DỊCH VỤ GARA Ô TÔ (GARAOTO)

## 1. TỔNG QUAN DỰ ÁN
GaraOto là hệ thống phần mềm Client-Server giúp xưởng dịch vụ/Gara ô tô điện tử hóa toàn bộ quy trình vận hành. 
Dự án đặc biệt chia làm hai mảng kinh doanh chính:
- **Dịch vụ Sửa Chữa (Maintenance & Repair):** Quản lý lịch hẹn, tiếp nhận xe, phân công kỹ thuật viên, xuất hóa đơn phụ tùng.
- **Dịch vụ Cho Thuê Xe Tự Lái (Car Rental):** Quản lý danh sách xe cho thuê, tình trạng sẵn sàng, tiếp nhận đơn đặt xe, duyệt, giao xe và hoàn trả trạng thái.
- Tích hợp thêm **Hệ thống đánh giá chất lượng dịch vụ (Rating Service)** để lắng nghe Feedback KH sau khi sử dụng 1 trong 2 dịch vụ trên.

---

## 2. KIẾN TRÚC VÀ CÔNG NGHỆ (TECH STACK)
Dự án sử dụng kiến trúc Monolithic nhưng phân mảnh rạch ròi Client-Server API:

### Backend (Java Spring Boot)
- **Framework:** Spring Boot 3.x (Java 17).
- **Core modules:** Spring Web (REST API), Spring Data JPA (Hibernate ORM), Spring Security.
- **Bảo mật (Security):** Json Web Token (JWT) + BCrypt Password Encoder. Áp dụng `@PreAuthorize` để giới hạn các ROLE như `Admin`, `KhachHang`, `NhanVien`.
- **Database:** PostgreSQL. Cơ chế cấu hình `spring.jpa.hibernate.ddl-auto` để sinh lược đồ CSDL tự động.
- **Kiến trúc luồng dữ liệu:** `Controller` (Tiếp nhận/Trả về DTO) -> `Service` (Xử lý Business Logic) -> `Repository` (Thao tác Database) -> `Entity` (Bản đồ hóa Dữ liệu bảng Postgres).

### Frontend (Vanilla HTML/CSS/JS)
- Không sử dụng các Framework/Library nặng nề như React hay Vue. Hệ thống theo chuẩn Vanilla thuần nhằm tối ưu hóa Loading Time và rèn luyện kỹ năng Core Logic.
- Điểm vượt trội: Tách biệt Javascript xử lý (Modules ES6) gồm `api.js` (Gọi Fetch REST), `auth.js` (Xử lý LocalStorage JWT), `guard.js` (Kiểm tra quyền Route), và `components.js` (Xử lý các Modal, Toast).
- UI/UX: Áp dụng CSS Flexbox/Grid Design System tiên tiến. Sử dụng FontAwesome ICON. Cấu trúc Sidebar chuẩn SPA (Single Page Application) mà không cần reload trang.

---

## 3. CƠ SỞ DỮ LIỆU CỐT LÕI (DATABASE SCHEMA)
Các Table chính (Entities) trong hệ thống:
1. `nguoi_dung`: Chứa thông tin đăng nhập Account (email, matKhau, role, initial info).
2. `xe_khach_hang`: Danh sách xe cá nhân của User sở hữu, kết nối với Lịch hẹn.
3. `xe_cho_thue`: Danh mục xe thuộc quyền sở hữu của Gara đang cho mướn. Có cột `trangThai` (SanSang, DangChoThue, DangBaoDuong).
4. `don_thue_xe`: Lưu thông tin KH nào mướn Xe nào, thời gian từ ngày bắt đầu đến kết thúc.
5. `lich_hen_sua_chua`: Phiếu trung tâm xử lý giao dịch.
6. `dich_vu_phu_tung`: Bảng giá niêm yết các loại vỏ lốp, nhớt, linh kiện máy móc Gara có.
7. `phan_cong`: Bảng trung gian ghi nhận Lịch hẹn A dùng bao nhiêu cái Phụ tùng B, gán cho Nhân Viên Kỹ Thuật C làm. (Many to Many).
8. `danh_gia`: Bảng điểm đánh giá (Rating từ 1->5 sao) dựa theo `loai_dich_vu` và `ma_tham_chieu`.

---

## 4. QUY TRÌNH LUỒNG HOẠT ĐỘNG (WORKFLOWS)

### Quy trình 1: Luồng Sửa Chữa Ô Tô (Maintenance Flow)
1. Khách Hàng Login -> Đặt lịch hẹn sửa chữa (Ghi chú mô tả lỗi, chọn ngày/giờ hỏng).
2. Trạng thái phiếu: `ChoXacNhan` (Mặc định).
3. Admin nhận được: Xem mô tả lỗi -> Bấm Duyệt phiếu -> Phiếu chuyển sang `DaXacNhan`. Khách Hàng đem xe tới Gara.
4. Admin bấm "Tiếp nhận xe" -> Phiếu sang trạng thái `DangTiepNhan`, sau đó Admin qua trang Phân Công.
5. Tại Phân công: Admin Add các Phụ tùng hỏng vào Phiếu, gán "Nhân Viên KT" -> Phiếu mang trạng thái `DangSuaChua`.
6. Hệ thống tự động tính thành "Tổng Tiền" trả về.
7. Khi Thợ sửa xong -> Admin Confirm "Hoàn thành" -> Phiếu đổi sang `HoanThanh`.
8. Lúc này tại màn hình Khách hàng xuất hiện nút màu vàng "Đánh Giá". Khách đánh giá xong hệ thống ghi nhận vào DB.

### Quy trình 2: Luồng Thuê Xe (Rental Flow)
1. Khách Hàng coi mục Xe Cho Thuê -> Chọn "Thuê Xế Hộp BMW".
2. Hệ thống kiểm tra xe phải có trạng thái xe là `SanSang`. Lập đơn thuê với status `ChoXacNhan`.
3. Admin phê duyệt trạng thái `DaXacNhan`. Hệ thống tự động kích hoạt Finite State Machine (Khóa xe, cập nhật Trạng thái xe kia ngoài bãi thành `DangThue` để chặn Booking chồng chéo).
4. Khách đi du lịch về, giao lại chìa khóa => Admin bấm `DaTra` (Hoàn xe).
5. Hệ thống hoàn lại Status của chiếc Xe kia về lại `SanSang` tự động.
6. Màn hình Khách hàng xuất hiện nút "Đánh Giá" chuyến đi.

---

## 5. CÁC THUẬT TOÁN ĐỈNH CAO TRONG DỰ ÁN
- **Authentication JWT Filtering Pipeline:** Thuật toán màng lọc kiểm tra Token Auth Headers trên mọi Request. Extract Claim ra ID của User và đẩy SecurityContext. Nếu thiếu hoặc token thao túng => Trị chối Error 401/403 tại Filter Core.
- **Workflow State Management Control:** Kỹ thuật FSM (Finite State Machine). Code Back-end nghiêm ngặt giới hạn cấm Admin không được nhảy cóc (Ví dụ không thể chuyển từ ChoXacNhan vọt lèo lên Hoạt Thành mà bỏ qua bước Sửa chữa). Các method Controller can thiệp vào Logic ném ra Exception nếu phát hiện gian lận trình tự API.
- **Client Render Engine Logic:** Khả năng tự render hàng trăm Component ra HTML mà chả cần Lib. Các Modal được bọc bằng JS Event Delegation (chống trùng lặp Listener Memory Leak) gọi tới Server tự động khóa form để tránh tấn công DDos Double Submit. Mọi thứ là Non-Blocking Async/Await Fetch.
