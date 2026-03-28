package com.example.garaoto.entity;

import com.example.garaoto.entity.DonThueXe;
import com.example.garaoto.entity.KhachHang;
import com.example.garaoto.entity.PhieuSuaChua;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "danh_gia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanhGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_danh_gia")
    private Integer maDanhGia;

    @ManyToOne
    @JoinColumn(name = "ma_khach_hang", nullable = false)
    private KhachHang khachHang;

    @Column(name = "loai_danh_gia", length = 50)
    private String loaiDanhGia;

    @ManyToOne
    @JoinColumn(name = "ma_phieu_sua")
    private PhieuSuaChua phieuSuaChua;

    @ManyToOne
    @JoinColumn(name = "ma_don_thue")
    private DonThueXe donThueXe;

    @Column(name = "so_sao")
    private Integer soSao;

    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "ngay_danh_gia")
    private LocalDateTime ngayDanhGia;
}