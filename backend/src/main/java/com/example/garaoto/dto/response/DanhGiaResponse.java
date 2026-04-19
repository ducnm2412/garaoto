package com.example.garaoto.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DanhGiaResponse {
    private Integer maDanhGia;
    private Integer maNguoiDung;
    private String tenKhachHang;
    private String loaiDichVu;
    private Integer maThamChieu;
    private Integer soSao;
    private String noiDung;
    private LocalDateTime ngayDanhGia;
}
