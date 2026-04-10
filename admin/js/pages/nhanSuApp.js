/**
 * ============================================================================
 * FILE: nhanSuApp.js
 * CHỨC NĂNG: Quản lý Thợ máy với luồng Register nâng cao & Xem hiệu suất
 * ============================================================================
 */

let allThoMayData = [];

document.addEventListener("DOMContentLoaded", () => {
  // Kiểm tra quyền Admin
  if (
    !localStorage.getItem("isAdminLoggedIn") ||
    localStorage.getItem("adminRole") !== "Admin"
  ) {
    window.location.replace("admin-login.html");
    return;
  }

  const adminName = localStorage.getItem("adminName");
  if (adminName)
    document.querySelector(".admin-name").textContent =
      "Xin chào, " + adminName + "!";

  loadDanhSachThoMay();
  setupActionButtons();
});

// --- MODULE 1: TẢI VÀ HIỂN THỊ DỮ LIỆU ---
async function loadDanhSachThoMay() {
  const tbody = document.querySelector("#nhanVienTable tbody");
  tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;"><i class="fas fa-spinner fa-spin"></i> Đang tải dữ liệu...</td></tr>`;

  try {
    const response = await fetch(
      "http://localhost:8080/api/nhan-vien-ky-thuat",
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
        },
      },
    );
    const result = await response.json();

    if (result.success && result.data) {
      allThoMayData = result.data.reverse();
      renderTable(allThoMayData);
    } else {
      tbody.innerHTML = `<tr><td colspan="6" style="text-align:center; color:red;">${result.message}</td></tr>`;
    }
  } catch (error) {
    tbody.innerHTML = `<tr><td colspan="6" style="text-align:center; color:red;">Lỗi kết nối máy chủ.</td></tr>`;
  }
}

function renderTable(dataArray) {
  const tbody = document.querySelector("#nhanVienTable tbody");
  tbody.innerHTML = "";

  if (dataArray.length === 0) {
    tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;">Chưa có thợ máy nào.</td></tr>`;
    return;
  }

  dataArray.forEach((item) => {
    const row = `
            <tr>
                <td><strong>#NV${item.maNhanVien}</strong></td>
                <td><strong>${item.hoTen || "Chưa cập nhật"}</strong></td>
                <td>${item.soDienThoai || "---"}</td>
                <td><span class="badge badge-info">${item.chuyenMon || "Chung"}</span></td>
                <td>${item.caLamViec || "Hành chính"}</td>
                <td>
                    <button class="btn-action btn-view" data-id="${item.maNhanVien}" title="Sửa chuyên môn"><i class="fas fa-user-edit"></i></button>
                    <button class="btn-action" style="color: #3b82f6" onclick="viewWorkHistory(${item.maNhanVien})" title="Xem lịch sử làm việc"><i class="fas fa-history"></i></button>
                    <button class="btn-action btn-reject" onclick="deleteThoMay(${item.maNhanVien})" title="Xóa thợ máy"><i class="fas fa-trash-alt"></i></button>
                </td>
            </tr>`;
    tbody.innerHTML += row;
  });
}

// --- MODULE 2: TÍNH NĂNG CỘNG ĐIỂM - XEM HIỆU SUẤT ---
async function viewWorkHistory(maNhanVien) {
  const nv = allThoMayData.find((i) => i.maNhanVien == maNhanVien);
  const modal = document.getElementById("nhanVienModal");
  const modalTitle = document.getElementById("modalTitle");
  const modalBody = document.querySelector("#nhanVienModal .modal-body");
  const modalFooter = document.querySelector("#nhanVienModal .modal-footer");

  modalTitle.textContent = `Lịch sử làm việc của ${nv.hoTen}`;
  modalBody.innerHTML = `<div style="text-align:center"><i class="fas fa-spinner fa-spin"></i> Đang tải dữ liệu việc làm...</div>`;
  modalFooter.style.display = "none"; // Ẩn nút lưu
  modal.style.display = "block";

  try {
    const response = await fetch(
      `http://localhost:8080/api/phan-cong-sua-chua/nhan-vien/${maNhanVien}`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
        },
      },
    );
    const result = await response.json();

    if (result.success && result.data) {
      let listHtml = `<table style="width:100%; font-size:13px; border-collapse:collapse;">
                <thead style="background:#f1f5f9">
                    <tr><th style="padding:8px">Mã Phiếu</th><th style="padding:8px">Ngày giao</th><th style="padding:8px">Trạng thái</th></tr>
                </thead>
                <tbody>`;

      result.data.slice(0, 10).forEach((job) => {
        listHtml += `<tr style="border-bottom:1px solid #eee">
                    <td style="padding:8px">#${job.maPhieuSua}</td>
                    <td style="padding:8px">${new Date(job.ngayPhanCong).toLocaleDateString("vi-VN")}</td>
                    <td style="padding:8px"><span class="badge ${job.trangThai === "HoanThanh" ? "badge-success" : "badge-warning"}">${job.trangThai}</span></td>
                </tr>`;
      });

      listHtml += `</tbody></table>`;
      if (result.data.length === 0)
        listHtml = `<p style="text-align:center; color:gray">Chưa có công việc nào được bàn giao.</p>`;
      modalBody.innerHTML = listHtml;
    }
  } catch (e) {
    modalBody.innerHTML = `<p style="color:red">Không thể lấy dữ liệu lịch sử.</p>`;
  }
}

// --- MODULE 3: XỬ LÝ FORM VÀ API (REGISTER + UPDATE) ---
function setupActionButtons() {
  const modal = document.getElementById("nhanVienModal");
  const modalFooter = document.querySelector("#nhanVienModal .modal-footer");

  document.getElementById("btnAddNhanVien").onclick = () => {
    document.getElementById("modalTitle").textContent = "Thêm Thợ Máy Mới";
    document.getElementById("nhanVienForm").reset();
    document.getElementById("nhanVienId").value = "";
    modalFooter.style.display = "block";
    setFieldsReadOnly(false);
    modal.style.display = "block";
  };

  // Kiểm tra email tồn tại khi đang nhập (Tính năng cộng điểm)
  document.getElementById("nvEmail").addEventListener("blur", async (e) => {
    const email = e.target.value;
    if (email && !document.getElementById("nhanVienId").value) {
      try {
        const res = await fetch(
          `http://localhost:8080/api/nguoi-dung/email?email=${email}`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
            },
          },
        );
        if (res.ok) alert("⚠️ Cảnh báo: Email này đã tồn tại trên hệ thống!");
      } catch (err) {}
    }
  });

  document.querySelector(".close-modal").onclick = () =>
    (modal.style.display = "none");

  document.querySelector("#nhanVienTable").addEventListener("click", (e) => {
    const btnEdit = e.target.closest(".btn-view");
    if (btnEdit) {
      modalFooter.style.display = "block";
      openEditModal(btnEdit.dataset.id);
    }
  });

  document.getElementById("nhanVienForm").onsubmit = saveThoMay;
}

function setFieldsReadOnly(isReadOnly) {
  document.getElementById("nvHoTen").readOnly = isReadOnly;
  document.getElementById("nvDienThoai").readOnly = isReadOnly;
  document.getElementById("nvEmail").readOnly = isReadOnly;
}

function openEditModal(id) {
  const nv = allThoMayData.find((i) => i.maNhanVien == id);
  if (!nv) return;

  document.getElementById("modalTitle").textContent =
    `Sửa thông tin: ${nv.hoTen}`;
  document.getElementById("nhanVienId").value = nv.maNhanVien;
  document.getElementById("nvHoTen").value = nv.hoTen;
  document.getElementById("nvDienThoai").value = nv.soDienThoai;
  document.getElementById("nvEmail").value = nv.email || "";
  document.getElementById("nvChuyenMon").value = nv.chuyenMon || "";
  document.getElementById("nvCaLamViec").value = nv.caLamViec || "";

  setFieldsReadOnly(true);
  document.getElementById("nhanVienModal").style.display = "block";
}

async function saveThoMay(e) {
  e.preventDefault();
  const id = document.getElementById("nhanVienId").value;
  const isUpdate = id !== "";
  const token = localStorage.getItem("adminToken");

  try {
    if (isUpdate) {
      const res = await fetch(
        `http://localhost:8080/api/nhan-vien-ky-thuat/${id}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            chuyenMon: document.getElementById("nvChuyenMon").value,
            caLamViec: document.getElementById("nvCaLamViec").value,
          }),
        },
      );
      if (res.ok) {
        alert("Cập nhật thành công!");
        document.getElementById("nhanVienModal").style.display = "none";
        loadDanhSachThoMay();
      }
    } else {
      const resReg = await fetch("http://localhost:8080/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          hoTen: document.getElementById("nvHoTen").value,
          email: document.getElementById("nvEmail").value,
          soDienThoai: document.getElementById("nvDienThoai").value,
          matKhau: "123456",
          vaiTro: "NhanVienKyThuat",
        }),
      });
      const regResult = await resReg.json();

      if (regResult.success) {
        // Sau khi Register, gọi thêm API PUT để cập nhật chuyên môn ngay lập tức
        // Lấy mã thợ máy vừa được tạo (thường Backend trả về trong data)
        const maMoi = regResult.data.maNhanVien;
        if (maMoi) {
          await fetch(`http://localhost:8080/api/nhan-vien-ky-thuat/${maMoi}`, {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({
              chuyenMon: document.getElementById("nvChuyenMon").value,
              caLamViec: document.getElementById("nvCaLamViec").value,
            }),
          });
        }
        alert("Đã thêm thợ máy mới thành công!");
        document.getElementById("nhanVienModal").style.display = "none";
        loadDanhSachThoMay();
      } else alert("Lỗi: " + regResult.message);
    }
  } catch (error) {
    alert("Lỗi hệ thống.");
  }
}

async function deleteThoMay(id) {
  const nv = allThoMayData.find((i) => i.maNhanVien == id);
  const token = localStorage.getItem("adminToken");

  // 1. Cảnh báo xác nhận lần 1
  if (
    !confirm(
      `Bạn có chắc chắn muốn xóa thợ máy ${nv.hoTen}? Thao tác này không thể hoàn tác.`,
    )
  )
    return;

  try {
    // 2. BƯỚC KIỂM TRA XUNG ĐỘT (Tính năng cộng điểm)
    // Gọi API lấy danh sách phân công của thợ máy này
    const resCheck = await fetch(
      `http://localhost:8080/api/phan-cong-sua-chua/nhan-vien/${id}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    );
    const checkResult = await resCheck.json();

    if (checkResult.success && checkResult.data.length > 0) {
      // Kiểm tra xem có xe nào đang sửa (DangThucHien hoặc DaPhanCong) không
      const isBusy = checkResult.data.some(
        (job) => job.trangThai !== "HoanThanh",
      );

      if (isBusy) {
        alert(
          `❌ KHÔNG THỂ XÓA: Thợ máy ${nv.hoTen} đang có công việc chưa hoàn thành. Vui lòng bàn giao việc cho thợ khác trước!`,
        );
        return;
      }

      // Nếu chỉ là lịch sử cũ (đã HoanThanh), đưa ra cảnh báo lần 2
      if (
        !confirm(
          `Cảnh báo: Thợ máy này có ${checkResult.data.length} bản ghi lịch sử sửa chữa. Nếu xóa, các dữ liệu này có thể bị ảnh hưởng. Bạn vẫn muốn xóa chứ?`,
        )
      ) {
        return;
      }
    }

    // 3. THỰC HIỆN XÓA KHI ĐÃ AN TOÀN
    const resDelete = await fetch(
      `http://localhost:8080/api/nhan-vien-ky-thuat/${id}`,
      {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      },
    );

    if (resDelete.ok) {
      alert("✅ Đã xóa nhân viên và tài khoản liên quan thành công!");
      loadDanhSachThoMay();
    } else {
      const errorData = await resDelete.json();
      alert(
        "❌ Lỗi từ hệ thống: " +
          (errorData.message || "Không thể xóa do ràng buộc dữ liệu tầng sâu."),
      );
    }
  } catch (e) {
    console.error("Lỗi xóa:", e);
    alert("❌ Lỗi kết nối đến máy chủ khi thực hiện xóa.");
  }
}
