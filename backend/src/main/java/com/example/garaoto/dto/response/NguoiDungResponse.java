package com.example.garaoto.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NguoiDungResponse {
    private Integer maNguoiDung;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String diaChi;
    private String vaiTro;
    private String trangThai;
    private LocalDateTime ngayTao;
}
