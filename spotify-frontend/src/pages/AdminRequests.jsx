import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { callApi } from '../utils/api';

export default function AdminRequests() {
    const [pendingRequests, setPendingRequests] = useState([]);
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const loadRequests = async () => {
        try {
            const res = await callApi('/api/admin/requests', {}, navigate);
            if (res) setPendingRequests(await res.json());
        } catch (err) {
            setMessage('Błąd: ' + err.message);
        }
    };

    useEffect(() => { loadRequests(); }, [navigate]);

    const handleAction = async (id, action) => {
        // Okienka decyzyjne (Pop-upy)
        if (action === 'approve') {
            const isConfirmed = window.confirm("Czy na pewno chcesz ZATWIERDZIĆ prośbę i USUNĄĆ te dane z bazy?");
            if (!isConfirmed) return;
        } else if (action === 'reject') {
            const isConfirmed = window.confirm("Czy na pewno chcesz ODRZUCIĆ tę prośbę?");
            if (!isConfirmed) return;
        }

        try {
            const res = await callApi(`/api/admin/request/${id}/${action}`, { method: 'POST' }, navigate);
            if (res) {
                if (action === 'approve') {
                    const data = await res.json();
                    window.alert(`Sukces! Usunięto ${data.deletedEntries} dokumentów z bazy.`);
                } else {
                    window.alert("Prośba odrzucona.");
                }
                // Aktualizujemy listę
                setPendingRequests(pendingRequests.filter(req => req.id !== id));
            }
        } catch (e) {
            window.alert("Błąd: " + e.message);
        }
    };

    return (
        <div style={{ maxWidth: '900px', margin: '0 auto' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', background: '#181818', padding: '20px', borderRadius: '12px', marginBottom: '30px' }}>
                {/* Naprawiono nachodzenie na siebie tekstu */}
                <h2 style={{ color: '#ff9800', margin: 0 }}>Oczekujące Zgłoszenia RODO</h2>
                <Link to="/admin" style={{ color: '#b3b3b3', textDecoration: 'none', fontWeight: 'bold' }}>&lt; Powrót do Panelu</Link>
            </div>

            {message && <p style={{ color: '#e91429', textAlign: 'center', fontWeight: 'bold' }}>{message}</p>}

            <div style={{ background: '#181818', padding: '20px', borderRadius: '12px' }}>
                {pendingRequests.length === 0 && !message ? (
                    <p style={{ textAlign: 'center', color: '#b3b3b3' }}>Brak oczekujących próśb od użytkowników. 🎉</p>
                ) : (
                    <table style={{ width: '100%', borderCollapse: 'collapse', color: 'white' }}>
                        <thead>
                        <tr style={{ borderBottom: '1px solid #282828', textAlign: 'left' }}>
                            <th style={{ padding: '12px' }}>ID</th>
                            <th style={{ padding: '12px' }}>Spotify ID</th>
                            <th style={{ padding: '12px' }}>Żądanie</th>
                            <th style={{ padding: '12px' }}>Akcja</th>
                        </tr>
                        </thead>
                        <tbody>
                        {pendingRequests.map(req => (
                            <tr key={req.id} style={{ borderBottom: '1px solid #282828' }}>
                                <td style={{ padding: '12px' }}>{req.id}</td>
                                <td style={{ padding: '12px', fontWeight: 'bold' }}>{req.spotifyId}</td>
                                <td style={{ padding: '12px' }}>Usuń starsze niż {req.daysToKeep} dni</td>
                                <td style={{ padding: '12px', display: 'flex', gap: '10px' }}>
                                    <button onClick={() => handleAction(req.id, 'approve')} style={{ background: '#1db954', border: 'none', padding: '8px 12px', borderRadius: '4px', fontWeight: 'bold', cursor: 'pointer' }}>Akceptuj</button>
                                    <button onClick={() => handleAction(req.id, 'reject')} style={{ background: '#e91429', color: 'white', border: 'none', padding: '8px 12px', borderRadius: '4px', fontWeight: 'bold', cursor: 'pointer' }}>Odrzuć</button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    );
}