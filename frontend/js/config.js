/* ============================================
   GARAOTO — Config
   ============================================ */

// Doi URL nay thanh dia chi Backend sau khi deploy (vd: Render)
const PRODUCTION_API_URL = 'https://garaoto-api.onrender.com/api';

const isLocal = ['localhost', '127.0.0.1'].includes(window.location.hostname);

const CONFIG = {
    API_BASE_URL: isLocal ? 'http://localhost:8080/api' : PRODUCTION_API_URL,
    TOKEN_KEY: 'garaoto_token',
    USER_KEY: 'garaoto_user',
    APP_NAME: 'GaraOto',
};

export default CONFIG;
