// Kiểm tra xem trong bộ nhớ có cờ "đã đăng nhập" chưa
const isLoggedIn = localStorage.getItem("isAdminLoggedIn");

// Nếu chưa có (người lạ truy cập link trực tiếp)
if (!isLoggedIn || isLoggedIn !== "true") {
  // Đá về trang đăng nhập ngay lập tức bằng replace (chống Back)
  window.location.replace("admin-login.html");
}
