import { API_BASE_URL } from '../utils/config';

export default function LoginPage() {
    return (
        <div style={{ maxWidth: '400px', margin: '80px auto', background: '#181818', padding: '40px', borderRadius: '12px', textAlign: 'center' }}>
            <h1 style={{ color: '#1db954', marginBottom: '30px' }}>Spotify Wrapped 🚀</h1>
            <div style={{ marginBottom: '40px' }}>
                <h3 style={{ color: 'white', marginBottom: '15px' }}>Zaloguj jako Użytkownik</h3>
                <a href={`${API_BASE_URL}/oauth2/authorization/spotify`} style={{ padding: '12px 24px', background: '#1db954', color: 'black', textDecoration: 'none', borderRadius: '30px', fontWeight: 'bold' }}>Zaloguj przez Spotify</a>
            </div>
            <hr style={{ borderColor: '#282828', marginBottom: '30px' }} />
            <div>
                <h3 style={{ color: '#ff9800', marginBottom: '15px' }}>Panel Administratora</h3>
                <form action={`${API_BASE_URL}/login`} method="POST" style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                    <input type="text" name="username" placeholder="Login admina" style={{ padding: '12px', borderRadius: '6px', border: '1px solid #333', background: '#222', color: 'white' }} />
                    <input type="password" name="password" placeholder="Hasło admina" style={{ padding: '12px', borderRadius: '6px', border: '1px solid #333', background: '#222', color: 'white' }} />
                    <button type="submit" style={{ padding: '12px', background: '#ff9800', color: 'black', border: 'none', borderRadius: '6px', fontWeight: 'bold', cursor: 'pointer' }}>Zaloguj jako Admin</button>
                </form>
            </div>
        </div>
    );
}