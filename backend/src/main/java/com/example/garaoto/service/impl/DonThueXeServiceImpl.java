package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.DonThueXeRequest;
import com.example.garaoto.dto.response.DonThueXeResponse;
import com.example.garaoto.entity.DonThueXe;
import com.example.garaoto.entity.KhachHang;
import com.example.garaoto.entity.XeChoThue;
import com.example.garaoto.exception.BadRequestException;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.DonThueXeRepository;
import com.example.garaoto.repository.KhachHangRepository;
import com.example.garaoto.repository.XeChoThueRepository;
import com.example.garaoto.service.DonThueXeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonThueXeServiceImpl implements DonThueXeService {

    private final DonThueXeRepository donThueXeRepository;
    private final KhachHangRepository khachHangRepository;
    private final XeChoThueRepository xeChoThueRepository;

    @Override
    public DonThueXeResponse create(DonThueXeRequest request) {
        if (request.getNgayTra().isBefore(request.getNgayNhan())) {
            throw new BadRequestException("Ngày trả phải lớn hơn hoặc bằng ngày nhận");
        }

        KhachHang khachHang = khachHangRepository.findById(request.getMaNguoiDung())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

        XeChoThue xeChoThue = xeChoThueRepository.findById(request.getMaXeThue())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe cho thuê"));

        // Tạm thời bỏ check ràng buộc xe "SanSang" để luồng trải nghiệm thuê xe trong Frontend được trơn tru
        // (Trong thực tế sẽ cần tự động cập nhật trạng thái XeChoThue thành Đang Thuê / Sẵn sàng)

        long soNgay = request.getNgayNhan().until(request.getNgayTra()).getDays() + 1L;
        BigDecimal tongTien = xeChoThue.getGiaTheoNgay().multiply(BigDecimal.valueOf(soNgay));
        BigDecimal tienCoc = tongTien.multiply(BigDecimal.valueOf(0.3));

        DonThueXe donThueXe = DonThueXe.builder()
                .khachHang(khachHang)
                .xeChoThue(xeChoThue)
                .ngayNhan(request.getNgayNhan())
                .ngayTra(request.getNgayTra())
                .diaDiemNhan(request.getDiaDiemNhan())
                .diaDiemTra(request.getDiaDiemTra())
                .tienCoc(tienCoc)
                .tongTien(tongTien)
                .trangThai("ChoDuyet")
                .ngayDat(LocalDateTime.now())
                .build();

        donThueXe = donThueXeRepository.save(donThueXe);
        return mapToResponse(donThueXe);
    }

    @Override
    public List<DonThueXeResponse> getAll() {
        return donThueXeRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public DonThueXeResponse getById(Integer id) {
        DonThueXe donThueXe = donThueXeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn thuê"));
        return mapToResponse(donThueXe);
    }

    @Override
    public List<DonThueXeResponse> getByKhachHang(Integer maNguoiDung) {
        return donThueXeRepository.findByKhachHang_MaNguoiDung(maNguoiDung)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public DonThueXeResponse updateStatus(Integer id, String trangThai) {
        DonThueXe donThueXe = donThueXeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn thuê"));

        donThueXe.chuyenTrangThai(trangThai);
        
        // Đồng bộ trạng thái xe cho thuê
        XeChoThue xe = donThueXe.getXeChoThue();
        if (xe != null) {
            String status = trangThai != null ? trangThai.toUpperCase() : "";
            if (status.contains("DANGTHUE") || status.contains("DANG_THUE") || status.contains("XACNHAN") || status.contains("DAXACNHAN")) {
                xe.capNhatTinhTrang("DangThue");
                xeChoThueRepository.save(xe);
            } else if (status.contains("TRA") || status.contains("HUY") || status.contains("KHONGDUYET") || status.contains("TỪ CHỐI")) {
                xe.capNhatTinhTrang("SanSang");
                xeChoThueRepository.save(xe);
            }
        }

        return mapToResponse(donThueXeRepository.save(donThueXe));
    }

    private DonThueXeResponse mapToResponse(DonThueXe donThueXe) {
        return DonThueXeResponse.builder()
                .maDonThue(donThueXe.getMaDonThue())
                .maNguoiDung(donThueXe.getKhachHang().getMaNguoiDung())
                .maXeThue(donThueXe.getXeChoThue().getMaXeThue())
                .ngayNhan(donThueXe.getNgayNhan())
                .ngayTra(donThueXe.getNgayTra())
                .diaDiemNhan(donThueXe.getDiaDiemNhan())
                .diaDiemTra(donThueXe.getDiaDiemTra())
                .tienCoc(donThueXe.getTienCoc())
                .tongTien(donThueXe.getTongTien())
                .trangThai(donThueXe.getTrangThai())
                .ngayDat(donThueXe.getNgayDat())
                .build();
    }
}
