/* ============================================
   GARAOTO — Toast Notification Module
   ============================================ */

const Toast = {
    _container: null,

    _ensureContainer() {
        if (!this._container) {
            this._container = document.createElement('div');
            this._container.className = 'toast-container';
            document.body.appendChild(this._container);
        }
    },

    /**
     * Show a toast notification
     * @param {string} message
     * @param {'success'|'error'|'warning'|'info'} type
     * @param {number} duration - ms before auto-dismiss
     */
    show(message, type = 'info', duration = 4000) {
        this._ensureContainer();

        const icons = {
            success: 'fa-solid fa-circle-check',
            error: 'fa-solid fa-circle-xmark',
            warning: 'fa-solid fa-triangle-exclamation',
            info: 'fa-solid fa-circle-info',
        };

        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.innerHTML = `
            <i class="toast-icon ${icons[type]}"></i>
            <span class="toast-message">${message}</span>
            <button class="toast-close" onclick="this.parentElement.remove()">
                <i class="fa-solid fa-xmark"></i>
            </button>
        `;

        this._container.appendChild(toast);

        // Auto remove
        setTimeout(() => {
            toast.classList.add('removing');
            setTimeout(() => toast.remove(), 300);
        }, duration);
    },

    success(message) { this.show(message, 'success'); },
    error(message) { this.show(message, 'error', 6000); },
    warning(message) { this.show(message, 'warning'); },
    info(message) { this.show(message, 'info'); },
};

export default Toast;
