import { useState, useEffect } from 'react';
import { useAuth } from '../AuthContext';
import { patientAPI, unknownPatientAPI, encounterAPI, medicalRecordAPI } from '../api';
import { Link } from 'react-router-dom';

export default function Dashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState({ patients: 0, unknown: 0, encounters: 0, records: 0 });
  const [recentPatients, setRecentPatients] = useState([]);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const [p, u, e, r] = await Promise.all([
        patientAPI.getAll().catch(() => ({ data: [] })),
        unknownPatientAPI.getAll().catch(() => ({ data: [] })),
        encounterAPI.getAll().catch(() => ({ data: [] })),
        medicalRecordAPI.getAll().catch(() => ({ data: [] })),
      ]);
      setStats({
        patients: p.data.length,
        unknown: u.data.length,
        encounters: e.data.length,
        records: r.data.length,
      });
      setRecentPatients(p.data.slice(0, 5));
    } catch (err) {
      console.error('Failed to load stats');
    }
  };

  return (
    <div className="main-content">
      <div className="page-header">
        <div>
          <h1>Dashboard</h1>
          <p style={{ color: 'var(--gray-500)', fontSize: 14 }}>
            Welcome back, {user?.email} ({user?.role})
          </p>
        </div>
      </div>

      <div className="dashboard-grid">
        <div className="stat-card">
          <div className="stat-icon blue">+</div>
          <div className="stat-info">
            <h3>{stats.patients}</h3>
            <p>Total Patients</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon yellow">?</div>
          <div className="stat-info">
            <h3>{stats.unknown}</h3>
            <p>Unknown Patients</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon green">~</div>
          <div className="stat-info">
            <h3>{stats.encounters}</h3>
            <p>Encounters</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon red">!</div>
          <div className="stat-info">
            <h3>{stats.records}</h3>
            <p>Medical Records</p>
          </div>
        </div>
      </div>

      <div className="grid-2">
        <div className="card">
          <div className="card-header">
            <h2>Recent Patients</h2>
            <Link to="/patients" className="btn btn-sm btn-outline">View All</Link>
          </div>
          <div className="card-body">
            {recentPatients.length === 0 ? (
              <div className="empty-state"><p>No patients yet</p></div>
            ) : (
              <div className="table-container">
                <table>
                  <thead>
                    <tr>
                      <th>MCID</th>
                      <th>Name</th>
                      <th>Gender</th>
                      <th>Village</th>
                    </tr>
                  </thead>
                  <tbody>
                    {recentPatients.map(p => (
                      <tr key={p.id}>
                        <td><strong>{p.mcid}</strong></td>
                        <td>{p.name}</td>
                        <td><span className="badge badge-blue">{p.gender}</span></td>
                        <td>{p.village || '-'}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>

        <div className="card">
          <div className="card-header">
            <h2>Quick Actions</h2>
          </div>
          <div className="card-body" style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
            {(user?.role === 'PATIENT') && (
              <Link to="/patients" className="btn btn-primary" style={{ justifyContent: 'center' }}>
                My Profile
              </Link>
            )}
            {(user?.role === 'DOCTOR' || user?.role === 'NURSE' || user?.role === 'ADMIN') && (
              <>
                <Link to="/patients" className="btn btn-primary" style={{ justifyContent: 'center' }}>
                  View Patients
                </Link>
                <Link to="/unknown-patients" className="btn btn-secondary" style={{ justifyContent: 'center' }}>
                  Unknown Patients
                </Link>
                <Link to="/documents" className="btn btn-secondary" style={{ justifyContent: 'center' }}>
                  Upload Document
                </Link>
                <Link to="/encounters" className="btn btn-secondary" style={{ justifyContent: 'center' }}>
                  Record Encounter
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
