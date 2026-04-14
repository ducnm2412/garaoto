package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "phieu_sua_chua")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhieuSuaChua implements ThucTheThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_phieu_sua")
    private Integer maPhieuSua;

    @ManyToOne
    @JoinColumn(name = "ma_lich_hen")
    private com.example.garaoto.entity.LichHenSuaChua lichHenSuaChua;

    @ManyToOne
    @JoinColumn(name = "ma_xe_kh", nullable = false)
    private com.example.garaoto.entity.XeKhachHang xeKhachHang;

    @ManyToOne
    @JoinColumn(name = "ma_khach_hang", referencedColumnName = "ma_nguoi_dung", nullable = false)
    private com.example.garaoto.entity.KhachHang khachHang;

    @Column(name = "ngay_nhan_xe")
    private LocalDateTime ngayNhanXe;

    @Column(name = "ngay_hoan_thanh")
    private LocalDateTime ngayHoanThanh;

    @Column(name = "chan_doan", columnDefinition = "TEXT")
    private String chanDoan;

    @Setter(AccessLevel.NONE)
    @Column(name = "tong_tien", precision = 15, scale = 2)
    private BigDecimal tongTien;

    @Setter(AccessLevel.NONE)
    @Column(name = "trang_thai")
    private String trangThai;

    // --- Tính Đóng Gói (Encapsulation) ---

    /**
     * Chỉ cho phép cập nhật trạng thái thông qua method nội bộ,
     * có thể chèn thêm logic kiểm tra hợp lệ ở đây sau này.
     */
    public void capNhatTrangThai(String trangThaiMoi) {
        if (trangThaiMoi != null && !trangThaiMoi.trim().isEmpty()) {
            this.trangThai = trangThaiMoi;
            if ("DA_HOAN_THANH".equalsIgnoreCase(trangThaiMoi)) {
                this.ngayHoanThanh = LocalDateTime.now();
            }
        }
    }

    public void capNhatTongTien(BigDecimal tongTienMoi) {
        if (tongTienMoi != null && tongTienMoi.compareTo(BigDecimal.ZERO) >= 0) {
            this.tongTien = tongTienMoi;
        } else {
            throw new IllegalArgumentException("Tổng tiền không hợp lệ!");
        }
    }

    // --- Tính Đa Hình (Polymorphism) - Implements ThucTheThanhToan ---

    @Override
    public BigDecimal getSoTienThanhToan() {
        return this.tongTien;
    }

    @Override
    public String getMaThamChieu() {
        return "PSC_" + this.maPhieuSua;
    }

    @Override
    public String getLoaiThanhToan() {
        return "SUA_CHUA";
    }
}
