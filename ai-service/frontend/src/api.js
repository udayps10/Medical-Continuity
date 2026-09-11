import axios from 'axios';

const API_BASE = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE,
  headers: { 'Content-Type': 'application/json' }
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  login: (email, password) => api.post('/auth/login', { email, password }),
  register: (data) => api.post('/auth/register', data),
};

export const patientAPI = {
  getAll: () => api.get('/patients'),
  getById: (id) => api.get(`/patients/${id}`),
  getByMcid: (mcid) => api.get(`/patients/mcid/${mcid}`),
  create: (data) => api.post('/patients', data),
  update: (id, data) => api.put(`/patients/${id}`, data),
  delete: (id) => api.delete(`/patients/${id}`),
};

export const hospitalAPI = {
  getAll: () => api.get('/hospitals'),
  getById: (id) => api.get(`/hospitals/${id}`),
  create: (data) => api.post('/hospitals', data),
  update: (id, data) => api.put(`/hospitals/${id}`, data),
  delete: (id) => api.delete(`/hospitals/${id}`),
};

export const encounterAPI = {
  getAll: () => api.get('/encounters'),
  getById: (id) => api.get(`/encounters/${id}`),
  getByPatient: (patientId) => api.get(`/encounters/patient/${patientId}`),
  create: (data) => api.post('/encounters', data),
  update: (id, data) => api.put(`/encounters/${id}`, data),
  delete: (id) => api.delete(`/encounters/${id}`),
};

export const medicalRecordAPI = {
  getAll: () => api.get('/medical-records'),
  getById: (id) => api.get(`/medical-records/${id}`),
  getByPatient: (patientId) => api.get(`/medical-records/patient/${patientId}`),
  create: (data) => api.post('/medical-records', data),
  update: (id, data) => api.put(`/medical-records/${id}`, data),
  delete: (id) => api.delete(`/medical-records/${id}`),
};

export const medicalDocumentAPI = {
  getAll: () => api.get('/medical-documents'),
  getById: (id) => api.get(`/medical-documents/${id}`),
  getByPatient: (patientId) => api.get(`/medical-documents/patient/${patientId}`),
  upload: (formData) => api.post('/medical-documents/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }),
  delete: (id) => api.delete(`/medical-documents/${id}`),
};

export const unknownPatientAPI = {
  getAll: () => api.get('/unknown-patients'),
  getById: (id) => api.get(`/unknown-patients/${id}`),
  getByTempId: (tempId) => api.get(`/unknown-patients/temporary/${tempId}`),
  create: (data) => api.post('/unknown-patients', data),
  update: (id, data) => api.put(`/unknown-patients/${id}`, data),
  resolve: (id, patientId) => api.put(`/unknown-patients/${id}/resolve/${patientId}`),
  delete: (id) => api.delete(`/unknown-patients/${id}`),
};

export const patientMatchAPI = {
  getById: (id) => api.get(`/patient-matches/${id}`),
  getByUnknownPatient: (unknownPatientId) => api.get(`/patient-matches/unknown-patient/${unknownPatientId}`),
  create: (data) => api.post('/patient-matches', data),
  updateStatus: (id, status, reviewedBy) => api.put(`/patient-matches/${id}/status?status=${status}&reviewedBy=${reviewedBy}`),
  delete: (id) => api.delete(`/patient-matches/${id}`),
};

export const aiAPI = {
  health: () => api.get('/ai/health'),
  getMatches: (unknownPatientId) => api.get(`/ai/match/${unknownPatientId}`),
  runMatch: (unknownPatientId) => api.post(`/ai/match/${unknownPatientId}`),
};

export default api;
