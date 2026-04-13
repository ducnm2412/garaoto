package com.example.garaoto.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhanVienKyThuatRequest {
    private String chuyenMon;
    private String caLamViec;
}
