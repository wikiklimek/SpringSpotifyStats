import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { BrowserRouter } from 'react-router-dom';
import UserDashboard from '../../pages/UserDashboard.jsx';
import * as api from '../../utils/api.js'; // Pobieramy Twój moduł api.js[cite: 6]
import '@testing-library/jest-dom';

// Mockujemy zewnętrzną funkcję, by nie strzelać fizycznie do backendu!
vi.mock('../../utils/api', () => ({
    callApi: vi.fn()
}));

describe('Komponent UserDashboard', () => {
    it('powinien wyświetlić listę Top Utworów po kliknięciu w przycisk', async () => {
        // GIVEN: Udajemy, że callApi z pliku api.js zwraca nam jeden utwór w formacie JSON
        api.callApi.mockResolvedValue({
            status: 200,
            json: async () => [{ name: "Super Song", artists: [{ name: "Super Artist" }] }]
        });

        // Musimy owinąć komponent w BrowserRouter, bo używa wewnątrz <Link>
        render(
            <BrowserRouter>
                <UserDashboard />
            </BrowserRouter>
        );

        // Zanim klikniemy, na ekranie jest napis powitalny
        expect(screen.getByText('Wybierz kategorię statystyk!')).toBeInTheDocument();

        // WHEN: Użytkownik wirtualnie klika w przycisk
        const topTracksBtn = screen.getByText('Top Utwory');
        fireEvent.click(topTracksBtn);

        // THEN: Czekamy (bo React podmienia stan asynchronicznie) aż pojawi się nasz wygenerowany utwór
        await waitFor(() => {
            expect(screen.getByText(/Twoje Top 10 Utworów/i)).toBeInTheDocument();
            expect(screen.getByText('Super Song')).toBeInTheDocument();
        });
    });
});