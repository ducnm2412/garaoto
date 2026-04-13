package com.example.garaoto.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHangRequest {

    @NotBlank(message = "CCCD không được để trống")
    private String cccd;

    @NotBlank(message = "Số GPLX không được để trống")
    private String soGplx;

    private String hangGplx;
}
