import { useState, useEffect, useRef } from 'react';
import { medicalDocumentAPI, patientAPI } from '../api';
import { useAuth } from '../AuthContext';
import { Link } from 'react-router-dom';

export default function MyDocuments() {
  const { user } = useAuth();
  const [documents, setDocuments] = useState([]);
  const [myPatient, setMyPatient] = useState(null);
  const [showUpload, setShowUpload] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);
  const [docType, setDocType] = useState('LAB_REPORT');
  const [uploading, setUploading] = useState(false);
  const fileRef = useRef();

  useEffect(() => { loadData(); }, []);

  const loadData = async () => {
    try {
      const patients = await patientAPI.getAll().catch(() => ({ data: [] }));
      const me = patients.data.find(p => p.phone === user.email || p.name?.includes(user.email?.split('@')[0]));
      if (me) {
        setMyPatient(me);
        const docs = await medicalDocumentAPI.getByPatient(me.id).catch(() => ({ data: [] }));
        setDocuments(docs.data);
      }
    } catch (err) {}
  };

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!selectedFile || !myPatient) return;
    setUploading(true);
    try {
      const formData = new FormData();
      formData.append('file', selectedFile);
      formData.append('patientId', myPatient.id);
      formData.append('documentType', docType);
      await medicalDocumentAPI.upload(formData);
      setShowUpload(false);
      setSelectedFile(null);
      loadData();
    } catch (err) {
      alert(err.response?.data?.error || 'Upload failed');
    } finally {
      setUploading(false);
    }
  };

  return (
    <div className="main-content">
      <div className="page-header">
        <div>
          <Link to="/" style={{ color: 'var(--primary)', fontSize: 14, textDecoration: 'none' }}>&larr; Back to Dashboard</Link>
          <h1>My Documents</h1>
        </div>
        <button className="btn btn-primary" onClick={() => setShowUpload(true)}>Upload Document</button>
      </div>

      {showUpload && (
        <div className="modal-overlay" onClick={() => setShowUpload(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Upload Medical Document</h2>
              <button className="btn btn-sm btn-secondary" onClick={() => setShowUpload(false)}>X</button>
            </div>
            <div className="modal-body">
              <form onSubmit={handleUpload}>
                <div className="form-group">
                  <label>Document Type</label>
                  <select className="form-select" value={docType} onChange={e => setDocType(e.target.value)}>
                    <option value="LAB_REPORT">Lab Report</option>
                    <option value="IMAGING">X-Ray / Imaging</option>
                    <option value="PRESCRIPTION">Prescription</option>
                    <option value="DISCHARGE_SUMMARY">Discharge Summary</option>
                    <option value="IDENTIFICATION">ID Proof</option>
                    <option value="INSURANCE">Insurance</option>
                    <option value="OTHER">Other</option>
                  </select>
                </div>
                <div className="form-group">
                  <label>Select File</label>
                  <div className="upload-area" onClick={() => fileRef.current.click()}>
                    <input ref={fileRef} type="file" style={{ display: 'none' }}
                      onChange={e => setSelectedFile(e.target.files[0])}
                      accept=".pdf,.jpg,.jpeg,.png,.dcm,.doc,.docx" />
                    {selectedFile ? (
                      <p><strong>{selectedFile.name}</strong> ({(selectedFile.size / 1024).toFixed(1)} KB)</p>
                    ) : (
                      <p>Click to select file (PDF, Image, Document)</p>
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
            <div className="empty-state">
              <p>No documents uploaded yet</p>
              <button className="btn btn-primary" onClick={() => setShowUpload(true)}>Upload Your First Document</button>
            </div>
          ) : (
            <div className="table-container">
              <table>
                <thead>
                  <tr><th>Type</th><th>File Name</th><th>Size</th><th>Status</th><th>Uploaded By</th><th>Date</th></tr>
                </thead>
                <tbody>
                  {documents.map(d => (
                    <tr key={d.id}>
                      <td><span className="badge badge-blue">{d.documentType}</span></td>
                      <td>{d.fileName}</td>
                      <td>{d.fileSize ? `${(d.fileSize / 1024).toFixed(1)} KB` : '-'}</td>
                      <td><span className={`badge ${d.processingStatus === 'COMPLETED' ? 'badge-green' : 'badge-yellow'}`}>{d.processingStatus}</span></td>
                      <td>{d.uploadedBy || '-'}</td>
                      <td>{d.uploadedAt?.split('T')[0]}</td>
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
