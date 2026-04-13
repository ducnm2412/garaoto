package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "nhan_vien_ky_thuat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@PrimaryKeyJoinColumn(name = "ma_nguoi_dung")
public class NhanVienKyThuat extends NguoiDung {

    @Column(name = "chuyen_mon")
    private String chuyenMon;

    @Column(name = "ca_lam_viec")
    private String caLamViec;
}
