package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.XeKhachHangRequest;
import com.example.garaoto.dto.response.XeKhachHangResponse;
import com.example.garaoto.entity.KhachHang;
import com.example.garaoto.entity.XeKhachHang;
import com.example.garaoto.exception.DuplicateResourceException;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.KhachHangRepository;
import com.example.garaoto.repository.XeKhachHangRepository;
import com.example.garaoto.service.XeKhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class XeKhachHangServiceImpl implements XeKhachHangService {

    private final XeKhachHangRepository xeKhachHangRepository;
    private final KhachHangRepository khachHangRepository;

    @Override
    public XeKhachHangResponse create(XeKhachHangRequest request) {
        if (xeKhachHangRepository.existsByBienSo(request.getBienSo())) {
            throw new DuplicateResourceException("Biển số đã tồn tại");
        }

        KhachHang khachHang = khachHangRepository.findById(request.getMaNguoiDung())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

        XeKhachHang xe = XeKhachHang.builder()
                .khachHang(khachHang)
                .bienSo(request.getBienSo())
                .hangXe(request.getHangXe())
                .dongXe(request.getDongXe())
                .namSanXuat(request.getNamSanXuat())
                .mauSac(request.getMauSac())
                .soKhung(request.getSoKhung())
                .soMay(request.getSoMay())
                .build();

        return mapToResponse(xeKhachHangRepository.save(xe));
    }

    @Override
    public XeKhachHangResponse update(Integer id, XeKhachHangRequest request) {
        XeKhachHang xe = xeKhachHangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe khách hàng"));

        KhachHang khachHang = khachHangRepository.findById(request.getMaNguoiDung())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

        xe.setKhachHang(khachHang);
        xe.setBienSo(request.getBienSo());
        xe.setHangXe(request.getHangXe());
        xe.setDongXe(request.getDongXe());
        xe.setNamSanXuat(request.getNamSanXuat());
        xe.setMauSac(request.getMauSac());
        xe.setSoKhung(request.getSoKhung());
        xe.setSoMay(request.getSoMay());

        return mapToResponse(xeKhachHangRepository.save(xe));
    }

    @Override
    public List<XeKhachHangResponse> getAll() {
        return xeKhachHangRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public XeKhachHangResponse getById(Integer id) {
        XeKhachHang xe = xeKhachHangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe khách hàng"));
        return mapToResponse(xe);
    }

    @Override
    public List<XeKhachHangResponse> getByKhachHang(Integer maNguoiDung) {
        return xeKhachHangRepository.findByKhachHang_MaNguoiDung(maNguoiDung)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void delete(Integer id) {
        if (!xeKhachHangRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy xe khách hàng");
        }
        xeKhachHangRepository.deleteById(id);
    }

    private XeKhachHangResponse mapToResponse(XeKhachHang xe) {
        return XeKhachHangResponse.builder()
                .maXeKh(xe.getMaXeKh())
                .maNguoiDung(xe.getKhachHang().getMaNguoiDung())
                .bienSo(xe.getBienSo())
                .hangXe(xe.getHangXe())
                .dongXe(xe.getDongXe())
                .namSanXuat(xe.getNamSanXuat())
                .mauSac(xe.getMauSac())
                .soKhung(xe.getSoKhung())
                .soMay(xe.getSoMay())
                .build();
    }
}
