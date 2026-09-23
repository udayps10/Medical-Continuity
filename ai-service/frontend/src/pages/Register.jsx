import { useState } from 'react';
import { useAuth } from '../AuthContext';
import { Link, useNavigate } from 'react-router-dom';
import { patientAPI } from '../api';

export default function Register() {
  const [form, setForm] = useState({
    email: '', password: '', fullName: '', role: 'PATIENT',
    mcid: '', phone: '', village: '', district: '', dateOfBirth: '', gender: 'MALE',
    weight: '', height: '', bloodGroup: '', allergies: ''
  });
  const [error, setError] = useState('');
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await register(form.email, form.password, form.fullName, form.role);

      if (form.role === 'PATIENT') {
        const patientData = {
          mcid: form.mcid || `MC-${Date.now()}`,
          name: form.fullName,
          dob: form.dateOfBirth || '2000-01-01',
          gender: form.gender,
          phone: form.phone || null,
          village: form.village,
          district: form.district,
          bloodGroup: form.bloodGroup || null,
          allergies: form.allergies || null,
          identityStatus: 'PROVISIONAL'
        };
        await patientAPI.create(patientData);
      }

      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data?.error || 'Registration failed');
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card" style={{ maxWidth: 500 }}>
        <div className="auth-header">
          <div className="auth-logo">+</div>
          <h1>Create Account</h1>
          <p>Join Medical Continuity</p>
        </div>

        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Account Type</label>
            <select className="form-select" value={form.role}
              onChange={(e) => setForm({ ...form, role: e.target.value })}>
              <option value="PATIENT">Patient</option>
              <option value="DOCTOR">Doctor</option>
              <option value="NURSE">Nurse</option>
            </select>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Full Name</label>
              <input type="text" className="form-input" value={form.fullName}
                onChange={(e) => setForm({ ...form, fullName: e.target.value })} required />
            </div>
            <div className="form-group">
              <label>Email</label>
              <input type="email" className="form-input" value={form.email}
                onChange={(e) => setForm({ ...form, email: e.target.value })} required />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Password</label>
              <input type="password" className="form-input" value={form.password}
                onChange={(e) => setForm({ ...form, password: e.target.value })} required />
            </div>
            {form.role === 'PATIENT' && (
              <div className="form-group">
                <label>MCID (optional)</label>
                <input type="text" className="form-input" value={form.mcid}
                  onChange={(e) => setForm({ ...form, mcid: e.target.value })}
                  placeholder="Auto-generated if empty" />
              </div>
            )}
          </div>

          {form.role === 'PATIENT' && (
            <>
              <div style={{ borderTop: '1px solid var(--gray-200)', margin: '16px 0', paddingTop: 16 }}>
                <h3 style={{ fontSize: 14, color: 'var(--primary)', marginBottom: 12 }}>Health Information</h3>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Date of Birth</label>
                  <input type="date" className="form-input" value={form.dateOfBirth}
                    onChange={(e) => setForm({ ...form, dateOfBirth: e.target.value })} />
                </div>
                <div className="form-group">
                  <label>Gender</label>
                  <select className="form-select" value={form.gender}
                    onChange={(e) => setForm({ ...form, gender: e.target.value })}>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                    <option value="OTHER">Other</option>
                  </select>
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Weight (kg)</label>
                  <input type="number" className="form-input" value={form.weight}
                    onChange={(e) => setForm({ ...form, weight: e.target.value })}
                    placeholder="e.g. 72" step="0.1" />
                </div>
                <div className="form-group">
                  <label>Height (cm)</label>
                  <input type="number" className="form-input" value={form.height}
                    onChange={(e) => setForm({ ...form, height: e.target.value })}
                    placeholder="e.g. 175" step="0.1" />
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Blood Group</label>
                  <select className="form-select" value={form.bloodGroup}
                    onChange={(e) => setForm({ ...form, bloodGroup: e.target.value })}>
                    <option value="">Select...</option>
                    <option value="A+">A+</option>
                    <option value="A-">A-</option>
                    <option value="B+">B+</option>
                    <option value="B-">B-</option>
                    <option value="AB+">AB+</option>
                    <option value="AB-">AB-</option>
                    <option value="O+">O+</option>
                    <option value="O-">O-</option>
                  </select>
                </div>
                <div className="form-group">
                  <label>Phone</label>
                  <input type="text" className="form-input" value={form.phone}
                    onChange={(e) => setForm({ ...form, phone: e.target.value })}
                    placeholder="Optional" />
                </div>
              </div>

              <div className="form-group">
                <label>Allergies</label>
                <input type="text" className="form-input" value={form.allergies}
                  onChange={(e) => setForm({ ...form, allergies: e.target.value })}
                  placeholder="e.g. Penicillin, Peanuts" />
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Village</label>
                  <input type="text" className="form-input" value={form.village}
                    onChange={(e) => setForm({ ...form, village: e.target.value })} />
                </div>
                <div className="form-group">
                  <label>District</label>
                  <input type="text" className="form-input" value={form.district}
                    onChange={(e) => setForm({ ...form, district: e.target.value })} />
                </div>
              </div>
            </>
          )}

          <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: 12 }}>
            Create Account
          </button>
        </form>

        <div className="auth-footer">
          Already have an account? <Link to="/login">Sign In</Link>
        </div>
      </div>
    </div>
  );
}
