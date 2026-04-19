package com.example.garaoto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GaraotoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GaraotoApplication.class, args);
	}

	@org.springframework.context.annotation.Bean
	public org.springframework.boot.CommandLineRunner initData(
			com.example.garaoto.repository.NguoiDungRepository nguoiDungRepo, 
			com.example.garaoto.repository.AdminRepository adminRepo, 
            com.example.garaoto.repository.DichVuSuaChuaRepository dichVuRepo,
			org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
		return args -> {
            boolean seeded = false;
			if (!nguoiDungRepo.existsByEmail("admin@gmail.com")) {
				com.example.garaoto.entity.Admin admin = com.example.garaoto.entity.Admin.builder()
						.hoTen("Quản Trị Viên Hệ Thống")
						.email("admin@gmail.com")
						.soDienThoai("0999999999")
						.diaChi("Hà Nội")
						.matKhau(passwordEncoder.encode("123"))
						.vaiTro("Admin")
						.trangThai("HoatDong")
						.chucVu("Tổng Giám Đốc")
						.ngayTao(java.time.LocalDateTime.now())
						.build();
				adminRepo.save(admin);
                seeded = true;
			}

            if (dichVuRepo.count() == 0) {
                java.util.List<com.example.garaoto.entity.DichVuSuaChua> dfServices = java.util.List.of(
                    com.example.garaoto.entity.DichVuSuaChua.builder().tenDichVu("Thay Nhớt Motul").moTa("Nhớt tổng hợp toàn phần").giaCoBan(java.math.BigDecimal.valueOf(350000)).trangThai("HoatDong").build(),
                    com.example.garaoto.entity.DichVuSuaChua.builder().tenDichVu("Bơm lốp khí Nito").moTa("Giữ áp suất ổn định").giaCoBan(java.math.BigDecimal.valueOf(50000)).trangThai("HoatDong").build(),
                    com.example.garaoto.entity.DichVuSuaChua.builder().tenDichVu("Cân mâm bấm chì").moTa("Tránh rung vô lăng").giaCoBan(java.math.BigDecimal.valueOf(250000)).trangThai("HoatDong").build()
                );
                dichVuRepo.saveAll(dfServices);
                seeded = true;
            }

            if (seeded) {
				System.out.println("\n\n=======================================================");
				System.out.println("HỆ THỐNG ĐÃ TỰ ĐỘNG KHỞI TẠO TÀI KHOẢN VÀ MENU DỊCH VỤ MẪU!");
				System.out.println("=======================================================\n\n");
            }
		};
	}
}
