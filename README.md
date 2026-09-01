# 🏥 Medical Continuity

> **A unified, AI-powered medical history continuity platform that helps doctors access the right patient information at the right time — even when records are fragmented across hospitals.**

## 🚨 The Problem

A patient's medical history is often scattered across multiple hospitals, clinics, laboratories, prescriptions, and paper documents.

Imagine:

1. A patient is treated at **Hospital A**.
2. They receive X-rays, blood tests, diagnoses, prescriptions, and discharge summaries.
3. Six months later, they visit **Hospital B**.
4. Hospital B may not have access to the patient's previous records.
5. The patient has to explain their history again or carry physical documents.

This creates:

* ❌ Fragmented medical histories
* ❌ Repeated tests
* ❌ Delayed diagnosis
* ❌ Medication/allergy risks
* ❌ Loss of important historical information
* ❌ Poor continuity between healthcare providers

---

# 💡 Our Solution

**Medical Continuity** creates a patient-centric layer that brings fragmented medical information together.

Instead of forcing doctors to search through dozens of documents, the system can:

> **Fetch → Extract → Understand → Organize → Summarize → Present**

the patient's relevant medical history.

The goal is not to replace doctors.

The goal is to give doctors **better context before they make decisions.**

---

# 🎯 Core Vision

### "Your medical history should follow you, not your hospital."

Medical Continuity aims to provide a secure and intelligent health-history layer where information from different sources can be transformed into a structured timeline.

For example:

```text
Patient
   │
   ├── Hospital A
   │     ├── Blood Tests
   │     ├── X-Ray
   │     ├── Diagnosis
   │     └── Prescription
   │
   ├── Hospital B
   │     ├── Consultation
   │     └── Lab Report
   │
   └── Hospital C
         ├── Surgery
         └── Discharge Summary
                │
                ▼
        Medical Continuity
                │
        ┌───────┴────────┐
        ▼                ▼
 Structured Timeline   AI Summary
        │                │
        └───────┬────────┘
                ▼
          Doctor Dashboard
```

---

# ✨ Key Features

## 1. 🧑‍⚕️ Unified Patient Timeline

All available medical events can be organized chronologically.

Example:

```text
2025-02-12
Blood Test
↓
2025-03-04
Hospital Consultation
↓
2025-03-08
Diagnosis
↓
2025-03-15
Surgery
↓
2025-03-22
Discharge
↓
2025-08-10
Follow-up
```

Doctors can quickly understand what happened and when.

---

## 2. 🤖 AI Medical Information Extraction

Medical documents may arrive as:

* PDFs
* Scanned reports
* Images
* Prescriptions
* Discharge summaries
* Laboratory reports

AI/OCR can extract relevant information such as:

```text
Patient Name
Age
Diagnosis
Symptoms
Medications
Allergies
Lab Results
Procedures
Doctors
Hospital
Dates
Follow-ups
```

The extracted information can then be converted into structured data.

---

## 3. 🧠 AI-Powered Medical Summary

Instead of making a doctor read multiple documents, Medical Continuity can generate a concise history.

Example:

```text
PATIENT SUMMARY

Previous Diagnosis:
Type 2 Diabetes

Previous Procedure:
Appendectomy — March 2025

Current Medications:
Metformin
Atorvastatin

Important History:
Previous hospitalization in March 2025.

Recent Lab:
HbA1c — 7.2%

Potentially Relevant Records:
March 2025 hospitalization
June 2025 blood report
August 2025 prescription
```

The AI is intended to **summarize existing records**, not independently diagnose the patient.

---

# 🔍 4. Intelligent Record Retrieval

When a doctor opens a patient's profile, the system can prioritize records relevant to the current consultation.

For example:

```text
Doctor:
"Patient has chest pain."

             ↓

Medical Continuity

             ↓

Relevant History
├── Previous cardiac reports
├── ECG
├── Blood tests
├── Previous medications
└── Relevant hospitalizations
```

This reduces the amount of irrelevant information presented to the doctor.

---

# 💊 5. Medication History

Medical Continuity can maintain a longitudinal medication history.

```text
Medication History

2025
├── Medicine A
├── Medicine B

2026
├── Medicine B
└── Medicine C
```

This helps doctors understand previous treatments and changes over time.

---

# ⚠️ 6. Important Medical Alerts

The platform can surface information already present in the patient's records, such as:

* Documented allergies
* Previous medications
* Important diagnoses
* Previous procedures
* Abnormal historical results

Example:

```text
⚠️ IMPORTANT

Patient has a documented allergy
to Medicine X.

Source:
Hospital B — Discharge Summary
```

The system should present the source record so healthcare professionals can verify important information.

---

# 🔐 7. Patient Data Security

Medical data is highly sensitive.

The architecture is therefore designed around:

* Authentication
* Authorization
* Role-based access
* Secure APIs
* Encryption
* Audit logging
* Controlled record access
* Patient consent/access policies

Example:

```text
Patient
   │
   │ Permission
   ▼
Healthcare Provider
   │
   ▼
Authorized Records
```

A doctor should only receive information they are authorized to access.

---

# 👨‍⚕️ Doctor Workflow

### Without Medical Continuity

```text
Patient arrives
      ↓
Doctor asks history
      ↓
Patient searches documents
      ↓
Doctor reads multiple reports
      ↓
Previous history reconstructed manually
      ↓
Consultation
```

### With Medical Continuity

```text
Patient arrives
      ↓
Patient identified
      ↓
Authorized records retrieved
      ↓
AI organizes relevant history
      ↓
Doctor reviews timeline + source records
      ↓
Consultation
```

---

# 🏗️ System Architecture

```text
                    ┌──────────────────┐
                    │     Patient      │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │ Authentication   │
                    │ & Authorization  │
                    └────────┬─────────┘
                             │
                             ▼
              ┌────────────────────────────┐
              │   Medical Continuity API   │
              └─────────────┬──────────────┘
                            │
              ┌─────────────┼─────────────┐
              ▼             ▼             ▼
        ┌──────────┐  ┌───────────┐  ┌──────────┐
        │ Records  │  │ AI / OCR  │  │ Patient  │
        │ Service  │  │ Pipeline  │  │ Service  │
        └────┬─────┘  └─────┬─────┘  └────┬─────┘
             │              │             │
             └──────────────┼─────────────┘
                            ▼
                   ┌─────────────────┐
                   │ Medical Records │
                   │    Database     │
                   └────────┬────────┘
                            │
                            ▼
                   ┌─────────────────┐
                   │ Doctor Dashboard│
                   └─────────────────┘
```

---

# 🧩 AI Pipeline

```text
Medical Document
      │
      ▼
    OCR
      │
      ▼
Text Extraction
      │
      ▼
Medical Entity Extraction
      │
      ├── Diagnosis
      ├── Medication
      ├── Lab Results
      ├── Procedure
      ├── Allergy
      └── Dates
      │
      ▼
Structured Medical Record
      │
      ▼
Patient Timeline
      │
      ▼
AI Summary
```

---

# 🗃️ Entity Model

### Entities

| Entity | Table | Description |
|--------|-------|-------------|
| `Patient` | `patients` | Core patient with MCID, demographics, contact info |
| `EmergencyContact` | `emergency_contacts` | Emergency contacts linked to a patient |
| `Hospital` | `hospitals` | Hospital with registration number |
| `PatientEncounter` | `patient_encounters` | Patient visits/encounters at hospitals |
| `MedicalRecord` | `medical_records` | Clinical records (diagnosis, prescriptions, lab results) |
| `MedicalDocument` | `medical_documents` | Uploaded documents linked to records |
| `UnknownPatient` | `unknown_patients` | Unidentified patients awaiting matching |
| `PatientMatch` | `patient_matches` | AI-powered matching between unknown and known patients |
| `Memory` | `memories` | Clinical notes and important patient context |
| `AuditLog` | `audit_logs` | System audit trail for all actions |

### Enums

`Gender`, `EncounterType`, `RecordType`, `DocumentType`, `ProcessingStatus`, `MemoryType`, `UnknownPatientStatus`, `PatientMatchStatus`, `AuditAction`, `AuditStatus`

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
UnknownPatient ──M:1──> Patient (resolved, nullable)
```

---

# 🔌 REST API Endpoints

### PatientController — `/api/patients`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/patients` | Create patient |
| GET | `/api/patients` | Get all patients |
| GET | `/api/patients/{id}` | Get patient by ID |
| GET | `/api/patients/mcid/{mcid}` | Get patient by MCID |
| PUT | `/api/patients/{id}` | Update patient |
| DELETE | `/api/patients/{id}` | Delete patient |

### HospitalController — `/api/hospitals`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/hospitals` | Create hospital |
| GET | `/api/hospitals` | Get all hospitals |
| GET | `/api/hospitals/{id}` | Get hospital by ID |
| PUT | `/api/hospitals/{id}` | Update hospital |
| DELETE | `/api/hospitals/{id}` | Delete hospital |

### EmergencyContactController — `/api/emergency-contacts`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/emergency-contacts` | Create contact (patient in body) |
| GET | `/api/emergency-contacts` | Get all contacts |
| GET | `/api/emergency-contacts/{id}` | Get contact by ID |
| GET | `/api/emergency-contacts/patient/{patientId}` | Get contacts by patient |
| PUT | `/api/emergency-contacts/{id}` | Update contact |
| DELETE | `/api/emergency-contacts/{id}` | Delete contact |

### PatientEncounterController — `/api/encounters`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/encounters` | Create encounter (patient/hospital in body) |
| GET | `/api/encounters` | Get all encounters |
| GET | `/api/encounters/{id}` | Get encounter by ID |
| GET | `/api/encounters/patient/{patientId}` | Get encounters by patient |
| GET | `/api/encounters/hospital/{hospitalId}` | Get encounters by hospital |
| PUT | `/api/encounters/{id}` | Update encounter |
| DELETE | `/api/encounters/{id}` | Delete encounter |

### MedicalRecordController — `/api/medical-records`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/medical-records` | Create record (patient/hospital in body) |
| GET | `/api/medical-records` | Get all records |
| GET | `/api/medical-records/{id}` | Get record by ID |
| GET | `/api/medical-records/patient/{patientId}` | Get records by patient |
| GET | `/api/medical-records/hospital/{hospitalId}` | Get records by hospital |
| PUT | `/api/medical-records/{id}` | Update record |
| DELETE | `/api/medical-records/{id}` | Delete record |

---

# 🗃️ Example Data Model

A simplified patient record could look like:

```json
{
  "patientId": "P001",
  "name": "Example Patient",
  "records": [
    {
      "date": "2025-03-15",
      "hospital": "Hospital A",
      "type": "Discharge Summary",
      "diagnosis": ["Example Diagnosis"],
      "medications": ["Medicine A"],
      "procedures": ["Example Procedure"],
      "sourceDocument": "document_001.pdf"
    }
  ]
}
```

The actual implementation can evolve depending on the backend and healthcare interoperability requirements.

---

# 🔄 Record Processing

Medical Continuity is designed to handle both structured and unstructured information.

### Structured

```text
Hospital API
     ↓
FHIR / Healthcare Data
     ↓
Medical Continuity
```

### Unstructured

```text
PDF / Image / Scan
        ↓
       OCR
        ↓
   AI Extraction
        ↓
Structured Record
```

This is important because real-world healthcare data is often inconsistent and may still exist in paper or scanned formats.

---

# 🌐 Interoperability

The long-term vision is to integrate with healthcare interoperability standards and ecosystems rather than creating another isolated medical database.

Potential integrations include:

* FHIR-based healthcare records
* Hospital systems
* Diagnostic laboratories
* Digital health ecosystems
* Patient-provided documents

The exact interoperability layer depends on deployment requirements and available APIs.

---

# 🛠️ Technology Stack

> The stack is intentionally modular and can change as the project evolves.

### Frontend

* React / modern web frontend
* Responsive Doctor Dashboard
* Patient Timeline
* Medical Record Viewer

### Backend

* Java 21 / Spring Boot 3.2
* Spring Data JPA
* REST APIs
* Constructor-based Dependency Injection

### Database

* MySQL 8+

### AI Layer

* OCR
* LLM-based information extraction
* Medical document summarization
* Semantic retrieval / RAG where appropriate

### Infrastructure

* Docker
* Cloud deployment
* Secure object storage for documents

---

# 🚀 Current Prototype

The current project focuses on demonstrating the core concept:

```text
Medical Documents
       ↓
Upload
       ↓
OCR / AI Extraction
       ↓
Structured Patient Data
       ↓
Medical Timeline
       ↓
AI Summary
       ↓
Doctor Dashboard
```

The prototype is primarily designed to demonstrate **medical continuity and intelligent retrieval**, rather than serve as a production clinical system.

---

# 🧪 Example Use Case

### Scenario

A patient visits Hospital A for a condition.

Hospital A generates:

```text
Blood Report
X-Ray
Diagnosis
Prescription
Discharge Summary
```

Later, the patient visits Hospital B.

Instead of starting from zero:

```text
Hospital B
    ↓
Medical Continuity
    ↓
Patient History
    ↓
Relevant Previous Records
    ↓
AI Summary
    ↓
Doctor
```

The doctor can quickly understand the patient's historical context and open the original documents when necessary.

---

# 🏆 Why This Matters

Healthcare isn't only about treating today's problem.

Doctors often need to know:

> **"What happened to this patient before?"**

Medical Continuity attempts to solve that problem by transforming fragmented medical information into a **continuous, searchable and understandable patient history**.

---

# 🔮 Future Roadmap

### Phase 1 — Prototype

* [x] Patient records concept
* [x] Medical document ingestion
* [x] AI/OCR extraction concept
* [x] Structured patient timeline
* [x] AI-generated summaries

### Phase 2 — Intelligent Retrieval

* [ ] Semantic medical record search
* [ ] Context-aware record retrieval
* [ ] Source-aware AI responses
* [ ] Advanced medical timeline

### Phase 3 — Interoperability

* [ ] FHIR integration
* [ ] Hospital integrations
* [ ] Laboratory integrations
* [ ] Digital health ecosystem integration

### Phase 4 — Production

* [ ] Strong identity verification
* [ ] Consent management
* [ ] Advanced audit logging
* [ ] Encryption and key management
* [ ] Compliance/security assessment
* [ ] High-availability infrastructure

---

# ⚠️ Important Disclaimer

Medical Continuity is a **technology prototype/concept** intended to demonstrate healthcare information continuity.

It does **not** replace doctors, clinical judgment, diagnosis, or emergency medical services.

AI-generated information should be treated as an assistive summary and verified against the original medical records by qualified healthcare professionals.

---

# 👥 Team

**Medical Continuity**

Building technology for a future where a patient's medical history doesn't get lost between hospitals.

---

## ⭐ Vision

> **One patient. One continuous medical history. Anywhere they receive care.**
