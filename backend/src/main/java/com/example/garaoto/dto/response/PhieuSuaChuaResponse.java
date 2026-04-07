package com.example.garaoto.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhieuSuaChuaResponse {
    private Integer maPhieuSua;
    private Integer maLichHen;
    private Integer maXeKh;
    private Integer maKhachHang;
    private LocalDateTime ngayNhanXe;
    private LocalDateTime ngayHoanThanh;
    private String chanDoan;
    private BigDecimal tongTien;
    private String trangThai;
}