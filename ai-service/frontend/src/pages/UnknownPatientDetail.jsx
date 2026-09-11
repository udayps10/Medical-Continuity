import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { unknownPatientAPI, patientMatchAPI, aiAPI, patientAPI } from '../api';

export default function UnknownPatientDetail() {
  const { id } = useParams();
  const [unknown, setUnknown] = useState(null);
  const [matches, setMatches] = useState([]);
  const [aiResults, setAiResults] = useState(null);
  const [loading, setLoading] = useState(false);
  const [patients, setPatients] = useState([]);

  useEffect(() => { loadData(); }, [id]);

  const loadData = async () => {
    try {
      const [u, m, p] = await Promise.all([
        unknownPatientAPI.getById(id),
        patientMatchAPI.getByUnknownPatient(id).catch(() => ({ data: [] })),
        patientAPI.getAll().catch(() => ({ data: [] })),
      ]);
      setUnknown(u.data);
      setMatches(m.data);
      setPatients(p.data);
    } catch (err) { console.error(err); }
  };

  const runAIMatch = async () => {
    setLoading(true);
    try {
      const res = await aiAPI.runMatch(id);
      setAiResults(res.data);
      loadData();
    } catch (err) {
      alert('AI matching failed. Make sure the AI service is running on port 5000.');
    } finally {
      setLoading(false);
    }
  };

  const resolveToPatient = async (patientId) => {
    if (!confirm('Resolve this unknown patient to the selected patient?')) return;
    try {
      await unknownPatientAPI.resolve(id, patientId);
      loadData();
    } catch (err) { alert('Failed to resolve'); }
  };

  const updateMatchStatus = async (matchId, status) => {
    try {
      await patientMatchAPI.updateStatus(matchId, status, 'doctor@medical.com');
      loadData();
    } catch (err) { alert('Failed to update status'); }
  };

  const getConfidenceBadge = (confidence) => {
    const map = { 'HIGH': 'badge-green', 'REVIEW': 'badge-yellow', 'UNRESOLVED': 'badge-red' };
    return map[confidence] || 'badge-gray';
  };

  if (!unknown) return <div className="main-content"><p>Loading...</p></div>;

  return (
    <div className="main-content">
      <div className="page-header">
        <div>
          <Link to="/unknown-patients" style={{ color: 'var(--primary)', fontSize: 14, textDecoration: 'none' }}>&larr; Back</Link>
          <h1>{unknown.temporaryId}</h1>
          <p style={{ color: 'var(--gray-500)' }}>Status: <span className={`badge ${unknown.status === 'IDENTIFIED' ? 'badge-green' : 'badge-yellow'}`}>{unknown.status}</span></p>
        </div>
        <button className="btn btn-primary" onClick={runAIMatch} disabled={loading}>
          {loading ? 'Matching...' : 'Run AI Match'}
        </button>
      </div>

      <div className="grid-2" style={{ marginBottom: 24 }}>
        <div className="card">
          <div className="card-header"><h2>Patient Details</h2></div>
          <div className="card-body">
            <div className="form-row"><div><strong>Gender:</strong> {unknown.gender}</div><div><strong>Age:</strong> {unknown.approximateAge || '-'}</div></div>
            <div className="form-row" style={{ marginTop: 8 }}><div><strong>Location:</strong> {unknown.location || '-'}</div><div><strong>Discovered:</strong> {unknown.discoveredAt?.split('T')[0]}</div></div>
            <div style={{ marginTop: 8 }}><strong>Description:</strong> {unknown.description || '-'}</div>
            {unknown.resolvedPatient && (
              <div style={{ marginTop: 12, padding: 12, background: 'var(--primary-50)', borderRadius: 8 }}>
                <strong>Resolved to:</strong> {unknown.resolvedPatient.name} ({unknown.resolvedPatient.mcid})
              </div>
            )}
          </div>
        </div>

        <div className="card">
          <div className="card-header"><h2>Quick Resolve</h2></div>
          <div className="card-body">
            <p style={{ fontSize: 13, color: 'var(--gray-500)', marginBottom: 12 }}>Select a patient to resolve this unknown patient to:</p>
            <select className="form-select" id="resolve-select">
              <option value="">Select patient...</option>
              {patients.map(p => (
                <option key={p.id} value={p.id}>{p.name} ({p.mcid})</option>
              ))}
            </select>
            <button className="btn btn-success" style={{ marginTop: 12 }}
              onClick={() => {
                const sel = document.getElementById('resolve-select');
                if (sel.value) resolveToPatient(parseInt(sel.value));
              }}>
              Resolve
            </button>
          </div>
        </div>
      </div>

      <div className="card">
        <div className="card-header">
          <h2>AI Match Results</h2>
          <span className="badge badge-blue">{matches.length} matches</span>
        </div>
        <div className="card-body">
          {matches.length === 0 ? (
            <div className="empty-state">
              <p>No matches yet</p>
              <button className="btn btn-primary" onClick={runAIMatch} disabled={loading}>
                {loading ? 'Running...' : 'Run AI Matching'}
              </button>
            </div>
          ) : (
            <div className="table-container">
              <table>
                <thead>
                  <tr>
                    <th>Patient</th>
                    <th>MCID</th>
                    <th>Score</th>
                    <th>Confidence</th>
                    <th>Status</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {matches.map(m => (
                    <tr key={m.id}>
                      <td>{m.candidatePatient?.name}</td>
                      <td>{m.candidatePatient?.mcid}</td>
                      <td>
                        <div className="match-score">
                          <div className="score-bar">
                            <div className={`score-fill ${m.similarityScore >= 0.85 ? 'score-high' : m.similarityScore >= 0.55 ? 'score-medium' : 'score-low'}`}
                              style={{ width: `${m.similarityScore * 100}%` }} />
                          </div>
                          {(m.similarityScore * 100).toFixed(1)}%
                        </div>
                      </td>
                      <td><span className={`badge ${getConfidenceBadge(m.matchReason?.includes('HIGH') ? 'HIGH' : m.matchReason?.includes('REVIEW') ? 'REVIEW' : 'UNRESOLVED')}`}>
                        {m.matchReason?.includes('HIGH') ? 'HIGH' : m.matchReason?.includes('REVIEW') ? 'REVIEW' : 'PENDING'}
                      </span></td>
                      <td><span className="badge badge-gray">{m.status}</span></td>
                      <td>
                        {m.status === 'PENDING' && (
                          <>
                            <button className="btn btn-sm btn-success" onClick={() => updateMatchStatus(m.id, 'CONFIRMED')}>Confirm</button>
                            <button className="btn btn-sm btn-danger" style={{ marginLeft: 4 }} onClick={() => updateMatchStatus(m.id, 'REJECTED')}>Reject</button>
                          </>
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
