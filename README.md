# Medical Continuity

A unified medical history continuity platform that helps doctors access the right patient information at the right time.

---

## Problem

A patient's medical history is scattered across multiple hospitals, clinics, and paper documents. When they visit a new hospital, doctors have no access to previous records.

---

## Solution

Medical Continuity creates a patient-centric layer that brings fragmented medical information together.

```text
Patient arrives at Hospital B
        |
        v
Medical Continuity
        |
   +---------+-----------+
   |                     |
   v                     v
Previous Records     AI Summary
   |                     |
   +----------+----------+
              |
              v
        Doctor Dashboard
```

---

## Project Structure

```
Medical-Continuity/
├── backend/                    Spring Boot 3.2 (Java 21)
│   ├── pom.xml
│   └── src/main/java/
│       └── com.medicalcontinuity.medicalcontinuity/
│           ├── config/         SecurityConfig, RestTemplateConfig
│           ├── controller/     REST API Controllers (9)
│           ├── service/        Business Logic (10)
│           ├── entity/         JPA Entities (11)
│           ├── repositories/   Spring Data JPA (11)
│           ├── enums/          Status/Type enums (10)
│           ├── security/       JWT Authentication
│           └── exception/      Global Exception Handling
│
├── ai-service/                 Flask (Python)
│   ├── app.py                  Flask API with /match endpoint
│   ├── matching.py             Patient matching algorithm
│   ├── embeddings.py           Sentence embeddings
│   ├── vector_search.py        Semantic search
│   ├── document_processing.py  Document chunking pipeline
│   ├── rag.py                  RAG pipeline
│   ├── evaluate.py             Accuracy evaluation
│   ├── requirements.txt        Python dependencies
│   └── frontend/               React (Vite)
│       ├── src/
│       │   ├── api.js          API service layer
│       │   ├── AuthContext.jsx  Authentication context
│       │   ├── components/     Navbar
│       │   └── pages/          10 pages
│       └── package.json
│
└── README.md
```

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 21, Spring Boot 3.2, Spring Data JPA, Hibernate |
| Database | MySQL 8+ (H2 for dev) |
| AI Service | Python 3, Flask, rapidfuzz, sentence-transformers |
| Frontend | React, Vite, Axios, React Router |
| Auth | JWT (jjwt), Spring Security, BCrypt |
| API | REST, JSON |

---

## Setup

### Backend

```bash
cd backend
mvn spring-boot:run
# Runs on http://localhost:8080
```

### AI Service

```bash
cd ai-service
pip install -r requirements.txt
python app.py
# Runs on http://localhost:5000
```

### Frontend

```bash
cd ai-service/frontend
npm install
npm run dev
# Runs on http://localhost:3000
```

---

## Authentication

### Register

```bash
POST /api/auth/register
{
  "email": "doctor@gmail.com",
  "password": "pass123",
  "fullName": "Dr. Smith",
  "role": "DOCTOR"
}
```

### Login

```bash
POST /api/auth/login
{
  "email": "doctor@gmail.com",
  "password": "pass123"
}
```

### Roles

| Role | Access |
|------|--------|
| PATIENT | Create own profile, upload own documents, view own records |
| DOCTOR | View patients, upload documents, create records, run AI matching, edit patient health info |
| NURSE | View patients, upload documents, record encounters, edit patient health info |
| ADMIN | Full access including hospital management |

---

## API Endpoints

### Auth — `/api/auth`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/register` | Register new user |
| POST | `/login` | Login, returns JWT |

### Patients — `/api/patients`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create patient (PATIENT only) |
| GET | `/` | Get all patients |
| GET | `/{id}` | Get patient by ID |
| GET | `/mcid/{mcid}` | Get patient by MCID |
| PUT | `/{id}` | Update patient |
| DELETE | `/{id}` | Delete patient |

**Patient Health Fields:**
- weight (Double, kg)
- height (Double, cm)
- bloodGroup (String: O+, A-, B+, etc.)
- allergies (String)
- village, district, phone, address

### Hospitals — `/api/hospitals`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create hospital (ADMIN) |
| GET | `/` | Get all hospitals |
| GET | `/{id}` | Get hospital by ID |
| PUT | `/{id}` | Update hospital |
| DELETE | `/{id}` | Delete hospital |

### Emergency Contacts — `/api/emergency-contacts`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create contact |
| GET | `/` | Get all contacts |
| GET | `/{id}` | Get contact by ID |
| GET | `/patient/{patientId}` | Get contacts by patient |
| PUT | `/{id}` | Update contact |
| DELETE | `/{id}` | Delete contact |

### Encounters — `/api/encounters`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Record encounter |
| GET | `/` | Get all encounters |
| GET | `/{id}` | Get encounter by ID |
| GET | `/patient/{patientId}` | Get encounters by patient |
| GET | `/hospital/{hospitalId}` | Get encounters by hospital |
| PUT | `/{id}` | Update encounter |
| DELETE | `/{id}` | Delete encounter |

### Medical Records — `/api/medical-records`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create record |
| GET | `/` | Get all records |
| GET | `/{id}` | Get record by ID |
| GET | `/patient/{patientId}` | Get records by patient |
| GET | `/hospital/{hospitalId}` | Get records by hospital |
| PUT | `/{id}` | Update record |
| DELETE | `/{id}` | Delete record |

### Medical Documents — `/api/medical-documents`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/upload` | Upload file (multipart) |
| POST | `/` | Create document metadata |
| GET | `/{id}` | Get document by ID |
| GET | `/patient/{patientId}` | Get documents by patient |
| GET | `/medical-record/{medicalRecordId}` | Get documents by record |
| PUT | `/{id}` | Update document |
| DELETE | `/{id}` | Delete document |

**Upload Parameters:**
- file — the file (PDF, image, document)
- patientId — which patient
- medicalRecordId — optional, link to record
- documentType — LAB_REPORT, PRESCRIPTION, IMAGING, etc.

### Unknown Patients — `/api/unknown-patients`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Register unknown patient |
| GET | `/` | Get all unknown patients |
| GET | `/{id}` | Get by ID |
| GET | `/temporary/{temporaryId}` | Get by TEMP ID |
| PUT | `/{id}` | Update |
| PUT | `/{id}/resolve/{patientId}` | Resolve to known patient |
| DELETE | `/{id}` | Delete |

### Patient Matches — `/api/patient-matches`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Create match |
| GET | `/{id}` | Get match by ID |
| GET | `/unknown-patient/{id}` | Get matches by unknown patient |
| PUT | `/{id}/status?status=X&reviewedBy=Y` | Update status |
| DELETE | `/{id}` | Delete match |

### AI Service — `/api/ai`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/health` | Health check |
| GET | `/match/{unknownPatientId}` | Get AI matches |
| POST | `/match/{unknownPatientId}` | Run match and save to DB |

---

## AI Matching Algorithm

### Weights

| Field | Weight | Why |
|-------|--------|-----|
| Face Similarity | 30% | Biometric — strongest signal |
| Name (fuzzy) | 40% | Core identifier — handles misspellings |
| Village | 25% | Geographic proximity |
| Gender | 15% | Visually identifiable |
| District | 10% | Location signal |
| Phone | 3% | Bonus when available |

### Confidence Levels

| Level | Score | Meaning |
|-------|-------|---------|
| HIGH | >= 0.85 | Strong match |
| REVIEW | >= 0.55 | Needs human review |
| UNRESOLVED | < 0.55 | No good match |

---

## Frontend Pages

| Page | URL | Description |
|------|-----|-------------|
| Login | `/login` | User login |
| Register | `/register` | User registration with health details |
| Dashboard | `/` | Stats + quick actions (role-based) |
| Patients | `/patients` | Patient list + search + create |
| Patient Detail | `/patients/:id` | Overview, encounters, records, documents |
| Documents | `/documents` | Upload and manage (Doctor/Nurse) |
| My Documents | `/my-documents` | Patient uploads own documents |
| My Records | `/my-records` | Patient views own records |
| Unknown Patients | `/unknown-patients` | Register + list |
| AI Matching | `/unknown-patients/:id` | Run match, view scores |
| Encounters | `/encounters` | Record patient visits |
| Medical Records | `/medical-records` | Create and view records |

---

## Database Schema

### Entities

| Entity | Table | Description |
|--------|-------|-------------|
| Patient | `patients` | Core patient with MCID, demographics, health info |
| User | `users` | Authentication (email, password, role) |
| Hospital | `hospitals` | Hospital with registration number |
| EmergencyContact | `emergency_contacts` | Patient emergency contacts |
| PatientEncounter | `patient_encounters` | Patient visits at hospitals |
| MedicalRecord | `medical_records` | Clinical records |
| MedicalDocument | `medical_documents` | Uploaded documents + metadata |
| UnknownPatient | `unknown_patients` | Unidentified patients |
| PatientMatch | `patient_matches` | AI matching results |
| Memory | `memories` | Clinical notes |
| AuditLog | `audit_logs` | System audit trail |

### Relationships

```text
Patient ──1:N──> EmergencyContact
Patient ──1:N──> PatientEncounter
Patient ──1:N──> MedicalRecord
Patient ──1:N──> MedicalDocument
Patient ──1:N──> PatientMatch (candidate)
Patient ──1:N──> Memory
Patient ──1:N──> AuditLog

Hospital ──1:N──> PatientEncounter
Hospital ──1:N──> MedicalRecord

MedicalRecord ──1:N──> MedicalDocument

UnknownPatient ──1:N──> PatientMatch
UnknownPatient ──M:1──> Patient (resolved)
```

---

## Error Handling

| Error | Code | When |
|-------|------|------|
| Not Found | 404 | Resource doesn't exist |
| Conflict | 409 | Duplicate MCID or registration number |
| Bad Request | 400 | Validation errors |
| Unauthorized | 401 | Missing/invalid JWT |
| Forbidden | 403 | Wrong role |
| Server Error | 500 | Unexpected error |

---

## Feature Summary

| Feature | Status |
|---------|--------|
| Patient CRUD | Done |
| Hospital CRUD | Done |
| Emergency Contacts | Done |
| Patient Encounters | Done |
| Medical Records | Done |
| Document Upload | Done |
| Unknown Patient Handling | Done |
| AI Patient Matching | Done |
| JWT Authentication | Done |
| Role-Based Access | Done |
| Health Profile (weight, height, allergies, blood group) | Done |
| Doctor/Nurse can edit patient health info | Done |
| Patient can upload own documents | Done |
| React Frontend (blue theme) | Done |
| Global Exception Handling | Done |
| Database Integrity Constraints | Done |

---

## Disclaimer

Medical Continuity is a technology prototype. It does not replace doctors, clinical judgment, or emergency medical services. AI-generated information should be verified by qualified healthcare professionals.

---

## Vision

> **One patient. One continuous medical history. Anywhere they receive care.**
