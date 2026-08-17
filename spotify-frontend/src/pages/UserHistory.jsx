import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { callApi } from '../utils/api';

export default function UserHistory() {
    const [history, setHistory] = useState([]);
    const [activeRequest, setActiveRequest] = useState(null);
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => { loadData(); }, []);

    const loadData = async () => {
        try {
            const histRes = await callApi('/api/history', {}, navigate);
            if (histRes && histRes.status === 200) setHistory(await histRes.json());

            const reqRes = await callApi('/api/privacy/status', {}, navigate);
            if (reqRes && reqRes.status === 200) {
                const textData = await reqRes.text();
                setActiveRequest(textData ? JSON.parse(textData) : null);
            } else {
                setActiveRequest(null);
            }
        } catch (e) {
            setMessage(`Błąd ładowania danych: ${e.message}`);
        }
    };

    const syncHistory = async () => {
        try {
            const res = await callApi('/api/recently-played/sync', {}, navigate);
            if (res) {
                const data = await res.json();
                setMessage(`✅ ${data.message}`);
            }
            loadData();
        } catch (e) { setMessage(`❌ Błąd synchronizacji: ${e.message}`); }
    };

    const requestDeletion = async (days) => {
        try {
            const res = await callApi(`/api/privacy/request-deletion?days=${days}`, { method: 'POST' }, navigate);
            if (res) {
                const data = await res.json();
                setMessage(`🛡️ ${data.message}`);
            }
            loadData();
        } catch (e) { setMessage(`❌ Błąd żądania RODO: ${e.message}`); }
    };

    const cancelRequest = async () => {
        try {
            const res = await callApi('/api/privacy/withdraw', { method: 'POST' }, navigate);
            if (res) {
                const data = await res.json();
                setMessage(`✅ ${data.message}`);
            }
            loadData();
        } catch (e) { setMessage(`❌ Błąd anulowania: ${e.message}`); }
    };

    return (
        <div style={{ maxWidth: '900px', margin: '0 auto' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', background: '#181818', padding: '20px', borderRadius: '12px', marginBottom: '20px' }}>
                <h1 style={{ color: '#1db954', margin: 0 }}>Historia Odsłuchań ⏱️</h1>
                <Link to="/user" style={{ color: '#b3b3b3', textDecoration: 'none', fontWeight: 'bold' }}>Powrót do Statystyk</Link>
            </div>

            <div style={{ textAlign: 'center', background: '#282828', padding: '20px', borderRadius: '12px', marginBottom: '20px' }}>
                <button onClick={syncHistory} style={{ background: '#1db954', color: 'black', border: 'none', padding: '10px 20px', borderRadius: '20px', fontWeight: 'bold', cursor: 'pointer', marginBottom: '15px' }}>🔄 Pobierz nowe z API Spotify</button>
                {message && <p style={{ color: 'white', fontWeight: 'bold' }}>{message}</p>}

                <hr style={{ borderColor: '#444' }}/>
                <h3 style={{ color: '#ff9800' }}>RODO - Zarządzanie Danymi</h3>

                {activeRequest ? (
                    <div>
                        <p>Masz aktywne żądanie usunięcia starszych niż: <b>{activeRequest.daysToKeep} dni</b>.</p>
                        <button onClick={cancelRequest} style={{ background: '#e91429', color: 'white', padding: '8px 15px', borderRadius: '5px', border: 'none', cursor: 'pointer' }}>Anuluj zapytanie</button>
                    </div>
                ) : (
                    <div style={{ display: 'flex', gap: '10px', justifyContent: 'center' }}>
                        {[7, 30, 90, 180].map(days => (
                            <button key={days} onClick={() => requestDeletion(days)} style={{ background: '#535353', color: 'white', padding: '8px 15px', borderRadius: '5px', border: 'none', cursor: 'pointer' }}>
                                Usuń &gt; {days} dni
                            </button>
                        ))}
                    </div>
                )}
            </div>

            <div style={{ background: '#181818', padding: '20px', borderRadius: '12px' }}>
                <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
                    <thead><tr style={{ borderBottom: '1px solid #333' }}><th>Tytuł</th><th>Artysta</th><th>Data Odsłuchania</th></tr></thead>
                    <tbody>
                    {history.map((h, i) => (
                        <tr key={i} style={{ borderBottom: '1px solid #282828' }}>
                            <td style={{ padding: '10px 0' }}>{h.track.name}</td>
                            <td>{h.track.artists[0]?.name}</td>
                            <td>{new Date(h.playedAt).toLocaleString()}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}