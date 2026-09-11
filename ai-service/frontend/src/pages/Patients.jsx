import { useState, useEffect } from 'react';
import { patientAPI } from '../api';
import { Link } from 'react-router-dom';
import { useAuth } from '../AuthContext';

export default function Patients() {
  const { user } = useAuth();
  const [patients, setPatients] = useState([]);
  const [search, setSearch] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ mcid: '', name: '', dateOfBirth: '', gender: 'MALE', phone: '', village: '', district: '', address: '' });

  useEffect(() => { loadPatients(); }, []);

  const loadPatients = async () => {
    try {
      const res = await patientAPI.getAll();
      setPatients(res.data);
    } catch (err) { console.error(err); }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await patientAPI.create(form);
      setShowForm(false);
      setForm({ mcid: '', name: '', dateOfBirth: '', gender: 'MALE', phone: '', village: '', district: '', address: '' });
      loadPatients();
    } catch (err) { alert(err.response?.data?.message || 'Failed to create patient'); }
  };

  const handleDelete = async (id) => {
    if (!confirm('Delete this patient?')) return;
    try {
      await patientAPI.delete(id);
      loadPatients();
    } catch (err) { alert('Failed to delete'); }
  };

  const filtered = patients.filter(p =>
    p.name?.toLowerCase().includes(search.toLowerCase()) ||
    p.mcid?.toLowerCase().includes(search.toLowerCase()) ||
    p.village?.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="main-content">
      <div className="page-header">
        <h1>Patients</h1>
        {user?.role === 'PATIENT' && (
          <button className="btn btn-primary" onClick={() => setShowForm(true)}>Create Profile</button>
        )}
      </div>

      <div className="card" style={{ marginBottom: 20 }}>
        <div className="card-body" style={{ padding: '12px 16px' }}>
          <input type="text" className="form-input" placeholder="Search by name, MCID, or village..."
            value={search} onChange={(e) => setSearch(e.target.value)} />
        </div>
      </div>

      {showForm && (
        <div className="modal-overlay" onClick={() => setShowForm(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Create Patient Profile</h2>
              <button className="btn btn-sm btn-secondary" onClick={() => setShowForm(false)}>X</button>
            </div>
            <div className="modal-body">
              <form onSubmit={handleCreate}>
                <div className="form-row">
                  <div className="form-group">
                    <label>MCID</label>
                    <input type="text" className="form-input" value={form.mcid}
                      onChange={e => setForm({...form, mcid: e.target.value})} required />
                  </div>
                  <div className="form-group">
                    <label>Full Name</label>
                    <input type="text" className="form-input" value={form.name}
                      onChange={e => setForm({...form, name: e.target.value})} required />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label>Date of Birth</label>
                    <input type="date" className="form-input" value={form.dateOfBirth}
                      onChange={e => setForm({...form, dateOfBirth: e.target.value})} required />
                  </div>
                  <div className="form-group">
                    <label>Gender</label>
                    <select className="form-select" value={form.gender}
                      onChange={e => setForm({...form, gender: e.target.value})}>
                      <option value="MALE">Male</option>
                      <option value="FEMALE">Female</option>
                      <option value="OTHER">Other</option>
                    </select>
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label>Phone</label>
                    <input type="text" className="form-input" value={form.phone}
                      onChange={e => setForm({...form, phone: e.target.value})} />
                  </div>
                  <div className="form-group">
                    <label>Village</label>
                    <input type="text" className="form-input" value={form.village}
                      onChange={e => setForm({...form, village: e.target.value})} />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label>District</label>
                    <input type="text" className="form-input" value={form.district}
                      onChange={e => setForm({...form, district: e.target.value})} />
                  </div>
                  <div className="form-group">
                    <label>Address</label>
                    <input type="text" className="form-input" value={form.address}
                      onChange={e => setForm({...form, address: e.target.value})} />
                  </div>
                </div>
                <button type="submit" className="btn btn-primary">Create Patient</button>
              </form>
            </div>
          </div>
        </div>
      )}

      <div className="card">
        <div className="card-body">
          {filtered.length === 0 ? (
            <div className="empty-state"><p>No patients found</p></div>
          ) : (
            <div className="table-container">
              <table>
                <thead>
                  <tr>
                    <th>MCID</th>
                    <th>Name</th>
                    <th>Gender</th>
                    <th>DOB</th>
                    <th>Village</th>
                    <th>District</th>
                    <th>Phone</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {filtered.map(p => (
                    <tr key={p.id}>
                      <td><strong>{p.mcid}</strong></td>
                      <td>{p.name}</td>
                      <td><span className="badge badge-blue">{p.gender}</span></td>
                      <td>{p.dateOfBirth}</td>
                      <td>{p.village || '-'}</td>
                      <td>{p.district || '-'}</td>
                      <td>{p.phone || '-'}</td>
                      <td>
                        <Link to={`/patients/${p.id}`} className="btn btn-sm btn-outline">View</Link>
                        {user?.role === 'PATIENT' && (
                          <button className="btn btn-sm btn-danger" style={{ marginLeft: 4 }} onClick={() => handleDelete(p.id)}>Delete</button>
                        )}
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
