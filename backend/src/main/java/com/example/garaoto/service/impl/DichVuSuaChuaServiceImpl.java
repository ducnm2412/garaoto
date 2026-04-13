package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.DichVuSuaChuaRequest;
import com.example.garaoto.dto.response.DichVuSuaChuaResponse;
import com.example.garaoto.entity.DichVuSuaChua;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.DichVuSuaChuaRepository;
import com.example.garaoto.service.DichVuSuaChuaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DichVuSuaChuaServiceImpl implements DichVuSuaChuaService {

    private final DichVuSuaChuaRepository dichVuSuaChuaRepository;

    @Override
    public DichVuSuaChuaResponse create(DichVuSuaChuaRequest request) {
        DichVuSuaChua dichVu = DichVuSuaChua.builder()
                .tenDichVu(request.getTenDichVu())
                .moTa(request.getMoTa())
                .giaCoBan(request.getGiaCoBan())
                .trangThai(request.getTrangThai())
                .build();

        return mapToResponse(dichVuSuaChuaRepository.save(dichVu));
    }

    @Override
    public DichVuSuaChuaResponse update(Integer id, DichVuSuaChuaRequest request) {
        DichVuSuaChua dichVu = dichVuSuaChuaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dịch vụ sửa chữa"));

        dichVu.setTenDichVu(request.getTenDichVu());
        dichVu.setMoTa(request.getMoTa());
        dichVu.setGiaCoBan(request.getGiaCoBan());
        dichVu.setTrangThai(request.getTrangThai());

        return mapToResponse(dichVuSuaChuaRepository.save(dichVu));
    }

    @Override
    public List<DichVuSuaChuaResponse> getAll() {
        return dichVuSuaChuaRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public DichVuSuaChuaResponse getById(Integer id) {
        DichVuSuaChua dichVu = dichVuSuaChuaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dịch vụ sửa chữa"));
        return mapToResponse(dichVu);
    }

    @Override
    public void delete(Integer id) {
        if (!dichVuSuaChuaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy dịch vụ sửa chữa");
        }
        dichVuSuaChuaRepository.deleteById(id);
    }

    private DichVuSuaChuaResponse mapToResponse(DichVuSuaChua dichVu) {
        return DichVuSuaChuaResponse.builder()
                .maDichVu(dichVu.getMaDichVu())
                .tenDichVu(dichVu.getTenDichVu())
                .moTa(dichVu.getMoTa())
                .giaCoBan(dichVu.getGiaCoBan())
                .trangThai(dichVu.getTrangThai())
                .build();
    }
}
