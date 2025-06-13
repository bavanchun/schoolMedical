package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.PupilDto;
import com.schoolhealth.schoolmedical.service.PupilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PupilController {

    @Autowired
    private PupilService pupilService;
//    @GetMapping("/pupils")
//    public List<PupilDto> getAllPupil() {
//        return pupilRepo.findAll().stream().map(pupilMapper::toDto).toList();
//    }
    @GetMapping("/pupils")
    public List<PupilDto> getAllPupil() {
        return pupilService.getAllPupils();
    }
}
