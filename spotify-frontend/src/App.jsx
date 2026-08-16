import { useState } from 'react';
import './App.css';

function App() {
  const [tracks, setTracks] = useState([]);
  const [error, setError] = useState(null);

  // Funkcja uderzająca do Spring Boota
  const fetchTopTracks = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/top-tracks', {
        // To jest absolutnie wymagane, żeby przeglądarka dołączyła ciastko sesji ze Springa!
        credentials: 'include'
      });

      if (response.status === 401 || response.status === 403) {
        setError('Nie jesteś zalogowana! Kliknij "Zaloguj przez Spotify".');
        return;
      }

      const data = await response.json();
      setTracks(data);
      setError(null);
    } catch (err) {
      setError('Wystąpił błąd komunikacji z serwerem Spring Boot.');
    }
  };

  return (
      <div style={{ textAlign: 'center', padding: '50px', fontFamily: 'sans-serif' }}>
        <h1>Mój React Spotify Wrapped 🚀</h1>

        {/* Krok 1: Przycisk logowania kieruje na nasz backend, który robi redirect do Spotify */}
        <div style={{ marginBottom: '20px' }}>
          <a
              href="http://localhost:8080/oauth2/authorization/spotify"
              style={{ padding: '10px 20px', background: '#1db954', color: 'black', textDecoration: 'none', borderRadius: '20px', fontWeight: 'bold', display: 'inline-block' }}
          >
            Zaloguj przez Spotify
          </a>
        </div>

        {/* Krok 2: Pobieranie danych */}
        <button onClick={fetchTopTracks} style={{ padding: '10px 20px', cursor: 'pointer' }}>
          Pobierz moje Top Tracks z API
        </button>

        {/* Wyświetlanie błędu (np. gdy ktoś nie jest zalogowany) */}
        {error && <p style={{ color: 'red', marginTop: '20px' }}>{error}</p>}

        {/* Wyświetlanie listy utworów */}
        <ul style={{ listStyle: 'none', padding: 0, marginTop: '30px' }}>
          {tracks.map((track, index) => (
              <li key={track.id || index} style={{ marginBottom: '10px', fontSize: '1.2rem' }}>
                {index + 1}. <strong>{track.name}</strong> - {track.artists[0]?.name}
              </li>
          ))}
        </ul>
      </div>
  );
}

export default App;