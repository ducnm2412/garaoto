package com.example.garaoto.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThanhToanRequest {

    @NotNull(message = "Mã khách hàng không được để trống")
    private Integer maNguoiDung;

    @NotNull(message = "Loại thanh toán không được để trống")
    private String loaiThanhToan;

    private Integer maPhieuSua;
    private Integer maDonThue;

    @NotNull(message = "Số tiền không được để trống")
    private BigDecimal soTien;

    private String phuongThuc;
    private String trangThai;
}
