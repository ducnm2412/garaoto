package com.example.garaoto.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XeChoThueResponse {
    private Integer maXeThue;
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