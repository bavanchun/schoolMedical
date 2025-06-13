package com.schoolhealth.schoolmedical.controller;


import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.request.PupilRequest;
import com.schoolhealth.schoolmedical.model.dto.response.PupilResponse;
import com.schoolhealth.schoolmedical.service.pupil.PupilService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pupils")
public class PupilController {

    private final PupilService pupilService;

    public PupilController(PupilService pupilService) {
        this.pupilService = pupilService;
    }

    @GetMapping("/{pupilId}")
    public ResponseEntity<PupilResponse> getPupilById(@PathVariable String pupilId) {
        Optional<Pupil> pupilOptional = pupilService.findByPupilId(pupilId);
        return pupilOptional.map(pupil -> ResponseEntity.ok(convertToResponse(pupil)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PupilResponse>> getAllPupils() {
        List<Pupil> pupils = pupilService.findAll();
        List<PupilResponse> responses = pupils.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<PupilResponse> createPupil(@RequestBody @Valid PupilRequest request) {
        Pupil pupil = pupilService.save(convertToEntity(request));
        return new ResponseEntity<>(convertToResponse(pupil), HttpStatus.CREATED);
    }

    @PutMapping("/{pupilId}")
    public ResponseEntity<PupilResponse> updatePupil(
            @PathVariable String pupilId,
            @RequestBody Pupil pupil) {
        // Đảm bảo ID trùng khớp
        if (!pupilId.equals(pupil.getPupilId())) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Pupil> existingPupil = pupilService.findByPupilId(pupilId);
        if (existingPupil.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pupil updatedPupil = pupilService.save(pupil);
        return ResponseEntity.ok(convertToResponse(updatedPupil));
    }

    @DeleteMapping("/{pupilId}")
    public ResponseEntity<Void> deletePupil(@PathVariable String pupilId) {
        Optional<Pupil> pupil = pupilService.findByPupilId(pupilId);
        if (pupil.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pupilService.delete(pupilId);
        return ResponseEntity.noContent().build();
    }

    private Pupil convertToEntity(PupilRequest request) {
        return Pupil.builder()
                .pupilId(request.getPupilId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDate(request.getBirthDate())
                .gender(request.getGender().charAt(0))
                .isActive(true)
                .build();
    }

    private PupilResponse convertToResponse(Pupil pupil) {
        return PupilResponse.builder()
                .pupilId(pupil.getPupilId())
                .firstName(pupil.getFirstName())
                .lastName(pupil.getLastName())
                .birthDate(pupil.getBirthDate())
                .gender(pupil.getGender())
                .isActive(pupil.isActive())
                .gradeName(pupil.getGrade() != null ? pupil.getGrade().getGradeName() : null)
                .build();
    }
}