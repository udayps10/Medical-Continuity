import { useState, useEffect } from 'react';
import { medicalRecordAPI, patientAPI } from '../api';
import { useAuth } from '../AuthContext';
import { Link } from 'react-router-dom';

export default function MyRecords() {
  const { user } = useAuth();
  const [records, setRecords] = useState([]);

  useEffect(() => { loadData(); }, []);

  const loadData = async () => {
    try {
      const patients = await patientAPI.getAll().catch(() => ({ data: [] }));
      const me = patients.data.find(p => p.phone === user.email || p.name?.includes(user.email?.split('@')[0]));
      if (me) {
        const recs = await medicalRecordAPI.getByPatient(me.id).catch(() => ({ data: [] }));
        setRecords(recs.data);
      }
    } catch (err) {}
  };

  return (
    <div className="main-content">
      <div className="page-header">
        <div>
          <Link to="/" style={{ color: 'var(--primary)', fontSize: 14, textDecoration: 'none' }}>&larr; Back to Dashboard</Link>
          <h1>My Medical Records</h1>
        </div>
      </div>

      <div className="card">
        <div className="card-body">
          {records.length === 0 ? (
            <div className="empty-state"><p>No medical records found</p></div>
          ) : (
            <div className="table-container">
              <table>
                <thead><tr><th>Type</th><th>Date</th><th>Doctor</th><th>Hospital</th><th>Summary</th></tr></thead>
                <tbody>
                  {records.map(r => (
                    <tr key={r.id}>
                      <td><span className="badge badge-green">{r.recordType}</span></td>
                      <td>{r.recordDate}</td>
                      <td>{r.doctorName || '-'}</td>
                      <td>{r.hospital?.name || '-'}</td>
                      <td>{r.summary || '-'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
