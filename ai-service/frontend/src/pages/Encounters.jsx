import { useState, useEffect } from 'react';
import { encounterAPI, patientAPI, hospitalAPI } from '../api';
import { useAuth } from '../AuthContext';

export default function Encounters() {
  const { user } = useAuth();
  const [encounters, setEncounters] = useState([]);
  const [patients, setPatients] = useState([]);
  const [hospitals, setHospitals] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ encounterType: 'EMERGENCY', arrivalTime: '', doctorName: '', notes: '', patientId: '', hospitalId: '' });

  useEffect(() => { loadData(); }, []);

  const loadData = async () => {
    try {
      const [e, p, h] = await Promise.all([
        encounterAPI.getAll().catch(() => ({ data: [] })),
        patientAPI.getAll().catch(() => ({ data: [] })),
        hospitalAPI.getAll().catch(() => ({ data: [] })),
      ]);
      setEncounters(e.data);
      setPatients(p.data);
      setHospitals(h.data);
    } catch (err) { console.error(err); }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await encounterAPI.create({
        encounterType: form.encounterType,
        arrivalTime: form.arrivalTime,
        doctorName: form.doctorName,
        notes: form.notes,
        patient: { id: parseInt(form.patientId) },
        hospital: { id: parseInt(form.hospitalId) },
      });
      setShowForm(false);
      setForm({ encounterType: 'EMERGENCY', arrivalTime: '', doctorName: '', notes: '', patientId: '', hospitalId: '' });
      loadData();
    } catch (err) { alert('Failed to create encounter'); }
  };

  const canCreate = user?.role === 'DOCTOR' || user?.role === 'NURSE' || user?.role === 'ADMIN';

  return (
    <div className="main-content">
      <div className="page-header">
        <h1>Patient Encounters</h1>
        {canCreate && <button className="btn btn-primary" onClick={() => setShowForm(true)}>Record Encounter</button>}
      </div>

      {showForm && (
        <div className="modal-overlay" onClick={() => setShowForm(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Record Encounter</h2>
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
                    <label>Encounter Type</label>
                    <select className="form-select" value={form.encounterType} onChange={e => setForm({...form, encounterType: e.target.value})}>
                      <option value="EMERGENCY">Emergency</option>
                      <option value="ADMITTED">Admitted</option>
                      <option value="OUTPATIENT">Outpatient</option>
                      <option value="TRANSFER">Transfer</option>
                    </select>
                  </div>
                  <div className="form-group">
                    <label>Arrival Time</label>
                    <input type="datetime-local" className="form-input" value={form.arrivalTime} onChange={e => setForm({...form, arrivalTime: e.target.value})} required />
                  </div>
                </div>
                <div className="form-group">
                  <label>Doctor Name</label>
                  <input type="text" className="form-input" value={form.doctorName} onChange={e => setForm({...form, doctorName: e.target.value})} />
                </div>
                <div className="form-group">
                  <label>Notes</label>
                  <textarea className="form-input" rows="3" value={form.notes} onChange={e => setForm({...form, notes: e.target.value})} />
                </div>
                <button type="submit" className="btn btn-primary">Record</button>
              </form>
            </div>
          </div>
        </div>
      )}

      <div className="card">
        <div className="card-body">
          {encounters.length === 0 ? <div className="empty-state"><p>No encounters recorded</p></div> : (
            <div className="table-container">
              <table>
                <thead><tr><th>Patient</th><th>Type</th><th>Hospital</th><th>Arrival</th><th>Doctor</th></tr></thead>
                <tbody>
                  {encounters.map(e => (
                    <tr key={e.id}>
                      <td>{e.patient?.name} ({e.patient?.mcid})</td>
                      <td><span className="badge badge-blue">{e.encounterType}</span></td>
                      <td>{e.hospital?.name}</td>
                      <td>{e.arrivalTime?.replace('T', ' ')}</td>
                      <td>{e.doctorName || '-'}</td>
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
