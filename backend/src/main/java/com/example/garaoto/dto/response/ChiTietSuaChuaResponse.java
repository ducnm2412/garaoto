package com.example.garaoto.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChiTietSuaChuaResponse {
    private Integer maChiTietSua;
    private Integer maPhieuSua;
    private Integer maDichVu;
    private Integer soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String ghiChu;
}
