package com.example.garaoto.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichHenSuaChuaResponse {
    private Integer maLichHen;
    private Integer maNguoiDung;
    private Integer maXeKh;
    private LocalDate ngayHen;
    private LocalTime gioHen;
    private String moTaLoi;
    private String trangThai;
    private String tenKhachHang;
    private LocalDateTime ngayTao;
}
