package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.ChiTietSuaChuaRequest;
import com.example.garaoto.dto.response.ChiTietSuaChuaResponse;
import com.example.garaoto.entity.ChiTietSuaChua;
import com.example.garaoto.entity.DichVuSuaChua;
import com.example.garaoto.entity.PhieuSuaChua;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.ChiTietSuaChuaRepository;
import com.example.garaoto.repository.DichVuSuaChuaRepository;
import com.example.garaoto.repository.PhieuSuaChuaRepository;
import com.example.garaoto.service.ChiTietSuaChuaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChiTietSuaChuaServiceImpl implements ChiTietSuaChuaService {

    private final ChiTietSuaChuaRepository chiTietSuaChuaRepository;
    private final PhieuSuaChuaRepository phieuSuaChuaRepository;
    private final DichVuSuaChuaRepository dichVuSuaChuaRepository;

    @Override
    public ChiTietSuaChuaResponse create(ChiTietSuaChuaRequest request) {
        PhieuSuaChua phieuSua = phieuSuaChuaRepository.findById(request.getMaPhieuSua())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu sửa"));

        DichVuSuaChua dichVu = dichVuSuaChuaRepository.findById(request.getMaDichVu())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dịch vụ sửa chữa"));

        BigDecimal donGia = request.getDonGia() != null ? request.getDonGia() : dichVu.getGiaCoBan();
        Integer soLuong = request.getSoLuong() != null ? request.getSoLuong() : 1;
        BigDecimal thanhTien = request.getThanhTien() != null
                ? request.getThanhTien()
                : donGia.multiply(BigDecimal.valueOf(soLuong));

        ChiTietSuaChua chiTiet = ChiTietSuaChua.builder()
                .phieuSuaChua(phieuSua)
                .dichVuSuaChua(dichVu)
                .soLuong(soLuong)
                .donGia(donGia)
                .thanhTien(thanhTien)
                .ghiChu(request.getGhiChu())
                .build();

        return mapToResponse(chiTietSuaChuaRepository.save(chiTiet));
    }

    @Override
    public List<ChiTietSuaChuaResponse> getByPhieuSua(Integer maPhieuSua) {
        return chiTietSuaChuaRepository.findByPhieuSuaChua_MaPhieuSua(maPhieuSua)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void delete(Integer id) {
        if (!chiTietSuaChuaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy chi tiết sửa chữa");
        }
        chiTietSuaChuaRepository.deleteById(id);
    }

    private ChiTietSuaChuaResponse mapToResponse(ChiTietSuaChua chiTiet) {
        return ChiTietSuaChuaResponse.builder()
                .maChiTietSua(chiTiet.getMaChiTietSua())
                .maPhieuSua(chiTiet.getPhieuSuaChua().getMaPhieuSua())
                .maDichVu(chiTiet.getDichVuSuaChua().getMaDichVu())
                .soLuong(chiTiet.getSoLuong())
                .donGia(chiTiet.getDonGia())
                .thanhTien(chiTiet.getThanhTien())
                .ghiChu(chiTiet.getGhiChu())
                .build();
    }
}
