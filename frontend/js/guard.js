/* ============================================
   GARAOTO — Guard Module
   Route protection for authenticated pages
   ============================================ */
import Auth from './auth.js';

const Guard = {
    /**
     * Require authentication — redirect to login if not logged in
     * Call this at the top of protected pages
     */
    requireAuth() {
        if (!Auth.isLoggedIn()) {
            const returnUrl = encodeURIComponent(window.location.href);
            window.location.href = `/pages/auth/login.html?returnUrl=${returnUrl}`;
            return false;
        }
        return true;
    },

    /**
     * Guest only — redirect to home if already logged in
     * Call this on login/register pages
     */
    guestOnly() {
        if (Auth.isLoggedIn()) {
            window.location.href = '/';
            return false;
        }
        return true;
    }
};

export default Guard;
