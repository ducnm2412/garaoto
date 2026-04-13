package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "nguoi_dung")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_nguoi_dung")
    private Integer maNguoiDung;

    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "vai_tro", nullable = false)
    private String vaiTro;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    // 1. TRỰC QUAN HÓA ĐA HÌNH (Ghi đè phương thức có sẵn - Virtual Polymorphism):
    // Lớp cha CÓ phần thân mặc định. Các lớp con có thể chọn "Ghi đè" (Override) lại hoặc lấy xài luôn.
    public String getGioiThieuCongViec() {
        return "Tôi là người dùng chung của hệ thống GaraOto tên là: " + this.hoTen;
    }

    // 2. TRỰC QUAN HÓA TRỪU TƯỢNG (Abstraction / Abstract Polymorphism):
    // Lớp cha CHỈ CÓ CÁI VỎ, KHÔNG CÓ THÂN. Ép buộc 100% lớp con phải định nghĩa mã nguồn.
    public abstract java.util.Map<String, Object> layChiTietRieng();
}
