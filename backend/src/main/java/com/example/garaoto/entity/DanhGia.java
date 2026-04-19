package com.example.garaoto.entity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_khach_hang", referencedColumnName = "ma_nguoi_dung", nullable = false)
    private KhachHang khachHang;

    @Column(name = "loai_dich_vu", nullable = false)
    private String loaiDichVu; 

    @Column(name = "ma_tham_chieu", nullable = false)
    private Integer maThamChieu; 

    @Column(name = "so_sao", nullable = false)
    private Integer soSao; 

    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "ngay_danh_gia", nullable = false)
    private LocalDateTime ngayDanhGia;
}
