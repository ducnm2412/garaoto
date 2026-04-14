package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.LichHenSuaChuaRequest;
import com.example.garaoto.dto.response.LichHenSuaChuaResponse;
import com.example.garaoto.entity.KhachHang;
import com.example.garaoto.entity.LichHenSuaChua;
import com.example.garaoto.entity.PhieuSuaChua;
import com.example.garaoto.entity.XeKhachHang;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.KhachHangRepository;
import com.example.garaoto.repository.LichHenSuaChuaRepository;
import com.example.garaoto.repository.PhieuSuaChuaRepository;
import com.example.garaoto.repository.XeKhachHangRepository;
import com.example.garaoto.service.LichHenSuaChuaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LichHenSuaChuaServiceImpl implements LichHenSuaChuaService {

    private final LichHenSuaChuaRepository lichHenSuaChuaRepository;
    private final KhachHangRepository khachHangRepository;
    private final XeKhachHangRepository xeKhachHangRepository;
    private final PhieuSuaChuaRepository phieuSuaChuaRepository;

    @Override
    public LichHenSuaChuaResponse create(LichHenSuaChuaRequest request) {
        KhachHang khachHang = khachHangRepository.findById(request.getMaNguoiDung())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

        XeKhachHang xeKhachHang = xeKhachHangRepository.findById(request.getMaXeKh())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe khách hàng"));

        LichHenSuaChua lichHen = LichHenSuaChua.builder()
                .khachHang(khachHang)
                .xeKhachHang(xeKhachHang)
                .ngayHen(request.getNgayHen())
                .gioHen(request.getGioHen())
                .moTaLoi(request.getMoTaLoi())
                .trangThai("ChoXacNhan")
                .ngayTao(LocalDateTime.now())
                .build();

        lichHen = lichHenSuaChuaRepository.save(lichHen);
        return mapToResponse(lichHen);
    }

    @Override
    public List<LichHenSuaChuaResponse> getAll() {
        return lichHenSuaChuaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public LichHenSuaChuaResponse getById(Integer id) {
        LichHenSuaChua lichHen = lichHenSuaChuaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch hẹn"));
        return mapToResponse(lichHen);
    }

    @Override
    public List<LichHenSuaChuaResponse> getByKhachHang(Integer maNguoiDung) {
        return lichHenSuaChuaRepository.findByKhachHang_MaNguoiDung(maNguoiDung)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public LichHenSuaChuaResponse updateStatus(Integer id, String trangThai) {
        LichHenSuaChua lichHen = lichHenSuaChuaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch hẹn"));

        lichHen.setTrangThai(trangThai);
        LichHenSuaChua savedLichHen = lichHenSuaChuaRepository.save(lichHen);

        if ("DaXacNhan".equals(trangThai)) {
            if (!phieuSuaChuaRepository.existsByLichHenSuaChua_MaLichHen(id)) {
                PhieuSuaChua phieuSua = PhieuSuaChua.builder()
                        .lichHenSuaChua(savedLichHen)
                        .xeKhachHang(savedLichHen.getXeKhachHang())
                        .khachHang(savedLichHen.getKhachHang())
                        .ngayNhanXe(LocalDateTime.now())
                        .chanDoan(savedLichHen.getMoTaLoi())
                        .build();
                phieuSua.capNhatTrangThai("TiepNhan");
                phieuSuaChuaRepository.save(phieuSua);
            }
        }

        return mapToResponse(savedLichHen);
    }

    @Override
    public void delete(Integer id) {
        if (!lichHenSuaChuaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy lịch hẹn");
        }
        lichHenSuaChuaRepository.deleteById(id);
    }

    private LichHenSuaChuaResponse mapToResponse(LichHenSuaChua lichHen) {
        return LichHenSuaChuaResponse.builder()
                .maLichHen(lichHen.getMaLichHen())
                .maNguoiDung(lichHen.getKhachHang().getMaNguoiDung())
                .maXeKh(lichHen.getXeKhachHang().getMaXeKh())
                .ngayHen(lichHen.getNgayHen())
                .gioHen(lichHen.getGioHen())
                .moTaLoi(lichHen.getMoTaLoi())
                .trangThai(lichHen.getTrangThai())
                .tenKhachHang(lichHen.getKhachHang().getHoTen())
                .ngayTao(lichHen.getNgayTao())
                .build();
    }
}
