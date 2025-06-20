package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.DiseaseVaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseVaccineResponse;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/disease-vaccines")
@RequiredArgsConstructor
@Tag(name = "Disease-Vaccine Mapping REST APIs",
     description = "CRUD REST APIs - Create, Get, and Delete Disease-Vaccine Mappings")
public class DiseaseVaccineController {
    private final DiseaseVaccineService service;

    @Operation(
        summary = "Create Disease-Vaccine Mapping REST API",
        description = "Create a mapping between a disease and a vaccine in the database"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Mapping created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "404", description = "Disease or Vaccine not found"),
        @ApiResponse(responseCode = "409", description = "Mapping already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<DiseaseVaccineResponse> create(
            @Parameter(description = "Disease-Vaccine mapping information", required = true)
            @Valid @RequestBody DiseaseVaccineRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Delete Disease-Vaccine Mapping REST API",
        description = "Delete a mapping between a disease and a vaccine from the database"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Mapping deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Mapping not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{diseaseId}/{vaccineId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Disease ID", required = true)
            @PathVariable int diseaseId,

            @Parameter(description = "Vaccine ID", required = true)
            @PathVariable int vaccineId) {
        service.delete(diseaseId, vaccineId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get All Disease-Vaccine Mappings REST API",
        description = "Retrieve all active disease-vaccine mappings from the database"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mappings retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<DiseaseVaccineResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
