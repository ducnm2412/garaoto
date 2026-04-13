package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.PhanCongSuaChuaRequest;
import com.example.garaoto.dto.response.PhanCongSuaChuaResponse;
import com.example.garaoto.entity.Admin;
import com.example.garaoto.entity.NhanVienKyThuat;
import com.example.garaoto.entity.PhanCongSuaChua;
import com.example.garaoto.entity.PhieuSuaChua;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.AdminRepository;
import com.example.garaoto.repository.NhanVienKyThuatRepository;
import com.example.garaoto.repository.PhanCongSuaChuaRepository;
import com.example.garaoto.repository.PhieuSuaChuaRepository;
import com.example.garaoto.service.PhanCongSuaChuaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhanCongSuaChuaServiceImpl implements PhanCongSuaChuaService {

    private final PhanCongSuaChuaRepository phanCongSuaChuaRepository;
    private final PhieuSuaChuaRepository phieuSuaChuaRepository;
    private final NhanVienKyThuatRepository nhanVienKyThuatRepository;
    private final AdminRepository adminRepository;

    @Override
    public PhanCongSuaChuaResponse create(PhanCongSuaChuaRequest request) {
        PhieuSuaChua phieuSua = phieuSuaChuaRepository.findById(request.getMaPhieuSua())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu sửa"));

        NhanVienKyThuat nhanVien = nhanVienKyThuatRepository.findById(request.getMaNguoiDungNv())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên kỹ thuật"));

        Admin admin = adminRepository.findById(request.getMaNguoiDungAdmin())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy admin"));

        PhanCongSuaChua phanCong = PhanCongSuaChua.builder()
                .phieuSuaChua(phieuSua)
                .nhanVienKyThuat(nhanVien)
                .admin(admin)
                .ngayPhanCong(LocalDateTime.now())
                .ghiChu(request.getGhiChu())
                .trangThai(request.getTrangThai() != null ? request.getTrangThai() : "DaPhanCong")
                .build();

        phanCong = phanCongSuaChuaRepository.save(phanCong);
        return mapToResponse(phanCong);
    }

    @Override
    public List<PhanCongSuaChuaResponse> getAll() {
        return phanCongSuaChuaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<PhanCongSuaChuaResponse> getByNhanVien(Integer maNguoiDung) {
        return phanCongSuaChuaRepository.findByNhanVienKyThuat_MaNguoiDung(maNguoiDung)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PhanCongSuaChuaResponse updateStatus(Integer id, String trangThai) {
        PhanCongSuaChua phanCong = phanCongSuaChuaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phân công"));

        phanCong.setTrangThai(trangThai);
        return mapToResponse(phanCongSuaChuaRepository.save(phanCong));
    }

    private PhanCongSuaChuaResponse mapToResponse(PhanCongSuaChua phanCong) {
        return PhanCongSuaChuaResponse.builder()
                .maPhanCong(phanCong.getMaPhanCong())
                .maPhieuSua(phanCong.getPhieuSuaChua().getMaPhieuSua())
                .maNguoiDungNv(phanCong.getNhanVienKyThuat().getMaNguoiDung())
                .maNguoiDungAdmin(phanCong.getAdmin().getMaNguoiDung())
                .ngayPhanCong(phanCong.getNgayPhanCong())
                .ghiChu(phanCong.getGhiChu())
                .trangThai(phanCong.getTrangThai())
                .build();
    }
}
