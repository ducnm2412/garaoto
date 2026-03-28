package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.ThanhToanRequest;
import com.example.garaoto.dto.response.ThanhToanResponse;
import com.example.garaoto.entity.DonThueXe;
import com.example.garaoto.entity.KhachHang;
import com.example.garaoto.entity.PhieuSuaChua;
import com.example.garaoto.entity.ThanhToan;
import com.example.garaoto.exception.BadRequestException;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.DonThueXeRepository;
import com.example.garaoto.repository.KhachHangRepository;
import com.example.garaoto.repository.PhieuSuaChuaRepository;
import com.example.garaoto.repository.ThanhToanRepository;
import com.example.garaoto.service.ThanhToanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThanhToanServiceImpl implements ThanhToanService {

    private final ThanhToanRepository thanhToanRepository;
    private final KhachHangRepository khachHangRepository;
    private final PhieuSuaChuaRepository phieuSuaChuaRepository;
    private final DonThueXeRepository donThueXeRepository;

    @Override
    public ThanhToanResponse create(ThanhToanRequest request) {
        KhachHang khachHang = khachHangRepository.findById(request.getMaKhachHang())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

        PhieuSuaChua phieuSuaChua = null;
        DonThueXe donThueXe = null;

        if ("SuaChua".equals(request.getLoaiThanhToan())) {
            if (request.getMaPhieuSua() == null) {
                throw new BadRequestException("Thiếu mã phiếu sửa");
            }
            phieuSuaChua = phieuSuaChuaRepository.findById(request.getMaPhieuSua())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu sửa"));
        } else if ("ThueXe".equals(request.getLoaiThanhToan())) {
            if (request.getMaDonThue() == null) {
                throw new BadRequestException("Thiếu mã đơn thuê");
            }
            donThueXe = donThueXeRepository.findById(request.getMaDonThue())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn thuê"));
        } else {
            throw new BadRequestException("Loại thanh toán không hợp lệ");
        }

        ThanhToan thanhToan = ThanhToan.builder()
                .khachHang(khachHang)
                .loaiThanhToan(request.getLoaiThanhToan())
                .phieuSuaChua(phieuSuaChua)
                .donThueXe(donThueXe)
                .soTien(request.getSoTien())
                .phuongThuc(request.getPhuongThuc())
                .trangThai(request.getTrangThai() != null ? request.getTrangThai() : "DaThanhToan")
                .ngayThanhToan(LocalDateTime.now())
                .build();

        thanhToan = thanhToanRepository.save(thanhToan);
        return mapToResponse(thanhToan);
    }

    @Override
    public List<ThanhToanResponse> getAll() {
        return thanhToanRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ThanhToanResponse getById(Integer id) {
        ThanhToan thanhToan = thanhToanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thanh toán"));
        return mapToResponse(thanhToan);
    }

    @Override
    public List<ThanhToanResponse> getByKhachHang(Integer maKhachHang) {
        return thanhToanRepository.findByKhachHang_MaKhachHang(maKhachHang)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ThanhToanResponse mapToResponse(ThanhToan thanhToan) {
        return ThanhToanResponse.builder()
                .maThanhToan(thanhToan.getMaThanhToan())
                .maKhachHang(thanhToan.getKhachHang().getMaKhachHang())
                .loaiThanhToan(thanhToan.getLoaiThanhToan())
                .maPhieuSua(thanhToan.getPhieuSuaChua() != null ? thanhToan.getPhieuSuaChua().getMaPhieuSua() : null)
                .maDonThue(thanhToan.getDonThueXe() != null ? thanhToan.getDonThueXe().getMaDonThue() : null)
                .soTien(thanhToan.getSoTien())
                .phuongThuc(thanhToan.getPhuongThuc())
                .trangThai(thanhToan.getTrangThai())
                .ngayThanhToan(thanhToan.getNgayThanhToan())
                .build();
    }
}