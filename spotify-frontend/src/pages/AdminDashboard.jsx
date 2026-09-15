import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { callApi } from '../utils/api';
import { API_BASE_URL } from '../utils/config';
import { ShieldCheck, Trash2, Users, LogOut } from 'lucide-react';

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
        <div className="max-w-4xl mx-auto">
            {/* Pasek nawigacyjny */}
            <div className="flex justify-between items-center bg-spotify-card p-5 px-7 rounded-2xl mb-5 border border-spotify-border shadow-lg">
                <div className="flex items-center gap-2.5">
                    <ShieldCheck className="w-6 h-6 text-amber-500" />
                    <h1 className="text-amber-500 text-2xl font-bold m-0">Panel Administratora ️</h1>
                </div>
                <div className="flex items-center gap-5">
                    <Link to="/admin/requests" className="text-white hover:text-amber-400 font-semibold text-sm transition-colors">
                        Zgłoszenia RODO
                    </Link>
                    <a href={`${API_BASE_URL}/logout`} className="flex items-center gap-1.5 text-spotify-gray hover:text-white text-sm font-semibold transition-colors">
                        <LogOut className="w-4 h-4" /> Wyloguj
                    </a>
                </div>
            </div>

            {/* Globalne czyszczenie bazy */}
            <div className="bg-neutral-900/90 p-5 rounded-2xl mb-5 text-center border border-neutral-800 shadow-md">
                <div className="flex items-center justify-center gap-2 mb-3">
                    <Trash2 className="w-4 h-4 text-red-500" />
                    <h3 className="m-0 text-red-500 text-sm font-bold uppercase tracking-wider">Globalne Czyszczenie Bazy Danych</h3>
                </div>
                <div className="flex gap-2.5 justify-center flex-wrap">
                    {[7, 30, 90, 180].map(days => (
                        <button
                            key={days}
                            onClick={() => deleteGlobal(days)}
                            className="bg-red-600/90 hover:bg-red-600 text-white py-2 px-4 rounded-full font-semibold text-xs transition-colors cursor-pointer shadow-sm"
                        >
                            Usuń wszystkie &gt; {days} dni
                        </button>
                    ))}
                </div>
            </div>

            {/* Tabela zarejestrowanych użytkowników */}
            <div className="bg-spotify-card p-6 rounded-2xl border border-spotify-border shadow-lg">
                <div className="flex items-center gap-2 mb-4">
                    <Users className="w-4 h-4 text-spotify-green" />
                    <h2 className="text-white text-lg font-semibold m-0">Zarejestrowani Użytkownicy</h2>
                </div>
                <table className="w-full border-collapse text-left">
                    <thead>
                    <tr className="border-b border-neutral-800 text-spotify-gray text-xs uppercase tracking-wider">
                        <th className="p-3 px-4">Użytkownik</th>
                        <th className="p-3 px-4">Email</th>
                        <th className="p-3 px-4">Zapisane dni w DB</th>
                        <th className="p-3 px-4">Akcja</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.map(u => (
                        <tr key={u.spotifyId} className="border-b border-neutral-900 hover:bg-neutral-900/40 transition-colors">
                            <td className="p-3.5 px-4 font-semibold text-sm">{u.displayName}</td>
                            <td className="p-3.5 px-4 text-spotify-gray text-sm">{u.email}</td>
                            <td className="p-3.5 px-4">
                                <span className="bg-neutral-900 border border-neutral-800 py-1 px-3 rounded-full text-spotify-green font-bold text-xs">
                                    {u.totalDocs}
                                </span>
                            </td>
                            <td className="p-3.5 px-4">
                                <Link
                                    to={`/admin/user/${u.spotifyId}`}
                                    className="inline-block bg-spotify-green hover:bg-spotify-green-hover text-black py-1.5 px-3.5 rounded-full font-bold text-xs hover:-translate-y-0.5 transition-all cursor-pointer"
                                >
                                    Szczegóły &gt;
                                </Link>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}