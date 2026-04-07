/* ============================================
   GARAOTO — Auth Module
   Manage login state, token, user info
   ============================================ */
import CONFIG from './config.js';

const Auth = {
    /**
     * Save auth data after login
     */
    saveAuth(token, user) {
        localStorage.setItem(CONFIG.TOKEN_KEY, token);
        localStorage.setItem(CONFIG.USER_KEY, JSON.stringify(user));
    },

    /**
     * Get JWT token
     */
    getToken() {
        return localStorage.getItem(CONFIG.TOKEN_KEY);
    },

    /**
     * Get current user info
     */
    getUser() {
        const data = localStorage.getItem(CONFIG.USER_KEY);
        if (!data) return null;
        try {
            return JSON.parse(data);
        } catch {
            return null;
        }
    },

    /**
     * Check if user is logged in
     */
    isLoggedIn() {
        const token = this.getToken();
        if (!token) return false;

        // Check if token is expired
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const expiry = payload.exp * 1000; // Convert to ms
            if (Date.now() >= expiry) {
                this.logout();
                return false;
            }
            return true;
        } catch {
            this.logout();
            return false;
        }
    },

    /**
     * Get user display name
     */
    getUserName() {
        const user = this.getUser();
        return user?.hoTen || user?.email || 'Người dùng';
    },

    /**
     * Get user initials for avatar
     */
    getUserInitials() {
        const name = this.getUserName();
        const parts = name.trim().split(' ');
        if (parts.length >= 2) {
            return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
        }
        return name.substring(0, 2).toUpperCase();
    },

    /**
     * Logout — clear all auth data
     */
    logout() {
        localStorage.removeItem(CONFIG.TOKEN_KEY);
        localStorage.removeItem(CONFIG.USER_KEY);
    },

    /**
     * Logout and redirect to login page
     */
    logoutAndRedirect() {
        this.logout();
        window.location.href = '/pages/auth/login.html';
    }
};

export default Auth;
