package com.example.garaoto.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChiTietSuaChuaRequest {
    private Integer maPhieuSua;
    private Integer maDichVu;
    private Integer soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String ghiChu;
}
