package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "thanh_toan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_thanh_toan")
    private Integer maThanhToan;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung", nullable = false)
    private com.example.garaoto.entity.KhachHang khachHang;

    @Column(name = "loai_thanh_toan")
    private String loaiThanhToan;

    @ManyToOne
    @JoinColumn(name = "ma_phieu_sua")
    private com.example.garaoto.entity.PhieuSuaChua phieuSuaChua;

    @ManyToOne
    @JoinColumn(name = "ma_don_thue")
    private com.example.garaoto.entity.DonThueXe donThueXe;

    @Column(name = "so_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal soTien;

    @Column(name = "phuong_thuc")
    private String phuongThuc;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "ngay_thanh_toan")
    private LocalDateTime ngayThanhToan;
}
