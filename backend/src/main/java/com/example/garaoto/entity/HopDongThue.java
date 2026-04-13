package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "hop_dong_thue")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HopDongThue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_hop_dong")
    private Integer maHopDong;

    @OneToOne
    @JoinColumn(name = "ma_don_thue", nullable = false, unique = true)
    private com.example.garaoto.entity.DonThueXe donThueXe;

    @Column(name = "ngay_lap")
    private LocalDateTime ngayLap;

    @Column(name = "dieu_khoan", columnDefinition = "TEXT")
    private String dieuKhoan;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "trang_thai")
    private String trangThai;
}
