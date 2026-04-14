package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@PrimaryKeyJoinColumn(name = "ma_nguoi_dung")
public class Admin extends NguoiDung {

    @Column(name = "chuc_vu")
    private String chucVu;

    @Override
    public String getGioiThieuCongViec() {
        return "[ADMIN] Tôi là Bán Giám Đốc/Quản Lý tên " + this.getHoTen() + " - Chức vụ: " + this.chucVu;
    }

    @Override
    public java.util.Map<String, Object> layChiTietRieng() {
        return java.util.Map.of("chucVu", this.chucVu != null ? this.chucVu : "");
    }
}
