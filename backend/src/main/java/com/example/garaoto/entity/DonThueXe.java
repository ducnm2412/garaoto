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
public class DonThueXe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_don_thue")
    private Integer maDonThue;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung", nullable = false)
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

    @Column(name = "tong_tien", precision = 15, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "ngay_dat")
    private LocalDateTime ngayDat;
}
