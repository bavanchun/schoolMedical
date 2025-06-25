package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.HealthConditionRequest;
import com.schoolhealth.schoolmedical.model.dto.response.HealthConditionResponse;
import com.schoolhealth.schoolmedical.service.HealthConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/health-conditions")
@RequiredArgsConstructor
public class HealthConditionController {

    private final HealthConditionService healthConditionService;

    /**
     * Tạo mới hồ sơ sức khỏe cho học sinh.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PARENT')")
    public ResponseEntity<HealthConditionResponse> create(@RequestBody HealthConditionRequest request) {
        HealthConditionResponse response = healthConditionService.create(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Lấy tất cả hồ sơ sức khỏe của 1 học sinh (chỉ trả về các hồ sơ đang còn hiệu lực).
     */
    @GetMapping("/pupil/{pupilId}")
    @PreAuthorize("hasAuthority('PARENT')")
    public ResponseEntity<List<HealthConditionResponse>> getByPupil(@PathVariable String pupilId) {
        List<HealthConditionResponse> responseList = healthConditionService.getAllByPupil(pupilId);
        return ResponseEntity.ok(responseList);
    }

    /**
     * Sửa thông tin hồ sơ sức khỏe.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PARENT')")
    public ResponseEntity<HealthConditionResponse> update(@PathVariable Long id, @RequestBody HealthConditionRequest request) {
        HealthConditionResponse response = healthConditionService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Xóa mềm (set isActive = false) hồ sơ sức khỏe.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PARENT')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        healthConditionService.delete(id);
        return ResponseEntity.ok().build();
    }
}