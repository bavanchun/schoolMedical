package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.VaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;
import com.schoolhealth.schoolmedical.service.vaccine.VaccineService;
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
@RequestMapping("/api/v1/vaccines")
@RequiredArgsConstructor
public class VaccineController {
    private final VaccineService vaccineService;

    @PostMapping
    public ResponseEntity<VaccineResponse> create(@RequestBody VaccineRequest request) {
        return ResponseEntity.ok(vaccineService.createVaccine(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VaccineResponse> update(@PathVariable int id, @RequestBody VaccineRequest request) {
        return ResponseEntity.ok(vaccineService.updateVaccine(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        vaccineService.deleteVaccine(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VaccineResponse> getById(@PathVariable int id) {
        return ResponseEntity.ok(vaccineService.getVaccineById(id));
    }

    @GetMapping
    public ResponseEntity<List<VaccineResponse>> getAll() {
        return ResponseEntity.ok(vaccineService.getAllVaccines());
    }
}
