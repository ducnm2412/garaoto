package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.PhieuSuaChuaRequest;
import com.example.garaoto.dto.response.PhieuSuaChuaResponse;
import com.example.garaoto.entity.KhachHang;
import com.example.garaoto.entity.LichHenSuaChua;
import com.example.garaoto.entity.PhieuSuaChua;
import com.example.garaoto.entity.XeKhachHang;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.KhachHangRepository;
import com.example.garaoto.repository.LichHenSuaChuaRepository;
import com.example.garaoto.repository.PhieuSuaChuaRepository;
import com.example.garaoto.repository.XeKhachHangRepository;
import com.example.garaoto.service.PhieuSuaChuaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhieuSuaChuaServiceImpl implements PhieuSuaChuaService {

    private final PhieuSuaChuaRepository phieuSuaChuaRepository;
    private final LichHenSuaChuaRepository lichHenSuaChuaRepository;
    private final XeKhachHangRepository xeKhachHangRepository;
    private final KhachHangRepository khachHangRepository;

    @Override
    public PhieuSuaChuaResponse create(PhieuSuaChuaRequest request) {
        LichHenSuaChua lichHen = null;
        if (request.getMaLichHen() != null) {
            lichHen = lichHenSuaChuaRepository.findById(request.getMaLichHen())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch hẹn"));
        }

        XeKhachHang xeKhachHang = xeKhachHangRepository.findById(request.getMaXeKh())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe khách hàng"));

        KhachHang khachHang = khachHangRepository.findById(request.getMaKhachHang())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

        PhieuSuaChua phieuSua = PhieuSuaChua.builder()
                .lichHenSuaChua(lichHen)
                .xeKhachHang(xeKhachHang)
                .khachHang(khachHang)
                .ngayNhanXe(LocalDateTime.now())
                .chanDoan(request.getChanDoan())
                .tongTien(request.getTongTien())
                .trangThai(request.getTrangThai() != null ? request.getTrangThai() : "TiepNhan")
                .build();

        phieuSua = phieuSuaChuaRepository.save(phieuSua);
        return mapToResponse(phieuSua);
    }

    @Override
    public List<PhieuSuaChuaResponse> getAll() {
        return phieuSuaChuaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PhieuSuaChuaResponse getById(Integer id) {
        PhieuSuaChua phieuSua = phieuSuaChuaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu sửa"));
        return mapToResponse(phieuSua);
    }

    @Override
    public List<PhieuSuaChuaResponse> getByKhachHang(Integer maKhachHang) {
        return phieuSuaChuaRepository.findByKhachHang_MaKhachHang(maKhachHang)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PhieuSuaChuaResponse updateStatus(Integer id, String trangThai) {
        PhieuSuaChua phieuSua = phieuSuaChuaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu sửa"));
        phieuSua.setTrangThai(trangThai);

        if ("DaSuaXong".equals(trangThai) || "BanGiao".equals(trangThai)) {
            phieuSua.setNgayHoanThanh(LocalDateTime.now());
        }

        return mapToResponse(phieuSuaChuaRepository.save(phieuSua));
    }

    @Override
    public PhieuSuaChuaResponse updateChanDoan(Integer id, String chanDoan, String trangThai) {
        PhieuSuaChua phieuSua = phieuSuaChuaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu sửa"));

        phieuSua.setChanDoan(chanDoan);
        if (trangThai != null) {
            phieuSua.setTrangThai(trangThai);
        }

        return mapToResponse(phieuSuaChuaRepository.save(phieuSua));
    }

    private PhieuSuaChuaResponse mapToResponse(PhieuSuaChua phieuSua) {
        return PhieuSuaChuaResponse.builder()
                .maPhieuSua(phieuSua.getMaPhieuSua())
                .maLichHen(phieuSua.getLichHenSuaChua() != null ? phieuSua.getLichHenSuaChua().getMaLichHen() : null)
                .maXeKh(phieuSua.getXeKhachHang().getMaXeKh())
                .maKhachHang(phieuSua.getKhachHang().getMaKhachHang())
                .ngayNhanXe(phieuSua.getNgayNhanXe())
                .ngayHoanThanh(phieuSua.getNgayHoanThanh())
                .chanDoan(phieuSua.getChanDoan())
                .tongTien(phieuSua.getTongTien())
                .trangThai(phieuSua.getTrangThai())
                .build();
    }
}