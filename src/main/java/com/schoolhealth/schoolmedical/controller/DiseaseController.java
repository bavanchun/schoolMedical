package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.DiseaseRequest;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseVaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseResponse;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseVaccineResponse;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseWithVaccinesWrapper;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;
import com.schoolhealth.schoolmedical.service.DiseaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diseases")
@Tag(name = "Disease Management REST APIs",
        description = "CRUD REST APIs - Create Disease, Update Disease, Get Disease, Get All Diseases, Delete Disease, Get Vaccines by Disease")
public class DiseaseController {
    private final DiseaseService diseaseService;

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
            @PathVariable Long id,

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
            @PathVariable Long id) {
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
            @PathVariable Long id) {
        DiseaseResponse disease = diseaseService.getDiseaseById(id);
        return ResponseEntity.ok(disease);
    }

    @Operation(
            summary = "Get All Diseases REST API",
            description = "Retrieve all diseases from the database with pagination"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Diseases retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<DiseaseResponse>> getAll(
            @Parameter(description = "Page number (starting from 1)", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "Page size (number of records per page)", example = "20")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Filter by active status", example = "true")
            @RequestParam(defaultValue = "true") boolean isActive
    ) {
        Page<DiseaseResponse> diseases = diseaseService.getAllDiseases(page, size, isActive);
        return ResponseEntity.ok(diseases);
    }

    @PostMapping("/assign-vaccine")
    @Operation(
            summary = "Assign vaccine to disease",
            description = "This API assigns a specific vaccine to a disease based on DiseaseVaccineRequest"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaccine assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Disease or vaccine not found"),
            @ApiResponse(responseCode = "409", description = "Vaccine is already assigned to this disease")
    })
    public ResponseEntity<DiseaseVaccineResponse> assignVaccineToDisease(@RequestBody @Valid DiseaseVaccineRequest request) {
        DiseaseVaccineResponse response = diseaseService.assignVaccineToDisease(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            // Return appropriate status code based on the error message
            if (response.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (response.getMessage().contains("already assigned")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
    }

    @GetMapping("/{diseaseId}/vaccines")
    @Operation(
            summary = "Get list of vaccines for a disease",
            description = "This API returns a list of all vaccines assigned to a specific disease"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Disease not found with the provided ID")
    })
    public ResponseEntity<List<VaccineResponse>> getVaccinesByDiseaseId(
            @Parameter(description = "ID of the disease", required = true)
            @PathVariable Long diseaseId) {
        List<VaccineResponse> vaccines = diseaseService.getVaccinesByDiseaseId(diseaseId);
        return ResponseEntity.ok(vaccines);
    }

    @DeleteMapping("/remove-vaccine")
    @Operation(
            summary = "Remove vaccine from disease",
            description = "This API removes a specific vaccine from a disease based on DiseaseVaccineRequest"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaccine removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Disease or vaccine not found"),
            @ApiResponse(responseCode = "409", description = "Vaccine does not exist in this disease's list")
    })
    public ResponseEntity<DiseaseVaccineResponse> removeVaccineFromDisease(@RequestBody @Valid DiseaseVaccineRequest request) {
        DiseaseVaccineResponse response = diseaseService.removeVaccineFromDisease(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            // Return appropriate status code based on the error message
            if (response.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (response.getMessage().contains("not assigned") ||
                      response.getMessage().contains("doesn't have any vaccines")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
    }

    @GetMapping("/vaccines")
    @Operation(
            summary = "Get All Diseases with Associated Vaccines REST API",
            description = "Retrieve all diseases with their associated vaccines in the format expected by frontend"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Diseases with vaccines retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DiseaseWithVaccinesWrapper> getAllDiseasesWithVaccines() {
        DiseaseWithVaccinesWrapper response = diseaseService.getAllDiseasesWithVaccines();
        return ResponseEntity.ok(response);
    }
}
