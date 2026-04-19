package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.DanhGiaRequest;
import com.example.garaoto.dto.response.DanhGiaResponse;
import com.example.garaoto.entity.DanhGia;
import com.example.garaoto.entity.KhachHang;
import com.example.garaoto.exception.BadRequestException;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.DanhGiaRepository;
import com.example.garaoto.repository.KhachHangRepository;
import com.example.garaoto.service.DanhGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DanhGiaServiceImpl implements DanhGiaService {

    private final DanhGiaRepository danhGiaRepository;
    private final KhachHangRepository khachHangRepository;

    @Override
    public DanhGiaResponse create(DanhGiaRequest request) {
        if (danhGiaRepository.existsByLoaiDichVuAndMaThamChieu(request.getLoaiDichVu(), request.getMaThamChieu())) {
            throw new BadRequestException("Bạn đã đánh giá dịch vụ này rồi!");
        }

        KhachHang khachHang = khachHangRepository.findById(request.getMaNguoiDung())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

        DanhGia danhGia = DanhGia.builder()
                .khachHang(khachHang)
                .loaiDichVu(request.getLoaiDichVu())
                .maThamChieu(request.getMaThamChieu())
                .soSao(request.getSoSao())
                .noiDung(request.getNoiDung())
                .ngayDanhGia(LocalDateTime.now())
                .build();

        danhGia = danhGiaRepository.save(danhGia);
        return mapToResponse(danhGia);
    }

    @Override
    public List<DanhGiaResponse> getAll() {
        return danhGiaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DanhGiaResponse> getByKhachHang(Integer maNguoiDung) {
        return danhGiaRepository.findByKhachHang_MaNguoiDung(maNguoiDung).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DanhGiaResponse getByThuThieu(String loaiDichVu, Integer maThamChieu) {
        DanhGia danhGia = danhGiaRepository.findByLoaiDichVuAndMaThamChieu(loaiDichVu, maThamChieu);
        if (danhGia == null) return null;
        return mapToResponse(danhGia);
    }

    @Override
    public void delete(Integer id) {
        if (!danhGiaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy đánh giá");
        }
        danhGiaRepository.deleteById(id);
    }

    private DanhGiaResponse mapToResponse(DanhGia danhGia) {
        return DanhGiaResponse.builder()
                .maDanhGia(danhGia.getMaDanhGia())
                .maNguoiDung(danhGia.getKhachHang().getMaNguoiDung())
                .tenKhachHang(danhGia.getKhachHang().getHoTen())
                .loaiDichVu(danhGia.getLoaiDichVu())
                .maThamChieu(danhGia.getMaThamChieu())
                .soSao(danhGia.getSoSao())
                .noiDung(danhGia.getNoiDung())
                .ngayDanhGia(danhGia.getNgayDanhGia())
                .build();
    }
}
