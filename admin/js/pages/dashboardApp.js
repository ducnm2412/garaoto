/**
 * ============================================================================
 * FILE: dashboardApp.js
 * CHỨC NĂNG: Vận hành trang Tổng quan (Dashboard) cho Admin và Thợ Máy
 * BAO GỒM: Thống kê số liệu, Bảng đơn thuê xe mới, Bảng lịch hẹn hôm nay.
 * ============================================================================
 */

// ============================================================================
// MODULE 1: KHỞI TẠO & PHÂN QUYỀN TRANG
// ============================================================================
document.addEventListener("DOMContentLoaded", () => {
  // 1. Kiểm tra đăng nhập
  if (!localStorage.getItem("isAdminLoggedIn")) {
    window.location.replace("admin-login.html");
    return;
  }

  const adminRole = localStorage.getItem("adminRole");
  const adminName = localStorage.getItem("adminName");

  // Hiển thị tên người dùng trên góc phải
  const nameEl = document.querySelector(".admin-name");
  if (adminName && nameEl) nameEl.textContent = "Xin chào, " + adminName + "!";

  // 2. Phân quyền hiển thị (Sidebar & Khối Thống kê)
  applyRoleBasedAccessControl(adminRole);
  setupDashboardTheoVaiTro(); // Biến hình giao diện cho Thợ Máy

  // 3. Tải dữ liệu tương ứng với Admin
  if (adminRole === "Admin") {
    loadDashboardStats(); // 4 Thẻ thống kê trên cùng
    loadRecentRentals(); // Bảng Thuê xe
    loadTodayRepairs(); // Bảng Lịch hẹn
    setupActionButtons(); // Lắng nghe sự kiện click trên các bảng
  }
});

// Hàm ẩn/hiện các menu ở Sidebar dựa theo vai trò
function applyRoleBasedAccessControl(role) {
  const menuItems = document.querySelectorAll(".sidebar-menu .menu-item");

  if (role === "NhanVienKyThuat") {
    menuItems.forEach((item) => {
      const menuText = item.textContent.trim();
      if (menuText !== "Tổng quan" && menuText !== "Dịch vụ Sửa chữa") {
        item.style.display = "none";
      }
    });

    // Ẩn các bảng không liên quan đến Thợ máy
    const rentalTable = document.querySelector(
      ".table-container:nth-of-type(1)",
    );
    if (rentalTable) rentalTable.style.display = "none";
  }
}

// ============================================================================
// MODULE 2: TÍNH TOÁN & HIỂN THỊ 4 THẺ THỐNG KÊ (DÀNH CHO ADMIN)
// ============================================================================
async function loadDashboardStats() {
  try {
    const headers = {
      Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
    };

    // Tối ưu tốc độ: Gọi song song 3 API để tính toán
    const [resDon, resLich, resXe] = await Promise.all([
      fetch("http://localhost:8080/api/don-thue-xe", { headers }),
      fetch("http://localhost:8080/api/lich-hen-sua-chua", { headers }),
      fetch("http://localhost:8080/api/xe-cho-thue", { headers }),
    ]);

    if (resDon.ok && resLich.ok && resXe.ok) {
      const donData = await resDon.json();
      const lichData = await resLich.json();
      const xeData = await resXe.json();

      const danhSachDon = donData.data || [];
      const danhSachLich = lichData.data || [];
      const danhSachXe = xeData.data || [];

      // 1. Đơn thuê chờ duyệt
      const donChoDuyet = danhSachDon.filter(
        (d) => d.trangThai === "ChoDuyet",
      ).length;
      document.getElementById("stat-pending-orders").innerText = donChoDuyet;

      // 2. Doanh thu (Từ Đơn thuê xe)
      const doanhThu = danhSachDon
        .filter((d) => ["DaXacNhan", "DangThue", "DaTra"].includes(d.trangThai))
        .reduce((sum, d) => sum + (d.tongTien || 0), 0);
      document.getElementById("stat-revenue").innerText =
        doanhThu.toLocaleString() + " đ";

      // 3. Lịch hẹn chờ xử lý & Đang sửa
      const dangSua = danhSachLich.filter((l) =>
        ["ChoXacNhan", "DaXacNhan"].includes(l.trangThai),
      ).length;
      document.getElementById("stat-repairing").innerText = dangSua;

      // 4. Số xe sẵn sàng cho thuê
      const xeSanSang = danhSachXe.filter(
        (x) => x.tinhTrang === "SanSang",
      ).length;
      document.getElementById("stat-available-cars").innerText = xeSanSang;
    }
  } catch (error) {
    console.error("Lỗi tính toán thống kê:", error);
  }
}

// ============================================================================
// MODULE 3: BẢNG "ĐƠN THUÊ XE GẦN ĐÂY" (BẢNG TRÁI)
// ============================================================================
async function loadRecentRentals() {
  const tbody = document.querySelector(".table-container:nth-of-type(1) tbody");
  if (!tbody) return;
  tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;"><i class="fas fa-spinner fa-spin"></i> Đang tải dữ liệu...</td></tr>`;

  try {
    const response = await fetch("http://localhost:8080/api/don-thue-xe", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
      },
    });

    if (!response.ok) throw new Error(`Lỗi máy chủ (${response.status})`);

    const result = await response.json();
    tbody.innerHTML = "";

    if (result.success && result.data) {
      // Chỉ lấy 5 đơn mới nhất
      const latestOrders = result.data.reverse().slice(0, 5);

      if (latestOrders.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;">Chưa có đơn thuê xe nào.</td></tr>`;
        return;
      }

      latestOrders.forEach((item) => {
        const ngayNhan = new Date(item.ngayNhan).toLocaleDateString("vi-VN");
        const tongTien = item.tongTien ? item.tongTien.toLocaleString() : "0";

        // Hiển thị nút Duyệt/Hủy nếu đang chờ duyệt, ngược lại hiện nút Xem
        let actionButtons =
          item.trangThai === "ChoDuyet"
            ? `<button class="btn-action btn-approve" data-id="${item.maDonThue}" title="Duyệt đơn"><i class="fas fa-check-circle"></i></button>
                       <button class="btn-action btn-reject" data-id="${item.maDonThue}" title="Từ chối"><i class="fas fa-times-circle"></i></button>`
            : `<button class="btn-action btn-view" data-id="${item.maDonThue}" title="Xem chi tiết"><i class="fas fa-eye"></i></button>`;

        const row = `
                    <tr>
                        <td><strong>#DT${item.maDonThue}</strong></td>
                        <td><a class="link-id btn-view-customer" data-id="${item.maKhachHang}">KH #${item.maKhachHang}</a></td>
                        <td><a class="link-id btn-view-car" data-id="${item.maXeThue}">XE #${item.maXeThue}</a></td>
                        <td>${ngayNhan}</td>
                        <td><strong style="color:#b45309">${tongTien} đ</strong></td>
                        <td><span class="badge ${getRentalBadgeClass(item.trangThai)}">${formatRentalStatus(item.trangThai)}</span></td>
                        <td>${actionButtons}</td>
                    </tr>`;
        tbody.innerHTML += row;
      });
    }
  } catch (error) {
    tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; color:red;">${error.message}</td></tr>`;
  }
}

// Cập nhật trạng thái Đơn thuê
async function updateOrderStatus(id, newStatus) {
  try {
    const response = await fetch(
      `http://localhost:8080/api/don-thue-xe/${id}/trang-thai?trangThai=${newStatus}`,
      {
        method: "PATCH",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
        },
      },
    );
    const result = await response.json();
    if (result.success) {
      alert("Cập nhật trạng thái đơn thuê thành công!");
      loadRecentRentals(); // Tải lại bảng Thuê xe
      loadDashboardStats(); // Tính lại thống kê
    } else alert("Lỗi: " + result.message);
  } catch (error) {
    alert("Lỗi kết nối máy chủ!");
  }
}

// ============================================================================
// MODULE 4: BẢNG "LỊCH HẸN HÔM NAY" (BẢNG PHẢI)
// ============================================================================
async function loadTodayRepairs() {
  const tbody = document.querySelector(".table-container:nth-of-type(2) tbody");
  if (!tbody) return;
  tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;"><i class="fas fa-spinner fa-spin"></i> Đang tải dữ liệu...</td></tr>`;

  try {
    const response = await fetch(
      "http://localhost:8080/api/lich-hen-sua-chua",
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
        },
      },
    );

    if (!response.ok) throw new Error(`Lỗi máy chủ (${response.status})`);

    const result = await response.json();
    tbody.innerHTML = "";

    if (result.success && result.data) {
      // Lọc ra các đơn chưa được xử lý xong (Ẩn Đã hủy và Hoàn thành)
      const activeRepairs = result.data.filter(
        (item) => item.trangThai !== "DaHuy" && item.trangThai !== "HoanThanh",
      );
      const latestRepairs = activeRepairs.reverse().slice(0, 5); // Lấy 5 đơn mới nhất

      if (latestRepairs.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;">Không có lịch hẹn chờ xử lý.</td></tr>`;
        return;
      }

      latestRepairs.forEach((item) => {
        const ngayHen = new Date(item.ngayHen).toLocaleDateString("vi-VN");
        const gioHen = item.gioHen ? item.gioHen.substring(0, 5) : "";

        let actionButtons =
          item.trangThai === "ChoXacNhan"
            ? `<button class="btn-action btn-approve-repair" data-id="${item.maLichHen}" title="Xác nhận lịch"><i class="fas fa-calendar-check"></i></button>
                       <button class="btn-action btn-reject-repair" data-id="${item.maLichHen}" title="Hủy lịch"><i class="fas fa-calendar-times"></i></button>`
            : `<button class="btn-action btn-view-repair" data-id="${item.maLichHen}" title="Xem chi tiết"><i class="fas fa-eye"></i></button>`;

        const row = `
                    <tr>
                        <td><strong>#LH${item.maLichHen}</strong></td>
                        <td><a class="link-id btn-view-customer" data-id="${item.maKhachHang}">KH #${item.maKhachHang}</a></td>
                        <td><a class="link-id btn-view-customer-car" data-id="${item.maXeKh}">XE KH #${item.maXeKh}</a></td>
                        <td>${ngayHen}<br><small style="color: #64748b; font-weight: 500;">${gioHen}</small></td>
                        <td><div style="max-width: 150px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;" title="${item.moTaLoi}">${item.moTaLoi || "---"}</div></td>
                        <td><span class="badge ${getRepairBadgeClass(item.trangThai)}">${formatRepairStatus(item.trangThai)}</span></td>
                        <td>${actionButtons}</td>
                    </tr>`;
        tbody.innerHTML += row;
      });
    }
  } catch (error) {
    tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; color:red;">${error.message}</td></tr>`;
  }
}

// Cập nhật trạng thái Lịch Hẹn
async function updateRepairStatus(id, newStatus) {
  try {
    const response = await fetch(
      `http://localhost:8080/api/lich-hen-sua-chua/${id}/trang-thai?trangThai=${newStatus}`,
      {
        method: "PATCH",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
        },
      },
    );
    const result = await response.json();
    if (result.success) {
      alert("Cập nhật lịch hẹn thành công!");
      loadTodayRepairs(); // Tải lại bảng Lịch hẹn
      loadDashboardStats(); // Tính lại thống kê
    } else alert("Lỗi: " + result.message);
  } catch (error) {
    alert("Lỗi kết nối máy chủ!");
  }
}

// ============================================================================
// MODULE 5: ĐIỀU HƯỚNG SỰ KIỆN CLICK (EVENT DELEGATION) VÀ MODAL
// ============================================================================
const modal = document.getElementById("infoModal");
const modalBody = document.getElementById("modalBody");
const modalTitle = document.getElementById("modalTitle");

document.querySelector(".close-modal").onclick = () =>
  (modal.style.display = "none");
window.onclick = (e) => {
  if (e.target == modal) modal.style.display = "none";
};

function openModal(title) {
  modalTitle.textContent = title;
  modalBody.innerHTML = `<div style="text-align:center; padding:20px;"><i class="fas fa-spinner fa-spin"></i> Đang tải dữ liệu...</div>`;
  modal.style.display = "block";
}

function setupActionButtons() {
  // 1. Lắng nghe trên Bảng Thuê Xe
  const rentalTable = document.querySelector(".table-container:nth-of-type(1)");
  if (rentalTable) {
    rentalTable.addEventListener("click", (e) => {
      // Nút Thao tác
      if (e.target.closest(".btn-approve"))
        updateOrderStatus(
          e.target.closest(".btn-approve").dataset.id,
          "DaXacNhan",
        );
      if (e.target.closest(".btn-reject"))
        updateOrderStatus(e.target.closest(".btn-reject").dataset.id, "DaHuy");
      if (e.target.closest(".btn-view")) {
        openModal(
          `Chi tiết Đơn Thuê #${e.target.closest(".btn-view").dataset.id}`,
        );
        fetchOrderDetails(e.target.closest(".btn-view").dataset.id);
      }
      // Nút xem tham chiếu
      if (e.target.classList.contains("btn-view-customer")) {
        openModal(`Thông tin Khách hàng`);
        fetchCustomerInfo(e.target.dataset.id);
      }
      if (e.target.classList.contains("btn-view-car")) {
        openModal(`Thông tin Xe Cho Thuê`);
        fetchCarInfo(e.target.dataset.id); // Gọi API lấy xe tự lái
      }
    });
  }

  // 2. Lắng nghe trên Bảng Lịch Hẹn
  const repairTable = document.querySelector(".table-container:nth-of-type(2)");
  if (repairTable) {
    repairTable.addEventListener("click", (e) => {
      // Nút Thao tác
      if (e.target.closest(".btn-approve-repair"))
        updateRepairStatus(
          e.target.closest(".btn-approve-repair").dataset.id,
          "DaXacNhan",
        );
      if (e.target.closest(".btn-reject-repair"))
        updateRepairStatus(
          e.target.closest(".btn-reject-repair").dataset.id,
          "DaHuy",
        );
      if (e.target.closest(".btn-view-repair")) {
        openModal(
          `Chi tiết Lịch hẹn #${e.target.closest(".btn-view-repair").dataset.id}`,
        );
        fetchRepairDetails(e.target.closest(".btn-view-repair").dataset.id);
      }
      // Nút xem tham chiếu
      if (e.target.classList.contains("btn-view-customer")) {
        openModal(`Thông tin Khách hàng`);
        fetchCustomerInfo(e.target.dataset.id);
      }
      if (e.target.classList.contains("btn-view-customer-car")) {
        openModal(`Thông tin Xe Sửa Chữa`);
        fetchCustomerCarInfo(e.target.dataset.id); // Gọi API lấy xe của khách
      }
    });
  }
}

// ============================================================================
// MODULE 6: FETCH API LẤY CHI TIẾT DỮ LIỆU ĐỔ VÀO MODAL
// ============================================================================

// 6.1. Thông tin Đơn Thuê
async function fetchOrderDetails(id) {
  try {
    const response = await fetch(
      `http://localhost:8080/api/don-thue-xe/${id}`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
        },
      },
    );
    const result = await response.json();
    if (result.success && result.data) {
      const don = result.data;
      modalBody.innerHTML = `
                <div class="order-details">
                    <p><strong>Ngày đặt:</strong> ${new Date(don.ngayDat).toLocaleString("vi-VN")} | <span class="badge ${getRentalBadgeClass(don.trangThai)}">${formatRentalStatus(don.trangThai)}</span></p>
                    <div style="display:grid; grid-template-columns:1fr 1fr; gap:10px; margin-bottom:15px">
                        <div style="background:#f8fafc; padding:10px; border-radius:4px">
                            <strong>Nhận:</strong> ${new Date(don.ngayNhan).toLocaleDateString("vi-VN")}<br>
                            <strong>Trả:</strong> ${new Date(don.ngayTra).toLocaleDateString("vi-VN")}
                        </div>
                        <div style="background:#f8fafc; padding:10px; border-radius:4px">
                            <strong>Nơi giao:</strong> ${don.diaDiemNhan || "Xưởng"}<br>
                            <strong>Nơi trả:</strong> ${don.diaDiemTra || "Xưởng"}
                        </div>
                    </div>
                    <div style="background:#fffbeb; padding:15px; border-radius:4px; font-size:18px; color:#b45309">
                        <strong>TỔNG TIỀN:</strong> ${don.tongTien ? don.tongTien.toLocaleString() : "0"} đ
                    </div>
                </div>`;
    }
  } catch (e) {
    modalBody.innerHTML = `<p style="color:red">Lỗi lấy chi tiết đơn.</p>`;
  }
}

// 6.2. Thông tin Lịch Hẹn
async function fetchRepairDetails(id) {
  try {
    const response = await fetch(
      `http://localhost:8080/api/lich-hen-sua-chua/${id}`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
        },
      },
    );
    const result = await response.json();
    if (result.success && result.data) {
      const lh = result.data;
      modalBody.innerHTML = `
                <div class="order-details">
                    <div style="text-align:center; padding:15px; background:#f8fafc; border-radius:6px; margin-bottom:15px">
                        <h4>THỜI GIAN ĐẾN XƯỞNG</h4>
                        <strong style="font-size:24px; color:#2563eb">${lh.gioHen} | ${new Date(lh.ngayHen).toLocaleDateString("vi-VN")}</strong>
                    </div>
                    <div style="background:#fef2f2; padding:15px; border-radius:6px">
                        <strong style="color:#dc2626">Lỗi khách báo:</strong>
                        <p style="font-style:italic">"${lh.moTaLoi || "Không rõ"}"</p>
                    </div>
                </div>`;
    }
  } catch (e) {
    modalBody.innerHTML = `<p style="color:red">Lỗi lấy chi tiết lịch.</p>`;
  }
}

// 6.3. Thông tin Tham chiếu (Khách, Xe Gara, Xe Khách)
async function fetchCustomerInfo(id) {
  try {
    const response = await fetch(`http://localhost:8080/api/khach-hang/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
      },
    });
    const result = await response.json();
    if (result.success && result.data) {
      const kh = result.data;
      modalBody.innerHTML = `<div><h4>${kh.hoTen}</h4><p>SĐT: ${kh.soDienThoai}</p><p>GPLX: ${kh.soGplx || "---"}</p></div>`;
    }
  } catch (e) {
    modalBody.innerHTML = `<p style="color:red">Lỗi lấy KH.</p>`;
  }
}

async function fetchCarInfo(id) {
  // Xe Cho Thuê
  try {
    const response = await fetch(
      `http://localhost:8080/api/xe-cho-thue/${id}`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
        },
      },
    );
    const result = await response.json();
    if (result.success && result.data) {
      const xe = result.data;
      modalBody.innerHTML = `<div style="text-align:center"><h4>${xe.hangXe} ${xe.dongXe}</h4><h2>${xe.bienSo}</h2><p>Giá tham khảo: ${xe.giaTheoNgay?.toLocaleString()} đ/ngày</p></div>`;
    }
  } catch (e) {
    modalBody.innerHTML = `<p style="color:red">Lỗi lấy Xe.</p>`;
  }
}

async function fetchCustomerCarInfo(id) {
  // Xe của Khách Hàng
  try {
    const response = await fetch(
      `http://localhost:8080/api/xe-khach-hang/${id}`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
        },
      },
    );
    const result = await response.json();
    if (result.success && result.data) {
      const xe = result.data;
      modalBody.innerHTML = `<div style="text-align:center"><h4>${xe.hangXe} ${xe.dongXe}</h4><h2>${xe.bienSo}</h2></div>`;
    }
  } catch (e) {
    modalBody.innerHTML = `<p style="color:red">Lỗi lấy Xe KH.</p>`;
  }
}

// ============================================================================
// MODULE 7: TIỆN ÍCH UI VÀ ĐỊNH DẠNG TEXT/MÀU SẮC
// ============================================================================
function getRentalBadgeClass(status) {
  switch (status) {
    case "ChoDuyet":
      return "badge-warning";
    case "DaXacNhan":
      return "badge-success";
    case "DangThue":
      return "badge-info";
    case "DaHuy":
      return "badge-danger";
    default:
      return "";
  }
}
function formatRentalStatus(status) {
  switch (status) {
    case "ChoDuyet":
      return "Chờ Duyệt";
    case "DaXacNhan":
      return "Đã Xác Nhận";
    case "DangThue":
      return "Đang Thuê";
    case "DaTra":
      return "Đã Trả";
    case "DaHuy":
      return "Đã Hủy";
    default:
      return status;
  }
}
function getRepairBadgeClass(status) {
  switch (status) {
    case "ChoXacNhan":
      return "badge-warning";
    case "DaXacNhan":
      return "badge-success";
    case "HoanThanh":
      return "badge-info";
    case "DaHuy":
      return "badge-danger";
    default:
      return "";
  }
}
function formatRepairStatus(status) {
  switch (status) {
    case "ChoXacNhan":
      return "Chờ Xác Nhận";
    case "DaXacNhan":
      return "Đã Xác Nhận";
    case "HoanThanh":
      return "Hoàn Thành";
    case "DaHuy":
      return "Đã Hủy";
    default:
      return status;
  }
}

// ============================================================================
// MODULE 8: GIAO DIỆN "BIẾN HÌNH" DÀNH CHO THỢ MÁY
// ============================================================================
async function setupDashboardTheoVaiTro() {
  const role = localStorage.getItem("adminRole");
  if (role !== "NhanVienKyThuat") return;

  // 1. Ẩn Bảng và thay đổi giao diện ban đầu
  const dashboardTables = document.querySelector(".dashboard-tables");
  if (dashboardTables) dashboardTables.style.display = "none";

  const pageTitle = document.querySelector(".page-title");
  if (pageTitle)
    pageTitle.innerHTML = `<i class="fas fa-tools"></i> Tổng quan Hiệu suất Công việc`;

  // 2. Lấy thông tin xác thực
  const maNguoiDung = localStorage.getItem("adminId"); // Đây là maNguoiDung từ loginApp.js
  const token = localStorage.getItem("adminToken");
  if (!maNguoiDung || !token) return;

  try {
    // 🛑 BƯỚC TRUNG GIAN QUAN TRỌNG: Tìm maNhanVien dựa trên maNguoiDung
    // Để tránh trường hợp maNguoiDung khác với maNhanVien khiến không hiện việc
    const resProfile = await fetch(
      `http://localhost:8080/api/nhan-vien-ky-thuat/nguoi-dung/${maNguoiDung}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    );
    const profileData = await resProfile.json();

    if (profileData.success && profileData.data) {
      const realMaNhanVien = profileData.data.maNhanVien; // Lấy được ID thợ máy thực tế
      console.log("Đã xác định ID thợ máy thực tế:", realMaNhanVien);

      // 3. Gọi API Lấy số liệu việc của Thợ bằng ID thật
      const res = await fetch(
        `http://localhost:8080/api/phan-cong-sua-chua/nhan-vien/${realMaNhanVien}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        },
      );
      const result = await res.json();
      const jobs = result.data || [];

      // 4. Phân loại trạng thái công việc
      const moiNhan = jobs.filter((j) => j.trangThai === "DaPhanCong").length;
      const dangLam = jobs.filter((j) => j.trangThai === "DangThucHien").length;
      const daXong = jobs.filter((j) => j.trangThai === "HoanThanh").length;

      // 5. Đổi thông tin 4 Thẻ thống kê
      const cards = document.querySelectorAll(".stat-card");
      if (cards.length >= 4) {
        // Card 1: Mới giao
        cards[0].querySelector(".stat-title").textContent = "Mới Được Giao";
        cards[0].querySelector(".stat-value").textContent = moiNhan;
        cards[0].querySelector(".stat-card-icon i").className =
          "fas fa-clipboard-list";

        // Card 2: Đang sửa
        cards[1].querySelector(".stat-title").textContent = "Đang Sửa Chữa";
        cards[1].querySelector(".stat-value").textContent = dangLam;
        cards[1].querySelector(".stat-card-icon i").className = "fas fa-wrench";

        // Card 3: Hoàn thành
        cards[2].querySelector(".stat-title").textContent = "Đã Hoàn Thành";
        cards[2].querySelector(".stat-value").textContent = daXong;
        cards[2].querySelector(".stat-card-icon i").className =
          "fas fa-check-circle";

        // Card 4: Tổng số
        cards[3].querySelector(".stat-title").textContent = "Tổng Xe Đã Nhận";
        cards[3].querySelector(".stat-value").textContent = jobs.length;
        cards[3].querySelector(".stat-card-icon i").className =
          "fas fa-car-side";
      }
    } else {
      console.warn("Không tìm thấy thông tin nhân viên cho người dùng này.");
    }
  } catch (e) {
    console.error("Lỗi đồng bộ dữ liệu thợ máy:", e);
  }
}
