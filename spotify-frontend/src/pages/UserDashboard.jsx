import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { callApi } from '../utils/api';
import { API_BASE_URL } from '../utils/config';

export default function UserDashboard() {
    const [contentHtml, setContentHtml] = useState('<h2 style="text-align: center; color: #b3b3b3;">Wybierz kategorię statystyk!</h2>');
    const navigate = useNavigate();

    const fetchTopTracks = async () => {
        try {
            const res = await callApi('/api/top-tracks', {}, navigate);
            if (!res || res.status === 204) return;
            const tracks = await res.json();
            let html = '<h2 style="color: #1db954;">🔥 Twoje Top 10 Utworów</h2>';
            tracks.forEach((t, i) => html += `<div style="padding: 10px; border-bottom: 1px solid #282828;">${i + 1}. <b>${t.name}</b> - <span style="color:#b3b3b3">${t.artists[0]?.name}</span></div>`);
            setContentHtml(html);
        } catch (error) { setContentHtml(`<h3 style="text-align: center; color: #b3b3b3;">${error.message}</h3>`); }
    };

    const fetchTopArtists = async () => {
        try {
            const res = await callApi('/api/top-artists', {}, navigate);
            if (!res || res.status === 204) return;
            const artists = await res.json();
            let html = '<h2 style="color: #1db954;">🎤 Twoi Top 10 Artyści</h2>';
            artists.forEach((a, i) => html += `<div style="padding: 10px; border-bottom: 1px solid #282828;">${i + 1}. <b>${a.name}</b></div>`);
            setContentHtml(html);
        } catch (error) { setContentHtml(`<h3 style="text-align: center; color: #b3b3b3;">${error.message}</h3>`); }
    };

    const fetchTopGenres = async () => {
        try {
            const res = await callApi('/api/top-genres', {}, navigate);
            if (!res || res.status === 204) return;
            const genres = await res.json();
            let html = '<h2 style="color: #1db954;">🎸 Twoje Top Gatunki</h2><ol style="line-height: 2; color: white;">';
            for (const [g, count] of Object.entries(genres)) {
                html += `<li><strong>${g}</strong> <span style="color: #1db954;">(${count} wystąpień)</span></li>`;
            }
            html += '</ol>';
            setContentHtml(html);
        } catch (error) { setContentHtml(`<h3 style="text-align: center; color: #b3b3b3;">${error.message}</h3>`); }
    };

    const fetchCurrentlyPlaying = async () => {
        try {
            const res = await callApi('/api/currently-playing', {}, navigate);

            // Kod 204 oznacza "no content", czyli Spotify milczy.
            if (res.status === 204) {
                setContentHtml('<h3 style="text-align: center; color: #b3b3b3;">nic w tym momencie nie słuchasz</h3>');
                return;
            }

            const data = await res.json();
            if (!data || !data.item) {
                setContentHtml('<h3 style="text-align: center; color: #b3b3b3;">nic w tym momencie nie słuchasz</h3>');
                return;
            }

            const track = data.item;
            const img = track.album?.images?.[0]?.url || '/images/default.png';
            setContentHtml(`
              <div style="text-align: center;">
                <h2 style="color: #1db954;">${data.is_playing ? '🔊 Teraz odtwarzane' : '⏸ Wstrzymane'}</h2>
                <img src="${img}" width="180" height="180" style="border-radius: 8px; margin: 15px 0;" />
                <h2 style="color: white;">${track.name}</h2>
                <h4 style="color: #b3b3b3;">${track.artists[0]?.name}</h4>
              </div>
            `);
        } catch (error) {
            // Jeśli wystąpił błąd "musisz się zalogować ponownie", to i tak navigate wyrzuci Cię na /
            // Jeśli to inny błąd API, wyświetlamy napis:
            if (error.message !== 'musisz się zalogować ponownie') {
                setContentHtml(`<h3 style="text-align: center; color: #b3b3b3;">nie możemy połączyć się ze Spotify: ${error.message}</h3>`);
            }
        }
    };

    return (
        <div style={{ maxWidth: '900px', margin: '0 auto' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', background: '#181818', padding: '20px', borderRadius: '12px', marginBottom: '20px' }}>
                <h1 style={{ color: '#1db954', margin: 0 }}>Statystyki Spotify 🎧</h1>
                <div style={{ display: 'flex', gap: '15px' }}>
                    <Link to="/user/history" style={{ color: 'white', textDecoration: 'none', fontWeight: 'bold' }}>Moja Historia ⏱️</Link>
                    <a href={`${API_BASE_URL}/logout`} style={{ color: '#e91429', textDecoration: 'none', fontWeight: 'bold' }}>Wyloguj</a>
                </div>
            </div>

            <div style={{ display: 'flex', gap: '10px', justifyContent: 'center', marginBottom: '20px', flexWrap: 'wrap' }}>
                <button onClick={fetchTopTracks} style={btnStyle}>🔥 Top Utwory</button>
                <button onClick={fetchTopArtists} style={btnStyle}>🎤 Top Artyści</button>
                <button onClick={fetchTopGenres} style={btnStyle}>🎸 Top Gatunki</button>
                <button onClick={fetchCurrentlyPlaying} style={btnStyle}>🎧 Teraz Odtwarzane</button>
            </div>

            <div style={{ background: '#181818', padding: '30px', borderRadius: '12px' }} dangerouslySetInnerHTML={{ __html: contentHtml }} />
        </div>
    );
}

const btnStyle = { backgroundColor: '#282828', color: 'white', border: 'none', padding: '12px 20px', borderRadius: '30px', fontWeight: 'bold', cursor: 'pointer' };