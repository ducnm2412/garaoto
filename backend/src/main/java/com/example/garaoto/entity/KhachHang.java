package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "khach_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@PrimaryKeyJoinColumn(name = "ma_nguoi_dung")
public class KhachHang extends NguoiDung {

    @Column(name = "cccd")
    private String cccd;

    @Column(name = "so_gplx")
    private String soGplx;

    @Column(name = "hang_gplx")
    private String hangGplx;
}
