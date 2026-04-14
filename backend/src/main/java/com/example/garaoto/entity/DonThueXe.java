package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "don_thue_xe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonThueXe implements ThucTheThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_don_thue")
    private Integer maDonThue;

    @ManyToOne
    @JoinColumn(name = "ma_khach_hang", referencedColumnName = "ma_nguoi_dung", nullable = false)
    private com.example.garaoto.entity.KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "ma_xe_thue", nullable = false)
    private com.example.garaoto.entity.XeChoThue xeChoThue;

    @Column(name = "ngay_nhan", nullable = false)
    private LocalDate ngayNhan;

    @Column(name = "ngay_tra", nullable = false)
    private LocalDate ngayTra;

    @Column(name = "dia_diem_nhan")
    private String diaDiemNhan;

    @Column(name = "dia_diem_tra")
    private String diaDiemTra;

    @Column(name = "tien_coc", precision = 15, scale = 2)
    private BigDecimal tienCoc;

    @Setter(AccessLevel.NONE)
    @Column(name = "tong_tien", precision = 15, scale = 2)
    private BigDecimal tongTien;

    @Setter(AccessLevel.NONE)
    @Column(name = "trang_thai")
    private String trangThai;

    // --- Tính Đóng Gói (Encapsulation) ---

    public void chuyenTrangThai(String trangThaiMoi) {
        if (trangThaiMoi != null && !trangThaiMoi.trim().isEmpty()) {
            // Có thể thêm logic: từ "CHỜ NHẬN" chỉ có thể sang "ĐANG THUÊ" hoặc "ĐÃ HỦY"
            this.trangThai = trangThaiMoi;
        }
    }

    public void capNhatTongTien(BigDecimal tongTienMoi) {
        if (tongTienMoi != null && tongTienMoi.compareTo(BigDecimal.ZERO) >= 0) {
            this.tongTien = tongTienMoi;
        } else {
            throw new IllegalArgumentException("Tổng tiền thuê xe không hợp lệ!");
        }
    }

    // --- Tính Đa Hình (Polymorphism) - Implements ThucTheThanhToan ---

    @Override
    public BigDecimal getSoTienThanhToan() {
        return this.tongTien;
    }

    @Override
    public String getMaThamChieu() {
        return "DTX_" + this.maDonThue;
    }

    @Override
    public String getLoaiThanhToan() {
        return "THUE_XE";
    }

    @Column(name = "ngay_dat")
    private LocalDateTime ngayDat;
}
