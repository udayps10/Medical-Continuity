import { useState, useEffect } from 'react';
import { unknownPatientAPI } from '../api';
import { Link } from 'react-router-dom';

export default function UnknownPatients() {
  const [unknowns, setUnknowns] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ gender: 'MALE', location: '', description: '', approximateAge: '' });

  useEffect(() => { loadUnknowns(); }, []);

  const loadUnknowns = async () => {
    try {
      const res = await unknownPatientAPI.getAll();
      setUnknowns(res.data);
    } catch (err) { console.error(err); }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await unknownPatientAPI.create({
        ...form,
        approximateAge: form.approximateAge ? parseInt(form.approximateAge) : null
      });
      setShowForm(false);
      setForm({ gender: 'MALE', location: '', description: '', approximateAge: '' });
      loadUnknowns();
    } catch (err) { alert('Failed to create unknown patient'); }
  };

  const handleDelete = async (id) => {
    if (!confirm('Delete this record?')) return;
    try {
      await unknownPatientAPI.delete(id);
      loadUnknowns();
    } catch (err) { alert('Failed to delete'); }
  };

  const getStatusBadge = (status) => {
    const map = {
      'UNIDENTIFIED': 'badge-red',
      'UNDER_REVIEW': 'badge-yellow',
      'MATCHED': 'badge-blue',
      'IDENTIFIED': 'badge-green',
    };
    return map[status] || 'badge-gray';
  };

  return (
    <div className="main-content">
      <div className="page-header">
        <h1>Unknown Patients</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(true)}>Register Unknown Patient</button>
      </div>

      {showForm && (
        <div className="modal-overlay" onClick={() => setShowForm(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Register Unknown Patient</h2>
              <button className="btn btn-sm btn-secondary" onClick={() => setShowForm(false)}>X</button>
            </div>
            <div className="modal-body">
              <form onSubmit={handleCreate}>
                <div className="form-row">
                  <div className="form-group">
                    <label>Gender</label>
                    <select className="form-select" value={form.gender}
                      onChange={e => setForm({...form, gender: e.target.value})}>
                      <option value="MALE">Male</option>
                      <option value="FEMALE">Female</option>
                      <option value="OTHER">Other</option>
                      <option value="UNKNOWN">Unknown</option>
                    </select>
                  </div>
                  <div className="form-group">
                    <label>Approximate Age</label>
                    <input type="number" className="form-input" value={form.approximateAge}
                      onChange={e => setForm({...form, approximateAge: e.target.value})} />
                  </div>
                </div>
                <div className="form-group">
                  <label>Location Found</label>
                  <input type="text" className="form-input" value={form.location}
                    onChange={e => setForm({...form, location: e.target.value})} required />
                </div>
                <div className="form-group">
                  <label>Description</label>
                  <textarea className="form-input" rows="3" value={form.description}
                    onChange={e => setForm({...form, description: e.target.value})} />
                </div>
                <button type="submit" className="btn btn-primary">Register</button>
              </form>
            </div>
          </div>
        </div>
      )}

      <div className="card">
        <div className="card-body">
          {unknowns.length === 0 ? (
            <div className="empty-state"><p>No unknown patients registered</p></div>
          ) : (
            <div className="table-container">
              <table>
                <thead>
                  <tr>
                    <th>Temp ID</th>
                    <th>Gender</th>
                    <th>Age</th>
                    <th>Location</th>
                    <th>Status</th>
                    <th>Discovered</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {unknowns.map(u => (
                    <tr key={u.id}>
                      <td><strong>{u.temporaryId}</strong></td>
                      <td><span className="badge badge-blue">{u.gender}</span></td>
                      <td>{u.approximateAge || '-'}</td>
                      <td>{u.location || '-'}</td>
                      <td><span className={`badge ${getStatusBadge(u.status)}`}>{u.status}</span></td>
                      <td>{u.discoveredAt?.split('T')[0]}</td>
                      <td>
                        <Link to={`/unknown-patients/${u.id}`} className="btn btn-sm btn-primary">View & Match</Link>
                        <button className="btn btn-sm btn-danger" style={{ marginLeft: 4 }} onClick={() => handleDelete(u.id)}>Delete</button>
                      </td>
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
