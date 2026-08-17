import { API_BASE_URL } from './config';

export const callApi = async (endpoint, options = {}, navigate) => {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            ...options,
            credentials: 'include'
        });

        if (response.status === 401) {
            throw new Error('musisz się zalogować ponownie');
        }

        if (response.status === 204) return response; // Nic nie gra w Spotify

        // Jeśli Spring odeśle stronę logowania (HTML)
        const contentType = response.headers.get("content-type");
        if (contentType && contentType.includes("text/html")) {
            if (navigate) navigate('/');
            throw new Error('musisz się zalogować ponownie');
        }

        if (!response.ok) {
            let errMsg = "błąd bazy danych";
            try {
                const errData = await response.json();
                if (errData.error) errMsg = errData.error;
            } catch(e) {}
            throw new Error(errMsg);
        }

        return response;
    } catch (err) {
        if (err.message === 'musisz się zalogować ponownie' && navigate) {
            navigate('/');
        }
        throw err; // Przekazujemy błąd wyżej
    }
};