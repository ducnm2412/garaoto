package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "xe_khach_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XeKhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_xe_kh")
    private Integer maXeKh;

    @ManyToOne
    @JoinColumn(name = "ma_khach_hang", nullable = false)
    private com.example.garaoto.entity.KhachHang khachHang;

    @Column(name = "bien_so", nullable = false)
    private String bienSo;

    @Column(name = "hang_xe")
    private String hangXe;

    @Column(name = "dong_xe")
    private String dongXe;

    @Column(name = "nam_san_xuat")
    private Integer namSanXuat;

    @Column(name = "mau_sac")
    private String mauSac;

    @Column(name = "so_khung")
    private String soKhung;

    @Column(name = "so_may")
    private String soMay;
}