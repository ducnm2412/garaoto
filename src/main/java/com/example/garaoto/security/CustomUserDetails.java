package com.example.garaoto.security;

import com.example.garaoto.entity.NguoiDung;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final NguoiDung nguoiDung;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + nguoiDung.getVaiTro()));
    }

    @Override
    public String getPassword() {
        return nguoiDung.getMatKhau();
    }

    @Override
    public String getUsername() {
        return nguoiDung.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"Khoa".equalsIgnoreCase(nguoiDung.getTrangThai());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "HoatDong".equalsIgnoreCase(nguoiDung.getTrangThai()) || nguoiDung.getTrangThai() == null;
    }

    public Integer getMaNguoiDung() {
        return nguoiDung.getMaNguoiDung();
    }

    public String getHoTen() {
        return nguoiDung.getHoTen();
    }

    public String getVaiTro() {
        return nguoiDung.getVaiTro();
    }
}