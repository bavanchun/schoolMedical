package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.DiseaseVaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseVaccineResponse;
import com.schoolhealth.schoolmedical.service.diseaseVaccine.DiseaseVaccineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/disease-vaccines")
@RequiredArgsConstructor
public class DiseaseVaccineController {
    private final DiseaseVaccineService service;

    @PostMapping
    public ResponseEntity<DiseaseVaccineResponse> create(@RequestBody DiseaseVaccineRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @DeleteMapping("/{diseaseId}/{vaccineId}")
    public ResponseEntity<Void> delete(@PathVariable int diseaseId, @PathVariable int vaccineId) {
        service.delete(diseaseId, vaccineId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<DiseaseVaccineResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
