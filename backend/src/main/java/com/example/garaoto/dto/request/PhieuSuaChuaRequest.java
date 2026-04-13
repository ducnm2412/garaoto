package com.example.garaoto.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhieuSuaChuaRequest {
    private Integer maLichHen;
    private Integer maXeKh;
    private Integer maNguoiDung;
    private String chanDoan;
    private BigDecimal tongTien;
    private String trangThai;
}
