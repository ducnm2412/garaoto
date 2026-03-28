package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "khach_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_khach_hang")
    private Integer maKhachHang;

    @OneToOne
    @JoinColumn(name = "ma_nguoi_dung", nullable = false, unique = true)
    private NguoiDung nguoiDung;

    @Column(name = "cccd")
    private String cccd;

    @Column(name = "so_gplx")
    private String soGplx;

    @Column(name = "hang_gplx")
    private String hangGplx;
}