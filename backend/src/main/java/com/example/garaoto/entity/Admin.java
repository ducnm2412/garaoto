package com.example.garaoto.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@PrimaryKeyJoinColumn(name = "ma_nguoi_dung")
public class Admin extends NguoiDung {

    @Column(name = "chuc_vu")
    private String chucVu;
}
