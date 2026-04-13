package com.example.garaoto.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XeKhachHangRequest {

    @NotNull(message = "Mã khách hàng không được để trống")
    private Integer maNguoiDung;

    @NotBlank(message = "Biển số không được để trống")
    private String bienSo;

    private String hangXe;
    private String dongXe;
    private Integer namSanXuat;
    private String mauSac;
    private String soKhung;
    private String soMay;
}
