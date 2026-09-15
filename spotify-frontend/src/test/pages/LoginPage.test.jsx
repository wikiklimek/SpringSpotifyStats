import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import LoginPage from '../../pages/LoginPage.jsx';
import '@testing-library/jest-dom';

describe('Komponent LoginPage', () => {
    it('powinien wyrenderować przycisk logowania przez Spotify', () => {
        // Renderuje widok w wirtualnej pamięci
        render(<LoginPage />);

        // Szuka na ekranie przycisku po jego tekście
        const spotifyBtn = screen.getByText(/Zaloguj przez Spotify/i);

        // Asercje: Przycisk musi istnieć i prowadzić do dobrego linku
        expect(spotifyBtn).toBeInTheDocument();
        expect(spotifyBtn.closest('a')).toHaveAttribute('href', expect.stringContaining('/oauth2/authorization/spotify'));
    });

    it('powinien wyrenderować formularz logowania dla administratora', () => {
        render(<LoginPage />);

        // Sprawdza czy inputy i przycisk logowania (formularz) są widoczne
        expect(screen.getByPlaceholderText('Login admina')).toBeInTheDocument();
        expect(screen.getByPlaceholderText('Hasło admina')).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /Zaloguj jako Admin/i })).toBeInTheDocument();
    });
});