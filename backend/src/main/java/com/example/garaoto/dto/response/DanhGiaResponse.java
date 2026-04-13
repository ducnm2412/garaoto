package com.example.garaoto.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanhGiaResponse {
    private Integer maDanhGia;
    private Integer maNguoiDung;
    private String loaiDanhGia;
    private Integer maPhieuSua;
    private Integer maDonThue;
    private Integer soSao;
    private String noiDung;
    private LocalDateTime ngayDanhGia;
}
