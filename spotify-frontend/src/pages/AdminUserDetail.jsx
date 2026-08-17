import { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { callApi } from '../utils/api';

export default function AdminUserDetail() {
    const { spotifyId } = useParams();
    const navigate = useNavigate();
    const [docs, setDocs] = useState([]);
    const [filterType, setFilterType] = useState('ALL');
    const [sortOrder, setSortOrder] = useState('DESC');

    const loadDocs = async () => {
        const res = await callApi(`/api/admin/users/${spotifyId}/docs`, {}, navigate);
        if (res) {
            const data = await res.json();
            const combined = [
                ...data.tracks.map(d => ({ ...d, type: 'TRACKS' })),
                ...data.artists.map(d => ({ ...d, type: 'ARTISTS' }))
            ];
            setDocs(combined);
        }
    };

    useEffect(() => { loadDocs(); }, [spotifyId]);

    const deleteLocal = async (days) => {
        if (!window.confirm(`Usunąć dokumenty starsze niż ${days} dni dla Tego Użytkownika?`)) return;
        const res = await callApi(`/api/admin/records?days=${days}&spotifyId=${spotifyId}`, { method: 'DELETE' }, navigate);
        if (res) {
            const data = await res.json();
            alert(`Usunięto ${data.deletedEntries} wpisów!`);
            loadDocs();
        }
    };

    const filteredDocs = docs
        .filter(d => filterType === 'ALL' ? true : d.type === filterType)
        .sort((a, b) => sortOrder === 'DESC' ? new Date(b.date) - new Date(a.date) : new Date(a.date) - new Date(b.date));

    return (
        <div style={{ maxWidth: '900px', margin: '0 auto' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', background: '#181818', padding: '20px', borderRadius: '12px', marginBottom: '20px' }}>
                <h1 style={{ color: '#ff9800', margin: 0 }}>Dokumenty Użytkownika</h1>
                <Link to="/admin" style={{ color: '#b3b3b3', textDecoration: 'none' }}>&lt; Powrót do listy</Link>
            </div>

            <div style={{ background: '#282828', padding: '20px', borderRadius: '12px', marginBottom: '20px', textAlign: 'center' }}>
                <h3 style={{ margin: '0 0 15px 0' }}>Usuwanie z bazy dla tego użytkownika</h3>
                <div style={{ display: 'flex', gap: '10px', justifyContent: 'center' }}>
                    {[7, 30, 90, 180].map(days => (
                        <button key={days} onClick={() => deleteLocal(days)} style={{ background: '#e91429', color: 'white', padding: '8px 15px', borderRadius: '5px', border: 'none', cursor: 'pointer' }}>
                            Usuń &gt; {days} dni
                        </button>
                    ))}
                </div>
            </div>

            <div style={{ background: '#181818', padding: '20px', borderRadius: '12px' }}>
                <div style={{ display: 'flex', gap: '20px', marginBottom: '20px' }}>
                    <select value={filterType} onChange={e => setFilterType(e.target.value)} style={{ padding: '8px', background: '#333', color: 'white', border: 'none', borderRadius: '4px' }}>
                        <option value="ALL">Wszystkie typy</option>
                        <option value="TRACKS">Tylko Tracks</option>
                        <option value="ARTISTS">Tylko Artists</option>
                    </select>
                    <select value={sortOrder} onChange={e => setSortOrder(e.target.value)} style={{ padding: '8px', background: '#333', color: 'white', border: 'none', borderRadius: '4px' }}>
                        <option value="DESC">Najnowsze najpierw</option>
                        <option value="ASC">Najstarsze najpierw</option>
                    </select>
                </div>

                <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
                    <thead><tr style={{ borderBottom: '1px solid #333' }}><th>Typ</th><th>Data w bazie</th><th>Ilość elementów</th></tr></thead>
                    <tbody>
                    {filteredDocs.map((d, i) => (
                        <tr key={i} style={{ borderBottom: '1px solid #282828' }}>
                            <td style={{ padding: '10px 0', color: d.type === 'TRACKS' ? '#1db954' : '#ff9800', fontWeight: 'bold' }}>{d.type}</td>
                            <td>{d.date}</td>
                            <td>{d.topTracks ? d.topTracks.length : d.topArtists?.length}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}