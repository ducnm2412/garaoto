package com.example.garaoto.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHangResponse {
    private Integer maNguoiDung;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String cccd;
    private String soGplx;
    private String hangGplx;
}
