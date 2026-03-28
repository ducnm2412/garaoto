package com.example.garaoto.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XeChoThueRequest {
    private String bienSo;
    private String hangXe;
    private String dongXe;
    private Integer soCho;
    private String hopSo;
    private String nhienLieu;
    private BigDecimal giaTheoNgay;
    private String tinhTrang;
    private String hinhAnh;
    private Integer namSanXuat;
}