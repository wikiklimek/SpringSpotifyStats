import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import UserDashboard from './pages/UserDashboard';
import UserHistory from './pages/UserHistory';
import AdminDashboard from './pages/AdminDashboard';
import AdminUserDetail from './pages/AdminUserDetail';
import AdminRequests from './pages/AdminRequests'; // NOWY PLIK

function App() {
    return (
        <Router>
            <div style={{ fontFamily: 'Poppins, sans-serif', backgroundColor: '#121212', color: 'white', minHeight: '100vh', padding: '20px', boxSizing: 'border-box' }}>
                <Routes>
                    <Route path="/" element={<LoginPage />} />
                    <Route path="/user" element={<UserDashboard />} />
                    <Route path="/user/history" element={<UserHistory />} />
                    <Route path="/admin" element={<AdminDashboard />} />
                    <Route path="/admin/requests" element={<AdminRequests />} />
                    <Route path="/admin/user/:spotifyId" element={<AdminUserDetail />} />
                </Routes>
            </div>
        </Router>
    );
}
export default App;