package com.example.garaoto.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichHenSuaChuaRequest {

    @NotNull(message = "Mã khách hàng không được để trống")
    private Integer maKhachHang;

    @NotNull(message = "Mã xe khách hàng không được để trống")
    private Integer maXeKh;

    @NotNull(message = "Ngày hẹn không được để trống")
    private LocalDate ngayHen;

    @NotNull(message = "Giờ hẹn không được để trống")
    private LocalTime gioHen;

    private String moTaLoi;
}