// 1. LẤY CÁC PHẦN TỬ DOM TỪ HTML
const form = document.getElementById("adminLoginForm");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const emailError = document.getElementById("emailError");
const passwordError = document.getElementById("passwordError");
const toggleBtn = document.getElementById("togglePassword");
const eyeIcon = document.getElementById("eyeIcon");

// 2. CÁC HÀM TIỆN ÍCH (FUNCTIONS)

// Hàm xóa trạng thái viền đỏ và câu báo lỗi
function clearErrors() {
  emailInput.classList.remove("input-error");
  passwordInput.classList.remove("input-error");
  emailError.textContent = "";
  passwordError.textContent = "";
}

// Hàm kiểm tra định dạng email hợp lệ
function validateEmailFormat(email) {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return re.test(email);
}

// Hàm xử lý thay đổi icon và ẩn/hiện mật khẩu
function togglePasswordVisibility() {
  const isPassword = passwordInput.getAttribute("type") === "password";

  // Đổi type qua lại giữa text và password
  passwordInput.setAttribute("type", isPassword ? "text" : "password");

  if (isPassword) {
    // Nếu đang ẩn -> chuyển sang hiện (Icon mắt gạch chéo)
    eyeIcon.innerHTML = `
            <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
            <line x1="1" y1="1" x2="23" y2="23"></line>
        `;
  } else {
    // Nếu đang hiện -> chuyển về ẩn (Icon mắt mở)
    eyeIcon.innerHTML = `
            <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
            <circle cx="12" cy="12" r="3"></circle>
        `;
  }
}

// Hàm xử lý kịch bản chính khi người dùng bấm Đăng nhập
function handleLogin(e) {
  e.preventDefault();
  clearErrors();
  let isValid = true;

  const emailVal = emailInput.value.trim();
  const passwordVal = passwordInput.value.trim();

  if (emailVal === "" || !validateEmailFormat(emailVal)) {
    emailInput.classList.add("input-error");
    isValid = false;
  }
  if (passwordVal === "") {
    passwordInput.classList.add("input-error");
    isValid = false;
  }

  if (isValid) {
    const loginData = { email: emailVal, matKhau: passwordVal };

    fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(loginData),
    })
      .then((response) => response.json())
      .then((result) => {
        if (result.success === true && result.data) {
          // 🛑 BƯỚC KIỂM TRA QUAN TRỌNG: Nhật nhấn F12, chọn tab Console để xem dòng này
          console.log("Dữ liệu Backend trả về khi Login:", result.data);

          const userRole = result.data.vaiTro;

          // Lưu các thông tin cơ bản
          localStorage.setItem("isAdminLoggedIn", "true");
          localStorage.setItem("adminToken", result.data.token);
          localStorage.setItem("adminName", result.data.hoTen);
          localStorage.setItem("adminRole", userRole);

          // Xử lý ID để hiển thị đúng công việc
          if (userRole === "NhanVienKyThuat") {
            // Nếu có maNhanVien thì lấy, không có thì dùng tạm maNguoiDung
            const idToSave = result.data.maNhanVien || result.data.maNguoiDung;
            localStorage.setItem("adminId", idToSave);
            console.log("Đã lưu ID Thợ máy là:", idToSave);
          } else {
            localStorage.setItem("adminId", result.data.maNguoiDung);
          }

          window.location.replace("dashboard.html");
        } else {
          passwordError.textContent =
            result.message || "Sai thông tin đăng nhập!";
        }
      })
      .catch((error) => {
        console.error("Lỗi kết nối:", error);
        emailError.textContent = "Máy chủ không phản hồi!";
      });
  }
}

// 3. GẮN SỰ KIỆN LẮNG NGHE (EVENT LISTENERS)
if (form) {
  form.addEventListener("submit", handleLogin);
}

if (toggleBtn) {
  toggleBtn.addEventListener("click", togglePasswordVisibility);
}
