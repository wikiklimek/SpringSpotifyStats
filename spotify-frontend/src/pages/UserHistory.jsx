import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { callApi } from '../utils/api';
import { RefreshCw, ArrowLeft, ShieldAlert } from 'lucide-react';

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
        } catch (e) { setMessage(` Błąd synchronizacji: ${e.message}`); }
    };

    const requestDeletion = async (days) => {
        try {
            const res = await callApi(`/api/privacy/request-deletion?days=${days}`, { method: 'POST' }, navigate);
            if (res) {
                const data = await res.json();
                setMessage(` ${data.message}`);
            }
            loadData();
        } catch (e) { setMessage(` Błąd żądania RODO: ${e.message}`); }
    };

    const cancelRequest = async () => {
        try {
            const res = await callApi('/api/privacy/withdraw', { method: 'POST' }, navigate);
            if (res) {
                const data = await res.json();
                setMessage(` ${data.message}`);
            }
            loadData();
        } catch (e) { setMessage(` Błąd anulowania: ${e.message}`); }
    };

    return (
        <div className="max-w-4xl mx-auto pb-10">
            {/* Nagłówek */}
            <div className="flex justify-between items-center bg-spotify-card p-5 px-7 rounded-2xl mb-5 border border-spotify-border shadow-lg">
                <h1 className="text-spotify-green text-2xl font-bold m-0">Historia Odsłuchań </h1>
                <Link to="/user" className="flex items-center gap-1 text-spotify-gray hover:text-white font-semibold text-sm transition-colors">
                    <ArrowLeft className="w-4 h-4" />  Powrót do Statystyk
                </Link>
            </div>

            {/* Moduł synchronizacji i RODO */}
            <div className="bg-spotify-card p-6 rounded-2xl mb-5 border border-spotify-border text-center shadow-lg">
                <button
                    onClick={syncHistory}
                    className="inline-flex items-center gap-2 bg-spotify-green hover:bg-spotify-green-hover text-black py-3 px-6 rounded-full font-bold text-sm shadow-md hover:-translate-y-0.5 transition-all cursor-pointer"
                >
                    <RefreshCw className="w-4 h-4" />  Pobierz nowe z API Spotify
                </button>
                {message && <p className="text-white font-semibold my-3 text-sm">{message}</p>}

                <div className="h-px bg-neutral-800 my-5"></div>

                <div className="flex items-center justify-center gap-2 mb-3">
                    <ShieldAlert className="w-4 h-4 text-amber-500" />
                    <h3 className="text-amber-500 text-base font-semibold m-0">RODO - Zarządzanie Danymi</h3>
                </div>

                {activeRequest ? (
                    <div className="bg-neutral-900 p-4 rounded-xl inline-block border border-neutral-800">
                        <p className="m-0 mb-3 text-sm">
                            Masz aktywne żądanie usunięcia starszych niż: <b>{activeRequest.daysToKeep} dni</b>.
                        </p>
                        <button
                            onClick={cancelRequest}
                            className="bg-red-600 hover:bg-red-500 text-white py-2 px-5 rounded-full font-semibold text-xs transition-colors cursor-pointer"
                        >
                            Anuluj zapytanie
                        </button>
                    </div>
                ) : (
                    <div className="flex gap-2.5 justify-center flex-wrap">
                        {[7, 30, 90, 180].map(days => (
                            <button
                                key={days}
                                onClick={() => requestDeletion(days)}
                                className="bg-neutral-800 hover:bg-neutral-700 text-white py-2 px-4 rounded-full border border-neutral-700 font-medium text-xs transition-colors cursor-pointer"
                            >
                                Usuń &gt; {days} dni
                            </button>
                        ))}
                    </div>
                )}
            </div>

            {/* Tabela historii */}
            <div className="bg-spotify-card p-6 rounded-2xl border border-spotify-border shadow-lg">
                <table className="w-full border-collapse text-left">
                    <thead>
                    <tr className="border-b border-neutral-800 text-spotify-gray text-xs uppercase tracking-wider">
                        <th className="p-3 px-4">Tytuł</th>
                        <th className="p-3 px-4">Artysta</th>
                        <th className="p-3 px-4">Data Odsłuchania</th>
                    </tr>
                    </thead>
                    <tbody>
                    {history.map((h, i) => (
                        <tr key={i} className="border-b border-neutral-900/60 hover:bg-neutral-900/30 transition-colors">
                            <td className="p-3.5 px-4 font-semibold text-white text-sm">{h.track?.name}</td>
                            <td className="p-3.5 px-4 text-spotify-gray text-sm">{h.track?.artists?.[0]?.name}</td>
                            <td className="p-3.5 px-4 text-spotify-gray text-xs">{new Date(h.playedAt).toLocaleString()}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}