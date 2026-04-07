package com.example.garaoto.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhanCongSuaChuaResponse {
    private Integer maPhanCong;
    private Integer maPhieuSua;
    private Integer maNhanVien;
    private Integer maAdmin;
    private LocalDateTime ngayPhanCong;
    private String ghiChu;
    private String trangThai;
}