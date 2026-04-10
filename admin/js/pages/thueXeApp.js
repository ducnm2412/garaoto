/**
 * ============================================================================
 * FILE: thueXeApp.js
 * CHỨC NĂNG: Quản lý toàn bộ luồng Dịch vụ Cho Thuê Xe tự lái
 * BAO GỒM: Tải danh sách, Lọc trạng thái, Duyệt/Hủy đơn, Giao/Nhận xe.
 * ============================================================================
 */

// ============================================================================
// MODULE 1: KHỞI TẠO & BIẾN TOÀN CỤC
// ============================================================================
let allRentalsData = []; // Lưu trữ dữ liệu gốc để xử lý tính năng Lọc (Filter)

document.addEventListener("DOMContentLoaded", () => {
  // 1. Chốt chặn bảo mật: Kiểm tra đăng nhập
  if (!localStorage.getItem("isAdminLoggedIn")) {
    window.location.replace("admin-login.html");
    return;
  }

  // 2. Hiển thị thông tin người dùng
  const adminName = localStorage.getItem("adminName");
  const nameEl = document.querySelector(".admin-name");
  if (adminName && nameEl) nameEl.textContent = "Xin chào, " + adminName + "!";

  // 3. Tải dữ liệu và Kích hoạt các sự kiện lắng nghe
  loadAllRentals();
  setupActionButtons();
  setupFilterListener(); // Tách riêng logic lắng nghe bộ lọc
});

// ============================================================================
// MODULE 2: TẢI DỮ LIỆU & VẼ BẢNG CHÍNH
// ============================================================================
async function loadAllRentals() {
  const tbody = document.querySelector("#fullRentalTable tbody");
  if (!tbody) return;

  tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;"><i class="fas fa-spinner fa-spin"></i> Đang tải toàn bộ dữ liệu...</td></tr>`;

  try {
    const response = await fetch("http://localhost:8080/api/don-thue-xe", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
      },
    });

    if (!response.ok) throw new Error(`Lỗi máy chủ: ${response.status}`);

    const textData = await response.text();
    if (!textData) throw new Error("Dữ liệu trả về rỗng");

    const result = JSON.parse(textData);

    if (result.success && result.data) {
      allRentalsData = result.data.reverse(); // Đơn mới nhất lên đầu

      // Xử lý logic hiển thị nếu đang có sẵn bộ lọc
      const filterSelect = document.getElementById("filterStatus");
      const currentFilter = filterSelect ? filterSelect.value : "ALL";

      if (currentFilter === "ALL") {
        renderTable(allRentalsData);
      } else {
        renderTable(
          allRentalsData.filter((item) => item.trangThai === currentFilter),
        );
      }
    } else {
      tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; color:red;">${result.message || "Lỗi lấy dữ liệu"}</td></tr>`;
    }
  } catch (error) {
    tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; color:red;">${error.message}</td></tr>`;
  }
}

function renderTable(dataArray) {
  const tbody = document.querySelector("#fullRentalTable tbody");
  if (!tbody) return;
  tbody.innerHTML = "";

  if (dataArray.length === 0) {
    tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;">Không có dữ liệu phù hợp.</td></tr>`;
    return;
  }

  dataArray.forEach((item) => {
    const ngayNhan = new Date(item.ngayNhan).toLocaleDateString("vi-VN");
    const ngayTra = new Date(item.ngayTra).toLocaleDateString("vi-VN");
    const tongTien = item.tongTien ? item.tongTien.toLocaleString() : "0";

    // Logic hiển thị nút bấm
    let actionButtons =
      item.trangThai === "ChoDuyet"
        ? `<button class="btn-action btn-approve" data-id="${item.maDonThue}" title="Duyệt đơn"><i class="fas fa-check-circle"></i></button>
               <button class="btn-action btn-reject" data-id="${item.maDonThue}" title="Từ chối"><i class="fas fa-times-circle"></i></button>
               <button class="btn-action btn-view" data-id="${item.maDonThue}" title="Xem chi tiết"><i class="fas fa-eye"></i></button>`
        : `<button class="btn-action btn-view" data-id="${item.maDonThue}" title="Xem chi tiết"><i class="fas fa-eye"></i></button>`;

    const row = `
            <tr>
                <td><strong>#DT${item.maDonThue}</strong></td>
                <td><a class="link-id btn-view-customer" data-id="${item.maKhachHang}">KH #${item.maKhachHang}</a></td>
                <td><a class="link-id btn-view-car" data-id="${item.maXeThue}">XE #${item.maXeThue}</a></td>
                <td>${ngayNhan} <br> <small style="color:gray;">đến ${ngayTra}</small></td>
                <td><strong style="color: #b45309;">${tongTien} đ</strong></td>
                <td><span class="badge ${getBadgeClass(item.trangThai)}">${formatStatus(item.trangThai)}</span></td>
                <td>${actionButtons}</td>
            </tr>`;
    tbody.innerHTML += row;
  });
}

// ============================================================================
// MODULE 3: BỘ LỌC TÌM KIẾM (FILTER)
// ============================================================================
function setupFilterListener() {
  const filterSelect = document.getElementById("filterStatus");
  if (!filterSelect) return;

  filterSelect.addEventListener("change", function (e) {
    const selectedStatus = e.target.value;
    if (selectedStatus === "ALL") {
      renderTable(allRentalsData);
    } else {
      const filteredData = allRentalsData.filter(
        (item) => item.trangThai === selectedStatus,
      );
      renderTable(filteredData);
    }
  });
}

// ============================================================================
// MODULE 4: ĐIỀU HƯỚNG SỰ KIỆN CLICK (EVENT DELEGATION) & MODAL
// ============================================================================
const modal = document.getElementById("infoModal");
const modalTitle = document.getElementById("modalTitle");
const modalBody = document.getElementById("modalBody");

document.querySelector(".close-modal").onclick = () =>
  (modal.style.display = "none");
window.onclick = (e) => {
  if (e.target == modal) modal.style.display = "none";
};

function openModal(title) {
  modalTitle.textContent = title;
  modalBody.innerHTML = `<div style="text-align:center; padding: 20px;"><i class="fas fa-spinner fa-spin"></i> Đang tải dữ liệu...</div>`;
  modal.style.display = "block";
}

function setupActionButtons() {
  const tableContainer = document.querySelector(".table-container");
  if (!tableContainer) return;

  // Lắng nghe toàn bộ click trong khu vực bảng
  tableContainer.addEventListener("click", (e) => {
    // Thao tác trực tiếp trên bảng
    if (e.target.closest(".btn-approve")) {
      const id = e.target.closest(".btn-approve").dataset.id;
      if (confirm(`XÁC NHẬN đơn thuê #${id}?`))
        updateOrderStatus(id, "DaXacNhan");
    }

    if (e.target.closest(".btn-reject")) {
      const id = e.target.closest(".btn-reject").dataset.id;
      if (confirm(`HỦY đơn thuê #${id}?`)) updateOrderStatus(id, "DaHuy");
    }

    // Mở Modal Xem chi tiết
    if (e.target.closest(".btn-view")) {
      const id = e.target.closest(".btn-view").dataset.id;
      openModal(`Chi tiết Đơn Thuê #${id}`);
      fetchOrderDetails(id);
    }

    // Mở Modal Thông tin Tham chiếu
    if (e.target.classList.contains("btn-view-customer")) {
      const khId = e.target.dataset.id;
      openModal(`Thông tin Khách hàng #${khId}`);
      fetchCustomerInfo(khId);
    }

    if (e.target.classList.contains("btn-view-car")) {
      const xeId = e.target.dataset.id;
      openModal(`Thông tin Xe cho thuê #${xeId}`);
      fetchCarInfo(xeId);
    }
  });
}

// ============================================================================
// MODULE 5: GỌI API CẬP NHẬT TRẠNG THÁI (ĐƯỢC GỌI TỪ BẢNG & MODAL)
// ============================================================================
async function updateOrderStatus(id, newStatus) {
  try {
    const response = await fetch(
      `http://localhost:8080/api/don-thue-xe/${id}/trang-thai?trangThai=${newStatus}`,
      {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
        },
      },
    );

    const result = await response.json();

    if (result.success) {
      alert("Cập nhật trạng thái đơn thuê thành công!");
      loadAllRentals(); // Tải lại dữ liệu (Bộ lọc vẫn được giữ nguyên qua hàm load)
    } else {
      alert("Lỗi: " + result.message);
    }
  } catch (error) {
    console.error("Lỗi cập nhật trạng thái:", error);
    alert("Không thể kết nối đến máy chủ!");
  }
}

// ============================================================================
// MODULE 6: FETCH API LẤY CHI TIẾT (ĐỔ VÀO MODAL)
// ============================================================================

// 6.1. Chi tiết Đơn Thuê (Tích hợp luôn 2 Nút Bàn Giao & Nhận Lại xe)
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
      const ngayNhan = new Date(don.ngayNhan).toLocaleDateString("vi-VN");
      const ngayTra = new Date(don.ngayTra).toLocaleDateString("vi-VN");
      const ngayDat = don.ngayDat
        ? new Date(don.ngayDat).toLocaleString("vi-VN")
        : "---";

      // Xử lý Nút thao tác nằm bên trong Modal
      let actionHtml = "";
      if (don.trangThai === "DaXacNhan") {
        actionHtml = `
                    <div style="margin-top: 20px; border-top: 1px solid #eee; padding-top: 15px; text-align: center;">
                        <button style="background: #2563eb; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; font-weight: 500;" 
                                onclick="if(confirm('Giao xe cho khách?')) { updateOrderStatus(${don.maDonThue}, 'DangThue'); document.getElementById('infoModal').style.display='none'; }">
                            <i class="fas fa-key"></i> Bàn giao xe cho khách
                        </button>
                    </div>`;
      } else if (don.trangThai === "DangThue") {
        actionHtml = `
                    <div style="margin-top: 20px; border-top: 1px solid #eee; padding-top: 15px; text-align: center;">
                        <button style="background: #16a34a; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; font-weight: 500;" 
                                onclick="if(confirm('Xác nhận nhận lại xe?')) { updateOrderStatus(${don.maDonThue}, 'DaTra'); document.getElementById('infoModal').style.display='none'; }">
                            <i class="fas fa-check-double"></i> Nhận lại xe (Hoàn thành)
                        </button>
                    </div>`;
      }

      modalBody.innerHTML = `
                <div class="order-details">
                    <div style="display: flex; justify-content: space-between; border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 15px;">
                        <span><strong>Ngày đặt:</strong> ${ngayDat}</span>
                        <span class="badge ${getBadgeClass(don.trangThai)}">${formatStatus(don.trangThai)}</span>
                    </div>
                    
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px; margin-bottom: 15px;">
                        <div style="background-color: #f8fafc; padding: 10px; border-radius: 6px;">
                            <p style="margin: 0 0 5px 0; color: #64748b; font-size: 13px;">THỜI GIAN THUÊ</p>
                            <p style="margin: 0;"><strong>Nhận:</strong> ${ngayNhan}</p>
                            <p style="margin: 0;"><strong>Trả:</strong> ${ngayTra}</p>
                        </div>
                        <div style="background-color: #f8fafc; padding: 10px; border-radius: 6px;">
                            <p style="margin: 0 0 5px 0; color: #64748b; font-size: 13px;">ĐỊA ĐIỂM GIAO NHẬN</p>
                            <p style="margin: 0;"><strong>Nhận:</strong> ${don.diaDiemNhan || "Gara Trung Tâm"}</p>
                            <p style="margin: 0;"><strong>Trả:</strong> ${don.diaDiemTra || "Gara Trung Tâm"}</p>
                        </div>
                    </div>
                    
                    <div style="background-color: #fffbeb; border: 1px solid #fde68a; padding: 15px; border-radius: 6px;">
                        <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                            <span>Tiền đặt cọc:</span>
                            <strong>${don.tienCoc ? don.tienCoc.toLocaleString() + " đ" : "0 đ"}</strong>
                        </div>
                        <div style="display: flex; justify-content: space-between; font-size: 18px; color: #b45309; border-top: 1px dashed #fcd34d; padding-top: 10px;">
                            <strong>TỔNG TIỀN:</strong>
                            <strong>${don.tongTien ? don.tongTien.toLocaleString() + " đ" : "0 đ"}</strong>
                        </div>
                    </div>
                    ${actionHtml}
                </div>`;
    } else {
      modalBody.innerHTML = `<p style="color:red; text-align:center;">${result.message || "Không tìm thấy thông tin đơn."}</p>`;
    }
  } catch (error) {
    modalBody.innerHTML = `<p style="color:red; text-align:center;">Lỗi kết nối đến máy chủ.</p>`;
  }
}

// 6.2. Thông tin Khách Hàng
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
      modalBody.innerHTML = `
                <div style="display: flex; flex-direction: column; gap: 10px;">
                    <div style="padding-bottom: 10px; border-bottom: 1px solid #eee;">
                        <h4 style="margin: 0; font-size: 16px;">${kh.hoTen || "Chưa cập nhật tên"}</h4>
                        <span style="color: gray; font-size: 14px;">${kh.email || "---"}</span>
                    </div>
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px;">
                        <p style="margin: 0;"><strong>Điện thoại:</strong><br>${kh.soDienThoai || "---"}</p>
                        <p style="margin: 0;"><strong>CCCD:</strong><br>${kh.cccd || "---"}</p>
                        <p style="margin: 0;"><strong>GPLX:</strong><br>${kh.soGplx || "---"} <span class="badge badge-info">${kh.hangGplx || "-"}</span></p>
                    </div>
                </div>`;
    } else {
      modalBody.innerHTML = `<p style="color:red; text-align:center;">Không tìm thấy thông tin.</p>`;
    }
  } catch (e) {
    modalBody.innerHTML = `<p style="color:red; text-align:center;">Lỗi lấy dữ liệu.</p>`;
  }
}

// 6.3. Thông tin Xe Cho Thuê
async function fetchCarInfo(id) {
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
      modalBody.innerHTML = `
                <div style="text-align: center;">
                    <h4 style="margin: 0 0 10px 0;">${xe.hangXe} ${xe.dongXe} (${xe.namSanXuat})</h4>
                    <span style="display:inline-block; padding:8px 15px; border:2px solid #1e293b; border-radius:4px; font-weight:bold; font-size: 18px; letter-spacing: 1px;">
                        ${xe.bienSo}
                    </span>
                    <div style="margin-top: 15px; font-size: 16px;">
                        Giá thuê tham khảo: <strong style="color:#ef4444;">${xe.giaTheoNgay ? xe.giaTheoNgay.toLocaleString() : 0} đ/ngày</strong>
                    </div>
                </div>`;
    } else {
      modalBody.innerHTML = `<p style="color:red; text-align:center;">Không tìm thấy thông tin.</p>`;
    }
  } catch (e) {
    modalBody.innerHTML = `<p style="color:red; text-align:center;">Lỗi kết nối máy chủ.</p>`;
  }
}

// ============================================================================
// MODULE 7: TIỆN ÍCH UI (BADGE & TEXT)
// ============================================================================
function getBadgeClass(status) {
  switch (status) {
    case "ChoDuyet":
      return "badge-warning";
    case "DaXacNhan":
      return "badge-success";
    case "DangThue":
      return "badge-info";
    case "DaHuy":
      return "badge-danger";
    case "DaTra":
      return "badge-secondary";
    default:
      return "";
  }
}

function formatStatus(status) {
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
