package com.example.garaoto.entity;

import com.example.garaoto.entity.NguoiDung;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_admin")
    private Integer maAdmin;

    @OneToOne
    @JoinColumn(name = "ma_nguoi_dung", nullable = false, unique = true)
    private NguoiDung nguoiDung;

    @Column(name = "chuc_vu")
    private String chucVu;
}