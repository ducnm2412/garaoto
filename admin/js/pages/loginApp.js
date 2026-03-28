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
    emailError.textContent = "Vui lòng nhập email quản trị.";
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

  // Nếu không có lỗi gì, in ra console (sau này sẽ dùng fetch gọi API ở đây)
  if (isValid) {
    console.log("Dữ liệu hợp lệ! Sẵn sàng gửi API với:", { email: emailVal });
    // window.location.href = "dashboard.html"; // Chuyển trang nếu thành công
  }
}

// 3. GẮN SỰ KIỆN LẮNG NGHE (EVENT LISTENERS)
form.addEventListener("submit", handleLogin);

if (toggleBtn) {
  toggleBtn.addEventListener("click", togglePasswordVisibility);
}
