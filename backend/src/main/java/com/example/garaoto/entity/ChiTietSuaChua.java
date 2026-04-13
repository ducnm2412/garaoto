package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "chi_tiet_sua_chua")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChiTietSuaChua {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_chi_tiet_sua")
    private Integer maChiTietSua;

    @ManyToOne
    @JoinColumn(name = "ma_phieu_sua", nullable = false)
    private com.example.garaoto.entity.PhieuSuaChua phieuSuaChua;

    @ManyToOne
    @JoinColumn(name = "ma_dich_vu", nullable = false)
    private com.example.garaoto.entity.DichVuSuaChua dichVuSuaChua;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "don_gia", precision = 15, scale = 2)
    private BigDecimal donGia;

    @Column(name = "thanh_tien", precision = 15, scale = 2)
    private BigDecimal thanhTien;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;
}
