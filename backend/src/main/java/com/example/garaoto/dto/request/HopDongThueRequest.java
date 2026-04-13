package com.example.garaoto.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HopDongThueRequest {
    private Integer maDonThue;
    private String dieuKhoan;
    private String ghiChu;
    private String trangThai;
}
