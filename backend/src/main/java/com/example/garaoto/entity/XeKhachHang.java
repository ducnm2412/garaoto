package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;

import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "xe_khach_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class XeKhachHang extends XeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_xe_kh")
    private Integer maXeKh;

    @ManyToOne
    @JoinColumn(name = "ma_khach_hang", referencedColumnName = "ma_nguoi_dung", nullable = false)
    private com.example.garaoto.entity.KhachHang khachHang;



    @Column(name = "mau_sac")
    private String mauSac;

    @Column(name = "so_khung")
    private String soKhung;

    @Column(name = "so_may")
    private String soMay;
}
