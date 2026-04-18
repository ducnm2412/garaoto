BEGIN;

CREATE TABLE IF NOT EXISTS public.admin
(
    ma_nguoi_dung integer NOT NULL,
    chuc_vu character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT admin_pkey PRIMARY KEY (ma_nguoi_dung),
    CONSTRAINT admin_ma_nguoi_dung_key UNIQUE (ma_nguoi_dung)
);

CREATE TABLE IF NOT EXISTS public.chi_tiet_sua_chua
(
    ma_chi_tiet_sua serial NOT NULL,
    ma_phieu_sua integer NOT NULL,
    ma_dich_vu integer NOT NULL,
    so_luong integer,
    don_gia numeric(15, 2),
    thanh_tien numeric(15, 2),
    ghi_chu text COLLATE pg_catalog."default",
    CONSTRAINT chi_tiet_sua_chua_pkey PRIMARY KEY (ma_chi_tiet_sua)
);

CREATE TABLE IF NOT EXISTS public.danh_gia
(
    ma_danh_gia serial NOT NULL,
    ma_khach_hang integer NOT NULL,
    loai_danh_gia character varying(50) COLLATE pg_catalog."default",
    ma_phieu_sua integer,
    ma_don_thue integer,
    so_sao integer,
    noi_dung text COLLATE pg_catalog."default",
    ngay_danh_gia timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT danh_gia_pkey PRIMARY KEY (ma_danh_gia)
);

CREATE TABLE IF NOT EXISTS public.dich_vu_sua_chua
(
    ma_dich_vu serial NOT NULL,
    ten_dich_vu character varying(255) COLLATE pg_catalog."default" NOT NULL,
    mo_ta text COLLATE pg_catalog."default",
    gia_co_ban numeric(15, 2),
    trang_thai character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT dich_vu_sua_chua_pkey PRIMARY KEY (ma_dich_vu)
);

CREATE TABLE IF NOT EXISTS public.don_thue_xe
(
    ma_don_thue serial NOT NULL,
    ma_khach_hang integer NOT NULL,
    ma_xe_thue integer NOT NULL,
    ngay_nhan date NOT NULL,
    ngay_tra date NOT NULL,
    dia_diem_nhan character varying(255) COLLATE pg_catalog."default",
    dia_diem_tra character varying(255) COLLATE pg_catalog."default",
    tien_coc numeric(15, 2),
    tong_tien numeric(15, 2),
    trang_thai character varying(50) COLLATE pg_catalog."default",
    ngay_dat timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT don_thue_xe_pkey PRIMARY KEY (ma_don_thue)
);

CREATE TABLE IF NOT EXISTS public.hop_dong_thue
(
    ma_hop_dong serial NOT NULL,
    ma_don_thue integer NOT NULL,
    ngay_lap timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    dieu_khoan text COLLATE pg_catalog."default",
    ghi_chu text COLLATE pg_catalog."default",
    trang_thai character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT hop_dong_thue_pkey PRIMARY KEY (ma_hop_dong),
    CONSTRAINT hop_dong_thue_ma_don_thue_key UNIQUE (ma_don_thue)
);

CREATE TABLE IF NOT EXISTS public.khach_hang
(
    ma_nguoi_dung integer NOT NULL,
    cccd character varying(20) COLLATE pg_catalog."default",
    so_gplx character varying(30) COLLATE pg_catalog."default",
    hang_gplx character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT khach_hang_pkey PRIMARY KEY (ma_nguoi_dung),
    CONSTRAINT khach_hang_ma_nguoi_dung_key UNIQUE (ma_nguoi_dung)
);

CREATE TABLE IF NOT EXISTS public.lich_hen_sua_chua
(
    ma_lich_hen serial NOT NULL,
    ma_khach_hang integer NOT NULL,
    ma_xe_kh integer NOT NULL,
    ngay_hen date NOT NULL,
    gio_hen time without time zone NOT NULL,
    mo_ta_loi text COLLATE pg_catalog."default",
    trang_thai character varying(50) COLLATE pg_catalog."default",
    ngay_tao timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT lich_hen_sua_chua_pkey PRIMARY KEY (ma_lich_hen)
);

CREATE TABLE IF NOT EXISTS public.nguoi_dung
(
    ma_nguoi_dung serial NOT NULL,
    ho_ten character varying(255) COLLATE pg_catalog."default",
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    so_dien_thoai character varying(20) COLLATE pg_catalog."default",
    mat_khau character varying(255) COLLATE pg_catalog."default" NOT NULL,
    dia_chi character varying(255) COLLATE pg_catalog."default",
    vai_tro character varying(50) COLLATE pg_catalog."default" NOT NULL,
    trang_thai character varying(50) COLLATE pg_catalog."default",
    ngay_tao timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT nguoi_dung_pkey PRIMARY KEY (ma_nguoi_dung),
    CONSTRAINT nguoi_dung_email_key UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS public.nhan_vien_ky_thuat
(
    ma_nguoi_dung integer NOT NULL,
    chuyen_mon character varying(100) COLLATE pg_catalog."default",
    ca_lam_viec character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT nhan_vien_ky_thuat_pkey PRIMARY KEY (ma_nguoi_dung),
    CONSTRAINT nhan_vien_ky_thuat_ma_nguoi_dung_key UNIQUE (ma_nguoi_dung)
);

CREATE TABLE IF NOT EXISTS public.phan_cong_sua_chua
(
    ma_phan_cong serial NOT NULL,
    ma_phieu_sua integer NOT NULL,
    ma_nhan_vien integer NOT NULL,
    ma_admin integer NOT NULL,
    ngay_phan_cong timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    ghi_chu text COLLATE pg_catalog."default",
    trang_thai character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT phan_cong_sua_chua_pkey PRIMARY KEY (ma_phan_cong)
);

CREATE TABLE IF NOT EXISTS public.phieu_sua_chua
(
    ma_phieu_sua serial NOT NULL,
    ma_lich_hen integer,
    ma_xe_kh integer NOT NULL,
    ma_khach_hang integer NOT NULL,
    ngay_nhan_xe timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    ngay_hoan_thanh timestamp without time zone,
    chan_doan text COLLATE pg_catalog."default",
    tong_tien numeric(15, 2),
    trang_thai character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT phieu_sua_chua_pkey PRIMARY KEY (ma_phieu_sua)
);

CREATE TABLE IF NOT EXISTS public.thanh_toan
(
    ma_thanh_toan serial NOT NULL,
    ma_khach_hang integer NOT NULL,
    loai_thanh_toan character varying(50) COLLATE pg_catalog."default",
    ma_phieu_sua integer,
    ma_don_thue integer,
    so_tien numeric(15, 2) NOT NULL,
    phuong_thuc character varying(50) COLLATE pg_catalog."default",
    trang_thai character varying(50) COLLATE pg_catalog."default",
    ngay_thanh_toan timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT thanh_toan_pkey PRIMARY KEY (ma_thanh_toan)
);

CREATE TABLE IF NOT EXISTS public.xe_cho_thue
(
    ma_xe_thue serial NOT NULL,
    bien_so character varying(20) COLLATE pg_catalog."default" NOT NULL,
    hang_xe character varying(100) COLLATE pg_catalog."default",
    dong_xe character varying(100) COLLATE pg_catalog."default",
    so_cho integer,
    hop_so character varying(50) COLLATE pg_catalog."default",
    nhien_lieu character varying(50) COLLATE pg_catalog."default",
    gia_theo_ngay numeric(15, 2),
    tinh_trang character varying(50) COLLATE pg_catalog."default",
    hinh_anh character varying(255) COLLATE pg_catalog."default",
    nam_san_xuat integer,
    CONSTRAINT xe_cho_thue_pkey PRIMARY KEY (ma_xe_thue),
    CONSTRAINT xe_cho_thue_bien_so_key UNIQUE (bien_so)
);

CREATE TABLE IF NOT EXISTS public.xe_khach_hang
(
    ma_xe_kh serial NOT NULL,
    ma_khach_hang integer NOT NULL,
    bien_so character varying(20) COLLATE pg_catalog."default" NOT NULL,
    hang_xe character varying(100) COLLATE pg_catalog."default",
    dong_xe character varying(100) COLLATE pg_catalog."default",
    nam_san_xuat integer,
    mau_sac character varying(50) COLLATE pg_catalog."default",
    so_khung character varying(100) COLLATE pg_catalog."default",
    so_may character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT xe_khach_hang_pkey PRIMARY KEY (ma_xe_kh)
);

ALTER TABLE IF EXISTS public.admin
    ADD CONSTRAINT fk_admin_nguoi_dung FOREIGN KEY (ma_nguoi_dung)
    REFERENCES public.nguoi_dung (ma_nguoi_dung) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS admin_ma_nguoi_dung_key
    ON public.admin(ma_nguoi_dung);


ALTER TABLE IF EXISTS public.chi_tiet_sua_chua
    ADD CONSTRAINT fk_chi_tiet_dich_vu FOREIGN KEY (ma_dich_vu)
    REFERENCES public.dich_vu_sua_chua (ma_dich_vu) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.chi_tiet_sua_chua
    ADD CONSTRAINT fk_chi_tiet_phieu_sua FOREIGN KEY (ma_phieu_sua)
    REFERENCES public.phieu_sua_chua (ma_phieu_sua) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.danh_gia
    ADD CONSTRAINT fk_danh_gia_don_thue FOREIGN KEY (ma_don_thue)
    REFERENCES public.don_thue_xe (ma_don_thue) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE SET NULL;


ALTER TABLE IF EXISTS public.danh_gia
    ADD CONSTRAINT fk_danh_gia_khach_hang FOREIGN KEY (ma_khach_hang)
    REFERENCES public.khach_hang (ma_nguoi_dung) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.danh_gia
    ADD CONSTRAINT fk_danh_gia_phieu_sua FOREIGN KEY (ma_phieu_sua)
    REFERENCES public.phieu_sua_chua (ma_phieu_sua) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE SET NULL;


ALTER TABLE IF EXISTS public.don_thue_xe
    ADD CONSTRAINT fk_don_thue_khach_hang FOREIGN KEY (ma_khach_hang)
    REFERENCES public.khach_hang (ma_nguoi_dung) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.don_thue_xe
    ADD CONSTRAINT fk_don_thue_xe FOREIGN KEY (ma_xe_thue)
    REFERENCES public.xe_cho_thue (ma_xe_thue) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.hop_dong_thue
    ADD CONSTRAINT fk_hop_dong_don_thue FOREIGN KEY (ma_don_thue)
    REFERENCES public.don_thue_xe (ma_don_thue) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS hop_dong_thue_ma_don_thue_key
    ON public.hop_dong_thue(ma_don_thue);


ALTER TABLE IF EXISTS public.khach_hang
    ADD CONSTRAINT fk_khach_hang_nguoi_dung FOREIGN KEY (ma_nguoi_dung)
    REFERENCES public.nguoi_dung (ma_nguoi_dung) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS khach_hang_ma_nguoi_dung_key
    ON public.khach_hang(ma_nguoi_dung);


ALTER TABLE IF EXISTS public.lich_hen_sua_chua
    ADD CONSTRAINT fk_lich_hen_khach_hang FOREIGN KEY (ma_khach_hang)
    REFERENCES public.khach_hang (ma_nguoi_dung) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.lich_hen_sua_chua
    ADD CONSTRAINT fk_lich_hen_xe_khach_hang FOREIGN KEY (ma_xe_kh)
    REFERENCES public.xe_khach_hang (ma_xe_kh) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.nhan_vien_ky_thuat
    ADD CONSTRAINT fk_nhan_vien_nguoi_dung FOREIGN KEY (ma_nguoi_dung)
    REFERENCES public.nguoi_dung (ma_nguoi_dung) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS nhan_vien_ky_thuat_ma_nguoi_dung_key
    ON public.nhan_vien_ky_thuat(ma_nguoi_dung);


ALTER TABLE IF EXISTS public.phan_cong_sua_chua
    ADD CONSTRAINT fk_phan_cong_admin FOREIGN KEY (ma_admin)
    REFERENCES public.admin (ma_nguoi_dung) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.phan_cong_sua_chua
    ADD CONSTRAINT fk_phan_cong_nhan_vien FOREIGN KEY (ma_nhan_vien)
    REFERENCES public.nhan_vien_ky_thuat (ma_nguoi_dung) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.phan_cong_sua_chua
    ADD CONSTRAINT fk_phan_cong_phieu_sua FOREIGN KEY (ma_phieu_sua)
    REFERENCES public.phieu_sua_chua (ma_phieu_sua) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.phieu_sua_chua
    ADD CONSTRAINT fk_phieu_sua_khach_hang FOREIGN KEY (ma_khach_hang)
    REFERENCES public.khach_hang (ma_nguoi_dung) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.phieu_sua_chua
    ADD CONSTRAINT fk_phieu_sua_lich_hen FOREIGN KEY (ma_lich_hen)
    REFERENCES public.lich_hen_sua_chua (ma_lich_hen) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE SET NULL;


ALTER TABLE IF EXISTS public.phieu_sua_chua
    ADD CONSTRAINT fk_phieu_sua_xe_khach_hang FOREIGN KEY (ma_xe_kh)
    REFERENCES public.xe_khach_hang (ma_xe_kh) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.thanh_toan
    ADD CONSTRAINT fk_thanh_toan_don_thue FOREIGN KEY (ma_don_thue)
    REFERENCES public.don_thue_xe (ma_don_thue) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE SET NULL;


ALTER TABLE IF EXISTS public.thanh_toan
    ADD CONSTRAINT fk_thanh_toan_khach_hang FOREIGN KEY (ma_khach_hang)
    REFERENCES public.khach_hang (ma_nguoi_dung) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;


ALTER TABLE IF EXISTS public.thanh_toan
    ADD CONSTRAINT fk_thanh_toan_phieu_sua FOREIGN KEY (ma_phieu_sua)
    REFERENCES public.phieu_sua_chua (ma_phieu_sua) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE SET NULL;


ALTER TABLE IF EXISTS public.xe_khach_hang
    ADD CONSTRAINT fk_xe_khach_hang_khach_hang FOREIGN KEY (ma_khach_hang)
    REFERENCES public.khach_hang (ma_nguoi_dung) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;

END;
