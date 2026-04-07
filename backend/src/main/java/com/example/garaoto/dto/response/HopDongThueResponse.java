package com.example.garaoto.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HopDongThueResponse {
    private Integer maHopDong;
    private Integer maDonThue;
    private LocalDateTime ngayLap;
    private String dieuKhoan;
    private String ghiChu;
    private String trangThai;
}