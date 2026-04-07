package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "lich_hen_sua_chua")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichHenSuaChua {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_lich_hen")
    private Integer maLichHen;

    @ManyToOne
    @JoinColumn(name = "ma_khach_hang", nullable = false)
    private com.example.garaoto.entity.KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "ma_xe_kh", nullable = false)
    private com.example.garaoto.entity.XeKhachHang xeKhachHang;

    @Column(name = "ngay_hen", nullable = false)
    private LocalDate ngayHen;

    @Column(name = "gio_hen", nullable = false)
    private LocalTime gioHen;

    @Column(name = "mo_ta_loi", columnDefinition = "TEXT")
    private String moTaLoi;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;
}