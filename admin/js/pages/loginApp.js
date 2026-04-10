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
  e.preventDefault(); // Ngăn form tự động reload trang
  clearErrors();
  let isValid = true;

  const emailVal = emailInput.value.trim();
  const passwordVal = passwordInput.value.trim();

  // Kiểm tra email
  if (emailVal === "") {
    emailInput.classList.add("input-error");
    emailError.textContent = "Vui lòng nhập email.";
    isValid = false;
  } else if (!validateEmailFormat(emailVal)) {
    emailInput.classList.add("input-error");
    emailError.textContent = "Định dạng email không hợp lệ.";
    isValid = false;
  }

  // Kiểm tra mật khẩu
  if (passwordVal === "") {
    passwordInput.classList.add("input-error");
    passwordError.textContent = "Vui lòng nhập mật khẩu.";
    isValid = false;
  }

  // --- PHẦN GỌI API ĐĂNG NHẬP ---
  if (isValid) {
    // Có thể thêm hiệu ứng loading ở đây để tăng UX (Ví dụ: Đổi text nút bấm thành "Đang đăng nhập...")

    const loginData = {
      email: emailVal,
      matKhau: passwordVal,
    };

    // Gọi API bằng fetch với method POST
    fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(loginData),
    })
      .then((response) => response.json())
      .then((result) => {
        // Kiểm tra kết quả trả về từ Backend
        if (result.success === true && result.data) {
          // 1. Lấy vai trò (Role) từ dữ liệu Backend trả về
          const userRole = result.data.vaiTro;

          // 2. Kiểm tra phân quyền: Chỉ Admin và Thợ máy mới được vào
          if (userRole === "Admin" || userRole === "NhanVienKyThuat") {
            console.log("Đăng nhập thành công với vai trò:", userRole);

            // 3. LƯU TRỮ LOCALSTORAGE
            localStorage.setItem("isAdminLoggedIn", "true");
            localStorage.setItem("adminToken", result.data.token);
            localStorage.setItem("adminName", result.data.hoTen);
            localStorage.setItem("adminRole", userRole);
            localStorage.setItem("adminId", result.data.maNguoiDung);

            // 4. CHUYỂN HƯỚNG THEO ROLE (Nâng cao UX)
            if (userRole === "NhanVienKyThuat") {
              // Thợ máy không có quyền xem Dashboard Tổng quan, đẩy thẳng sang trang Sửa chữa
              window.location.replace("dashboard.html");
            } else {
              // Admin thì vào trang Dashboard mặc định
              window.location.replace("dashboard.html");
            }
          } else {
            // Nếu là Khách Hàng cố tình đăng nhập vào trang Quản trị -> Chặn
            passwordInput.classList.add("input-error");
            passwordError.textContent =
              "Tài khoản của bạn không có quyền truy cập khu vực Quản trị!";
          }
        } else {
          // Đăng nhập thất bại (Sai pass/email)
          passwordInput.classList.add("input-error");
          passwordError.textContent =
            result.message || "Email hoặc mật khẩu không đúng!";
        }
      })
      .catch((error) => {
        console.error("Lỗi kết nối:", error);
        emailError.textContent =
          "Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại mạng hoặc liên hệ Admin.";
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
