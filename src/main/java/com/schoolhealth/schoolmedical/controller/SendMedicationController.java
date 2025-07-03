package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.entity.enums.StatusSendMedication;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.request.UpdateStatusSendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.SendMedicationRes;
import com.schoolhealth.schoolmedical.repository.SendMedicationRepo;
import com.schoolhealth.schoolmedical.service.sendMedication.SendMedicalService;
import com.schoolhealth.schoolmedical.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/send-medication")
public class SendMedicationController {
    @Autowired
    private SendMedicalService sendMedicalService;
    @Autowired
    private UserService userService;
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
}
