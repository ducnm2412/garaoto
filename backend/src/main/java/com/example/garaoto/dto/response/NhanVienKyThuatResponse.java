package com.example.garaoto.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhanVienKyThuatResponse {
    private Integer maNguoiDung;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String chuyenMon;
    private String caLamViec;
}
