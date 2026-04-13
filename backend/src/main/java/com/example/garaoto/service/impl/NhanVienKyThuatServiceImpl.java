package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.NhanVienKyThuatRequest;
import com.example.garaoto.dto.response.NhanVienKyThuatResponse;
import com.example.garaoto.entity.NhanVienKyThuat;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.NhanVienKyThuatRepository;
import com.example.garaoto.service.NhanVienKyThuatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NhanVienKyThuatServiceImpl implements NhanVienKyThuatService {

    private final NhanVienKyThuatRepository nhanVienKyThuatRepository;

    @Override
    public List<NhanVienKyThuatResponse> getAll() {
        return nhanVienKyThuatRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public NhanVienKyThuatResponse getById(Integer id) {
        NhanVienKyThuat nhanVien = nhanVienKyThuatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên kỹ thuật"));
        return mapToResponse(nhanVien);
    }

    @Override
    public NhanVienKyThuatResponse getByNguoiDungId(Integer maNguoiDung) {
        NhanVienKyThuat nhanVien = nhanVienKyThuatRepository.findById(maNguoiDung)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên kỹ thuật"));
        return mapToResponse(nhanVien);
    }

    @Override
    public NhanVienKyThuatResponse update(Integer id, NhanVienKyThuatRequest request) {
        NhanVienKyThuat nhanVien = nhanVienKyThuatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên kỹ thuật"));

        nhanVien.setChuyenMon(request.getChuyenMon());
        nhanVien.setCaLamViec(request.getCaLamViec());

        return mapToResponse(nhanVienKyThuatRepository.save(nhanVien));
    }

    @Override
    public void delete(Integer id) {
        if (!nhanVienKyThuatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy nhân viên kỹ thuật");
        }
        nhanVienKyThuatRepository.deleteById(id);
    }

    private NhanVienKyThuatResponse mapToResponse(NhanVienKyThuat nhanVien) {
        return NhanVienKyThuatResponse.builder()
                .maNguoiDung(nhanVien.getMaNguoiDung())
                .hoTen(nhanVien.getHoTen())
                .email(nhanVien.getEmail())
                .soDienThoai(nhanVien.getSoDienThoai())
                .chuyenMon(nhanVien.getChuyenMon())
                .caLamViec(nhanVien.getCaLamViec())
                .build();
    }
}
