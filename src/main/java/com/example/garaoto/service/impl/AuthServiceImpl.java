package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.LoginRequest;
import com.example.garaoto.dto.request.RegisterRequest;
import com.example.garaoto.dto.response.AuthResponse;
import com.example.garaoto.entity.Admin;
import com.example.garaoto.entity.KhachHang;
import com.example.garaoto.entity.NguoiDung;
import com.example.garaoto.entity.NhanVienKyThuat;
import com.example.garaoto.exception.BadRequestException;
import com.example.garaoto.exception.DuplicateResourceException;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.AdminRepository;
import com.example.garaoto.repository.KhachHangRepository;
import com.example.garaoto.repository.NguoiDungRepository;
import com.example.garaoto.repository.NhanVienKyThuatRepository;
import com.example.garaoto.security.JwtService;
import com.example.garaoto.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final NguoiDungRepository nguoiDungRepository;
    private final KhachHangRepository khachHangRepository;
    private final AdminRepository adminRepository;
    private final NhanVienKyThuatRepository nhanVienKyThuatRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (nguoiDungRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email đã tồn tại");
        }

        NguoiDung nguoiDung = NguoiDung.builder()
                .hoTen(request.getHoTen())
                .email(request.getEmail())
                .soDienThoai(request.getSoDienThoai())
                .matKhau(passwordEncoder.encode(request.getMatKhau()))
                .diaChi(request.getDiaChi())
                .vaiTro(request.getVaiTro())
                .trangThai("HoatDong")
                .ngayTao(LocalDateTime.now())
                .build();

        nguoiDung = nguoiDungRepository.save(nguoiDung);

        switch (request.getVaiTro()) {
            case "KhachHang" -> {
                KhachHang khachHang = KhachHang.builder()
                        .nguoiDung(nguoiDung)
                        .cccd(request.getCccd())
                        .soGplx(request.getSoGplx())
                        .hangGplx(request.getHangGplx())
                        .build();
                khachHangRepository.save(khachHang);
            }
            case "Admin" -> {
                Admin admin = Admin.builder()
                        .nguoiDung(nguoiDung)
                        .chucVu(request.getChucVu())
                        .build();
                adminRepository.save(admin);
            }
            case "NhanVienKyThuat" -> {
                NhanVienKyThuat nhanVien = NhanVienKyThuat.builder()
                        .nguoiDung(nguoiDung)
                        .chuyenMon(request.getChuyenMon())
                        .caLamViec(request.getCaLamViec())
                        .build();
                nhanVienKyThuatRepository.save(nhanVien);
            }
            default -> throw new BadRequestException("Vai trò không hợp lệ");
        }

        String token = jwtService.generateToken(nguoiDung.getEmail());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .maNguoiDung(nguoiDung.getMaNguoiDung())
                .hoTen(nguoiDung.getHoTen())
                .email(nguoiDung.getEmail())
                .vaiTro(nguoiDung.getVaiTro())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản"));

        if (!passwordEncoder.matches(request.getMatKhau(), nguoiDung.getMatKhau())) {
            throw new BadRequestException("Email hoặc mật khẩu không đúng");
        }

        String token = jwtService.generateToken(nguoiDung.getEmail());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .maNguoiDung(nguoiDung.getMaNguoiDung())
                .hoTen(nguoiDung.getHoTen())
                .email(nguoiDung.getEmail())
                .vaiTro(nguoiDung.getVaiTro())
                .build();
    }
}