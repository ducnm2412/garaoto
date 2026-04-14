package com.example.garaoto.service.impl;

import com.example.garaoto.dto.request.XeChoThueRequest;
import com.example.garaoto.dto.response.XeChoThueResponse;
import com.example.garaoto.entity.XeChoThue;
import com.example.garaoto.exception.DuplicateResourceException;
import com.example.garaoto.exception.ResourceNotFoundException;
import com.example.garaoto.repository.XeChoThueRepository;
import com.example.garaoto.service.XeChoThueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class XeChoThueServiceImpl implements XeChoThueService {

    private final XeChoThueRepository xeChoThueRepository;

    @Override
    public XeChoThueResponse create(XeChoThueRequest request) {
        if (xeChoThueRepository.existsByBienSo(request.getBienSo())) {
            throw new DuplicateResourceException("Biển số xe đã tồn tại");
        }

        XeChoThue xe = XeChoThue.builder()
                .bienSo(request.getBienSo())
                .hangXe(request.getHangXe())
                .dongXe(request.getDongXe())
                .soCho(request.getSoCho())
                .hopSo(request.getHopSo())
                .nhienLieu(request.getNhienLieu())
                .giaTheoNgay(request.getGiaTheoNgay())
                .tinhTrang(request.getTinhTrang())
                .hinhAnh(request.getHinhAnh())
                .namSanXuat(request.getNamSanXuat())
                .build();

        return mapToResponse(xeChoThueRepository.save(xe));
    }

    @Override
    public XeChoThueResponse update(Integer id, XeChoThueRequest request) {
        XeChoThue xe = xeChoThueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe cho thuê"));

        xe.setBienSo(request.getBienSo());
        xe.setHangXe(request.getHangXe());
        xe.setDongXe(request.getDongXe());
        xe.setSoCho(request.getSoCho());
        xe.setHopSo(request.getHopSo());
        xe.setNhienLieu(request.getNhienLieu());
        xe.setGiaTheoNgay(request.getGiaTheoNgay());
        xe.capNhatTinhTrang(request.getTinhTrang());
        xe.setHinhAnh(request.getHinhAnh());
        xe.setNamSanXuat(request.getNamSanXuat());

        return mapToResponse(xeChoThueRepository.save(xe));
    }

    @Override
    public List<XeChoThueResponse> getAll() {
        return xeChoThueRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public XeChoThueResponse getById(Integer id) {
        XeChoThue xe = xeChoThueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe cho thuê"));
        return mapToResponse(xe);
    }

    @Override
    public void delete(Integer id) {
        if (!xeChoThueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy xe cho thuê");
        }
        xeChoThueRepository.deleteById(id);
    }

    private XeChoThueResponse mapToResponse(XeChoThue xe) {
        return XeChoThueResponse.builder()
                .maXeThue(xe.getMaXeThue())
                .bienSo(xe.getBienSo())
                .hangXe(xe.getHangXe())
                .dongXe(xe.getDongXe())
                .soCho(xe.getSoCho())
                .hopSo(xe.getHopSo())
                .nhienLieu(xe.getNhienLieu())
                .giaTheoNgay(xe.getGiaTheoNgay())
                .tinhTrang(xe.getTinhTrang())
                .hinhAnh(xe.getHinhAnh())
                .namSanXuat(xe.getNamSanXuat())
                .build();
    }
}
