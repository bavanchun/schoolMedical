package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.DiseaseRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseResponse;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;
import com.schoolhealth.schoolmedical.service.disease.DiseaseService;
import com.schoolhealth.schoolmedical.service.diseaseVaccine.DiseaseVaccineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diseases")
@RequiredArgsConstructor
public class DiseaseController {
    private final DiseaseService diseaseService;
    private final DiseaseVaccineService diseaseVaccineService;

    @PostMapping
    public ResponseEntity<DiseaseResponse> create(@RequestBody DiseaseRequest request) {
        return ResponseEntity.ok(diseaseService.createDisease(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiseaseResponse> update(@PathVariable int id, @RequestBody DiseaseRequest request) {
        return ResponseEntity.ok(diseaseService.updateDisease(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        diseaseService.deleteDisease(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiseaseResponse> getById(@PathVariable int id) {
        return ResponseEntity.ok(diseaseService.getDiseaseById(id));
    }

    @GetMapping
    public ResponseEntity<List<DiseaseResponse>> getAll() {
        return ResponseEntity.ok(diseaseService.getAllDiseases());
    }

    @GetMapping("/{diseaseId}/vaccines")
    public ResponseEntity<List<VaccineResponse>> getVaccinesByDiseaseId(@PathVariable int diseaseId) {
        return ResponseEntity.ok(diseaseVaccineService.getVaccinesByDiseaseId(diseaseId));
    }
}
