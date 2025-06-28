package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.constant.ValidationConstants;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.model.dto.PupilDto;
import com.schoolhealth.schoolmedical.model.dto.request.AssignClassRequest;
import com.schoolhealth.schoolmedical.model.mapper.PupilMapper;
import com.schoolhealth.schoolmedical.repository.UserRepository;
import com.schoolhealth.schoolmedical.service.PupilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PupilDto> createPupil(
            @Parameter(description = "Thông tin học sinh cần tạo")
            @Valid @RequestBody PupilDto dto) {
        // Kiểm tra ngày sinh
        if (dto.getBirthDate() != null && dto.getBirthDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ValidationConstants.BIRTH_DATE_MESSAGE);
        }

        // Kiểm tra số điện thoại của phụ huynh
        if (dto.getParentPhoneNumber() != null && !Pattern.matches(ValidationConstants.PHONE_NUMBER_REGEX, dto.getParentPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ValidationConstants.PHONE_NUMBER_MESSAGE);
        }

        // Kiểm tra số điện thoại phụ huynh có tồn tại và là PARENT active
        if (dto.getParentPhoneNumber() != null) {
            boolean isValidParent = userRepository.findActiveParentByPhoneNumber(dto.getParentPhoneNumber()).isPresent();
            if (!isValidParent) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ValidationConstants.PARENT_PHONE_NOT_EXIST_MESSAGE);
            }
        }

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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PupilDto> updatePupil(
            @Parameter(description = "ID của học sinh cần cập nhật")
            @PathVariable String id,
            @Parameter(description = "Thông tin học sinh mới")
            @Valid @RequestBody PupilDto dto) {

        // Kiểm tra ngày sinh
        if (dto.getBirthDate() != null && dto.getBirthDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ValidationConstants.BIRTH_DATE_MESSAGE);
        }

        // Kiểm tra số điện thoại của phụ huynh
        if (dto.getParentPhoneNumber() != null && !Pattern.matches(ValidationConstants.PHONE_NUMBER_REGEX, dto.getParentPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ValidationConstants.PHONE_NUMBER_MESSAGE);
        }

        // Kiểm tra số điện thoại phụ huynh có tồn tại và là PARENT active
        if (dto.getParentPhoneNumber() != null) {
            boolean isValidParent = userRepository.findActiveParentByPhoneNumber(dto.getParentPhoneNumber()).isPresent();
            if (!isValidParent) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ValidationConstants.PARENT_PHONE_NOT_EXIST_MESSAGE);
            }
        }

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
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<PupilDto>> getMyChildren() {
        // Lấy thông tin người dùng hiện tại từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName(); // Lấy số điện thoại từ authentication

        System.out.println("DEBUG - Attempting to fetch children for parent with phone: " + phoneNumber);
        System.out.println("DEBUG - Authentication authorities: " + authentication.getAuthorities());

        // Tìm user từ repository với điều kiện là phụ huynh đang hoạt động
        User currentUser = userRepository.findActiveParentByPhoneNumber(phoneNumber)
                .orElseThrow(() -> {
                    System.out.println("ERROR - No active parent found with phone: " + phoneNumber);
                    // Kiểm tra xem có user nào không phải PARENT hoặc không active với số điện thoại này
                    userRepository.findByPhoneNumber(phoneNumber).ifPresent(user -> {
                        System.out.println("DEBUG - Found user with matching phone but wrong role/status: "
                            + "Role=" + user.getRole() + ", IsActive=" + user.isActive());
                    });
                    return new RuntimeException("Không tìm thấy thông tin phụ huynh đang hoạt động");
                });

        System.out.println("DEBUG - Found parent: " + currentUser.getFirstName() + " " + currentUser.getLastName()
            + " (ID=" + currentUser.getUserId() + ", Role=" + currentUser.getRole() + ")");

        // Phương pháp 1: Lấy danh sách con từ mối quan hệ User-Pupil
        List<Pupil> childrenFromRelationship = new ArrayList<>();
        if (currentUser.getPupils() != null) {
            childrenFromRelationship = currentUser.getPupils();
        }

        // Phương pháp 2: Tìm trực tiếp các học sinh có parentPhoneNumber trùng với số điện thoại phụ huynh
        List<Pupil> childrenByPhoneNumber = pupilService.findByParentPhoneNumber(phoneNumber);

        // Gộp hai danh sách và loại bỏ trùng lặp
        Set<String> processedIds = new HashSet<>();
        List<Pupil> allChildren = new ArrayList<>();

        // Thêm học sinh từ mối quan hệ
        for (Pupil child : childrenFromRelationship) {
            if (!processedIds.contains(child.getPupilId())) {
                allChildren.add(child);
                processedIds.add(child.getPupilId());
            }
        }

        // Thêm học sinh từ tìm kiếm theo số điện thoại
        for (Pupil child : childrenByPhoneNumber) {
            if (!processedIds.contains(child.getPupilId())) {
                allChildren.add(child);
                processedIds.add(child.getPupilId());
            }
        }

        // Đảm bảo không bỏ sót học sinh nào - logging kiểm tra
        System.out.println("DEBUG - Số lượng học sinh tìm thấy cho phụ huynh " + phoneNumber + ": " + allChildren.size());
        System.out.println("DEBUG - Chi tiết: Từ mối quan hệ: " + childrenFromRelationship.size() + ", Từ số điện thoại: " + childrenByPhoneNumber.size());

        // Chuyển đổi danh sách Pupil thành PupilDto
        List<PupilDto> childrenDtos = allChildren.stream()
                .map(pupilMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(childrenDtos);
    }
}
