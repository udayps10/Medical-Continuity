import { useState, useEffect } from 'react';
import { useAuth } from '../AuthContext';
import { patientAPI, unknownPatientAPI, encounterAPI, medicalRecordAPI, medicalDocumentAPI, patientMatchAPI } from '../api';
import { Link } from 'react-router-dom';

export default function Dashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState({ patients: 0, unknown: 0, encounters: 0, records: 0 });
  const [recentPatients, setRecentPatients] = useState([]);
  const [myProfile, setMyProfile] = useState(null);
  const [myDocuments, setMyDocuments] = useState([]);
  const [myRecords, setMyRecords] = useState([]);

  useEffect(() => { loadStats(); }, [user]);

  const loadStats = async () => {
    try {
      if (user?.role === 'PATIENT') {
        // Patient: load own profile, documents, records
        const patients = await patientAPI.getAll().catch(() => ({ data: [] }));
        // Find patient matching logged-in email
        const me = patients.data.find(p => p.phone === user.email || p.name?.includes(user.email?.split('@')[0]));
        if (me) {
          setMyProfile(me);
          const [docs, recs] = await Promise.all([
            medicalDocumentAPI.getByPatient(me.id).catch(() => ({ data: [] })),
            medicalRecordAPI.getByPatient(me.id).catch(() => ({ data: [] })),
          ]);
          setMyDocuments(docs.data);
          setMyRecords(recs.data);
        }
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
    } catch (err) {}
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

      {/* Patient View */}
      {isPatient && (
        <>
          <div className="dashboard-grid">
            <div className="stat-card">
              <div className="stat-icon blue">+</div>
              <div className="stat-info">
                <h3>{myDocuments.length}</h3>
                <p>My Documents</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon green">~</div>
              <div className="stat-info">
                <h3>{myRecords.length}</h3>
                <p>Medical Records</p>
              </div>
            </div>
          </div>

          <div className="grid-2">
            {/* Health Profile */}
            <div className="card">
              <div className="card-header"><h2>My Health Profile</h2></div>
              <div className="card-body">
                {myProfile ? (
                  <>
                    <div className="form-row"><div><strong>Name:</strong> {myProfile.name}</div><div><strong>MCID:</strong> {myProfile.mcid}</div></div>
                    <div className="form-row" style={{ marginTop: 8 }}><div><strong>Gender:</strong> {myProfile.gender}</div><div><strong>DOB:</strong> {myProfile.dateOfBirth}</div></div>
                    <div className="form-row" style={{ marginTop: 8 }}><div><strong>Blood Group:</strong> {myProfile.bloodGroup || 'Not set'}</div><div><strong>Weight:</strong> {myProfile.weight ? `${myProfile.weight} kg` : 'Not set'}</div></div>
                    <div className="form-row" style={{ marginTop: 8 }}><div><strong>Height:</strong> {myProfile.height ? `${myProfile.height} cm` : 'Not set'}</div><div><strong>Phone:</strong> {myProfile.phone || 'Not set'}</div></div>
                    <div style={{ marginTop: 8 }}><strong>Allergies:</strong> {myProfile.allergies || 'None recorded'}</div>
                    <div style={{ marginTop: 8 }}><strong>Village:</strong> {myProfile.village || '-'}</div>
                    <div style={{ marginTop: 8 }}><strong>District:</strong> {myProfile.district || '-'}</div>
                  </>
                ) : (
                  <p style={{ color: 'var(--gray-500)' }}>No profile found. Contact hospital to create your patient record.</p>
                )}
              </div>
            </div>

            {/* Quick Actions */}
            <div className="card">
              <div className="card-header"><h2>Quick Actions</h2></div>
              <div className="card-body" style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                <Link to="/my-documents" className="btn btn-primary" style={{ justifyContent: 'center' }}>
                  Upload Document (X-Ray, Report...)
                </Link>
                <Link to="/my-records" className="btn btn-secondary" style={{ justifyContent: 'center' }}>
                  View My Medical Records
                </Link>
                <Link to="/encounters" className="btn btn-secondary" style={{ justifyContent: 'center' }}>
                  View My Encounters
                </Link>
              </div>
            </div>
          </div>

          {/* Recent Documents */}
          {myDocuments.length > 0 && (
            <div className="card" style={{ marginTop: 20 }}>
              <div className="card-header">
                <h2>Recent Documents</h2>
                <Link to="/my-documents" className="btn btn-sm btn-outline">View All</Link>
              </div>
              <div className="card-body">
                <div className="table-container">
                  <table>
                    <thead><tr><th>Type</th><th>File</th><th>Status</th><th>Date</th></tr></thead>
                    <tbody>
                      {myDocuments.slice(0, 5).map(d => (
                        <tr key={d.id}>
                          <td><span className="badge badge-blue">{d.documentType}</span></td>
                          <td>{d.fileName}</td>
                          <td><span className={`badge ${d.processingStatus === 'COMPLETED' ? 'badge-green' : 'badge-yellow'}`}>{d.processingStatus}</span></td>
                          <td>{d.uploadedAt?.split('T')[0]}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          )}
        </>
      )}

      {/* Doctor/Nurse/Admin View */}
      {canViewPatients && (
        <>
          <div className="dashboard-grid">
            <div className="stat-card"><div className="stat-icon blue">+</div><div className="stat-info"><h3>{stats.patients}</h3><p>Total Patients</p></div></div>
            <div className="stat-card"><div className="stat-icon yellow">?</div><div className="stat-info"><h3>{stats.unknown}</h3><p>Unknown Patients</p></div></div>
            <div className="stat-card"><div className="stat-icon green">~</div><div className="stat-info"><h3>{stats.encounters}</h3><p>Encounters</p></div></div>
            <div className="stat-card"><div className="stat-icon red">!</div><div className="stat-info"><h3>{stats.records}</h3><p>Medical Records</p></div></div>
          </div>

          <div className="grid-2">
            <div className="card">
              <div className="card-header">
                <h2>Recent Patients</h2>
                <Link to="/patients" className="btn btn-sm btn-outline">View All</Link>
              </div>
              <div className="card-body">
                {recentPatients.length === 0 ? <div className="empty-state"><p>No patients yet</p></div> : (
                  <div className="table-container">
                    <table>
                      <thead><tr><th>MCID</th><th>Name</th><th>Gender</th><th>Village</th></tr></thead>
                      <tbody>
                        {recentPatients.map(p => (
                          <tr key={p.id}><td><strong>{p.mcid}</strong></td><td>{p.name}</td><td><span className="badge badge-blue">{p.gender}</span></td><td>{p.village || '-'}</td></tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            </div>

            <div className="card">
              <div className="card-header"><h2>Quick Actions</h2></div>
              <div className="card-body" style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                <Link to="/patients" className="btn btn-primary" style={{ justifyContent: 'center' }}>View Patients</Link>
                <Link to="/unknown-patients" className="btn btn-secondary" style={{ justifyContent: 'center' }}>Unknown Patients</Link>
                {(isDoctor || isNurse) && <Link to="/documents" className="btn btn-secondary" style={{ justifyContent: 'center' }}>Upload Document</Link>}
                {(isDoctor || isNurse) && <Link to="/encounters" className="btn btn-secondary" style={{ justifyContent: 'center' }}>Record Encounter</Link>}
                {isDoctor && <Link to="/medical-records" className="btn btn-secondary" style={{ justifyContent: 'center' }}>Medical Records</Link>}
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
}
