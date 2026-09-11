import { useState, useEffect } from 'react';
import { medicalRecordAPI, patientAPI, hospitalAPI } from '../api';
import { useAuth } from '../AuthContext';

export default function MedicalRecords() {
  const { user } = useAuth();
  const [records, setRecords] = useState([]);
  const [patients, setPatients] = useState([]);
  const [hospitals, setHospitals] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ recordType: 'DIAGNOSIS', recordDate: '', doctorName: '', summary: '', notes: '', patientId: '', hospitalId: '' });

  useEffect(() => { loadData(); }, []);

  const loadData = async () => {
    try {
      const [r, p, h] = await Promise.all([
        medicalRecordAPI.getAll().catch(() => ({ data: [] })),
        patientAPI.getAll().catch(() => ({ data: [] })),
        hospitalAPI.getAll().catch(() => ({ data: [] })),
      ]);
      setRecords(r.data);
      setPatients(p.data);
      setHospitals(h.data);
    } catch (err) { console.error(err); }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await medicalRecordAPI.create({
        ...form,
        patient: { id: parseInt(form.patientId) },
        hospital: { id: parseInt(form.hospitalId) },
      });
      setShowForm(false);
      setForm({ recordType: 'DIAGNOSIS', recordDate: '', doctorName: '', summary: '', notes: '', patientId: '', hospitalId: '' });
      loadData();
    } catch (err) { alert('Failed to create record'); }
  };

  const canCreate = user?.role === 'DOCTOR' || user?.role === 'ADMIN';

  return (
    <div className="main-content">
      <div className="page-header">
        <h1>Medical Records</h1>
        {canCreate && <button className="btn btn-primary" onClick={() => setShowForm(true)}>Create Record</button>}
      </div>

      {showForm && (
        <div className="modal-overlay" onClick={() => setShowForm(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Create Medical Record</h2>
              <button className="btn btn-sm btn-secondary" onClick={() => setShowForm(false)}>X</button>
            </div>
            <div className="modal-body">
              <form onSubmit={handleCreate}>
                <div className="form-row">
                  <div className="form-group">
                    <label>Patient</label>
                    <select className="form-select" value={form.patientId} onChange={e => setForm({...form, patientId: e.target.value})} required>
                      <option value="">Select...</option>
                      {patients.map(p => <option key={p.id} value={p.id}>{p.name} ({p.mcid})</option>)}
                    </select>
                  </div>
                  <div className="form-group">
                    <label>Hospital</label>
                    <select className="form-select" value={form.hospitalId} onChange={e => setForm({...form, hospitalId: e.target.value})} required>
                      <option value="">Select...</option>
                      {hospitals.map(h => <option key={h.id} value={h.id}>{h.name}</option>)}
                    </select>
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label>Record Type</label>
                    <select className="form-select" value={form.recordType} onChange={e => setForm({...form, recordType: e.target.value})}>
                      <option value="DIAGNOSIS">Diagnosis</option>
                      <option value="PRESCRIPTION">Prescription</option>
                      <option value="LAB_RESULT">Lab Result</option>
                      <option value="SURGERY">Surgery</option>
                      <option value="VACCINATION">Vaccination</option>
                      <option value="ALLERGY">Allergy</option>
                      <option value="RADIOLOGY">Radiology</option>
                      <option value="DISCHARGE_SUMMARY">Discharge Summary</option>
                    </select>
                  </div>
                  <div className="form-group">
                    <label>Record Date</label>
                    <input type="date" className="form-input" value={form.recordDate} onChange={e => setForm({...form, recordDate: e.target.value})} required />
                  </div>
                </div>
                <div className="form-group">
                  <label>Doctor Name</label>
                  <input type="text" className="form-input" value={form.doctorName} onChange={e => setForm({...form, doctorName: e.target.value})} />
                </div>
                <div className="form-group">
                  <label>Summary</label>
                  <textarea className="form-input" rows="2" value={form.summary} onChange={e => setForm({...form, summary: e.target.value})} />
                </div>
                <div className="form-group">
                  <label>Notes</label>
                  <textarea className="form-input" rows="3" value={form.notes} onChange={e => setForm({...form, notes: e.target.value})} />
                </div>
                <button type="submit" className="btn btn-primary">Create Record</button>
              </form>
            </div>
          </div>
        </div>
      )}

      <div className="card">
        <div className="card-body">
          {records.length === 0 ? <div className="empty-state"><p>No records found</p></div> : (
            <div className="table-container">
              <table>
                <thead><tr><th>Patient</th><th>Type</th><th>Date</th><th>Doctor</th><th>Hospital</th><th>Summary</th></tr></thead>
                <tbody>
                  {records.map(r => (
                    <tr key={r.id}>
                      <td>{r.patient?.name}</td>
                      <td><span className="badge badge-green">{r.recordType}</span></td>
                      <td>{r.recordDate}</td>
                      <td>{r.doctorName || '-'}</td>
                      <td>{r.hospital?.name}</td>
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
