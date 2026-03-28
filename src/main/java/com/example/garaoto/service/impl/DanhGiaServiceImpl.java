package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.DanhGiaRequest;
import com.example.garaoto.dto.response.DanhGiaResponse;
import com.example.garaoto.entity.DanhGia;
import com.example.garaoto.entity.DonThueXe;
import com.example.garaoto.entity.KhachHang;
import com.example.garaoto.entity.PhieuSuaChua;
import com.example.garaoto.exception.BadRequestException;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.DanhGiaRepository;
import com.example.garaoto.repository.DonThueXeRepository;
import com.example.garaoto.repository.KhachHangRepository;
import com.example.garaoto.repository.PhieuSuaChuaRepository;
import com.example.garaoto.service.DanhGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DanhGiaServiceImpl implements DanhGiaService {

    private final DanhGiaRepository danhGiaRepository;
    private final KhachHangRepository khachHangRepository;
    private final PhieuSuaChuaRepository phieuSuaChuaRepository;
    private final DonThueXeRepository donThueXeRepository;

    @Override
    public DanhGiaResponse create(DanhGiaRequest request) {
        KhachHang khachHang = khachHangRepository.findById(request.getMaKhachHang())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

        PhieuSuaChua phieuSuaChua = null;
        DonThueXe donThueXe = null;

        if ("SuaChua".equals(request.getLoaiDanhGia())) {
            if (request.getMaPhieuSua() == null) {
                throw new BadRequestException("Thiếu mã phiếu sửa");
            }
            phieuSuaChua = phieuSuaChuaRepository.findById(request.getMaPhieuSua())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu sửa"));
        } else if ("ThueXe".equals(request.getLoaiDanhGia())) {
            if (request.getMaDonThue() == null) {
                throw new BadRequestException("Thiếu mã đơn thuê");
            }
            donThueXe = donThueXeRepository.findById(request.getMaDonThue())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn thuê"));
        } else {
            throw new BadRequestException("Loại đánh giá không hợp lệ");
        }

        DanhGia danhGia = DanhGia.builder()
                .khachHang(khachHang)
                .loaiDanhGia(request.getLoaiDanhGia())
                .phieuSuaChua(phieuSuaChua)
                .donThueXe(donThueXe)
                .soSao(request.getSoSao())
                .noiDung(request.getNoiDung())
                .ngayDanhGia(LocalDateTime.now())
                .build();

        danhGia = danhGiaRepository.save(danhGia);
        return mapToResponse(danhGia);
    }

    @Override
    public List<DanhGiaResponse> getAll() {
        return danhGiaRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<DanhGiaResponse> getByKhachHang(Integer maKhachHang) {
        return danhGiaRepository.findByKhachHang_MaKhachHang(maKhachHang)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private DanhGiaResponse mapToResponse(DanhGia danhGia) {
        return DanhGiaResponse.builder()
                .maDanhGia(danhGia.getMaDanhGia())
                .maKhachHang(danhGia.getKhachHang().getMaKhachHang())
                .loaiDanhGia(danhGia.getLoaiDanhGia())
                .maPhieuSua(danhGia.getPhieuSuaChua() != null ? danhGia.getPhieuSuaChua().getMaPhieuSua() : null)
                .maDonThue(danhGia.getDonThueXe() != null ? danhGia.getDonThueXe().getMaDonThue() : null)
                .soSao(danhGia.getSoSao())
                .noiDung(danhGia.getNoiDung())
                .ngayDanhGia(danhGia.getNgayDanhGia())
                .build();
    }
}