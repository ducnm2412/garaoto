package com.example.garaoto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class XeBase {

    @Column(name = "bien_so", nullable = false)
    private String bienSo;

    @Column(name = "hang_xe")
    private String hangXe;

    @Column(name = "dong_xe")
    private String dongXe;

    @Column(name = "nam_san_xuat")
    private Integer namSanXuat;

}
