import axios from "axios";

const rawBaseUrl = import.meta.env.VITE_API_BASE_URL || "";
export const API_BASE_URL = rawBaseUrl.endsWith('/') ? rawBaseUrl.slice(0, -1) : rawBaseUrl;

const api = axios.create({
    baseURL: `${API_BASE_URL}/api`,
    headers: {
        "Content-Type": "application/json",
    },
});

// Automatically attach JWT token
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => Promise.reject(error)
);

export default api;