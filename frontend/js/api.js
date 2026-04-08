/* ============================================
   GARAOTO — API Module
   HTTP client with JWT auto-attach
   ============================================ */
import CONFIG from './config.js';
import Auth from './auth.js';

const Api = {
    /**
     * Build headers with JWT token
     */
    _getHeaders(isJson = true) {
        const headers = {};
        if (isJson) {
            headers['Content-Type'] = 'application/json';
        }
        const token = Auth.getToken();
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
        return headers;
    },

    /**
     * Handle API response
     */
    async _handleResponse(response) {
        // Token expired or unauthorized
        if (response.status === 401) {
            Auth.logoutAndRedirect();
            throw new Error('Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.');
        }

        // Forbidden
        if (response.status === 403) {
            throw new Error('Bạn không có quyền thực hiện hành động này.');
        }

        // Parse response
        const data = await response.json().catch(() => null);

        if (!response.ok) {
            const message = data?.message || data?.error || `Lỗi ${response.status}`;
            throw new Error(message);
        }

        return data;
    },

    /**
     * GET request
     */
    async get(endpoint) {
        const response = await fetch(`${CONFIG.API_BASE_URL}${endpoint}`, {
            method: 'GET',
            headers: this._getHeaders(),
        });
        return this._handleResponse(response);
    },

    /**
     * POST request
     */
    async post(endpoint, body) {
        const response = await fetch(`${CONFIG.API_BASE_URL}${endpoint}`, {
            method: 'POST',
            headers: this._getHeaders(),
            body: JSON.stringify(body),
        });
        return this._handleResponse(response);
    },

    /**
     * PUT request
     */
    async put(endpoint, body) {
        const response = await fetch(`${CONFIG.API_BASE_URL}${endpoint}`, {
            method: 'PUT',
            headers: this._getHeaders(),
            body: JSON.stringify(body),
        });
        return this._handleResponse(response);
    },

    /**
     * DELETE request
     */
    async delete(endpoint) {
        const response = await fetch(`${CONFIG.API_BASE_URL}${endpoint}`, {
            method: 'DELETE',
            headers: this._getHeaders(),
        });
        return this._handleResponse(response);
    },

    /**
     * PATCH request
     */
    async patch(endpoint, body = {}) {
        const response = await fetch(`${CONFIG.API_BASE_URL}${endpoint}`, {
            method: 'PATCH',
            headers: this._getHeaders(),
            body: Object.keys(body).length > 0 ? JSON.stringify(body) : undefined,
        });
        return this._handleResponse(response);
    },
};

export default Api;
