package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.ParentHealthRecordRequest;
import com.schoolhealth.schoolmedical.model.dto.response.ParentHealthRecordResponse;
import com.schoolhealth.schoolmedical.service.parentrecord.ParentHealthRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parent-health-records")
@RequiredArgsConstructor
@Tag(name = "Parent Health Records", description = "CRUD APIs for parents to manage medical/health records for their children")
public class ParentHealthRecordController {

    private final ParentHealthRecordService parentHealthRecordService;

    @PostMapping
    @Operation(
            summary = "Create a new health record",
            description = "Parent creates a medical/health record for a child",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ParentHealthRecordRequest.class),
                            examples = @ExampleObject(value = "{\n  \"name\": \"Peanut Allergy\",\n  \"reactionOrNote\": \"Carries epipen\",\n  \"imageUrl\": \"http://example.com/photo.png\",\n  \"typeHistory\": \"ALLERGY\",\n  \"pupilId\": \"PUP123\",\n  \"isActive\": true\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Record created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ParentHealthRecordResponse.class),
                                    examples = @ExampleObject(value = "{\n  \"conditionId\": 1,\n  \"name\": \"Peanut Allergy\",\n  \"reactionOrNote\": \"Carries epipen\",\n  \"imageUrl\": \"http://example.com/photo.png\",\n  \"active\": true,\n  \"typeHistory\": \"ALLERGY\",\n  \"pupilId\": \"PUP123\"\n}")
                            )
                    )
            }
    )
    public ResponseEntity<ParentHealthRecordResponse> createParentHealthRecord(
            @Valid @RequestBody ParentHealthRecordRequest request) {
        ParentHealthRecordResponse response = parentHealthRecordService.createParentHealthRecord(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/pupil/{pupilId}")
    @Operation(
            summary = "List health records by pupil",
            description = "Retrieve all health records associated with a pupil",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List retrieved",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ParentHealthRecordResponse.class),
                                    examples = @ExampleObject(value = "[ {\n  \"conditionId\": 1,\n  \"name\": \"Peanut Allergy\",\n  \"reactionOrNote\": \"Carries epipen\",\n  \"imageUrl\": \"http://example.com/photo.png\",\n  \"active\": true,\n  \"typeHistory\": \"ALLERGY\",\n  \"pupilId\": \"PUP123\"\n} ]")
                            )
                    )
            }
    )
    public ResponseEntity<List<ParentHealthRecordResponse>> getParentHealthRecordsByPupil(
            @PathVariable String pupilId) {
        List<ParentHealthRecordResponse> responses = parentHealthRecordService.getAllParentHealthRecordsByPupil(pupilId);
        return ResponseEntity.ok(responses);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update a health record",
            description = "Update an existing health record by ID",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ParentHealthRecordRequest.class),
                            examples = @ExampleObject(value = "{\n  \"name\": \"Updated Allergy\",\n  \"reactionOrNote\": \"Updated note\",\n  \"imageUrl\": \"http://example.com/photo.png\",\n  \"typeHistory\": \"MEDICAL_HISTORY\",\n  \"pupilId\": \"PUP123\",\n  \"isActive\": true\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Record updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ParentHealthRecordResponse.class),
                                    examples = @ExampleObject(value = "{\n  \"conditionId\": 1,\n  \"name\": \"Updated Allergy\",\n  \"reactionOrNote\": \"Updated note\",\n  \"imageUrl\": \"http://example.com/photo.png\",\n  \"active\": true,\n  \"typeHistory\": \"MEDICAL_HISTORY\",\n  \"pupilId\": \"PUP123\"\n}")
                            ))
            }
    )
    public ResponseEntity<ParentHealthRecordResponse> updateParentHealthRecord(
            @PathVariable Long id,
            @Valid @RequestBody ParentHealthRecordRequest request) {
        ParentHealthRecordResponse response = parentHealthRecordService.updateParentHealthRecord(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a health record", description = "Mark a health record as inactive")
    public ResponseEntity<Void> deleteParentHealthRecord(@PathVariable Long id) {
        parentHealthRecordService.deleteParentHealthRecord(id);
        return ResponseEntity.noContent().build();
    }
}