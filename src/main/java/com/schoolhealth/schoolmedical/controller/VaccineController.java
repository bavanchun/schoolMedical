package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.VaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;
import com.schoolhealth.schoolmedical.service.vaccine.VaccineService;
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
@RequestMapping("/api/v1/vaccines")
@RequiredArgsConstructor
@Tag(name = "Vaccine Management REST APIs",
     description = "CRUD REST APIs - Create Vaccine, Update Vaccine, Get Vaccine, Get All Vaccines, Delete Vaccine")
public class VaccineController {
    private final VaccineService vaccineService;

    @Operation(
        summary = "Create Vaccine REST API",
        description = "Save a new vaccine to the database"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Vaccine created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<VaccineResponse> create(
            @Parameter(description = "Vaccine information to save", required = true)
            @Valid @RequestBody VaccineRequest request) {
        VaccineResponse createdVaccine = vaccineService.createVaccine(request);
        return new ResponseEntity<>(createdVaccine, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Update Vaccine REST API",
        description = "Update an existing vaccine in the database by ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vaccine updated successfully"),
        @ApiResponse(responseCode = "404", description = "Vaccine not found with the given ID"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VaccineResponse> update(
            @Parameter(description = "Vaccine ID to update", required = true)
            @PathVariable int id,

            @Parameter(description = "Updated vaccine information", required = true)
            @Valid @RequestBody VaccineRequest request) {
        return ResponseEntity.ok(vaccineService.updateVaccine(id, request));
    }

    @Operation(
        summary = "Delete Vaccine REST API",
        description = "Delete a vaccine from the database by ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Vaccine deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Vaccine not found with the given ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Vaccine ID to delete", required = true)
            @PathVariable int id) {
        vaccineService.deleteVaccine(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get Vaccine By ID REST API",
        description = "Get a single vaccine from the database by ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vaccine found successfully"),
        @ApiResponse(responseCode = "404", description = "Vaccine not found with the given ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VaccineResponse> getById(
            @Parameter(description = "Vaccine ID to retrieve", required = true)
            @PathVariable int id) {
        return ResponseEntity.ok(vaccineService.getVaccineById(id));
    }

    @Operation(
        summary = "Get All Vaccines REST API",
        description = "Retrieve all vaccines from the database with pagination"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vaccines retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<VaccineResponse>> getAll(
            @Parameter(description = "Page number (starting from 1)", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "Page size (number of records per page)", example = "20")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Filter by active status", example = "true")
            @RequestParam(defaultValue = "true") boolean isActive) {
        Page<VaccineResponse> vaccines = vaccineService.getAllVaccines(page, size, isActive);
        return ResponseEntity.ok(vaccines);
    }
}
