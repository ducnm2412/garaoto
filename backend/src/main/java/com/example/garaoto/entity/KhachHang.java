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

    @Override
    public String getGioiThieuCongViec() {
        return "[KHÁCH] Tôi là Khách Hàng tên " + this.getHoTen() + " - Đang sở hữu Bằng Lái hạng: " + this.hangGplx;
    }

    @Override
    public java.util.Map<String, Object> layChiTietRieng() {
        return java.util.Map.of(
            "cccd", this.cccd != null ? this.cccd : "",
            "soGplx", this.soGplx != null ? this.soGplx : "",
            "hangGplx", this.hangGplx != null ? this.hangGplx : ""
        );
    }
}
