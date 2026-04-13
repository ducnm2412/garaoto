package com.example.garaoto.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThanhToanResponse {
    private Integer maThanhToan;
    private Integer maNguoiDung;
    private String loaiThanhToan;
    private Integer maPhieuSua;
    private Integer maDonThue;
    private BigDecimal soTien;
    private String phuongThuc;
    private String trangThai;
    private LocalDateTime ngayThanhToan;
}
