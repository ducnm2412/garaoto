/**
 * ============================================================================
 * FILE: suaChuaApp.js
 * CHỨC NĂNG: Quản lý luồng Dịch vụ sửa chữa (Admin & Thợ máy)
 * BAO GỒM: Khởi tạo, Phân quyền, Tải dữ liệu, Xử lý giao diện và Gọi API
 * ============================================================================
 */

// ============================================================================
// MODULE 1: KHỞI TẠO & BIẾN TOÀN CỤC
// ============================================================================
let allRepairsData = []; // Danh sách Lịch hẹn
let allPhieuSuaData = []; // Danh sách Phiếu sửa chữa
let assignedLichHenIds = []; // Danh sách Lịch hẹn đã được giao việc (ẩn khỏi Tab 1)
let isThoMay = false; // Cờ kiểm tra vai trò

// Chạy ngay khi trang web vừa tải xong HTML
document.addEventListener("DOMContentLoaded", () => {
  // 1. Kiểm tra trạng thái đăng nhập
  if (!localStorage.getItem("isAdminLoggedIn")) {
    window.location.replace("admin-login.html");
    return;
  }

  // 2. Hiển thị tên người dùng
  const adminName = localStorage.getItem("adminName");
  const nameEl = document.querySelector(".admin-name");
  if (adminName && nameEl) nameEl.textContent = "Xin chào, " + adminName + "!";

  // 3. Phân quyền và Thiết lập giao diện
  applyRoleBasedAccessControl(localStorage.getItem("adminRole"));
  setupTabs();

  // 4. Gọi dữ liệu & Kích hoạt lắng nghe sự kiện click
  loadDashboardData();
  setupRepairActionButtons();
});

// Phân quyền hiển thị Menu và Tab dựa theo Role
function applyRoleBasedAccessControl(role) {
  const menuItems = document.querySelectorAll(".sidebar-menu .menu-item");
  if (role === "NhanVienKyThuat") {
    // Thợ máy chỉ xem Tổng quan và Dịch vụ sửa chữa
    menuItems.forEach((item) => {
      if (
        item.textContent.trim() !== "Tổng quan" &&
        item.textContent.trim() !== "Dịch vụ Sửa chữa"
      ) {
        item.style.display = "none";
      }
    });
    // Ẩn Tab Lịch hẹn, tự động chuyển sang Tab Tiến độ
    document.getElementById("btnTabLichHen").style.display = "none";
    switchTab("tabTienDo", "btnTabTienDo");
  }
}

// Cấu hình sự kiện chuyển đổi giữa các Tab
function setupTabs() {
  const btnLichHen = document.getElementById("btnTabLichHen");
  const btnTienDo = document.getElementById("btnTabTienDo");

  if (btnLichHen)
    btnLichHen.onclick = () => switchTab("tabLichHen", "btnTabLichHen");
  if (btnTienDo)
    btnTienDo.onclick = () => switchTab("tabTienDo", "btnTabTienDo");
}

function switchTab(tabId, btnId) {
  document
    .querySelectorAll(".tab-content")
    .forEach((tab) => (tab.style.display = "none"));
  document.getElementById(tabId).style.display = "block";

  document.querySelectorAll(".tab-btn").forEach((btn) => {
    btn.style.background = "transparent";
    btn.style.color = "#64748b";
  });

  const activeBtn = document.getElementById(btnId);
  activeBtn.style.background = "#2563eb";
  activeBtn.style.color = "white";
}

// ============================================================================
// MODULE 2: TẢI & PHÂN LUỒNG DỮ LIỆU CHÍNH (LOAD DASHBOARD)
// ============================================================================
async function loadDashboardData() {
  const role = localStorage.getItem("adminRole");
  const myId = localStorage.getItem("adminId");
  const token = localStorage.getItem("adminToken");
  isThoMay = role === "NhanVienKyThuat";

  const headers = {
    Authorization: `Bearer ${token}`,
  };

  const tbodyTienDo = document.querySelector("#tienDoTable tbody");
  if (tbodyTienDo)
    tbodyTienDo.innerHTML = `<tr><td colspan="7" style="text-align:center;">Đang tải dữ liệu...</td></tr>`;

  if (isThoMay && (!myId || myId === "undefined" || myId === "null")) {
    tbodyTienDo.innerHTML = `<tr><td colspan="7" style="text-align:center; color:red; font-weight:bold;">LỖI: Không tìm thấy ID Nhân viên.</td></tr>`;
    return;
  }

  try {
    if (isThoMay) {
      const resProfile = await fetch(
        `http://localhost:8080/api/nhan-vien-ky-thuat/nguoi-dung/${myId}`,
        {
          headers: headers,
        },
      );
      const profileData = await resProfile.json();

      if (profileData.success && profileData.data) {
        const realMaNhanVien = profileData.data.maNhanVien;
        console.log("Tìm thấy mã thợ máy thật:", realMaNhanVien);

        const resPhanCong = await fetch(
          `http://localhost:8080/api/phan-cong-sua-chua/nhan-vien/${realMaNhanVien}`,
          { headers },
        );
        if (!resPhanCong.ok) throw new Error("API Phân công từ chối truy cập");
        const dataPhanCong = await resPhanCong.json();

        if (
          dataPhanCong.success &&
          dataPhanCong.data &&
          dataPhanCong.data.length > 0
        ) {
          renderThoMayTable(dataPhanCong.data.reverse());
        } else {
          renderThoMayTable([]);
        }
      } else {
        throw new Error("Không tìm thấy thông tin nhân viên kỹ thuật.");
      }
    } else {
      const resPhieu = await fetch("http://localhost:8080/api/phieu-sua-chua", {
        headers,
      });
      const dataPhieu = await resPhieu.json();
      if (dataPhieu.success && dataPhieu.data) {
        allPhieuSuaData = dataPhieu.data.reverse();
        assignedLichHenIds = allPhieuSuaData
          .map((p) => p.maLichHen)
          .filter((id) => id !== null);
      }
      renderTienDoTable(allPhieuSuaData);

      const resLich = await fetch(
        "http://localhost:8080/api/lich-hen-sua-chua",
        { headers },
      );
      const dataLich = await resLich.json();
      if (dataLich.success && dataLich.data) {
        allRepairsData = dataLich.data.reverse();
        renderRepairTable(allRepairsData);
      }
    }
  } catch (error) {
    console.error("Lỗi tải dữ liệu:", error);
    if (tbodyTienDo)
      tbodyTienDo.innerHTML = `<tr><td colspan="7" style="text-align:center; color:red;">Lỗi tải dữ liệu: ${error.message}</td></tr>`;
  }
}

// ============================================================================
// MODULE 3: RENDER GIAO DIỆN BẢNG (DOM MANIPULATION)
// ============================================================================

// 1. Vẽ Bảng Lịch Hẹn (Admin)
function renderRepairTable(dataArray) {
  const tbody = document.querySelector("#fullRepairTable tbody");
  tbody.innerHTML = "";

  // Bỏ qua Lịch đã hủy, Lịch hoàn thành và Lịch đã giao việc
  const pendingAppointments = dataArray.filter(
    (item) =>
      item.trangThai !== "DaHuy" &&
      item.trangThai !== "HoanThanh" &&
      !assignedLichHenIds.includes(item.maLichHen),
  );

  if (pendingAppointments.length === 0) {
    tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;">Tuyệt vời! Không có lịch hẹn nào đang ứ đọng.</td></tr>`;
    return;
  }

  pendingAppointments.forEach((item) => {
    const ngayHen = new Date(item.ngayHen).toLocaleDateString("vi-VN");
    const gioHen = item.gioHen ? item.gioHen.substring(0, 5) : "";

    const row = `
      <tr>
        <td><strong>#LH${item.maLichHen}</strong></td>
        <td><a class="link-id btn-view-customer" data-id="${item.maKhachHang}">KH #${item.maKhachHang}</a></td>
        <td><a class="link-id btn-view-customer-car" data-id="${item.maXeKh}">XE KH #${item.maXeKh}</a></td>
        <td>${ngayHen}<br><small style="color: #64748b; font-weight: 500;">${gioHen}</small></td>
        <td><div style="max-width: 250px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;" title="${item.moTaLoi}">${item.moTaLoi || "Không có"}</div></td>
        <td><span class="badge ${getRepairBadgeClass(item.trangThai)}">${formatRepairStatus(item.trangThai)}</span></td>
        <td>
          <button class="btn-action btn-reject-repair" data-id="${item.maLichHen}" title="Hủy lịch"><i class="fas fa-calendar-times"></i></button>
          <button class="btn-action btn-view-repair" data-id="${item.maLichHen}" title="Xem & Phân công"><i class="fas fa-eye"></i></button>
        </td>
      </tr>`;
    tbody.innerHTML += row;
  });
}

// 2. Vẽ Bảng Tiến Độ Xưởng (Admin)
function renderTienDoTable(dataArray) {
  const table = document.querySelector("#tienDoTable");
  if (!table) return;

  const thead = table.querySelector("thead");
  thead.innerHTML = `
    <tr>
      <th>Mã Phiếu</th><th>Tham chiếu LH</th><th>Xe Sửa Chữa</th><th>Tiến Độ Sửa</th><th>Tổng Tiền</th><th>Trạng Thái</th><th>Thao Tác</th>
    </tr>`;

  const tbody = table.querySelector("tbody");
  tbody.innerHTML = "";

  if (dataArray.length === 0) {
    tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;">Xưởng đang trống. Không có xe nào đang sửa.</td></tr>`;
    return;
  }

  dataArray.forEach((item) => {
    const tongTien = item.tongTien ? item.tongTien.toLocaleString() : "0";
    const textLH = item.maLichHen
      ? `<strong>#LH${item.maLichHen}</strong>`
      : `<span style="font-size: 12px; color: #94a3b8; font-style: italic;">Khách vãng lai</span>`;
    const btnThanhToan =
      item.trangThai === "DaSuaXong"
        ? `<button class="btn-action btn-approve" title="Bàn giao & Thu tiền" onclick="hoanTatPhieu(${item.maPhieuSua})"><i class="fas fa-file-invoice-dollar"></i></button>`
        : "";

    const row = `
      <tr>
        <td><strong>Phiếu #${item.maPhieuSua}</strong></td>
        <td>${textLH}</td>
        <td><a class="link-id btn-view-customer-car" data-id="${item.maXeKh}">XE KH #${item.maXeKh}</a></td>
        <td><strong>Chẩn đoán:</strong><br><small>${item.chanDoan || "Chưa khám"}</small></td>
        <td><strong>${tongTien} đ</strong></td>
        <td><span class="badge ${getPhieuBadgeClass(item.trangThai)}">${formatPhieuStatus(item.trangThai)}</span></td>
        <td>
          <button class="btn-action btn-view-phieu" data-id="${item.maPhieuSua}" title="Xem Phiếu"><i class="fas fa-eye"></i></button>
          ${btnThanhToan}
        </td>
      </tr>`;
    tbody.innerHTML += row;
  });
}

// 3. Vẽ Bảng Tiến Độ (Thợ Máy)
function renderThoMayTable(dataArray) {
  const table = document.querySelector("#tienDoTable");
  if (!table) return;

  const thead = table.querySelector("thead");
  thead.innerHTML = `
    <tr>
      <th>Mã Phiếu</th><th>Ngày Giao</th><th>Thông Tin Xe & Lỗi</th><th>Ghi Chú Của Admin</th><th>Trạng Thái</th><th>Thao Tác</th>
    </tr>`;

  const tbody = table.querySelector("tbody");
  tbody.innerHTML = "";

  if (dataArray.length === 0) {
    tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;">Bạn chưa có công việc nào được phân công.</td></tr>`;
    return;
  }

  dataArray.forEach((item) => {
    let actionBtn = "";
    if (item.trangThai === "DaPhanCong") {
      actionBtn = `<button class="btn-action btn-start" onclick="updateTienDoTho(${item.maPhanCong}, ${item.maPhieuSua}, 'DangThucHien')"><i class="fas fa-play"></i> Bắt đầu làm</button>`;
    } else if (item.trangThai === "DangThucHien") {
      actionBtn = `<button class="btn-action btn-approve" onclick="updateTienDoTho(${item.maPhanCong}, ${item.maPhieuSua}, 'HoanThanh')"><i class="fas fa-check"></i> Sửa xong</button>`;
    } else {
      actionBtn = `<span style="color:green"><i class="fas fa-check-double"></i> Đã xong</span>`;
    }

    const row = `
      <tr>
        <td><strong>Phiếu #${item.maPhieuSua}</strong></td>
        <td><small style="color: #475569;">${new Date(item.ngayPhanCong).toLocaleDateString("vi-VN")}</small></td>
        <td>
          <button class="btn-action btn-view-car-tech" data-id="${item.maPhieuSua}" style="background:#f1f5f9; border:1px solid #cbd5e1; border-radius:4px; padding:6px 10px; font-weight:bold;">
            <i class="fas fa-car" style="color: #2563eb; margin-right:5px;"></i> Bệnh án & Nhận xe
          </button>
        </td>
        <td><div style="max-width: 250px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;" title="${item.ghiChu}">${item.ghiChu || "Không có"}</div></td>
        <td><span class="badge ${item.trangThai === "HoanThanh" ? "badge-success" : "badge-warning"}">${item.trangThai}</span></td>
        <td>${actionBtn}</td>
      </tr>`;
    tbody.innerHTML += row;
  });
}

// ============================================================================
// MODULE 4: LẮNG NGHE SỰ KIỆN CLICK (DELEGATION) & MODAL
// ============================================================================
const modal = document.getElementById("infoModal");
document.querySelector(".close-modal").onclick = () =>
  (modal.style.display = "none");
window.onclick = (e) => {
  if (e.target == modal) modal.style.display = "none";
};

function openModal(title) {
  document.getElementById("modalTitle").textContent = title;
  document.getElementById("modalBody").innerHTML =
    `<div style="text-align:center; padding:20px;"><i class="fas fa-spinner fa-spin"></i> Đang tải...</div>`;
  modal.style.display = "block";
}

function setupRepairActionButtons() {
  // Lắng nghe toàn bộ click trong bảng bằng Event Delegation
  document.addEventListener("click", (e) => {
    // Nút: Hủy lịch hẹn
    if (e.target.closest(".btn-reject-repair")) {
      const id = e.target.closest(".btn-reject-repair").dataset.id;
      if (confirm(`HỦY lịch hẹn #${id}?`)) updateRepairStatus(id, "DaHuy");
    }

    // Nút: Xem Lịch hẹn (Admin)
    if (e.target.closest(".btn-view-repair")) {
      const id = e.target.closest(".btn-view-repair").dataset.id;
      openModal(`Chi tiết Lịch hẹn #${id}`);
      fetchRepairDetails(id);
    }

    // Nút: Xem Phiếu Sửa (Admin)
    if (e.target.closest(".btn-view-phieu")) {
      const id = e.target.closest(".btn-view-phieu").dataset.id;
      openModal(`Chi tiết Phiếu Sửa Chữa #${id}`);
      fetchPhieuDetails(id);
    }

    // Nút: Xem Bệnh án xe (Thợ Máy)
    if (e.target.closest(".btn-view-car-tech")) {
      const id = e.target.closest(".btn-view-car-tech").dataset.id;
      openModal(`Bệnh án & Chi tiết Xe (Phiếu #${id})`);
      fetchChiTietXeChoTho(id);
    }

    // Nút: Xem Khách Hàng
    if (e.target.closest(".btn-view-customer")) {
      openModal(`Thông tin Khách hàng`);
      fetchCustomerInfo(e.target.closest(".btn-view-customer").dataset.id);
    }

    // Nút: Xem Phương Tiện
    if (e.target.closest(".btn-view-customer-car")) {
      openModal(`Thông tin Xe của Khách`);
      fetchCustomerCarInfo(
        e.target.closest(".btn-view-customer-car").dataset.id,
      );
    }
  });
}

// ============================================================================
// MODULE 5: FETCH DỮ LIỆU ĐỂ ĐỔ VÀO MODAL
// ============================================================================

// Tải chi tiết Lịch Hẹn & Form Giao việc (Admin)
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
    const lh = result.data;
    const role = localStorage.getItem("adminRole");
    let actionHtml = "";

    // Form giao thợ máy (chỉ hiện khi chưa làm)
    if (
      role === "Admin" &&
      (lh.trangThai === "ChoXacNhan" || lh.trangThai === "DaXacNhan")
    ) {
      let thongTinThoMay = `<option value="" disabled selected>-- Chọn nhân viên kỹ thuật --</option>`;
      const resNV = await fetch(
        "http://localhost:8080/api/nhan-vien-ky-thuat",
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
          },
        },
      );
      const dataNV = await resNV.json();
      dataNV.data.forEach(
        (nv) =>
          (thongTinThoMay += `<option value="${nv.maNhanVien}">${nv.hoTen} - ${nv.chuyenMon || "Thợ"}</option>`),
      );

      const btnText =
        lh.trangThai === "ChoXacNhan"
          ? "Duyệt & Tạo phiếu"
          : "Tạo phiếu & Giao việc";

      actionHtml = `
        <div style="margin-top: 20px; border-top: 1px solid #eee; padding-top: 15px;">
          <p style="margin-bottom: 5px; font-weight: bold;">Giao xe vào xưởng:</p>
          <div style="display: flex; gap: 10px;">
            <select id="selectNhanVien" class="form-control" style="flex: 1; padding: 8px;">${thongTinThoMay}</select>
            <button style="background: #10b981; color: white; border: none; padding: 10px 15px; border-radius: 4px; cursor: pointer; font-weight: bold;" 
                    onclick="phanCongVaDuyet(${lh.maLichHen}, ${lh.maXeKh}, ${lh.maKhachHang})">
              <i class="fas fa-arrow-right"></i> ${btnText}
            </button>
          </div>
        </div>`;
    }

    document.getElementById("modalBody").innerHTML = `
      <div class="order-details">
        <div style="background-color: #f8fafc; padding: 15px; border-radius: 6px; margin-bottom: 15px; text-align: center;">
          <h4 style="margin: 0 0 10px 0; color: #0f172a;">LỊCH HẸN MANG XE TỚI</h4>
          <div style="font-size: 24px; color: #2563eb; font-weight: bold;">
            <i class="far fa-clock"></i> ${lh.gioHen || "--:--"} | ${new Date(lh.ngayHen).toLocaleDateString("vi-VN")}
          </div>
        </div>
        <div style="background-color: #fef2f2; border: 1px solid #fecaca; padding: 15px; border-radius: 6px;">
          <p style="margin: 0 0 5px 0; color: #dc2626; font-weight: bold; font-size: 14px;"> Lỗi báo trước:</p>
          <p style="margin: 0; font-style: italic;">"${lh.moTaLoi || "Không có"}"</p>
        </div>
        ${actionHtml}
      </div>`;
  } catch (error) {
    document.getElementById("modalBody").innerHTML =
      `<p style="color:red; text-align:center;">Lỗi máy chủ.</p>`;
  }
}

// Tải Chi tiết Phiếu Sửa & Form Cập nhật Tiền (Admin)
async function fetchPhieuDetails(id) {
  try {
    const role = localStorage.getItem("adminRole");
    const headers = {
      Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
    };
    const response = await fetch(
      `http://localhost:8080/api/phieu-sua-chua/${id}`,
      { headers },
    );
    const result = await response.json();

    if (result.success === true && result.data) {
      const phieu = result.data;
      const ngayNhan = new Date(phieu.ngayNhanXe).toLocaleString("vi-VN");

      // Form nhập tiền (Chỉ hiện khi xe chưa bàn giao)
      let updateFormHtml = "";
      if (role === "Admin" && phieu.trangThai !== "BanGiao") {
        updateFormHtml = `
          <div style="margin-top: 20px; border-top: 2px dashed #cbd5e1; padding-top: 20px;">
            <p style="margin: 0 0 15px 0; font-weight: bold;"><i class="fas fa-edit text-primary"></i> CẬP NHẬT CHI PHÍ</p>
            <div style="margin-bottom: 15px;">
              <label style="display:block; font-size:13px; font-weight:bold;">Tình trạng / Chẩn đoán:</label>
              <input type="text" id="inputChanDoan" class="form-control" style="width: 100%; padding: 10px; border-radius: 4px;" value="${phieu.chanDoan || ""}">
            </div>
            <div style="margin-bottom: 15px;">
              <label style="display:block; font-size:13px; font-weight:bold;">Tổng tiền thanh toán (VNĐ):</label>
              <input type="number" id="inputTongTien" class="form-control" style="width: 100%; padding: 10px; border-radius: 4px; font-weight: bold; color: #b45309;" value="${phieu.tongTien || 0}">
            </div>
            <button style="background: #2563eb; color: white; border: none; padding: 12px 15px; border-radius: 4px; cursor: pointer; width: 100%; font-weight: bold;" onclick="capNhatHoaDon(${phieu.maPhieuSua})">
              <i class="fas fa-save"></i> Lưu hóa đơn
            </button>
          </div>`;
      }

      document.getElementById("modalBody").innerHTML = `
        <div class="order-details">
          <div style="display: flex; justify-content: space-between; border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 15px;">
            <span><strong>Ngày nhận:</strong> ${ngayNhan}</span>
            <span class="badge ${getPhieuBadgeClass(phieu.trangThai)}">${formatPhieuStatus(phieu.trangThai)}</span>
          </div>
          <div style="background-color: #fffbeb; border: 1px solid #fde68a; padding: 15px; border-radius: 6px; display: flex; justify-content: space-between; align-items: center;">
            <span style="font-size: 16px; color: #b45309; font-weight: bold;">TỔNG CHI PHÍ:</span>
            <strong style="font-size: 22px; color: #b45309;">${phieu.tongTien ? phieu.tongTien.toLocaleString() : "0"} đ</strong>
          </div>
          ${updateFormHtml}
        </div>`;
    }
  } catch (error) {
    document.getElementById("modalBody").innerHTML =
      `<p style="color:red; text-align:center;">Lỗi kết nối máy chủ.</p>`;
  }
}

// Lấy thông tin Bệnh án cho Thợ máy (Gộp 2 API)
async function fetchChiTietXeChoTho(maPhieuSua) {
  try {
    const headers = {
      Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
    };

    // 1. Lấy mã Xe từ Phiếu
    const resPhieu = await fetch(
      `http://localhost:8080/api/phieu-sua-chua/${maPhieuSua}`,
      { headers },
    );
    const dataPhieu = await resPhieu.json();
    const phieu = dataPhieu.data;

    // 2. Lấy Biển số từ mã Xe
    let xeInfoHtml = `<p>Không có dữ liệu xe.</p>`;
    if (phieu.maXeKh) {
      const resXe = await fetch(
        `http://localhost:8080/api/xe-khach-hang/${phieu.maXeKh}`,
        { headers },
      );
      const dataXe = await resXe.json();
      if (dataXe.success && dataXe.data) {
        const xe = dataXe.data;
        xeInfoHtml = `
          <div style="text-align: center; margin-bottom: 15px;">
            <h4 style="margin: 0 0 10px 0; color: #0f172a;">${xe.hangXe} ${xe.dongXe} (${xe.namSanXuat || "---"})</h4>
            <span style="display:inline-block; padding:10px 20px; border:3px solid #1e293b; border-radius:8px; font-weight:900; font-size: 22px; letter-spacing: 2px;">${xe.bienSo}</span>
          </div>`;
      }
    }

    document.getElementById("modalBody").innerHTML = `
      <div class="order-details">
        <div style="background-color: #f1f5f9; padding: 20px; border-radius: 8px; margin-bottom: 20px;">
          <p style="font-weight: bold; color: #64748b; font-size: 12px;">1. NHẬN DIỆN XE TRONG XƯỞNG</p>
          ${xeInfoHtml}
        </div>
        <div style="background-color: #fef2f2; border: 1px solid #fecaca; padding: 20px; border-radius: 8px;">
          <p style="font-weight: bold; color: #dc2626; font-size: 12px;">2. CHẨN ĐOÁN LỖI TỪ ADMIN</p>
          <p style="font-style: italic; font-size: 16px;">"${phieu.chanDoan || "Chờ thợ máy kiểm tra."}"</p>
        </div>
      </div>`;
  } catch (error) {
    document.getElementById("modalBody").innerHTML =
      `<p style="color:red; text-align:center;">Lỗi: ${error.message}</p>`;
  }
}

// Các hàm fetch cơ bản: Khách hàng & Phương tiện
async function fetchCustomerInfo(id) {
  try {
    const response = await fetch(`http://localhost:8080/api/khach-hang/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
      },
    });
    const result = await response.json();
    if (result.success === true && result.data) {
      const kh = result.data;
      document.getElementById("modalBody").innerHTML = `
        <div><h4>${kh.hoTen || "---"}</h4><p>Điện thoại: ${kh.soDienThoai}</p></div>`;
    }
  } catch (e) {
    document.getElementById("modalBody").innerHTML =
      `<p style="color:red;">Lỗi lấy dữ liệu.</p>`;
  }
}

async function fetchCustomerCarInfo(id) {
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
    if (result.success === true && result.data) {
      const xe = result.data;
      document.getElementById("modalBody").innerHTML = `
        <div style="text-align: center;"><h4>${xe.hangXe} ${xe.dongXe}</h4><h3>${xe.bienSo}</h3></div>`;
    }
  } catch (e) {
    document.getElementById("modalBody").innerHTML =
      `<p style="color:red;">Lỗi kết nối máy chủ.</p>`;
  }
}

// ============================================================================
// MODULE 6: CÁC LUỒNG CẬP NHẬT TRẠNG THÁI (API UPDATERS)
// ============================================================================

// Admin: Hủy lịch hẹn
async function updateRepairStatus(id, newStatus) {
  try {
    await fetch(
      `http://localhost:8080/api/lich-hen-sua-chua/${id}/trang-thai?trangThai=${newStatus}`,
      {
        method: "PATCH",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
        },
      },
    );
    alert("Cập nhật thành công!");
    loadDashboardData();
  } catch (error) {
    alert("Lỗi máy chủ!");
  }
}

// Admin: Duyệt Lịch -> Tạo Phiếu -> Phân Công
async function phanCongVaDuyet(maLichHen, maXeKh, maKhachHang) {
  const maNhanVien = document.getElementById("selectNhanVien").value;
  const adminId = localStorage.getItem("adminId") || 1;
  const headers = {
    "Content-Type": "application/json",
    Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
  };

  if (!maNhanVien) return alert("⚠️ Vui lòng chọn thợ máy!");
  if (!confirm("Tạo phiếu sửa chữa và giao việc?")) return;
  document.body.style.cursor = "wait";

  try {
    // 1. Tạo Phiếu
    const phieuRes = await fetch("http://localhost:8080/api/phieu-sua-chua", {
      method: "POST",
      headers,
      body: JSON.stringify({
        maLichHen: parseInt(maLichHen),
        maXeKh: parseInt(maXeKh),
        maKhachHang: parseInt(maKhachHang),
        chanDoan: "Chờ kiểm tra",
        tongTien: 0,
        trangThai: "TiepNhan",
      }),
    });
    const phieuData = await phieuRes.json();
    if (!phieuRes.ok || !phieuData.success)
      throw new Error(phieuData.message || "Lỗi tạo Phiếu!");
    const maPhieuTaoMoi = phieuData.data.maPhieuSua;

    // 2. Giao việc Thợ máy
    const phanCongRes = await fetch(
      "http://localhost:8080/api/phan-cong-sua-chua",
      {
        method: "POST",
        headers,
        body: JSON.stringify({
          maPhieuSua: maPhieuTaoMoi,
          maNhanVien: parseInt(maNhanVien),
          maAdmin: parseInt(adminId),
          ghiChu: "Giao việc mới",
          trangThai: "DaPhanCong",
        }),
      },
    );
    const phanCongData = await phanCongRes.json();
    if (!phanCongRes.ok || !phanCongData.success)
      throw new Error(phanCongData.message || "Lỗi phân công thợ máy!");

    // 3. Đổi Lịch hẹn
    await fetch(
      `http://localhost:8080/api/lich-hen-sua-chua/${maLichHen}/trang-thai?trangThai=DaXacNhan`,
      { method: "PATCH", headers },
    );

    alert("✅ Xe đã được chuyển sang Xưởng.");
    document.getElementById("infoModal").style.display = "none";
    loadDashboardData();
  } catch (error) {
    alert("❌ Thất bại: " + error.message);
  } finally {
    document.body.style.cursor = "default";
  }
}

// Thợ Máy: Nhận việc & Báo hoàn thành (Cập nhật 2 bảng cùng lúc)
async function updateTienDoTho(maPhanCong, maPhieuSua, statusPhanCong) {
  const actionMsg =
    statusPhanCong === "DangThucHien"
      ? "Bắt đầu tiến hành sửa xe?"
      : "Xác nhận đã sửa xong toàn bộ?";
  if (!confirm(`🔧 ${actionMsg}`)) return;

  document.body.style.cursor = "wait";
  const headers = {
    Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
  };
  const mapPhieuStatus = { DangThucHien: "DangSua", HoanThanh: "DaSuaXong" };

  try {
    const resPhanCong = await fetch(
      `http://localhost:8080/api/phan-cong-sua-chua/${maPhanCong}/trang-thai?trangThai=${statusPhanCong}`,
      { method: "PATCH", headers },
    );
    if (!resPhanCong.ok)
      throw new Error("Không thể cập nhật tiến độ cho Thợ máy!");

    const resPhieu = await fetch(
      `http://localhost:8080/api/phieu-sua-chua/${maPhieuSua}/trang-thai?trangThai=${mapPhieuStatus[statusPhanCong]}`,
      { method: "PATCH", headers },
    );
    if (!resPhieu.ok) throw new Error("Lỗi đồng bộ dữ liệu sang Phiếu tổng!");

    alert("✅ Đã cập nhật tiến độ công việc!");
    loadDashboardData();
  } catch (error) {
    alert("❌ Lỗi: " + error.message);
  } finally {
    document.body.style.cursor = "default";
  }
}

// Admin: Cập nhật Tiền & Chẩn đoán
async function capNhatHoaDon(maPhieuSua) {
  const chanDoanMoi = document.getElementById("inputChanDoan").value.trim();
  const tongTienMoi = document.getElementById("inputTongTien").value;
  if (tongTienMoi < 0) return alert("⚠️ Tổng tiền không được âm!");

  document.body.style.cursor = "wait";
  try {
    const response = await fetch(
      `http://localhost:8080/api/phieu-sua-chua/${maPhieuSua}`,
      {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
        },
        body: JSON.stringify({
          chanDoan: chanDoanMoi,
          tongTien: parseInt(tongTienMoi),
        }),
      },
    );
    const result = await response.json();
    if (result.success) {
      alert("✅ Đã cập nhật hóa đơn thành công!");
      document.getElementById("infoModal").style.display = "none";
      loadDashboardData();
    } else throw new Error("Không thể cập nhật!");
  } catch (error) {
    alert("❌ Lỗi: " + error.message);
  } finally {
    document.body.style.cursor = "default";
  }
}

// Admin: Bàn giao xe (Chuyển sang tab Thanh toán)
async function hoanTatPhieu(maPhieuSua) {
  if (!confirm("Bàn giao xe cho khách và hoàn tất hồ sơ?")) return;
  try {
    await fetch(
      `http://localhost:8080/api/phieu-sua-chua/${maPhieuSua}/trang-thai?trangThai=BanGiao`,
      {
        method: "PATCH",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
        },
      },
    );
    alert("Đã bàn giao xe thành công!");
    loadDashboardData();
  } catch (e) {
    alert("Lỗi!");
  }
}

// ============================================================================
// MODULE 7: TIỆN ÍCH UI (TRẠNG THÁI & MÀU SẮC)
// ============================================================================
function getRepairBadgeClass(status) {
  switch (status) {
    case "ChoXacNhan":
      return "badge-warning";
    case "DaXacNhan":
      return "badge-success";
    default:
      return "badge-secondary";
  }
}
function formatRepairStatus(status) {
  switch (status) {
    case "ChoXacNhan":
      return "Chờ Xác Nhận";
    case "DaXacNhan":
      return "Đã Xác Nhận";
    default:
      return status;
  }
}
function getPhieuBadgeClass(status) {
  switch (status) {
    case "TiepNhan":
      return "badge-warning";
    case "DangSua":
      return "badge-info";
    case "DaSuaXong":
      return "badge-success";
    case "BanGiao":
      return "badge-secondary";
    default:
      return "";
  }
}
function formatPhieuStatus(status) {
  switch (status) {
    case "TiepNhan":
      return "Mới Tiếp Nhận";
    case "DangSua":
      return "Đang Sửa Chữa";
    case "DaSuaXong":
      return "Đã Sửa Xong";
    case "BanGiao":
      return "Đã Bàn Giao";
    default:
      return status;
  }
}

/* ============================================================================
MODULE 8: KHÁCH VÃNG LAI (TẠM ẨN - CHỜ MODULE PHƯƠNG TIỆN)
// Code tạo Form khách vãng lai (không có lịch hẹn) được comment lại 
// để mở sau khi Admin đã có khả năng tạo nhanh Khách hàng mới.
============================================================================
*/
