package com.example.garaoto.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XeKhachHangResponse {
    private Integer maXeKh;
    private Integer maNguoiDung;
    private String bienSo;
    private String hangXe;
    private String dongXe;
    private Integer namSanXuat;
    private String mauSac;
    private String soKhung;
    private String soMay;
}
