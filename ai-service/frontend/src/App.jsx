import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './AuthContext';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Patients from './pages/Patients';
import PatientDetail from './pages/PatientDetail';
import Documents from './pages/Documents';
import MyDocuments from './pages/MyDocuments';
import MyRecords from './pages/MyRecords';
import UnknownPatients from './pages/UnknownPatients';
import UnknownPatientDetail from './pages/UnknownPatientDetail';
import Encounters from './pages/Encounters';
import MedicalRecords from './pages/MedicalRecords';
import Navbar from './components/Navbar';
import './index.css';

function ProtectedRoute({ children }) {
  const { user, loading } = useAuth();
  if (loading) return <div className="main-content"><p>Loading...</p></div>;
  if (!user) return <Navigate to="/login" />;
  return children;
}

function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/" element={<ProtectedRoute><Navbar /><Dashboard /></ProtectedRoute>} />
      <Route path="/patients" element={<ProtectedRoute><Navbar /><Patients /></ProtectedRoute>} />
      <Route path="/patients/:id" element={<ProtectedRoute><Navbar /><PatientDetail /></ProtectedRoute>} />
      <Route path="/documents" element={<ProtectedRoute><Navbar /><Documents /></ProtectedRoute>} />
      <Route path="/my-documents" element={<ProtectedRoute><Navbar /><MyDocuments /></ProtectedRoute>} />
      <Route path="/my-records" element={<ProtectedRoute><Navbar /><MyRecords /></ProtectedRoute>} />
      <Route path="/unknown-patients" element={<ProtectedRoute><Navbar /><UnknownPatients /></ProtectedRoute>} />
      <Route path="/unknown-patients/:id" element={<ProtectedRoute><Navbar /><UnknownPatientDetail /></ProtectedRoute>} />
      <Route path="/encounters" element={<ProtectedRoute><Navbar /><Encounters /></ProtectedRoute>} />
      <Route path="/medical-records" element={<ProtectedRoute><Navbar /><MedicalRecords /></ProtectedRoute>} />
    </Routes>
  );
}

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="app">
          <AppRoutes />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
