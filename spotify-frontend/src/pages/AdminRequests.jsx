import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { callApi } from '../utils/api';
import { ArrowLeft, Check, X, ShieldAlert } from 'lucide-react';

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
                setPendingRequests(pendingRequests.filter(req => req.id !== id));
            }
        } catch (e) {
            window.alert("Błąd: " + e.message);
        }
    };

    return (
        <div className="max-w-4xl mx-auto">
            {/* Nagłówek */}
            <div className="flex justify-between items-center bg-spotify-card p-5 px-7 rounded-2xl mb-6 border border-spotify-border shadow-lg">
                <div className="flex items-center gap-2.5">
                    <ShieldAlert className="w-6 h-6 text-amber-500" />
                    <h2 className="text-amber-500 text-xl font-bold m-0">Oczekujące Zgłoszenia RODO</h2>
                </div>
                <Link to="/admin" className="flex items-center gap-1 text-spotify-gray hover:text-white font-semibold text-sm transition-colors">
                    <ArrowLeft className="w-4 h-4" />  Powrót do Panelu
                </Link>
            </div>

            {message && <p className="text-red-500 text-center font-semibold mb-4 text-sm">{message}</p>}

            {/* Tabela żądań */}
            <div className="bg-spotify-card p-6 rounded-2xl border border-spotify-border shadow-lg">
                {pendingRequests.length === 0 && !message ? (
                    <p className="text-center text-spotify-gray my-8 text-sm">Brak oczekujących próśb od użytkowników. </p>
                ) : (
                    <table className="w-full border-collapse text-white text-left">
                        <thead>
                        <tr className="border-b border-neutral-800 text-spotify-gray text-xs uppercase tracking-wider">
                            <th className="p-3 px-4">ID</th>
                            <th className="p-3 px-4">Spotify ID</th>
                            <th className="p-3 px-4">Żądanie</th>
                            <th className="p-3 px-4">Akcja</th>
                        </tr>
                        </thead>
                        <tbody>
                        {pendingRequests.map(req => (
                            <tr key={req.id} className="border-b border-neutral-900 hover:bg-neutral-900/30 transition-colors">
                                <td className="p-3.5 px-4 text-sm">{req.id}</td>
                                <td className="p-3.5 px-4 font-bold text-sm">{req.spotifyId}</td>
                                <td className="p-3.5 px-4 text-sm">Usuń starsze niż {req.daysToKeep} dni</td>
                                <td className="p-3.5 px-4 flex gap-2">
                                    <button
                                        onClick={() => handleAction(req.id, 'approve')}
                                        className="inline-flex items-center gap-1 bg-spotify-green hover:bg-spotify-green-hover text-black font-bold py-1.5 px-3.5 rounded-full text-xs transition-colors cursor-pointer"
                                    >
                                        <Check className="w-3.5 h-3.5" /> Akceptuj
                                    </button>
                                    <button
                                        onClick={() => handleAction(req.id, 'reject')}
                                        className="inline-flex items-center gap-1 bg-red-600 hover:bg-red-500 text-white font-bold py-1.5 px-3.5 rounded-full text-xs transition-colors cursor-pointer"
                                    >
                                        <X className="w-3.5 h-3.5" /> Odrzuć
                                    </button>
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