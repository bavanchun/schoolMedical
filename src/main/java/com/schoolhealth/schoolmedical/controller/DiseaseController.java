package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.DiseaseRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseResponse;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;
import com.schoolhealth.schoolmedical.service.disease.DiseaseService;
import com.schoolhealth.schoolmedical.service.diseaseVaccine.DiseaseVaccineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Disease Management REST APIs",
     description = "CRUD REST APIs - Create Disease, Update Disease, Get Disease, Get All Diseases, Delete Disease, Get Vaccines by Disease")
public class DiseaseController {
    private final DiseaseService diseaseService;
    private final DiseaseVaccineService diseaseVaccineService;

    @Operation(
        summary = "Create Disease REST API",
        description = "Save a new disease to the database"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Disease created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<DiseaseResponse> create(
            @Parameter(description = "Disease information to save", required = true)
            @Valid @RequestBody DiseaseRequest request) {
        DiseaseResponse createdDisease = diseaseService.createDisease(request);
        return new ResponseEntity<>(createdDisease, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Update Disease REST API",
        description = "Update an existing disease in the database by ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Disease updated successfully"),
        @ApiResponse(responseCode = "404", description = "Disease not found with the given ID"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DiseaseResponse> update(
            @Parameter(description = "Disease ID to update", required = true)
            @PathVariable int id,

            @Parameter(description = "Updated disease information", required = true)
            @Valid @RequestBody DiseaseRequest request) {
        DiseaseResponse updatedDisease = diseaseService.updateDisease(id, request);
        return ResponseEntity.ok(updatedDisease);
    }

    @Operation(
        summary = "Delete Disease REST API",
        description = "Delete a disease from the database by ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Disease deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Disease not found with the given ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Disease ID to delete", required = true)
            @PathVariable int id) {
        diseaseService.deleteDisease(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get Disease By ID REST API",
        description = "Get a single disease from the database by ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Disease found successfully"),
        @ApiResponse(responseCode = "404", description = "Disease not found with the given ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DiseaseResponse> getById(
            @Parameter(description = "Disease ID to retrieve", required = true)
            @PathVariable int id) {
        DiseaseResponse disease = diseaseService.getDiseaseById(id);
        return ResponseEntity.ok(disease);
    }

    @Operation(
        summary = "Get All Diseases REST API",
        description = "Retrieve all diseases from the database"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Diseases retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<DiseaseResponse>> getAll() {
        List<DiseaseResponse> diseases = diseaseService.getAllDiseases();
        return ResponseEntity.ok(diseases);
    }

    @Operation(
        summary = "Get Vaccines by Disease ID REST API",
        description = "Retrieve all vaccines associated with a specific disease"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vaccines retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Disease not found with the given ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{diseaseId}/vaccines")
    public ResponseEntity<List<VaccineResponse>> getVaccinesByDiseaseId(
            @Parameter(description = "Disease ID to retrieve vaccines for", required = true)
            @PathVariable int diseaseId) {
        List<VaccineResponse> vaccines = diseaseVaccineService.getVaccinesByDiseaseId(diseaseId);
        return ResponseEntity.ok(vaccines);
    }
}
