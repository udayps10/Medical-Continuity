import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { patientAPI, encounterAPI, medicalRecordAPI, medicalDocumentAPI } from '../api';

export default function PatientDetail() {
  const { id } = useParams();
  const [patient, setPatient] = useState(null);
  const [encounters, setEncounters] = useState([]);
  const [records, setRecords] = useState([]);
  const [documents, setDocuments] = useState([]);
  const [activeTab, setActiveTab] = useState('overview');

  useEffect(() => {
    loadData();
  }, [id]);

  const loadData = async () => {
    try {
      const [p, e, r, d] = await Promise.all([
        patientAPI.getById(id),
        encounterAPI.getByPatient(id).catch(() => ({ data: [] })),
        medicalRecordAPI.getByPatient(id).catch(() => ({ data: [] })),
        medicalDocumentAPI.getByPatient(id).catch(() => ({ data: [] })),
      ]);
      setPatient(p.data);
      setEncounters(e.data);
      setRecords(r.data);
      setDocuments(d.data);
    } catch (err) { console.error(err); }
  };

  if (!patient) return <div className="main-content"><p>Loading...</p></div>;

  return (
    <div className="main-content">
      <div className="page-header">
        <div>
          <Link to="/patients" style={{ color: 'var(--primary)', fontSize: 14, textDecoration: 'none' }}>&larr; Back to Patients</Link>
          <h1>{patient.name}</h1>
          <p style={{ color: 'var(--gray-500)' }}>MCID: {patient.mcid}</p>
        </div>
      </div>

      <div className="dashboard-grid" style={{ marginBottom: 24 }}>
        <div className="stat-card">
          <div className="stat-icon blue">+</div>
          <div className="stat-info">
            <h3>{encounters.length}</h3>
            <p>Encounters</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon green">~</div>
          <div className="stat-info">
            <h3>{records.length}</h3>
            <p>Medical Records</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon yellow">!</div>
          <div className="stat-info">
            <h3>{documents.length}</h3>
            <p>Documents</p>
          </div>
        </div>
      </div>

      <div style={{ display: 'flex', gap: 4, marginBottom: 20 }}>
        {['overview', 'encounters', 'records', 'documents'].map(tab => (
          <button key={tab} className={`btn ${activeTab === tab ? 'btn-primary' : 'btn-secondary'}`}
            onClick={() => setActiveTab(tab)}>
            {tab.charAt(0).toUpperCase() + tab.slice(1)}
          </button>
        ))}
      </div>

      {activeTab === 'overview' && (
        <div className="card">
          <div className="card-body">
            <div className="form-row">
              <div><strong>Gender:</strong> {patient.gender}</div>
              <div><strong>DOB:</strong> {patient.dateOfBirth}</div>
            </div>
            <div className="form-row" style={{ marginTop: 12 }}>
              <div><strong>Phone:</strong> {patient.phone || '-'}</div>
              <div><strong>Village:</strong> {patient.village || '-'}</div>
            </div>
            <div className="form-row" style={{ marginTop: 12 }}>
              <div><strong>District:</strong> {patient.district || '-'}</div>
              <div><strong>Address:</strong> {patient.address || '-'}</div>
            </div>
          </div>
        </div>
      )}

      {activeTab === 'encounters' && (
        <div className="card">
          <div className="card-body">
            {encounters.length === 0 ? <div className="empty-state"><p>No encounters</p></div> : (
              <div className="table-container">
                <table>
                  <thead><tr><th>Type</th><th>Arrival</th><th>Discharge</th><th>Doctor</th><th>Hospital</th></tr></thead>
                  <tbody>
                    {encounters.map(e => (
                      <tr key={e.id}>
                        <td><span className="badge badge-blue">{e.encounterType}</span></td>
                        <td>{e.arrivalTime}</td>
                        <td>{e.dischargeTime || 'Ongoing'}</td>
                        <td>{e.doctorName || '-'}</td>
                        <td>{e.hospital?.name || '-'}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>
      )}

      {activeTab === 'records' && (
        <div className="card">
          <div className="card-body">
            {records.length === 0 ? <div className="empty-state"><p>No records</p></div> : (
              <div className="table-container">
                <table>
                  <thead><tr><th>Type</th><th>Date</th><th>Doctor</th><th>Summary</th><th>Hospital</th></tr></thead>
                  <tbody>
                    {records.map(r => (
                      <tr key={r.id}>
                        <td><span className="badge badge-green">{r.recordType}</span></td>
                        <td>{r.recordDate}</td>
                        <td>{r.doctorName || '-'}</td>
                        <td>{r.summary || '-'}</td>
                        <td>{r.hospital?.name || '-'}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>
      )}

      {activeTab === 'documents' && (
        <div className="card">
          <div className="card-body">
            {documents.length === 0 ? <div className="empty-state"><p>No documents</p></div> : (
              <div className="table-container">
                <table>
                  <thead><tr><th>Type</th><th>File</th><th>Size</th><th>Status</th><th>Uploaded By</th></tr></thead>
                  <tbody>
                    {documents.map(d => (
                      <tr key={d.id}>
                        <td><span className="badge badge-blue">{d.documentType}</span></td>
                        <td>{d.fileName}</td>
                        <td>{d.fileSize ? `${(d.fileSize / 1024).toFixed(1)} KB` : '-'}</td>
                        <td><span className="badge badge-yellow">{d.processingStatus}</span></td>
                        <td>{d.uploadedBy || '-'}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
