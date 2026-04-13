package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "dich_vu_sua_chua")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DichVuSuaChua {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_dich_vu")
    private Integer maDichVu;

    @Column(name = "ten_dich_vu", nullable = false)
    private String tenDichVu;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "gia_co_ban", precision = 15, scale = 2)
    private BigDecimal giaCoBan;

    @Column(name = "trang_thai")
    private String trangThai;
}
