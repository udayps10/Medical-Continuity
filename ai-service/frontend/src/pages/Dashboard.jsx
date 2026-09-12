import { useState, useEffect } from 'react';
import { useAuth } from '../AuthContext';
import { patientAPI, unknownPatientAPI, encounterAPI, medicalRecordAPI } from '../api';
import { Link } from 'react-router-dom';

export default function Dashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState({ patients: 0, unknown: 0, encounters: 0, records: 0 });
  const [recentPatients, setRecentPatients] = useState([]);
  const [myProfile, setMyProfile] = useState(null);

  useEffect(() => { loadStats(); }, [user]);

  const loadStats = async () => {
    try {
      if (user?.role === 'PATIENT') {
        // Patient can only see own data - skip stats that require admin access
        return;
      }
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
      // Silently handle - user may not have access
    }
  };

  const isDoctor = user?.role === 'DOCTOR';
  const isNurse = user?.role === 'NURSE';
  const isAdmin = user?.role === 'ADMIN';
  const isPatient = user?.role === 'PATIENT';
  const canViewPatients = isDoctor || isNurse || isAdmin;

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

      {/* Stats - only for doctor/nurse/admin */}
      {canViewPatients && (
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
      )}

      {/* Patient view */}
      {isPatient && (
        <div className="card" style={{ marginBottom: 20 }}>
          <div className="card-header"><h2>Your Account</h2></div>
          <div className="card-body">
            <p>You are logged in as a <strong>Patient</strong>.</p>
            <p style={{ marginTop: 8, color: 'var(--gray-500)' }}>
              Your profile is managed by the hospital. Contact your doctor for any updates.
            </p>
          </div>
        </div>
      )}

      <div className="grid-2">
        {/* Recent patients - only for doctor/nurse/admin */}
        {canViewPatients && (
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
                      <tr><th>MCID</th><th>Name</th><th>Gender</th><th>Village</th></tr>
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
        )}

        {/* Quick Actions */}
        <div className="card">
          <div className="card-header"><h2>Quick Actions</h2></div>
          <div className="card-body" style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
            {isPatient && (
              <>
                <p style={{ color: 'var(--gray-500)', fontSize: 14, marginBottom: 8 }}>
                  As a patient, you can view your medical records at the hospital.
                </p>
              </>
            )}
            {canViewPatients && (
              <>
                <Link to="/patients" className="btn btn-primary" style={{ justifyContent: 'center' }}>
                  View Patients
                </Link>
                <Link to="/unknown-patients" className="btn btn-secondary" style={{ justifyContent: 'center' }}>
                  Unknown Patients
                </Link>
                {(isDoctor || isNurse) && (
                  <Link to="/documents" className="btn btn-secondary" style={{ justifyContent: 'center' }}>
                    Upload Document
                  </Link>
                )}
                {(isDoctor || isNurse) && (
                  <Link to="/encounters" className="btn btn-secondary" style={{ justifyContent: 'center' }}>
                    Record Encounter
                  </Link>
                )}
                {isDoctor && (
                  <Link to="/medical-records" className="btn btn-secondary" style={{ justifyContent: 'center' }}>
                    Medical Records
                  </Link>
                )}
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
