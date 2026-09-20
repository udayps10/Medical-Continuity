"""One-time generator for the revised ERD's plain JPA entities."""
from pathlib import Path
import re

root = Path(__file__).parent / 'src/main/java/com/medicalcontinuity/medicalcontinuity'
entities = root / 'entity'
enums = root / 'enums'

enum_values = {
    'IdentityStatus': 'PROVISIONAL VERIFIED MERGED',
    'MembershipStatus': 'ACTIVE REVOKED',
    'StaffRole': 'DOCTOR NURSE HOSPITAL_ADMIN',
    'GrantStatus': 'ACTIVE REVOKED EXPIRED',
    'GrantMethod': 'OTP QR EMERGENCY',
    'AccessScope': 'READ_RECORDS READ_DOCUMENTS READ_ALL',
    'ConfidenceLevel': 'HIGH REVIEW UNRESOLVED',
    'MatchRunStatus': 'PENDING RUNNING COMPLETED FAILED',
    'SummaryStatus': 'CURRENT STALE FAILED',
}
for name, values in enum_values.items():
    (enums / f'{name}.java').write_text(f'package com.medicalcontinuity.medicalcontinuity.enums;\n\npublic enum {name} {{\n    '+', '.join(values.split())+'\n}\n')

def snake(s):
    return re.sub(r'(?<!^)(?=[A-Z])', '_', s).lower()

def col(name, typ='String', nullable=True, extra='', default=None):
    return (name, typ, f'@Column(name = "{snake(name)}", nullable = {str(nullable).lower()}' + (', '+extra if extra else '') + ')', default)

def text(name, nullable=True):
    return col(name, nullable=nullable, extra='columnDefinition = "TEXT"')

def enum(name, typ, default=None):
    n,t,a,d = col(name,typ,False,'length = 32', default and typ+'.'+default)
    return n,t,'@Enumerated(EnumType.STRING)\n    '+a,d

def fk(name, typ, nullable=False, unique=False):
    rel = '@OneToOne' if unique else '@ManyToOne'
    return (name,typ,rel+f'(fetch = FetchType.LAZY, optional = {str(nullable).lower()})\n    @JoinColumn(name = "{snake(name)}_id", nullable = {str(nullable).lower()}, unique = {str(unique).lower()})',None)

def timestamp(name):
    return col(name,'Instant',False, 'updatable = false')

def make(name,table,fields,unique=(),extra='',created=None):
    constraints = ''
    if unique:
        constraints = ', uniqueConstraints = @UniqueConstraint(columnNames = {'+', '.join('"'+x+'"' for x in unique)+'})'
    source = f'''package com.medicalcontinuity.medicalcontinuity.entity;

import com.medicalcontinuity.medicalcontinuity.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "{table}"{constraints})
@JsonIgnoreProperties({{"hibernateLazyInitializer", "handler"}})
public class {name} {{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

'''
    for n,t,a,d in fields:
        source += '    '+a+'\n    private '+t+' '+n+(' = '+d if d else '')+';\n\n'
    source += f'    public {name}() {{}}\n\n'
    for n,t,a,d in [('id','Long','',None)]+fields:
        cap = n[0].upper()+n[1:]
        if n=='passwordHash': source += '    @JsonIgnore\n'
        source += f'    public {t} get{cap}() {{ return {n}; }}\n'
        source += f'    public void set{cap}({t} {n}) {{ this.{n} = {n}; }}\n\n'
    if created:
        source += f'    @PrePersist\n    protected void onCreate() {{\n        if ({created} == null) {created} = Instant.now();\n    }}\n\n'
    source += extra+'}\n'
    (entities / f'{name}.java').write_text(source)

make('User','users', [col('email',nullable=False,extra='unique = true, length = 254'),col('passwordHash',nullable=False),col('fullName',nullable=False),enum('role','UserRole','PATIENT'),col('enabled','Boolean',False,default='true'),timestamp('createdAt')],created='createdAt',extra='''    public enum UserRole { PATIENT, DOCTOR, NURSE, HOSPITAL, ADMIN }

    public User(String email, String passwordHash, String fullName, UserRole role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
    }

    @JsonIgnore
    public String getPassword() { return passwordHash; }
    public void setPassword(String password) { this.passwordHash = password; }
''')
make('Hospital','hospitals',[col('registrationNumber',nullable=False,extra='unique = true'),col('name',nullable=False),col('address'),col('city'),col('state'),col('phone')])
make('HospitalMembership','hospital_memberships',[fk('user','User'),fk('hospital','Hospital'),enum('staffRole','StaffRole'),enum('status','MembershipStatus','ACTIVE')],unique=('user_id','hospital_id'))
make('EmergencyContact','emergency_contacts',[fk('patient','Patient'),col('name',nullable=False),col('relationship'),col('phone',nullable=False),col('primaryContact','Boolean',False,default='false'),col('address')])
make('Encounter','encounters',[fk('patient','Patient'),fk('hospital','Hospital'),enum('encounterType','EncounterType'),timestamp('arrivalTime'),col('dischargeTime','Instant'),col('doctorName'),text('notes')],created='arrivalTime')
make('Observation','observations',[fk('encounter','Encounter'),col('observationType',nullable=False),col('value','BigDecimal',False,'precision = 19, scale = 6'),col('unit',nullable=False),timestamp('observedAt')],created='observedAt')
make('MedicalRecord','medical_records',[fk('patient','Patient'),fk('hospital','Hospital'),fk('encounter','Encounter',True),fk('createdByUser','User'),enum('recordType','RecordType'),col('recordDate','LocalDate',False),text('summary'),text('notes'),col('doctorName')])
make('MedicalDocument','medical_documents',[fk('patient','Patient'),fk('hospital','Hospital'),fk('medicalRecord','MedicalRecord',True),fk('uploadedByUser','User'),enum('documentType','DocumentType'),col('fileName',nullable=False,extra='length = 512'),col('storageKey',nullable=False,extra='length = 1024'),col('mimeType'),col('fileSize','Long'),col('contentHash',extra='length = 64'),enum('processingStatus','ProcessingStatus','PENDING'),text('processingError'),timestamp('uploadedAt'),col('processedAt','Instant')],created='uploadedAt',extra='''    public String getStoredFilePath() { return storageKey; }
    public void setStoredFilePath(String path) { storageKey = path; }
    public String getUploadedBy() { return uploadedByUser == null ? null : uploadedByUser.getEmail(); }
''')
make('DocumentChunk','document_chunks',[fk('document','MedicalDocument'),col('chunkIndex','Integer',False),text('text',False),col('pageNumber','Integer'),col('embeddingRef',nullable=False),col('embeddingModelVersion',nullable=False)],unique=('document_id','chunk_index'))
make('AiSummary','ai_summaries',[fk('patient','Patient'),fk('generatedByUser','User'),text('content',False),col('modelVersion',nullable=False),timestamp('generatedAt'),enum('status','SummaryStatus','CURRENT')],created='generatedAt')
make('UnknownPatientCase','unknown_patient_cases',[fk('provisionalPatient','Patient'),col('temporaryId',nullable=False,extra='unique = true'),col('approximateAge','Integer'),col('reportedName'),col('gender','Gender',extra='length = 20'),col('phone'),col('village'),col('district'),col('foundLocation'),text('description'),col('photoRef',extra='length = 1024'),fk('capturedByHospital','Hospital'),timestamp('capturedAt'),fk('resolvedPatient','Patient',True),fk('resolvedByUser','User',True),col('resolvedAt','Instant'),enum('status','UnknownPatientStatus','UNIDENTIFIED')],created='capturedAt',extra='''    public String getLocation() { return foundLocation; }
    public void setLocation(String location) { foundLocation = location; }
''')
p=entities/'UnknownPatientCase.java'
p.write_text(p.read_text().replace('    @Column(name = "gender"','    @Enumerated(EnumType.STRING)\n    @Column(name = "gender"'))
make('MatchRun','match_runs',[fk('unknownCase','UnknownPatientCase'),fk('requestedByUser','User'),col('algorithmVersion',nullable=False),timestamp('createdAt'),enum('status','MatchRunStatus','PENDING')],created='createdAt')
def score(name):
    n,t,a,d = col(name,'BigDecimal',False,'precision = 7, scale = 6')
    return n,t,'@DecimalMin("0.0")\n    @DecimalMax("1.0")\n    '+a,d
make('PatientMatch','patient_matches',[fk('matchRun','MatchRun'),fk('candidatePatient','Patient'),score('similarityScore'),enum('confidenceLevel','ConfidenceLevel','UNRESOLVED'),text('evidence'),score('evidenceCoverage'),enum('reviewStatus','PatientMatchStatus','PENDING'),fk('reviewedByUser','User',True),col('reviewedAt','Instant')],unique=('match_run_id','candidate_patient_id'),extra='''    public PatientMatchStatus getStatus() { return reviewStatus; }
    public void setStatus(PatientMatchStatus status) { reviewStatus = status; }
    public String getMatchReason() { return evidence; }
    public void setMatchReason(String reason) { evidence = reason; }
''')
make('AccessGrant','access_grants',[fk('patient','Patient'),fk('hospital','Hospital'),fk('initiatedByUser','User'),fk('grantedByUser','User',True),enum('grantedVia','GrantMethod'),enum('scope','AccessScope'),col('consentEvidenceRef',extra='length = 1024'),col('reasonCode'),timestamp('grantedAt'),col('expiresAt','Instant',False),fk('revokedByUser','User',True),col('revokedAt','Instant'),enum('status','GrantStatus','ACTIVE')],created='grantedAt')
make('AuditLog','audit_logs',[fk('actorUser','User'),fk('patient','Patient',True),fk('hospital','Hospital',True),fk('accessGrant','AccessGrant',True),enum('action','AuditAction'),col('resourceType',nullable=False),col('resourceId','Long'),enum('outcome','AuditStatus'),col('reasonCode'),timestamp('timestamp')],created='timestamp')

# Preserve the user's existing Patient fields while updating relationship ownership.
p=entities/'Patient.java'
s=p.read_text().replace('import com.medicalcontinuity.medicalcontinuity.enums.Gender;', 'import com.medicalcontinuity.medicalcontinuity.enums.Gender;\nimport com.medicalcontinuity.medicalcontinuity.enums.IdentityStatus;').replace('LocalDateTime','Instant')
s=s.replace('@Column(nullable = false, length = 255)', '@Column(length = 255)').replace('@Column(name = "date_of_birth", nullable = false)', '@Column(name = "date_of_birth")').replace('@Column(nullable = false, length = 20)', '@Column(length = 20)')
s=s.replace(', cascade = CascadeType.ALL, orphanRemoval = true', '')
s=s.replace('    @OneToMany', '    @JsonIgnore\n    @OneToMany').replace('    @JsonIgnore\n    @JsonIgnore','    @JsonIgnore')
s=s.replace('// Reference photo — used as the matching candidate against UnknownPatient.photoRef\n    // for face-similarity scoring, and for known-patient re-identify (Eka Care-style\n    // 1:1 face confirm) at intake.', '// Reference image for future matching; no biometric matching is implemented yet.')
extra_fields=[fk('user','User',True,True),enum('identityStatus','IdentityStatus','PROVISIONAL'),fk('mergedIntoPatient','Patient',True)]
addition=''
for n,t,a,d in extra_fields:
    addition+='    '+a+'\n    private '+t+' '+n+(' = '+d if d else '')+';\n\n'
    cap=n[0].upper()+n[1:]
    addition+=f'    public {t} get{cap}() {{ return {n}; }}\n    public void set{cap}({t} {n}) {{ this.{n} = {n}; }}\n\n'
s=s.replace('    @PrePersist',addition+'    @PrePersist')
p.write_text(s)

# Update Java type references without renaming the established HTTP controllers.
for p in root.rglob('*.java'):
    if p.parent == entities: continue
    s=p.read_text()
    s=re.sub(r'\bPatientEncounter\b','Encounter',s)
    s=re.sub(r'\bUnknownPatient\b','UnknownPatientCase',s)
    s=s.replace('findByUnknownPatientId','findByMatchRunUnknownCaseId')
    s=s.replace('findByUserId(String userId)','findByActorUserId(Long actorUserId)')
    p.write_text(s)

# Memory was an unused legacy entity; summaries now have explicit source links.
p=root/'repositories/MemoryRepository.java'
p.rename(root/'repositories/AiSummaryRepository.java')
p=root/'repositories/AiSummaryRepository.java'
p.write_text(p.read_text().replace('Memory','AiSummary'))
for name in ['HospitalMembership','Observation','DocumentChunk','MatchRun','AccessGrant']:
    (root/'repositories'/f'{name}Repository.java').write_text(f'''package com.medicalcontinuity.medicalcontinuity.repositories;

import com.medicalcontinuity.medicalcontinuity.entity.{name};
import org.springframework.data.jpa.repository.JpaRepository;

public interface {name}Repository extends JpaRepository<{name}, Long> {{}}
''')
