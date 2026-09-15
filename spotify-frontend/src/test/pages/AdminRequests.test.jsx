import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { BrowserRouter } from 'react-router-dom';
import AdminRequests from '../../pages/AdminRequests.jsx';
import * as api from '../../utils/api.js'; // Pobieramy Twój moduł api.js
import '@testing-library/jest-dom';

vi.mock('../../utils/api', () => ({
    callApi: vi.fn()
}));

describe('Komponent AdminRequests (Zgłoszenia RODO)', () => {

    it('powinien wyświetlić komunikat gdy brak zgłoszeń', async () => {
        // GIVEN: Baza danych jest pusta
        api.callApi.mockResolvedValue({ json: async () => [] });

        render(<BrowserRouter><AdminRequests /></BrowserRouter>);

        // THEN
        await waitFor(() => {
            expect(screen.getByText(/Brak oczekujących próśb/i)).toBeInTheDocument();
        });
    });

    it('powinien wyrenderować tabelkę z prośbami użytkowników', async () => {
        // GIVEN: API wysyła jedną oczekującą prośbę
        api.callApi.mockResolvedValue({
            json: async () => [
                { id: 101, spotifyId: "uzytkownik_testowy", daysToKeep: 30 }
            ]
        });

        render(<BrowserRouter><AdminRequests /></BrowserRouter>);

        // THEN: Oczekujemy, że dane z JSONa idealnie wpasują się w komórki HTML
        await waitFor(() => {
            expect(screen.getByText('101')).toBeInTheDocument();
            expect(screen.getByText('uzytkownik_testowy')).toBeInTheDocument();
            expect(screen.getByText('Usuń starsze niż 30 dni')).toBeInTheDocument();
        });
    });

    it('powinien aktywować okienko potwierdzenia (confirm) przy akceptacji zgłoszenia', async () => {
        api.callApi.mockResolvedValue({
            json: async () => [{ id: 1, spotifyId: "userX", daysToKeep: 7 }]
        });

        // Zastępujemy przeglądarkowy pop-up
        window.confirm = vi.fn().mockReturnValue(false);

        render(<BrowserRouter><AdminRequests /></BrowserRouter>);

        // Czekamy aż pojawi się przycisk, a następnie w niego klikamy
        const acceptBtn = await screen.findByText('Akceptuj');
        fireEvent.click(acceptBtn);

        // THEN
        expect(window.confirm).toHaveBeenCalledWith('Czy na pewno chcesz ZATWIERDZIĆ prośbę i USUNĄĆ te dane z bazy?');
    });
});