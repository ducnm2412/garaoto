/* ============================================
   GARAOTO — Components Module
   Load shared HTML fragments (header, footer)
   ============================================ */
import Auth from './auth.js';

const Components = {
    /**
     * Load and inject the header
     */
    async loadHeader() {
        const placeholder = document.getElementById('header-placeholder');
        if (!placeholder) return;

        const isLoggedIn = Auth.isLoggedIn();
        const userName = Auth.getUserName();
        const initials = Auth.getUserInitials();

        // Determine current page for active nav
        const currentPath = window.location.pathname;

        const getActiveClass = (path) => {
            if (path === '/' && (currentPath === '/' || currentPath === '/index.html')) return 'active';
            if (path !== '/' && currentPath.startsWith(path)) return 'active';
            return '';
        };

        placeholder.innerHTML = `
        <header class="header" id="main-header">
            <div class="container">
                <a href="/" class="header-logo">
                    <i class="fa-solid fa-car-side"></i>
                    GaraOto
                </a>

                <nav class="header-nav" id="main-nav">
                    <a href="/" class="${getActiveClass('/')}">Trang chủ</a>
                    <a href="/pages/dich-vu/index.html" class="${getActiveClass('/pages/dich-vu')}">Dịch vụ</a>
                    <a href="/pages/xe-cho-thue/index.html" class="${getActiveClass('/pages/xe-cho-thue')}">Xe cho thuê</a>
                    <a href="/pages/gioi-thieu.html" class="${getActiveClass('/pages/gioi-thieu')}">Giới thiệu</a>
                    <a href="/pages/lien-he.html" class="${getActiveClass('/pages/lien-he')}">Liên hệ</a>
                </nav>

                <div class="header-actions">
                    ${isLoggedIn ? `
                    <div class="user-menu" id="user-menu">
                        <button class="user-menu-toggle" id="user-menu-toggle">
                            <div class="user-avatar">${initials}</div>
                            <span>${userName}</span>
                            <i class="fa-solid fa-chevron-down" style="font-size: 10px;"></i>
                        </button>
                        <div class="user-dropdown" id="user-dropdown">
                            ${Auth.getUser()?.vaiTro === 'Admin' ? `
                            <a href="/pages/admin/index.html">
                                <i class="fa-solid fa-user-shield"></i> Trang Quản Trị
                            </a>
                            ` : `
                            <a href="/pages/tai-khoan/index.html">
                                <i class="fa-solid fa-gauge-high"></i> Dashboard
                            </a>
                            `}
                            <a href="/pages/tai-khoan/cap-nhat.html">
                                <i class="fa-solid fa-user-pen"></i> Thông tin cá nhân
                            </a>
                            <a href="/pages/tai-khoan/xe-cua-toi.html">
                                <i class="fa-solid fa-car"></i> Xe của tôi
                            </a>
                            <a href="/pages/tai-khoan/lich-hen.html">
                                <i class="fa-solid fa-calendar-check"></i> Lịch hẹn
                            </a>
                            <a href="/pages/tai-khoan/don-thue.html">
                                <i class="fa-solid fa-file-contract"></i> Đơn thuê xe
                            </a>
                            <div class="divider"></div>
                            <button class="logout-btn" id="logout-btn">
                                <i class="fa-solid fa-right-from-bracket"></i> Đăng xuất
                            </button>
                        </div>
                    </div>
                    ` : `
                    <a href="/pages/auth/login.html" class="btn btn-outline btn-sm">Đăng nhập</a>
                    <a href="/pages/auth/register.html" class="btn btn-primary btn-sm">Đăng ký</a>
                    `}
                    <button class="mobile-menu-btn" id="mobile-menu-btn">
                        <i class="fa-solid fa-bars"></i>
                    </button>
                </div>
            </div>
        </header>`;

        // Init header behaviors
        this._initHeaderScroll();
        this._initMobileMenu();
        if (isLoggedIn) {
            this._initUserMenu();
        }
    },

    /**
     * Load and inject the footer
     */
    async loadFooter() {
        const placeholder = document.getElementById('footer-placeholder');
        if (!placeholder) return;

        placeholder.innerHTML = `
        <footer class="footer">
            <div class="container">
                <div class="footer-grid">
                    <div>
                        <div class="footer-brand">
                            <i class="fa-solid fa-car-side"></i>
                            GaraOto
                        </div>
                        <p class="footer-desc">
                            Hệ thống gara ô tô chuyên nghiệp — cung cấp dịch vụ sửa chữa, bảo dưỡng 
                            và cho thuê xe uy tín hàng đầu. Cam kết chất lượng và sự hài lòng của khách hàng.
                        </p>
                        <div class="footer-social">
                            <a href="#"><i class="fa-brands fa-facebook-f"></i></a>
                            <a href="#"><i class="fa-brands fa-youtube"></i></a>
                            <a href="#"><i class="fa-brands fa-tiktok"></i></a>
                            <a href="#"><i class="fa-solid fa-envelope"></i></a>
                        </div>
                    </div>

                    <div>
                        <h4 class="footer-title">Dịch vụ</h4>
                        <div class="footer-links">
                            <a href="/pages/dich-vu/index.html">Sửa chữa ô tô</a>
                            <a href="/pages/dich-vu/index.html">Bảo dưỡng định kỳ</a>
                            <a href="/pages/xe-cho-thue/index.html">Cho thuê xe</a>
                            <a href="/pages/dat-lich/index.html">Đặt lịch hẹn</a>
                        </div>
                    </div>

                    <div>
                        <h4 class="footer-title">Liên kết</h4>
                        <div class="footer-links">
                            <a href="/pages/gioi-thieu.html">Giới thiệu</a>
                            <a href="/pages/lien-he.html">Liên hệ</a>
                            <a href="#">Chính sách bảo mật</a>
                            <a href="#">Điều khoản sử dụng</a>
                        </div>
                    </div>

                    <div>
                        <h4 class="footer-title">Liên hệ</h4>
                        <div class="footer-contact-item">
                            <i class="fa-solid fa-location-dot"></i>
                            <span>123 Nguyễn Văn Linh, Quận 7, TP.HCM</span>
                        </div>
                        <div class="footer-contact-item">
                            <i class="fa-solid fa-phone"></i>
                            <span>0123 456 789</span>
                        </div>
                        <div class="footer-contact-item">
                            <i class="fa-solid fa-envelope"></i>
                            <span>contact@garaoto.vn</span>
                        </div>
                        <div class="footer-contact-item">
                            <i class="fa-solid fa-clock"></i>
                            <span>T2 - CN: 7:00 - 19:00</span>
                        </div>
                    </div>
                </div>

                <div class="footer-bottom">
                    <span>&copy; 2026 GaraOto. All rights reserved.</span>
                    <span>Made with <i class="fa-solid fa-heart" style="color: var(--danger);"></i> in Vietnam</span>
                </div>
            </div>
        </footer>`;
    },

    /* --- Private: Header scroll effect --- */
    _initHeaderScroll() {
        const header = document.getElementById('main-header');
        if (!header) return;

        window.addEventListener('scroll', () => {
            if (window.scrollY > 20) {
                header.classList.add('scrolled');
            } else {
                header.classList.remove('scrolled');
            }
        });
    },

    /* --- Private: Mobile menu toggle --- */
    _initMobileMenu() {
        const btn = document.getElementById('mobile-menu-btn');
        const nav = document.getElementById('main-nav');
        if (!btn || !nav) return;

        btn.addEventListener('click', () => {
            nav.classList.toggle('show');
            const icon = btn.querySelector('i');
            icon.className = nav.classList.contains('show')
                ? 'fa-solid fa-xmark'
                : 'fa-solid fa-bars';
        });
    },

    /* --- Private: User dropdown menu --- */
    _initUserMenu() {
        const toggle = document.getElementById('user-menu-toggle');
        const dropdown = document.getElementById('user-dropdown');
        const logoutBtn = document.getElementById('logout-btn');

        if (!toggle || !dropdown) return;

        toggle.addEventListener('click', (e) => {
            e.stopPropagation();
            dropdown.classList.toggle('show');
        });

        // Close on click outside
        document.addEventListener('click', () => {
            dropdown.classList.remove('show');
        });

        dropdown.addEventListener('click', (e) => {
            e.stopPropagation();
        });

        // Logout
        if (logoutBtn) {
            logoutBtn.addEventListener('click', () => {
                Auth.logoutAndRedirect();
            });
        }
    },

    /**
     * Show "Login Required" modal
     */
    showLoginRequired() {
        const overlay = document.createElement('div');
        overlay.className = 'modal-overlay show login-required-modal';
        overlay.innerHTML = `
            <div class="modal">
                <div class="modal-body">
                    <i class="fa-solid fa-lock"></i>
                    <h3>Yêu cầu đăng nhập</h3>
                    <p>Bạn cần đăng nhập để sử dụng chức năng này.</p>
                    <div class="btn-group">
                        <button class="btn btn-ghost" onclick="this.closest('.modal-overlay').remove()">Đóng</button>
                        <a href="/pages/auth/login.html?returnUrl=${encodeURIComponent(window.location.href)}" class="btn btn-primary">
                            Đăng nhập ngay
                        </a>
                    </div>
                </div>
            </div>
        `;

        document.body.appendChild(overlay);

        overlay.addEventListener('click', (e) => {
            if (e.target === overlay) overlay.remove();
        });
    },

    /**
     * Show Beautiful UI Confirm Modal
     */
    confirm(message) {
        return new Promise(resolve => {
            const overlay = document.createElement('div');
            overlay.className = 'modal-overlay show';
            overlay.innerHTML = `
                <div class="modal" style="display: block; max-width: 400px; border-radius: var(--radius-lg); text-align: center; border-top: 4px solid var(--primary-500);">
                    <div class="modal-body" style="padding: 2rem;">
                        <div style="font-size: 3.5rem; color: var(--primary-500); margin-bottom: 1rem;">
                            <i class="fa-solid fa-circle-question"></i>
                        </div>
                        <h3 style="margin-bottom: 0.5rem">Xác nhận thao tác</h3>
                        <p style="color: var(--gray-600); margin-bottom: 2rem">${message}</p>
                        <div style="display: flex; gap: 1rem; justify-content: center;">
                            <button class="btn btn-ghost" id="btn-modal-cancel">Hủy</button>
                            <button class="btn btn-primary" id="btn-modal-ok">Đồng ý</button>
                        </div>
                    </div>
                </div>
            `;
            document.body.appendChild(overlay);

            const btnCancel = overlay.querySelector('.btn-ghost');
            const btnOk = overlay.querySelector('.btn-primary');

            btnCancel.onclick = () => {
                overlay.classList.remove('show');
                setTimeout(() => overlay.remove(), 200);
                resolve(false);
            };
            btnOk.onclick = () => {
                overlay.classList.remove('show');
                setTimeout(() => overlay.remove(), 200);
                resolve(true);
            };
        });
    }
};

export default Components;
