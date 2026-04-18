/* ============================================
   GARAOTO — Utils Module
   Format money, date, validation helpers
   ============================================ */

const Utils = {
    /**
     * Format number as Vietnamese currency
     * @param {number} amount
     * @returns {string} e.g. "1.500.000 ₫"
     */
    formatMoney(amount) {
        if (!amount && amount !== 0) return '—';
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND',
        }).format(amount);
    },

    /**
     * Format date string to Vietnamese format
     * @param {string} dateStr - ISO date string
     * @returns {string} e.g. "07/04/2026"
     */
    formatDate(dateStr) {
        if (!dateStr) return '—';
        const date = new Date(dateStr);
        return date.toLocaleDateString('vi-VN', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
        });
    },

    /**
     * Format datetime string
     * @param {string} dateStr
     * @returns {string} e.g. "07/04/2026 20:30"
     */
    formatDateTime(dateStr) {
        if (!dateStr) return '—';
        const date = new Date(dateStr);
        return date.toLocaleDateString('vi-VN', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
        });
    },

    /**
     * Get status badge HTML
     * @param {string} status
     * @returns {string} HTML string
     */
    getStatusBadge(status) {
        const statusMap = {
            // Nguoi dung
            'HoatDong': { label: 'Hoạt động', class: 'badge-success' },
            'Khoa': { label: 'Khóa', class: 'badge-danger' },
            // Lich hen
            'ChoXacNhan': { label: 'Chờ xác nhận', class: 'badge-warning' },
            'DaXacNhan': { label: 'Đã xác nhận', class: 'badge-info' },
            'DaHuy': { label: 'Đã hủy', class: 'badge-danger' },
            'HoanThanh': { label: 'Hoàn thành', class: 'badge-success' },
            // Phieu sua
            'TiepNhan': { label: 'Tiếp nhận', class: 'badge-info' },
            'DangSua': { label: 'Đang sửa', class: 'badge-warning' },
            'DaSuaXong': { label: 'Đã sửa xong', class: 'badge-primary' },
            'BanGiao': { label: 'Bàn giao', class: 'badge-success' },
            // Phan cong
            'DaPhanCong': { label: 'Đã phân công', class: 'badge-info' },
            'DangThucHien': { label: 'Đang thực hiện', class: 'badge-warning' },
            // Xe cho thue
            'SanSang': { label: 'Sẵn sàng', class: 'badge-success' },
            'DangThue': { label: 'Đang thuê', class: 'badge-warning' },
            'BaoTri': { label: 'Bảo trì', class: 'badge-gray' },
            // Don thue
            'ChoDuyet': { label: 'Chờ duyệt', class: 'badge-warning' },
            'DaTra': { label: 'Đã trả', class: 'badge-success' },
            // Thanh toan
            'ChoThanhToan': { label: 'Chờ thanh toán', class: 'badge-warning' },
            'DaThanhToan': { label: 'Đã thanh toán', class: 'badge-success' },
            'ThatBai': { label: 'Thất bại', class: 'badge-danger' },
        };

        const info = statusMap[status] || { label: status, class: 'badge-gray' };
        return `<span class="badge ${info.class}">${info.label}</span>`;
    },

    /**
     * Simple email validation
     */
    isValidEmail(email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    },

    /**
     * Simple phone validation (Vietnam)
     */
    isValidPhone(phone) {
        return /^(0[3|5|7|8|9])\d{8}$/.test(phone);
    },

    /**
     * Get URL query parameter
     */
    getUrlParam(name) {
        const params = new URLSearchParams(window.location.search);
        return params.get(name);
    },

    /**
     * Truncate text with ellipsis
     */
    truncate(text, maxLength = 100) {
        if (!text) return '';
        return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
    },

    /**
     * Debounce function
     */
    debounce(func, wait = 300) {
        let timeout;
        return function (...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(this, args), wait);
        };
    },

    escapeHtml(unsafe) {
        if (!unsafe) return '';
        return unsafe.toString()
             .replace(/&/g, "&amp;")
             .replace(/</g, "&lt;")
             .replace(/>/g, "&gt;")
             .replace(/"/g, "&quot;")
             .replace(/'/g, "&#039;");
    },
    
    escapeQuotes(str) {
        if (!str) return '';
        return str.toString().replace(/'/g, "\\'").replace(/"/g, "&quot;");
    }
};

export default Utils;
