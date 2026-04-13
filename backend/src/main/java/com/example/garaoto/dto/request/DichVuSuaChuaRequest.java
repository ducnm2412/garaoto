package com.example.garaoto.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DichVuSuaChuaRequest {

    @NotBlank(message = "Tên dịch vụ không được để trống")
    private String tenDichVu;

    private String moTa;
    private BigDecimal giaCoBan;
    private String trangThai;
}
