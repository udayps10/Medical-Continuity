import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { patientAPI, encounterAPI, medicalRecordAPI, medicalDocumentAPI } from '../api';
import { useAuth } from '../AuthContext';

export default function PatientDetail() {
  const { id } = useParams();
  const { user } = useAuth();
  const [patient, setPatient] = useState(null);
  const [encounters, setEncounters] = useState([]);
  const [records, setRecords] = useState([]);
  const [documents, setDocuments] = useState([]);
  const [activeTab, setActiveTab] = useState('overview');
  const [editing, setEditing] = useState(false);
  const [editForm, setEditForm] = useState({});

  const isDoctor = user?.role === 'DOCTOR';

  useEffect(() => { loadData(); }, [id]);

  const loadData = async () => {
    try {
      const [p, e, r, d] = await Promise.all([
        patientAPI.getById(id),
        encounterAPI.getByPatient(id).catch(() => ({ data: [] })),
        medicalRecordAPI.getByPatient(id).catch(() => ({ data: [] })),
        medicalDocumentAPI.getByPatient(id).catch(() => ({ data: [] })),
      ]);
      setPatient(p.data);
      setEditForm({
        name: p.data.name || '',
        phone: p.data.phone || '',
        village: p.data.village || '',
        district: p.data.district || '',
        weight: p.data.weight || '',
        height: p.data.height || '',
        bloodGroup: p.data.bloodGroup || '',
        allergies: p.data.allergies || '',
      });
      setEncounters(e.data);
      setRecords(r.data);
      setDocuments(d.data);
    } catch (err) { console.error(err); }
  };

  const handleSave = async () => {
    try {
      const updated = await patientAPI.update(id, {
        ...patient,
        ...editForm,
        weight: editForm.weight ? parseFloat(editForm.weight) : null,
        height: editForm.height ? parseFloat(editForm.height) : null,
      });
      setPatient(updated.data);
      setEditing(false);
    } catch (err) { alert('Failed to update'); }
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
        {isDoctor && !editing && (
          <button className="btn btn-primary" onClick={() => setEditing(true)}>Edit Health Info</button>
        )}
      </div>

      <div className="dashboard-grid" style={{ marginBottom: 24 }}>
        <div className="stat-card"><div className="stat-icon blue">+</div><div className="stat-info"><h3>{encounters.length}</h3><p>Encounters</p></div></div>
        <div className="stat-card"><div className="stat-icon green">~</div><div className="stat-info"><h3>{records.length}</h3><p>Medical Records</p></div></div>
        <div className="stat-card"><div className="stat-icon yellow">!</div><div className="stat-info"><h3>{documents.length}</h3><p>Documents</p></div></div>
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
            {editing ? (
              <>
                <h3 style={{ marginBottom: 16 }}>Edit Patient Health Info</h3>
                <div className="form-row">
                  <div className="form-group"><label>Name</label><input className="form-input" value={editForm.name} onChange={e => setEditForm({...editForm, name: e.target.value})} /></div>
                  <div className="form-group"><label>Phone</label><input className="form-input" value={editForm.phone} onChange={e => setEditForm({...editForm, phone: e.target.value})} /></div>
                </div>
                <div className="form-row">
                  <div className="form-group"><label>Weight (kg)</label><input type="number" className="form-input" value={editForm.weight} onChange={e => setEditForm({...editForm, weight: e.target.value})} step="0.1" /></div>
                  <div className="form-group"><label>Height (cm)</label><input type="number" className="form-input" value={editForm.height} onChange={e => setEditForm({...editForm, height: e.target.value})} step="0.1" /></div>
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label>Blood Group</label>
                    <select className="form-select" value={editForm.bloodGroup} onChange={e => setEditForm({...editForm, bloodGroup: e.target.value})}>
                      <option value="">Select...</option>
                      <option value="A+">A+</option><option value="A-">A-</option>
                      <option value="B+">B+</option><option value="B-">B-</option>
                      <option value="AB+">AB+</option><option value="AB-">AB-</option>
                      <option value="O+">O+</option><option value="O-">O-</option>
                    </select>
                  </div>
                  <div className="form-group"><label>Village</label><input className="form-input" value={editForm.village} onChange={e => setEditForm({...editForm, village: e.target.value})} /></div>
                </div>
                <div className="form-group">
                  <label>Allergies</label>
                  <input className="form-input" value={editForm.allergies} onChange={e => setEditForm({...editForm, allergies: e.target.value})} placeholder="e.g. Penicillin, Peanuts" />
                </div>
                <div className="form-group"><label>District</label><input className="form-input" value={editForm.district} onChange={e => setEditForm({...editForm, district: e.target.value})} /></div>
                <div style={{ display: 'flex', gap: 8 }}>
                  <button className="btn btn-primary" onClick={handleSave}>Save Changes</button>
                  <button className="btn btn-secondary" onClick={() => setEditing(false)}>Cancel</button>
                </div>
              </>
            ) : (
              <>
                <div className="form-row"><div><strong>Name:</strong> {patient.name}</div><div><strong>MCID:</strong> {patient.mcid}</div></div>
                <div className="form-row" style={{ marginTop: 8 }}><div><strong>Gender:</strong> {patient.gender}</div><div><strong>DOB:</strong> {patient.dateOfBirth}</div></div>
                <div className="form-row" style={{ marginTop: 8 }}><div><strong>Blood Group:</strong> {patient.bloodGroup || 'Not set'}</div><div><strong>Weight:</strong> {patient.weight ? `${patient.weight} kg` : 'Not set'}</div></div>
                <div className="form-row" style={{ marginTop: 8 }}><div><strong>Height:</strong> {patient.height ? `${patient.height} cm` : 'Not set'}</div><div><strong>Phone:</strong> {patient.phone || 'Not set'}</div></div>
                <div style={{ marginTop: 8 }}><strong>Allergies:</strong> {patient.allergies || 'None recorded'}</div>
                <div style={{ marginTop: 8 }}><strong>Village:</strong> {patient.village || '-'}</div>
                <div style={{ marginTop: 8 }}><strong>District:</strong> {patient.district || '-'}</div>
              </>
            )}
          </div>
        </div>
      )}

      {activeTab === 'encounters' && (
        <div className="card"><div className="card-body">
          {encounters.length === 0 ? <div className="empty-state"><p>No encounters</p></div> : (
            <div className="table-container"><table>
              <thead><tr><th>Type</th><th>Arrival</th><th>Discharge</th><th>Doctor</th><th>Hospital</th></tr></thead>
              <tbody>{encounters.map(e => (
                <tr key={e.id}><td><span className="badge badge-blue">{e.encounterType}</span></td><td>{e.arrivalTime}</td><td>{e.dischargeTime || 'Ongoing'}</td><td>{e.doctorName || '-'}</td><td>{e.hospital?.name || '-'}</td></tr>
              ))}</tbody>
            </table></div>
          )}
        </div></div>
      )}

      {activeTab === 'records' && (
        <div className="card"><div className="card-body">
          {records.length === 0 ? <div className="empty-state"><p>No records</p></div> : (
            <div className="table-container"><table>
              <thead><tr><th>Type</th><th>Date</th><th>Doctor</th><th>Summary</th><th>Hospital</th></tr></thead>
              <tbody>{records.map(r => (
                <tr key={r.id}><td><span className="badge badge-green">{r.recordType}</span></td><td>{r.recordDate}</td><td>{r.doctorName || '-'}</td><td>{r.summary || '-'}</td><td>{r.hospital?.name || '-'}</td></tr>
              ))}</tbody>
            </table></div>
          )}
        </div></div>
      )}

      {activeTab === 'documents' && (
        <div className="card"><div className="card-body">
          {documents.length === 0 ? <div className="empty-state"><p>No documents</p></div> : (
            <div className="table-container"><table>
              <thead><tr><th>Type</th><th>File</th><th>Size</th><th>Status</th><th>Uploaded By</th></tr></thead>
              <tbody>{documents.map(d => (
                <tr key={d.id}><td><span className="badge badge-blue">{d.documentType}</span></td><td>{d.fileName}</td><td>{d.fileSize ? `${(d.fileSize/1024).toFixed(1)} KB` : '-'}</td><td><span className="badge badge-yellow">{d.processingStatus}</span></td><td>{d.uploadedBy || '-'}</td></tr>
              ))}</tbody>
            </table></div>
          )}
        </div></div>
      )}
    </div>
  );
}
