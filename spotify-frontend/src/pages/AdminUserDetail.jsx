import { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { callApi } from '../utils/api';
import { ArrowLeft, Trash2, Filter } from 'lucide-react';

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
        <div className="max-w-4xl mx-auto pb-10">
            {/* Nagłówek */}
            <div className="flex justify-between items-center bg-spotify-card p-5 px-7 rounded-2xl mb-5 border border-spotify-border shadow-lg">
                <h1 className="text-amber-500 text-2xl font-bold m-0">Dokumenty Użytkownika</h1>
                <Link to="/admin" className="flex items-center gap-1 text-spotify-gray hover:text-white font-semibold text-sm transition-colors">
                    <ArrowLeft className="w-4 h-4" /> &lt; Powrót do listy
                </Link>
            </div>

            {/* Panel usuwania dla użytkownika */}
            <div className="bg-neutral-900/90 p-5 rounded-2xl mb-5 text-center border border-neutral-800 shadow-md">
                <div className="flex items-center justify-center gap-2 mb-3">
                    <Trash2 className="w-4 h-4 text-red-500" />
                    <h3 className="m-0 text-white text-sm font-semibold">Usuwanie z bazy dla tego użytkownika</h3>
                </div>
                <div className="flex gap-2.5 justify-center flex-wrap">
                    {[7, 30, 90, 180].map(days => (
                        <button
                            key={days}
                            onClick={() => deleteLocal(days)}
                            className="bg-red-600 hover:bg-red-500 text-white py-2 px-4 rounded-full font-semibold text-xs transition-colors cursor-pointer shadow-sm"
                        >
                            Usuń &gt; {days} dni
                        </button>
                    ))}
                </div>
            </div>

            {/* Filtry i Tabela */}
            <div className="bg-spotify-card p-6 rounded-2xl border border-spotify-border shadow-lg">
                <div className="flex items-center gap-3 mb-5">
                    <Filter className="w-4 h-4 text-spotify-gray" />
                    <select
                        value={filterType}
                        onChange={e => setFilterType(e.target.value)}
                        className="bg-neutral-900 border border-neutral-800 text-white text-xs rounded-lg p-2 focus:outline-none"
                    >
                        <option value="ALL">Wszystkie typy</option>
                        <option value="TRACKS">Tylko Tracks</option>
                        <option value="ARTISTS">Tylko Artists</option>
                    </select>
                    <select
                        value={sortOrder}
                        onChange={e => setSortOrder(e.target.value)}
                        className="bg-neutral-900 border border-neutral-800 text-white text-xs rounded-lg p-2 focus:outline-none"
                    >
                        <option value="DESC">Najnowsze najpierw</option>
                        <option value="ASC">Najstarsze najpierw</option>
                    </select>
                </div>

                <table className="w-full border-collapse text-left">
                    <thead>
                    <tr className="border-b border-neutral-800 text-spotify-gray text-xs uppercase tracking-wider">
                        <th className="p-3 px-4">Typ</th>
                        <th className="p-3 px-4">Data w bazie</th>
                        <th className="p-3 px-4">Ilość elementów</th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredDocs.map((d, i) => (
                        <tr key={i} className="border-b border-neutral-900 hover:bg-neutral-900/30 transition-colors">
                            <td className={`p-3.5 px-4 font-bold text-sm ${d.type === 'TRACKS' ? 'text-spotify-green' : 'text-amber-500'}`}>
                                {d.type}
                            </td>
                            <td className="p-3.5 px-4 text-sm text-white">{d.date}</td>
                            <td className="p-3.5 px-4 text-sm text-spotify-gray">{d.topTracks ? d.topTracks.length : d.topArtists?.length}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}