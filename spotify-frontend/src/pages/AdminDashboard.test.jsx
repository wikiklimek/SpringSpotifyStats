import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { BrowserRouter } from 'react-router-dom';
import AdminDashboard from './AdminDashboard';
import * as api from '../utils/api';

// Podmieniamy funkcję komunikującą się z backendem
vi.mock('../utils/api', () => ({
    callApi: vi.fn()
}));

describe('Komponent AdminDashboard', () => {

    it('powinien pobrać i wyświetlić listę użytkowników w tabeli', async () => {
        // GIVEN: Udajemy odpowiedź z backendu
        api.callApi.mockResolvedValue({
            json: async () => [
                { spotifyId: "user1", displayName: "Wiktoria", email: "test@test.pl", totalDocs: 42 }
            ]
        });

        // WHEN: Renderujemy widok Admina
        render(
            <BrowserRouter>
                <AdminDashboard />
            </BrowserRouter>
        );

        // THEN: Oczekujemy aż dane "spłyną" asynchronicznie i pojawią się w HTMLu
        await waitFor(() => {
            expect(screen.getByText('Wiktoria')).toBeInTheDocument();
            expect(screen.getByText('test@test.pl')).toBeInTheDocument();
            expect(screen.getByText('42')).toBeInTheDocument();
        });
    });

    it('powinien wyświetlić okno potwierdzenia (confirm) przy usuwaniu globalnym', async () => {
        api.callApi.mockResolvedValue({ json: async () => [] });

        // GIVEN: Nadpisujemy (mockujemy) wbudowaną przeglądarkową funkcję okienka "Czy na pewno?"
        window.confirm = vi.fn().mockReturnValue(false); // Użytkownik klika "Anuluj"

        render(
            <BrowserRouter>
                <AdminDashboard />
            </BrowserRouter>
        );

        // WHEN: Klikamy czerwony przycisk usuwania starszych niż 7 dni
        const deleteBtn = screen.getByText('Usuń wszystkie > 7 dni');
        fireEvent.click(deleteBtn);

        // THEN: Okienko powinno się pokazać
        expect(window.confirm).toHaveBeenCalledWith('Czy na pewno chcesz usunąć dokumenty starsze niż 7 dni dla WSZYSTKICH?');
    });
});