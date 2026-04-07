package com.example.garaoto.service.impl;

import com.example.garaoto.repository.AdminRepository;
import com.example.garaoto.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public List<String> getAllAdminNames() {
        return adminRepository.findAll()
                .stream()
                .map(admin -> admin.getNguoiDung().getHoTen())
                .toList();
    }
}