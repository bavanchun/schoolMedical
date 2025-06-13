package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.PupilDto;
import com.schoolhealth.schoolmedical.service.PupilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PupilController {

    @Autowired
    private PupilService pupilService;

    @GetMapping("/pupils")
    public List<PupilDto> getAllPupil() {
        return pupilService.getAllPupils();
    }
}
