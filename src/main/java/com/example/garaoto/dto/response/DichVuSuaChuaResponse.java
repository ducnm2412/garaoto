package com.example.garaoto.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DichVuSuaChuaResponse {
    private Integer maDichVu;
    private String tenDichVu;
    private String moTa;
    private BigDecimal giaCoBan;
    private String trangThai;
}