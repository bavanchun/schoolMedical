package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.SendMedicationRes;
import com.schoolhealth.schoolmedical.service.sendMedical.SendMedicalService;
import com.schoolhealth.schoolmedical.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/send-medication")
public class SendMedicationController {
    @Autowired
    private SendMedicalService sendMedicalService;
    @Autowired
    private UserService userService;
    @PostMapping("/create")
    public ResponseEntity<SendMedicationRes> createSendMedication(HttpServletRequest request, @RequestBody SendMedicationReq sendMedicationReq) {
        // Logic to create send medication will go here
        String userId = userService.getCurrentUserId(request);
        return ResponseEntity.ok(sendMedicalService.createSendMedication(sendMedicationReq,userId));
    }
}
