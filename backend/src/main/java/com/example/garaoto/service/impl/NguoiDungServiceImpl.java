package com.example.garaoto.service.impl;

import com.example.garaoto.dto.response.NguoiDungResponse;
import com.example.garaoto.entity.NguoiDung;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.NguoiDungRepository;
import com.example.garaoto.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NguoiDungServiceImpl implements NguoiDungService {

    private final NguoiDungRepository nguoiDungRepository;

    @Override
    public List<NguoiDungResponse> getAll() {
        return nguoiDungRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public NguoiDungResponse getById(Integer id) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        return mapToResponse(nguoiDung);
    }

    @Override
    public NguoiDungResponse getByEmail(String email) {
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        return mapToResponse(nguoiDung);
    }

    @Override
    public void delete(Integer id) {
        if (!nguoiDungRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy người dùng");
        }
        nguoiDungRepository.deleteById(id);
    }

    @Override
    public NguoiDungResponse update(Integer id, com.example.garaoto.dto.request.NguoiDungRequest request) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        if (request.getHoTen() != null && !request.getHoTen().isBlank()) {
            nguoiDung.setHoTen(request.getHoTen());
        }
        if (request.getSoDienThoai() != null && !request.getSoDienThoai().isBlank()) {
            nguoiDung.setSoDienThoai(request.getSoDienThoai());
        }
        if (request.getDiaChi() != null) {
            nguoiDung.setDiaChi(request.getDiaChi());
        }

        NguoiDung updated = nguoiDungRepository.save(nguoiDung);
        return mapToResponse(updated);
    }

    private NguoiDungResponse mapToResponse(NguoiDung nguoiDung) {
        return NguoiDungResponse.builder()
                .maNguoiDung(nguoiDung.getMaNguoiDung())
                .hoTen(nguoiDung.getHoTen())
                .email(nguoiDung.getEmail())
                .soDienThoai(nguoiDung.getSoDienThoai())
                .diaChi(nguoiDung.getDiaChi())
                .vaiTro(nguoiDung.getVaiTro())
                .trangThai(nguoiDung.getTrangThai())
                .ngayTao(nguoiDung.getNgayTao())
                .build();
    }
}
