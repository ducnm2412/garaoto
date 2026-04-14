package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "xe_cho_thue")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@AttributeOverride(name = "bienSo", column = @Column(name = "bien_so", nullable = false, unique = true))
public class XeChoThue extends XeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_xe_thue")
    private Integer maXeThue;


    @Column(name = "so_cho")
    private Integer soCho;

    @Column(name = "hop_so")
    private String hopSo;

    @Column(name = "nhien_lieu")
    private String nhienLieu;

    @Column(name = "gia_theo_ngay", precision = 15, scale = 2)
    private BigDecimal giaTheoNgay;

    @Setter(AccessLevel.NONE)
    @Column(name = "tinh_trang")
    private String tinhTrang;

    public void capNhatTinhTrang(String tinhTrangMoi) {
        if (tinhTrangMoi != null && !tinhTrangMoi.trim().isEmpty()) {
            this.tinhTrang = tinhTrangMoi;
        }
    }

    @Column(name = "hinh_anh")
    private String hinhAnh;


}
