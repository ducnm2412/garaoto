package com.example.garaoto.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhanCongSuaChuaRequest {

    @NotNull(message = "Mã phiếu sửa không được để trống")
    private Integer maPhieuSua;

    @NotNull(message = "Mã nhân viên không được để trống")
    private Integer maNhanVien;

    @NotNull(message = "Mã admin không được để trống")
    private Integer maAdmin;

    private String ghiChu;
    private String trangThai;
}