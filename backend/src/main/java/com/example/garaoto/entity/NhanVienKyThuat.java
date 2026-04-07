package com.example.garaoto.entity;

import com.example.garaoto.entity.NguoiDung;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nhan_vien_ky_thuat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhanVienKyThuat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_nhan_vien")
    private Integer maNhanVien;

    @OneToOne
    @JoinColumn(name = "ma_nguoi_dung", nullable = false, unique = true)
    private NguoiDung nguoiDung;

    @Column(name = "chuyen_mon")
    private String chuyenMon;

    @Column(name = "ca_lam_viec")
    private String caLamViec;
}