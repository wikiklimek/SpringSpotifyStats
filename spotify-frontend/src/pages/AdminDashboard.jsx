import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { callApi } from '../utils/api';
import { API_BASE_URL } from '../utils/config';

export default function AdminDashboard() {
    const [users, setUsers] = useState([]);
    const navigate = useNavigate();

    const loadUsers = async () => {
        try {
            const res = await callApi('/api/admin/users', {}, navigate);
            if (res) setUsers(await res.json());
        } catch (e) {
            console.error(e);
        }
    };

    useEffect(() => { loadUsers(); }, []);

    const deleteGlobal = async (days) => {
        if (!window.confirm(`Czy na pewno chcesz usunąć dokumenty starsze niż ${days} dni dla WSZYSTKICH?`)) return;
        try {
            const res = await callApi(`/api/admin/records?days=${days}`, { method: 'DELETE' }, navigate);
            if (res) {
                const data = await res.json();
                alert(`Pomyślnie usunięto ${data.deletedEntries} dokumentów z bazy!`);
                loadUsers();
            }
        } catch (e) { alert("Błąd: " + e.message); }
    };

    return (
        <div style={{ maxWidth: '900px', margin: '0 auto' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', background: '#181818', padding: '20px', borderRadius: '12px', marginBottom: '20px', alignItems: 'center' }}>
                <h1 style={{ color: '#ff9800', margin: 0 }}>Panel Administratora 🛡️</h1>
                <div style={{ display: 'flex', gap: '15px' }}>
                    <Link to="/admin/requests" style={{ color: 'white', textDecoration: 'none', fontWeight: 'bold' }}>Zgłoszenia RODO</Link>
                    <a href={`${API_BASE_URL}/logout`} style={{ color: '#b3b3b3', textDecoration: 'none' }}>Wyloguj</a>
                </div>
            </div>

            {/* Reszta kodu pozostaje bez zmian (Globalne czyszczenie i tabela użytkowników) */}
            <div style={{ background: '#282828', padding: '20px', borderRadius: '12px', marginBottom: '20px', textAlign: 'center' }}>
                <h3 style={{ margin: '0 0 15px 0', color: '#e91429' }}>Globalne Czyszczenie Bazy Danych</h3>
                <div style={{ display: 'flex', gap: '10px', justifyContent: 'center' }}>
                    {[7, 30, 90, 180].map(days => (
                        <button key={days} onClick={() => deleteGlobal(days)} style={{ background: '#e91429', color: 'white', padding: '8px 15px', borderRadius: '5px', border: 'none', cursor: 'pointer' }}>
                            Usuń wszystkie &gt; {days} dni
                        </button>
                    ))}
                </div>
            </div>

            <div style={{ background: '#181818', padding: '20px', borderRadius: '12px' }}>
                <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
                    <thead><tr style={{ borderBottom: '1px solid #333' }}><th>Użytkownik</th><th>Email</th><th>Zapisane dni w DB</th><th>Akcja</th></tr></thead>
                    <tbody>
                    {users.map(u => (
                        <tr key={u.spotifyId} style={{ borderBottom: '1px solid #282828' }}>
                            <td style={{ padding: '15px 0', fontWeight: 'bold' }}>{u.displayName}</td>
                            <td>{u.email}</td>
                            <td><span style={{ background: '#333', padding: '4px 8px', borderRadius: '10px' }}>{u.totalDocs}</span></td>
                            <td>
                                <Link to={`/admin/user/${u.spotifyId}`} style={{ background: '#1db954', color: 'black', padding: '6px 12px', borderRadius: '4px', textDecoration: 'none', fontWeight: 'bold' }}>Szczegóły &gt;</Link>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}