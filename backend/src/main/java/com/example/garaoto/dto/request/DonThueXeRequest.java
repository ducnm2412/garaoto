package com.example.garaoto.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonThueXeRequest {

    @NotNull(message = "Mã khách hàng không được để trống")
    private Integer maKhachHang;

    @NotNull(message = "Mã xe thuê không được để trống")
    private Integer maXeThue;

    @NotNull(message = "Ngày nhận không được để trống")
    private LocalDate ngayNhan;

    @NotNull(message = "Ngày trả không được để trống")
    private LocalDate ngayTra;

    private String diaDiemNhan;
    private String diaDiemTra;
}