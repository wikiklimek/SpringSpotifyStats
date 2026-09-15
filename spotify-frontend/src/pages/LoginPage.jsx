import { API_BASE_URL } from '../utils/config';
import { Shield, KeyRound, User } from 'lucide-react';

export default function LoginPage() {
    return (
        <div className="flex justify-center items-center min-h-[85vh] p-5">
            <div className="w-full max-w-md bg-spotify-card p-10 rounded-2xl shadow-2xl border border-spotify-border">
                {/* Logo & Nagłówek */}
                <div className="text-center mb-8">
                    <img
                        src="/images/spotify.png"
                        alt="Spotify Logo"
                        className="w-16 h-16 mx-auto mb-4 object-contain"
                        onError={(e) => { e.target.style.display = 'none'; }}
                    />
                    <h1 className="text-spotify-green text-3xl font-extrabold tracking-tight m-0">Spotify Wrapped</h1>
                    <p className="text-spotify-gray text-sm mt-1.5 font-normal">Odkryj swoje muzyczne podsumowanie</p>
                </div>

                {/* Sekcja użytkownika */}
                <div className="mb-8 text-center">
                    <h3 className="text-white text-lg font-medium mb-4">Zaloguj jako Użytkownik</h3>
                    <a
                        href={`${API_BASE_URL}/oauth2/authorization/spotify`}
                        className="inline-flex items-center justify-center w-4/5 py-3.5 px-6 bg-spotify-green hover:bg-spotify-green-hover text-black font-bold rounded-full text-sm shadow-lg shadow-spotify-green/20 hover:-translate-y-0.5 active:translate-y-0 transition-all duration-200 cursor-pointer"
                    >
                        Zaloguj przez Spotify
                    </a>
                </div>

                {/* Rozdzielacz */}
                <div className="flex items-center my-7">
                    <div className="flex-1 h-px bg-spotify-border"></div>
                    <span className="px-3 text-spotify-gray text-xs uppercase tracking-widest font-semibold">lub</span>
                    <div className="flex-1 h-px bg-spotify-border"></div>
                </div>

                {/* Sekcja administratora */}
                <div>
                    <div className="flex items-center justify-center gap-2 mb-4">
                        <Shield className="w-4 h-4 text-amber-500" />
                        <h3 className="text-amber-500 text-base font-semibold m-0">Panel Administratora</h3>
                    </div>
                    <form action={`${API_BASE_URL}/login`} method="POST" className="flex flex-col gap-3.5">
                        <div className="relative">
                            <User className="absolute left-3.5 top-3.5 w-4 h-4 text-spotify-gray" />
                            <input
                                type="text"
                                name="username"
                                placeholder="Login admina"
                                className="w-full pl-10 pr-4 py-3 bg-neutral-900 border border-neutral-800 rounded-lg text-white text-sm focus:border-amber-500 focus:outline-none transition-colors box-border"
                                required
                            />
                        </div>
                        <div className="relative">
                            <KeyRound className="absolute left-3.5 top-3.5 w-4 h-4 text-spotify-gray" />
                            <input
                                type="password"
                                name="password"
                                placeholder="Hasło admina"
                                className="w-full pl-10 pr-4 py-3 bg-neutral-900 border border-neutral-800 rounded-lg text-white text-sm focus:border-amber-500 focus:outline-none transition-colors box-border"
                                required
                            />
                        </div>
                        <button
                            type="submit"
                            className="mt-1.5 py-3 px-4 bg-amber-500 hover:bg-amber-400 text-black font-bold rounded-full text-sm hover:-translate-y-0.5 active:translate-y-0 transition-all duration-200 cursor-pointer shadow-md shadow-amber-500/20"
                        >
                            Zaloguj jako Admin
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}