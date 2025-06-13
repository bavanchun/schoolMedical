package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.PupilDto;
import com.schoolhealth.schoolmedical.service.PupilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pupils")
public class PupilController {

    private final PupilService pupilService;

    @Autowired
    public PupilController(PupilService pupilService) {
        this.pupilService = pupilService;
    }

    @GetMapping
    public List<PupilDto> getAllPupil() {
        return pupilService.getAllPupils();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PupilDto> getPupilById(@PathVariable String id) {
        PupilDto dto = pupilService.getPupilById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PupilDto> createPupil(@RequestBody PupilDto dto) {
        PupilDto created = pupilService.createPupil(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PupilDto> updatePupil(
            @PathVariable String id,
            @RequestBody PupilDto dto) {

        PupilDto updated = pupilService.updatePupil(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePupil(@PathVariable String id) {
        pupilService.deletePupil(id);
        return ResponseEntity.noContent().build();
    }
}
