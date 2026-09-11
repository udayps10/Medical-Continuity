import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../AuthContext';

export default function Navbar() {
  const { user, logout } = useAuth();
  const location = useLocation();

  const isActive = (path) => location.pathname === path ? 'nav-link active' : 'nav-link';

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
          <path d="M22 12h-4l-3 9L9 3l-3 9H2"/>
        </svg>
        Medical Continuity
      </div>

      <div className="navbar-nav">
        <Link to="/" className={isActive('/')}>Dashboard</Link>
        <Link to="/patients" className={isActive('/patients')}>Patients</Link>
        <Link to="/encounters" className={isActive('/encounters')}>Encounters</Link>
        <Link to="/medical-records" className={isActive('/medical-records')}>Records</Link>
        <Link to="/documents" className={isActive('/documents')}>Documents</Link>
        <Link to="/unknown-patients" className={isActive('/unknown-patients')}>Unknown</Link>
      </div>

      <div className="navbar-user">
        <span className="user-badge">{user?.role}</span>
        <span style={{ fontSize: 14 }}>{user?.email}</span>
        <button className="btn-logout" onClick={logout}>Logout</button>
      </div>
    </nav>
  );
}
