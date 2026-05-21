const DEFAULT_API_BASE_URL =
    process.env.NODE_ENV === 'development'
        ? `${window.location.protocol}//${window.location.hostname}:8080/api`
        : '/api';

export const API_BASE_URL = (process.env.REACT_APP_API_BASE_URL || DEFAULT_API_BASE_URL).replace(/\/+$/, '');

export async function apiRequest(endpoint, method = 'GET', body = null) {
    try {
        const headers = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        };

        const config = {
            method,
            headers,
            credentials: 'include',
        };

        if (body && (method === 'POST' || method === 'PUT')) {
            config.body = JSON.stringify(body);
        }

        const cleanEndpoint = endpoint.replace(/^\/+/, '');
        const url = `${API_BASE_URL}/${cleanEndpoint}`;

        const response = await fetch(url, config);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        if (method === 'DELETE' || response.status === 204) {
            return { success: true };
        }

        const contentType = response.headers.get('Content-Type');
        if (contentType && contentType.includes('application/json')) {
            return await response.json();
        }
        
        return { success: true };
    } catch (error) {
        console.error('API Request Error:', error);
        throw error;
    }
}