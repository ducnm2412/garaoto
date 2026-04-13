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
public class PhieuSuaChua {

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
    @JoinColumn(name = "ma_nguoi_dung", nullable = false)
    private com.example.garaoto.entity.KhachHang khachHang;

    @Column(name = "ngay_nhan_xe")
    private LocalDateTime ngayNhanXe;

    @Column(name = "ngay_hoan_thanh")
    private LocalDateTime ngayHoanThanh;

    @Column(name = "chan_doan", columnDefinition = "TEXT")
    private String chanDoan;

    @Column(name = "tong_tien", precision = 15, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "trang_thai")
    private String trangThai;
}
