import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { callApi } from '../utils/api';
import { API_BASE_URL } from '../utils/config';
import { Flame, Mic2, Disc3, Headphones, History, LogOut } from 'lucide-react';

export default function UserDashboard() {
    const [view, setView] = useState({ type: 'WELCOME', data: null, error: null });
    const navigate = useNavigate();

    const fetchTopTracks = async () => {
        try {
            const res = await callApi('/api/top-tracks', {}, navigate);
            if (!res || res.status === 204) return;
            const tracks = await res.json();
            setView({ type: 'TRACKS', data: tracks, error: null });
        } catch (error) {
            setView({ type: 'ERROR', data: null, error: error.message });
        }
    };

    const fetchTopArtists = async () => {
        try {
            const res = await callApi('/api/top-artists', {}, navigate);
            if (!res || res.status === 204) return;
            const artists = await res.json();
            setView({ type: 'ARTISTS', data: artists, error: null });
        } catch (error) {
            setView({ type: 'ERROR', data: null, error: error.message });
        }
    };

    const fetchTopGenres = async () => {
        try {
            const res = await callApi('/api/top-genres', {}, navigate);
            if (!res || res.status === 204) return;
            const genres = await res.json();
            setView({ type: 'GENRES', data: genres, error: null });
        } catch (error) {
            setView({ type: 'ERROR', data: null, error: error.message });
        }
    };

    const fetchCurrentlyPlaying = async () => {
        try {
            const res = await callApi('/api/currently-playing', {}, navigate);
            if (res.status === 204) {
                setView({ type: 'EMPTY_PLAYING', data: null, error: null });
                return;
            }
            const data = await res.json();
            if (!data || !data.item) {
                setView({ type: 'EMPTY_PLAYING', data: null, error: null });
                return;
            }
            setView({ type: 'PLAYING', data, error: null });
        } catch (error) {
            if (error.message !== 'musisz się zalogować ponownie') {
                setView({ type: 'SPOTIFY_ERROR', data: null, error: error.message });
            }
        }
    };

    return (
        <div className="max-w-4xl mx-auto pb-10">
            {/* Pasek nawigacyjny */}
            <div className="flex justify-between items-center bg-gradient-to-r from-neutral-900 to-spotify-card p-5 px-7 rounded-2xl mb-6 shadow-xl border border-spotify-border">
                <div className="flex items-center gap-3">
                    <img
                        src="/images/spotify.png"
                        alt="Logo"
                        className="w-8 h-8 object-contain"
                        onError={(e) => { e.target.style.display = 'none'; }}
                    />
                    <h1 className="text-spotify-green text-2xl font-bold tracking-tight m-0">Spotify Wrapped</h1>
                </div>
                <div className="flex items-center gap-5">
                    <Link to="/user/history" className="flex items-center gap-1.5 text-white hover:text-spotify-green font-semibold text-sm transition-colors">
                        <History className="w-4 h-4" /> Moja Historia
                    </Link>
                    <a href={`${API_BASE_URL}/logout`} className="flex items-center gap-1.5 text-red-400 hover:text-red-300 font-semibold text-sm transition-colors">
                        <LogOut className="w-4 h-4" /> Wyloguj
                    </a>
                </div>
            </div>

            {/* Przyciski kategorii */}
            <div className="flex gap-3 justify-center mb-6 flex-wrap">
                <button
                    onClick={fetchTopTracks}
                    className="flex items-center gap-2 bg-neutral-800 hover:bg-neutral-700 active:bg-neutral-900 text-white border border-neutral-700 py-2.5 px-5 rounded-full font-semibold text-sm shadow-md hover:-translate-y-0.5 transition-all cursor-pointer"
                >
                    <Flame className="w-4 h-4 text-orange-400" />  Top Utwory
                </button>
                <button
                    onClick={fetchTopArtists}
                    className="flex items-center gap-2 bg-neutral-800 hover:bg-neutral-700 active:bg-neutral-900 text-white border border-neutral-700 py-2.5 px-5 rounded-full font-semibold text-sm shadow-md hover:-translate-y-0.5 transition-all cursor-pointer"
                >
                    <Mic2 className="w-4 h-4 text-spotify-green" />  Top Artyści
                </button>
                <button
                    onClick={fetchTopGenres}
                    className="flex items-center gap-2 bg-neutral-800 hover:bg-neutral-700 active:bg-neutral-900 text-white border border-neutral-700 py-2.5 px-5 rounded-full font-semibold text-sm shadow-md hover:-translate-y-0.5 transition-all cursor-pointer"
                >
                    <Disc3 className="w-4 h-4 text-purple-400" />  Top Gatunki
                </button>
                <button
                    onClick={fetchCurrentlyPlaying}
                    className="flex items-center gap-2 bg-neutral-800 hover:bg-neutral-700 active:bg-neutral-900 text-white border border-neutral-700 py-2.5 px-5 rounded-full font-semibold text-sm shadow-md hover:-translate-y-0.5 transition-all cursor-pointer"
                >
                    <Headphones className="w-4 h-4 text-blue-400" />  Teraz Odtwarzane
                </button>
            </div>

            {/* Główna karta prezentacji danych */}
            <div className="bg-spotify-card p-8 rounded-2xl shadow-2xl border border-spotify-border min-h-[320px]">
                {view.type === 'WELCOME' && (
                    <h2 className="text-center text-spotify-gray font-normal my-14 text-xl">Wybierz kategorię statystyk!</h2>
                )}

                {view.type === 'TRACKS' && (
                    <div>
                        <h2 className="text-spotify-green text-2xl font-bold mb-6"> Twoje Top 10 Utworów</h2>
                        <div className="flex flex-col gap-2.5">
                            {view.data.map((track, i) => (
                                <div key={track.id || i} className="flex items-center justify-between p-3.5 px-5 bg-neutral-900/80 hover:bg-neutral-800/80 rounded-xl border border-neutral-800 transition-colors">
                                    <div className="flex items-center gap-3">
                                        <span className="text-spotify-green font-bold text-base w-6">#{i + 1}</span>
                                        <span className="font-semibold text-white text-sm">{track.name}</span>
                                    </div>
                                    <span className="text-spotify-gray text-sm">{track.artists?.[0]?.name}</span>
                                </div>
                            ))}
                        </div>
                    </div>
                )}

                {view.type === 'ARTISTS' && (
                    <div>
                        <h2 className="text-spotify-green text-2xl font-bold mb-6"> Twoi Top 10 Artyści</h2>
                        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-5 gap-4">
                            {view.data.map((artist, i) => (
                                <div key={artist.id || i} className="bg-neutral-900/80 hover:bg-neutral-800/80 p-4 rounded-xl text-center border border-neutral-800 transition-all hover:-translate-y-1">
                                    <img
                                        src={artist.images?.[0]?.url || artist.imageUrl || '/images/user.png'}
                                        alt={artist.name}
                                        className="w-20 h-20 rounded-full object-cover mx-auto mb-3 border-2 border-spotify-green shadow-md"
                                        onError={(e) => { e.target.src = '/images/user.png'; }}
                                    />
                                    <div className="font-semibold text-white text-sm truncate">{artist.name}</div>
                                    <div className="text-spotify-green text-xs font-bold mt-1">Top #{i + 1}</div>
                                </div>
                            ))}
                        </div>
                    </div>
                )}

                {view.type === 'GENRES' && (
                    <div>
                        <h2 className="text-spotify-green text-2xl font-bold mb-6"> Twoje Top Gatunki</h2>
                        <div className="flex flex-col gap-2.5">
                            {Object.entries(view.data).map(([genre, count], index) => (
                                <div key={genre} className="flex justify-between items-center p-3 px-5 bg-neutral-900/80 rounded-lg border-l-4 border-spotify-green border-y border-r border-neutral-800">
                                    <span className="font-semibold capitalize text-white text-sm">{index + 1}. {genre}</span>
                                    <span className="bg-neutral-800 px-3 py-1 rounded-full text-spotify-green font-bold text-xs">{count} odtworzeń</span>
                                </div>
                            ))}
                        </div>
                    </div>
                )}

                {view.type === 'PLAYING' && (
                    <div className="text-center py-5">
                        <span className="inline-block bg-spotify-green/15 text-spotify-green px-4 py-1.5 rounded-full text-xs font-bold tracking-wider mb-4">
                            {view.data.is_playing ? ' TERAZ ODTWARZANE' : ' WSTRZYMANE'}
                        </span>
                        <div>
                            <img
                                src={view.data.item?.album?.images?.[0]?.url || '/images/default.png'}
                                width="220"
                                height="220"
                                alt="Okładka albumu"
                                className="rounded-2xl mx-auto my-4 shadow-2xl border border-neutral-800"
                            />
                        </div>
                        <h2 className="text-white text-2xl font-bold m-0 mb-1.5">{view.data.item?.name}</h2>
                        <h4 className="text-spotify-gray font-normal text-base m-0">{view.data.item?.artists?.[0]?.name}</h4>
                    </div>
                )}

                {view.type === 'EMPTY_PLAYING' && (
                    <h3 className="text-center text-spotify-gray my-14 text-base font-normal">nic w tym momencie nie słuchasz</h3>
                )}

                {view.type === 'SPOTIFY_ERROR' && (
                    <h3 className="text-center text-spotify-gray my-14 text-base font-normal">nie możemy połączyć się ze Spotify: {view.error}</h3>
                )}

                {view.type === 'ERROR' && (
                    <h3 className="text-center text-spotify-gray my-14 text-base font-normal">{view.error}</h3>
                )}
            </div>
        </div>
    );
}