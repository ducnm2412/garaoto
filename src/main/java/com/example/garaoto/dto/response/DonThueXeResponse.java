package com.example.garaoto.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonThueXeResponse {
    private Integer maDonThue;
    private Integer maKhachHang;
    private Integer maXeThue;
    private LocalDate ngayNhan;
    private LocalDate ngayTra;
    private String diaDiemNhan;
    private String diaDiemTra;
    private BigDecimal tienCoc;
    private BigDecimal tongTien;
    private String trangThai;
    private LocalDateTime ngayDat;
}