package com.schoolhealth.schoolmedical.controller;


import com.schoolhealth.schoolmedical.model.dto.request.MedicationLogReq;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.request.UpdateStatusSendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.PupilRes;
import com.schoolhealth.schoolmedical.model.dto.response.SendMedicationRes;
import com.schoolhealth.schoolmedical.model.dto.response.SendMedicationSimpleRes;
import com.schoolhealth.schoolmedical.service.pupil.PupilService;
import com.schoolhealth.schoolmedical.service.sendMedication.MedicationLogsService;
import com.schoolhealth.schoolmedical.service.sendMedication.SendMedicalService;
import com.schoolhealth.schoolmedical.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/send-medication")
public class SendMedicationController {
    @Autowired
    private SendMedicalService sendMedicalService;
    @Autowired
    private UserService userService;
    @Autowired
    private MedicationLogsService  medicationLogsService;
    @Autowired
    PupilService pupilService;
    @PostMapping()
    public ResponseEntity<SendMedicationRes> createSendMedication(@RequestBody SendMedicationReq sendMedicationReq, HttpServletRequest request) {
        // Logic to create send medication will go here
        String parentId = userService.getCurrentUserId(request);
        return ResponseEntity.ok(sendMedicalService.createSendMedication(sendMedicationReq, parentId));
    }
    @GetMapping("/{pupilId}")
    public ResponseEntity<?> getSendMedicationByPupilId(@PathVariable String pupilId) {
        List<SendMedicationRes> sendMedicationRes = sendMedicalService.getAllSendMedication(pupilId);
        return ResponseEntity.ok(sendMedicationRes);
    }
    @PatchMapping("/{sendMedicationId}")
    @PreAuthorize("hasRole('SCHOOL_NURSE')")
    public ResponseEntity<?> updateStatusSendMedication(@PathVariable Long sendMedicationId, @RequestBody UpdateStatusSendMedicationReq statusSendMedication) {
        // Logic to update send medication will go here
        sendMedicalService.updateStatus(sendMedicationId,statusSendMedication.getStatusSendMedication());
        return ResponseEntity.ok("Send medication status updated successfully");
    }
    @DeleteMapping("/delete/{sendMedicationId}")
    public  ResponseEntity<?> deleteSendMedication(@PathVariable Long sendMedicationId) {
        sendMedicalService.deleteSendMedication(sendMedicationId);
        return ResponseEntity.ok("Send medication status deleted successfully");
    }
    @GetMapping("/pupil")
    public ResponseEntity<?> getAllSendMedicationByPupilId() {
        return ResponseEntity.ok(sendMedicalService.getQuantityPupilForSession());
    }
    @GetMapping("/pending")
    public ResponseEntity<?> getAllPendingSendMedication() {
        return ResponseEntity.ok(sendMedicalService.getSendMedicationByPending());
    }

    @GetMapping("pupil/session")
    public ResponseEntity<?> getAllPupilBySessionAndGrade(@RequestParam int session, @RequestParam Long grade) {
       // return ResponseEntity.ok(sendMedicalService.getAllPupilBySessionAndGrade(session, grade));
        return ResponseEntity.ok(sendMedicalService.getAllPupilBySessionAndGrade(session, grade));
    }
    @GetMapping("/detailPupil/{pupilId}/session/{session}")
    public ResponseEntity<?> getSendMedicationByPupil(@PathVariable String pupilId, @PathVariable int session) {
        List<SendMedicationRes> sendMedicationRes = sendMedicalService.getSendMedicationByPupil(pupilId, session);
        return ResponseEntity.ok(sendMedicationRes);
    }
    @PostMapping("/medicationLog")
    public ResponseEntity<?> saveMedicationLogForPrescription(@RequestBody @Valid MedicationLogReq medicationLogReq){
        return ResponseEntity.ok(medicationLogsService.saveMedicationLogForPrescription(medicationLogReq));
    }
    @GetMapping("/allSendMedication")
    public ResponseEntity<?> getAllSendMedicationWithGivenTime() {
        return ResponseEntity.ok(sendMedicalService.getAllSendMedication());
    }
    @GetMapping("/medicationLogs/{sendMedicationId}")
    public ResponseEntity<?> getMedicationLogsBySendMedicationId(@PathVariable Long sendMedicationId, @RequestParam(required = false) Optional<LocalDate> givenTime) {
        return ResponseEntity.ok(medicationLogsService.getMedicationLogsBySendMedicationId(sendMedicationId, givenTime));
    }
    @GetMapping("/allByComplete")
    public ResponseEntity<?> getAllByComplete() {
        List<SendMedicationRes> sendMedicationRes = sendMedicalService.getAllByComplete();
        return ResponseEntity.ok(sendMedicationRes);
    }
    @GetMapping("/allByInProgress")
    public ResponseEntity<?> getAllByInProgress() {
        List<SendMedicationRes> sendMedicationRes = sendMedicalService.getAllByInProgress();
        return ResponseEntity.ok(sendMedicationRes);
    }
    @GetMapping("/prescription-prepare")
    public ResponseEntity<?> getAllPrescriptionPrepare(@RequestParam Long grade, @RequestParam int session) {
        List<SendMedicationSimpleRes> prescriptionOfPupil = sendMedicalService.getSendMedicationByGradeAndSession(grade, session, LocalDate.now());
        return ResponseEntity.ok(prescriptionOfPupil);
    }
    @GetMapping("/approved")
    @PreAuthorize("hasAnyRole('SCHOOL_NURSE', 'MANAGER','ADMIN')")
    public ResponseEntity<?> getAllApprovedSendMedication() {
        List<SendMedicationRes> sendMedicationRes = sendMedicalService.getSendMedicationByApproved();
        return ResponseEntity.ok(sendMedicationRes);
    }
}
