package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "phan_cong_sua_chua")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhanCongSuaChua {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_phan_cong")
    private Integer maPhanCong;

    @ManyToOne
    @JoinColumn(name = "ma_phieu_sua", nullable = false)
    private com.example.garaoto.entity.PhieuSuaChua phieuSuaChua;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung_nv", nullable = false)
    private com.example.garaoto.entity.NhanVienKyThuat nhanVienKyThuat;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung_admin", nullable = false)
    private com.example.garaoto.entity.Admin admin;

    @Column(name = "ngay_phan_cong")
    private LocalDateTime ngayPhanCong;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "trang_thai")
    private String trangThai;
}
