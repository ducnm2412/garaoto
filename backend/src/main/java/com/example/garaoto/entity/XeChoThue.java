package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "xe_cho_thue")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XeChoThue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_xe_thue")
    private Integer maXeThue;

    @Column(name = "bien_so", nullable = false, unique = true)
    private String bienSo;

    @Column(name = "hang_xe")
    private String hangXe;

    @Column(name = "dong_xe")
    private String dongXe;

    @Column(name = "so_cho")
    private Integer soCho;

    @Column(name = "hop_so")
    private String hopSo;

    @Column(name = "nhien_lieu")
    private String nhienLieu;

    @Column(name = "gia_theo_ngay", precision = 15, scale = 2)
    private BigDecimal giaTheoNgay;

    @Column(name = "tinh_trang")
    private String tinhTrang;

    @Column(name = "hinh_anh")
    private String hinhAnh;

    @Column(name = "nam_san_xuat")
    private Integer namSanXuat;
}