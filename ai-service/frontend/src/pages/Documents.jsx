import { useState, useEffect, useRef } from 'react';
import { medicalDocumentAPI, patientAPI } from '../api';
import { useAuth } from '../AuthContext';

export default function Documents() {
  const { user } = useAuth();
  const [documents, setDocuments] = useState([]);
  const [patients, setPatients] = useState([]);
  const [showUpload, setShowUpload] = useState(false);
  const [uploadForm, setUploadForm] = useState({ patientId: '', documentType: 'LAB_REPORT' });
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const fileRef = useRef();

  useEffect(() => { loadData(); }, []);

  const loadData = async () => {
    try {
      const [d, p] = await Promise.all([
        medicalDocumentAPI.getAll().catch(() => ({ data: [] })),
        patientAPI.getAll().catch(() => ({ data: [] })),
      ]);
      setDocuments(d.data);
      setPatients(p.data);
    } catch (err) { console.error(err); }
  };

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!selectedFile || !uploadForm.patientId) return;
    setUploading(true);
    try {
      const formData = new FormData();
      formData.append('file', selectedFile);
      formData.append('patientId', uploadForm.patientId);
      formData.append('documentType', uploadForm.documentType);
      await medicalDocumentAPI.upload(formData);
      setShowUpload(false);
      setSelectedFile(null);
      setUploadForm({ patientId: '', documentType: 'LAB_REPORT' });
      loadData();
    } catch (err) {
      alert(err.response?.data?.error || 'Upload failed');
    } finally {
      setUploading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('Delete this document?')) return;
    try {
      await medicalDocumentAPI.delete(id);
      loadData();
    } catch (err) { alert('Failed to delete'); }
  };

  const canUpload = user?.role === 'DOCTOR' || user?.role === 'NURSE' || user?.role === 'ADMIN';

  return (
    <div className="main-content">
      <div className="page-header">
        <h1>Medical Documents</h1>
        {canUpload && (
          <button className="btn btn-primary" onClick={() => setShowUpload(true)}>Upload Document</button>
        )}
      </div>

      {showUpload && (
        <div className="modal-overlay" onClick={() => setShowUpload(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Upload Document</h2>
              <button className="btn btn-sm btn-secondary" onClick={() => setShowUpload(false)}>X</button>
            </div>
            <div className="modal-body">
              <form onSubmit={handleUpload}>
                <div className="form-group">
                  <label>Patient</label>
                  <select className="form-select" value={uploadForm.patientId}
                    onChange={e => setUploadForm({...uploadForm, patientId: e.target.value})} required>
                    <option value="">Select patient...</option>
                    {patients.map(p => (
                      <option key={p.id} value={p.id}>{p.name} ({p.mcid})</option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label>Document Type</label>
                  <select className="form-select" value={uploadForm.documentType}
                    onChange={e => setUploadForm({...uploadForm, documentType: e.target.value})}>
                    <option value="LAB_REPORT">Lab Report</option>
                    <option value="PRESCRIPTION">Prescription</option>
                    <option value="IMAGING">Imaging</option>
                    <option value="DISCHARGE_SUMMARY">Discharge Summary</option>
                    <option value="REFERRAL">Referral</option>
                    <option value="IDENTIFICATION">Identification</option>
                    <option value="INSURANCE">Insurance</option>
                    <option value="OTHER">Other</option>
                  </select>
                </div>
                <div className="form-group">
                  <label>File</label>
                  <div className="upload-area" onClick={() => fileRef.current.click()}>
                    <input ref={fileRef} type="file" style={{ display: 'none' }}
                      onChange={e => setSelectedFile(e.target.files[0])}
                      accept=".pdf,.jpg,.jpeg,.png,.dcm,.doc,.docx" />
                    {selectedFile ? (
                      <p><strong>{selectedFile.name}</strong> ({(selectedFile.size / 1024).toFixed(1)} KB)</p>
                    ) : (
                      <p>Click to select file</p>
                    )}
                  </div>
                </div>
                <button type="submit" className="btn btn-primary" disabled={uploading}>
                  {uploading ? 'Uploading...' : 'Upload'}
                </button>
              </form>
            </div>
          </div>
        </div>
      )}

      <div className="card">
        <div className="card-body">
          {documents.length === 0 ? (
            <div className="empty-state"><p>No documents uploaded yet</p></div>
          ) : (
            <div className="table-container">
              <table>
                <thead>
                  <tr>
                    <th>Type</th>
                    <th>File Name</th>
                    <th>Patient</th>
                    <th>Size</th>
                    <th>Status</th>
                    <th>Uploaded By</th>
                    <th>Date</th>
                    {canUpload && <th>Actions</th>}
                  </tr>
                </thead>
                <tbody>
                  {documents.map(d => (
                    <tr key={d.id}>
                      <td><span className="badge badge-blue">{d.documentType}</span></td>
                      <td>{d.fileName}</td>
                      <td>{d.patient?.name || '-'}</td>
                      <td>{d.fileSize ? `${(d.fileSize / 1024).toFixed(1)} KB` : '-'}</td>
                      <td><span className={`badge ${d.processingStatus === 'COMPLETED' ? 'badge-green' : 'badge-yellow'}`}>{d.processingStatus}</span></td>
                      <td>{d.uploadedBy || '-'}</td>
                      <td>{d.uploadedAt?.split('T')[0]}</td>
                      {canUpload && (
                        <td>
                          <button className="btn btn-sm btn-danger" onClick={() => handleDelete(d.id)}>Delete</button>
                        </td>
                      )}
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
