package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.model.dto.PupilDto;
import com.schoolhealth.schoolmedical.model.dto.request.AssignClassRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationHistoryResponse;
import com.schoolhealth.schoolmedical.model.mapper.PupilMapper;
import com.schoolhealth.schoolmedical.repository.UserRepository;
import com.schoolhealth.schoolmedical.service.PupilService;
//import com.schoolhealth.schoolmedical.service.vaccinationHistory.VaccinationHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pupils")
@Tag(name = "Pupil Management", description = "API quản lý học sinh trong hệ thống trường học")
public class PupilController {

    @Autowired
    private PupilService pupilService;

    @Autowired
    private PupilMapper pupilMapper;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private VaccinationHistoryService vaccinationHistoryService;

    @Operation(
            summary = "Lấy danh sách tất cả học sinh",
            description = "Trả về danh sách tất cả học sinh có trạng thái active=true"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Thành công",
            content = @Content(schema = @Schema(implementation = PupilDto.class))
    )
    @GetMapping
    public List<PupilDto> getAllPupil() {
        return pupilService.getAllPupils();
    }

    @Operation(
            summary = "Lấy thông tin học sinh theo ID",
            description = "Trả về thông tin chi tiết của học sinh theo pupilId cung cấp"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tìm thấy học sinh",
                    content = @Content(schema = @Schema(implementation = PupilDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy học sinh với ID đã cung cấp",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PupilDto> getPupilById(
            @Parameter(description = "ID của học sinh cần tìm")
            @PathVariable String id) {
        PupilDto dto = pupilService.getPupilById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Tạo học sinh mới",
            description = "Tạo một học sinh mới với thông tin được cung cấp"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Học sinh được tạo thành công",
                    content = @Content(schema = @Schema(implementation = PupilDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dữ liệu không hợp lệ",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<PupilDto> createPupil(
            @Parameter(description = "Thông tin học sinh cần tạo")
            @RequestBody PupilDto dto) {
        PupilDto created = pupilService.createPupil(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @Operation(
            summary = "Cập nhật thông tin học sinh",
            description = "Cập nhật thông tin của học sinh dựa trên ID cung cấp"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cập nhật học sinh thành công",
                    content = @Content(schema = @Schema(implementation = PupilDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy học sinh với ID đã cung cấp",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dữ liệu không hợp lệ",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<PupilDto> updatePupil(
            @Parameter(description = "ID của học sinh cần cập nhật")
            @PathVariable String id,
            @Parameter(description = "Thông tin học sinh mới")
            @RequestBody PupilDto dto) {

        PupilDto updated = pupilService.updatePupil(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Xóa học sinh",
            description = "Đánh dấu học sinh là inactive (xóa mềm) bằng ID cung cấp"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Đánh dấu xóa học sinh thành công"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy học sinh với ID đã cung cấp",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePupil(
            @Parameter(description = "ID của học sinh cần xóa")
            @PathVariable String id) {
        pupilService.deletePupil(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Gán lớp học cho học sinh",
            description = "Gán hoặc thay đổi lớp học cho học sinh đã tồn tại"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Gán lớp học thành công",
                    content = @Content(schema = @Schema(implementation = Pupil.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy học sinh hoặc lớp học với ID đã cung cấp",
                    content = @Content
            )
    })
    @PostMapping("assign")
    public ResponseEntity<Pupil> assignClassToPupil(
            @Parameter(description = "Thông tin gán lớp học")
            @RequestBody AssignClassRequest assignRequest) {
        Pupil pupil = pupilService.assignPupilClass(assignRequest);
        return ResponseEntity.ok(pupil);
    }

    @Operation(
            summary = "Lấy danh sách con của phụ huynh đang đăng nhập",
            description = "API này dành cho phụ huynh để xem danh sách con của họ",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Thành công",
                    content = @Content(schema = @Schema(implementation = PupilDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Không có quyền truy cập",
                    content = @Content
            )
    })
    @GetMapping("/my-children")
    @PreAuthorize("hasAuthority('PARENT')")
    public ResponseEntity<List<PupilDto>> getMyChildren() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        User currentUser = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng"));

        if (currentUser.getRole() != Role.PARENT) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Set<String> processedIds = new HashSet<>();
        List<Pupil> allChildren = new ArrayList<>();

        // Thu thập từ mối quan hệ User-Pupil
        if (currentUser.getPupils() != null) {
            currentUser.getPupils().stream()
                .filter(child -> processedIds.add(child.getPupilId()))
                .forEach(allChildren::add);
        }

        // Thu thập từ parentPhoneNumber
        pupilService.findByParentPhoneNumber(phoneNumber).stream()
                .filter(child -> processedIds.add(child.getPupilId()))
                .forEach(allChildren::add);

        return ResponseEntity.ok(allChildren.stream()
                .map(pupilMapper::toDto)
                .collect(Collectors.toList()));
    }

//    @GetMapping("/{id}/vaccinations/{historyId}")
//    public ResponseEntity<VaccinationHistoryResponse> getById(@PathVariable int historyId) {
//        return ResponseEntity.ok(vaccinationHistoryService.getById(historyId));
//    }
}
