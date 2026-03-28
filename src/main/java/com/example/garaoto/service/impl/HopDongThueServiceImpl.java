package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.HopDongThueRequest;
import com.example.garaoto.dto.response.HopDongThueResponse;
import com.example.garaoto.entity.DonThueXe;
import com.example.garaoto.entity.HopDongThue;
import com.example.garaoto.exception.DuplicateResourceException;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.DonThueXeRepository;
import com.example.garaoto.repository.HopDongThueRepository;
import com.example.garaoto.service.HopDongThueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HopDongThueServiceImpl implements HopDongThueService {

    private final HopDongThueRepository hopDongThueRepository;
    private final DonThueXeRepository donThueXeRepository;

    @Override
    public HopDongThueResponse create(HopDongThueRequest request) {
        if (hopDongThueRepository.findByDonThueXe_MaDonThue(request.getMaDonThue()).isPresent()) {
            throw new DuplicateResourceException("Đơn thuê này đã có hợp đồng");
        }

        DonThueXe donThueXe = donThueXeRepository.findById(request.getMaDonThue())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn thuê"));

        HopDongThue hopDong = HopDongThue.builder()
                .donThueXe(donThueXe)
                .ngayLap(LocalDateTime.now())
                .dieuKhoan(request.getDieuKhoan())
                .ghiChu(request.getGhiChu())
                .trangThai(request.getTrangThai())
                .build();

        return mapToResponse(hopDongThueRepository.save(hopDong));
    }

    @Override
    public HopDongThueResponse getById(Integer id) {
        HopDongThue hopDong = hopDongThueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hợp đồng thuê"));
        return mapToResponse(hopDong);
    }

    @Override
    public HopDongThueResponse getByDonThue(Integer maDonThue) {
        HopDongThue hopDong = hopDongThueRepository.findByDonThueXe_MaDonThue(maDonThue)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hợp đồng thuê"));
        return mapToResponse(hopDong);
    }

    @Override
    public List<HopDongThueResponse> getAll() {
        return hopDongThueRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public void delete(Integer id) {
        if (!hopDongThueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy hợp đồng thuê");
        }
        hopDongThueRepository.deleteById(id);
    }

    private HopDongThueResponse mapToResponse(HopDongThue hopDong) {
        return HopDongThueResponse.builder()
                .maHopDong(hopDong.getMaHopDong())
                .maDonThue(hopDong.getDonThueXe().getMaDonThue())
                .ngayLap(hopDong.getNgayLap())
                .dieuKhoan(hopDong.getDieuKhoan())
                .ghiChu(hopDong.getGhiChu())
                .trangThai(hopDong.getTrangThai())
                .build();
    }
}