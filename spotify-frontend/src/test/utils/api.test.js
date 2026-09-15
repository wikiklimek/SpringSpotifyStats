import { describe, it, expect, vi } from 'vitest';
import { callApi } from '../../utils/api.js';
import { API_BASE_URL } from '../../utils/config.js';

describe('Funkcja callApi', () => {
    it('powinna rzucić błąd 401 z odpowiednim komunikatem', async () => {
        // GIVEN: Udajemy (mockujemy) globalną funkcję fetch, żeby zwróciła kod 401
        global.fetch = vi.fn().mockResolvedValue({
            status: 401
        });

        // WHEN & THEN: Wywołanie funkcji powinno rzucić błędem
        await expect(callApi('/tajny-endpoint'))
            .rejects
            .toThrow('musisz się zalogować ponownie');

        expect(global.fetch).toHaveBeenCalledWith(`${API_BASE_URL}/tajny-endpoint`, expect.any(Object));
    });

    it('powinna zwrócić dane gdy odpowiedź to 200 OK', async () => {
        // GIVEN
        global.fetch = vi.fn().mockResolvedValue({
            status: 200,
            ok: true,
            json: async () => ({ id: 1, name: "Test" }),
            headers: new Headers()
        });

        // WHEN
        const response = await callApi('/data');
        const data = await response.json();

        // THEN
        expect(data.name).toBe("Test");
    });
});