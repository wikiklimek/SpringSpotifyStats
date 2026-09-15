import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import UserDashboard from './pages/UserDashboard';
import UserHistory from './pages/UserHistory';
import AdminDashboard from './pages/AdminDashboard';
import AdminUserDetail from './pages/AdminUserDetail';
import AdminRequests from './pages/AdminRequests';

export default function App() {
    return (
        <Router>
            <div className="min-h-screen bg-spotify-black text-white p-5 font-['Poppins',sans-serif] box-border selection:bg-spotify-green selection:text-black">
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