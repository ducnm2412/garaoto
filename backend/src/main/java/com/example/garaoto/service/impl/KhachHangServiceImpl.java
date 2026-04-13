package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.KhachHangRequest;
import com.example.garaoto.dto.response.KhachHangResponse;
import com.example.garaoto.entity.KhachHang;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.KhachHangRepository;
import com.example.garaoto.service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KhachHangServiceImpl implements KhachHangService {

    private final KhachHangRepository khachHangRepository;

    @Override
    public List<KhachHangResponse> getAll() {
        return khachHangRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public KhachHangResponse getById(Integer id) {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));
        return mapToResponse(khachHang);
    }

    @Override
    public KhachHangResponse getByNguoiDungId(Integer maNguoiDung) {
        KhachHang khachHang = khachHangRepository.findById(maNguoiDung)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));
        return mapToResponse(khachHang);
    }

    @Override
    public KhachHangResponse update(Integer id, KhachHangRequest request) {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

        khachHang.setCccd(request.getCccd());
        khachHang.setSoGplx(request.getSoGplx());
        khachHang.setHangGplx(request.getHangGplx());

        return mapToResponse(khachHangRepository.save(khachHang));
    }

    @Override
    public void delete(Integer id) {
        if (!khachHangRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy khách hàng");
        }
        khachHangRepository.deleteById(id);
    }

    private KhachHangResponse mapToResponse(KhachHang khachHang) {
        return KhachHangResponse.builder()
                .maNguoiDung(khachHang.getMaNguoiDung())
                .hoTen(khachHang.getHoTen())
                .email(khachHang.getEmail())
                .soDienThoai(khachHang.getSoDienThoai())
                .cccd(khachHang.getCccd())
                .soGplx(khachHang.getSoGplx())
                .hangGplx(khachHang.getHangGplx())
                .build();
    }
}
