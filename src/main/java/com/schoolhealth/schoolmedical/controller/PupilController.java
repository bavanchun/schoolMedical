package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.PupilRes;
import com.schoolhealth.schoolmedical.model.dto.request.AssignClassRequest;
import com.schoolhealth.schoolmedical.model.dto.response.SendMedicationRes;
import com.schoolhealth.schoolmedical.model.mapper.PupilMapper;
import com.schoolhealth.schoolmedical.repository.UserRepository;
import com.schoolhealth.schoolmedical.service.pupil.PupilService;
//import com.schoolhealth.schoolmedical.service.vaccinationHistory.VaccinationHistoryService;
import com.schoolhealth.schoolmedical.service.sendMedication.SendMedicalService;
import com.schoolhealth.schoolmedical.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            content = @Content(schema = @Schema(implementation = PupilRes.class))
    )
    @GetMapping
    public List<PupilRes> getAllPupil() {
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
                    content = @Content(schema = @Schema(implementation = PupilRes.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy học sinh với ID đã cung cấp",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PupilRes> getPupilById(
            @Parameter(description = "ID của học sinh cần tìm")
            @PathVariable String id) {
        PupilRes dto = pupilService.getPupilById(id);
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
                    content = @Content(schema = @Schema(implementation = PupilRes.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dữ liệu không hợp lệ",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<PupilRes> createPupil(
            @Parameter(description = "Thông tin học sinh cần tạo")
            @RequestBody PupilRes dto) {
        PupilRes created = pupilService.createPupil(dto);
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
                    content = @Content(schema = @Schema(implementation = PupilRes.class))
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
    public ResponseEntity<PupilRes> updatePupil(
            @Parameter(description = "ID của học sinh cần cập nhật")
            @PathVariable String id,
            @Parameter(description = "Thông tin học sinh mới")
            @RequestBody PupilRes dto) {

        PupilRes updated = pupilService.updatePupil(id, dto);
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

//    @Operation(
//            summary = "Lấy danh sách con của phụ huynh đang đăng nhập",
//            description = "API này dành cho phụ huynh để xem danh sách con của họ",
//            security = @SecurityRequirement(name = "bearerAuth")
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Thành công",
//                    content = @Content(schema = @Schema(implementation = PupilRes.class))
//            ),
//            @ApiResponse(
//                    responseCode = "403",
//                    description = "Không có quyền truy cập",
//                    content = @Content
//            )
//    })
//    @GetMapping("/my-children")
//    @PreAuthorize("hasAuthority('PARENT')")
//<<<<<<<HEAD
//
//    public ResponseEntity<List<PupilRes>> getMyChildren() {
//        // Lấy thông tin người dùng hiện tại từ SecurityContext
//=======
//        public ResponseEntity<List<PupilDto>> getMyChildren () {
//>>>>>>>51379 ca89e9eaf167f624b00b56622c98906f515
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String phoneNumber = authentication.getName();
//
//            User currentUser = userRepository.findByPhoneNumber(phoneNumber)
//                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng"));
//
//            if (currentUser.getRole() != Role.PARENT) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//            }
//
//            Set<String> processedIds = new HashSet<>();
//            List<Pupil> allChildren = new ArrayList<>();
//
//            // Thu thập từ mối quan hệ User-Pupil
//            if (currentUser.getPupils() != null) {
//                currentUser.getPupils().stream()
//                        .filter(child -> processedIds.add(child.getPupilId()))
//                        .forEach(allChildren::add);
//            }
//
//            // Thu thập từ parentPhoneNumber
//            pupilService.findByParentPhoneNumber(phoneNumber).stream()
//                    .filter(child -> processedIds.add(child.getPupilId()))
//                    .forEach(allChildren::add);
//
//
//            // Đảm bảo không bỏ sót học sinh nào - logging kiểm tra
//            System.out.println("Số lượng học sinh tìm thấy cho phụ huynh " + phoneNumber + ": " + allChildren.size());
//            System.out.println("Chi tiết: Từ mối quan hệ: " + childrenFromRelationship.size() + ", Từ số điện thoại: " + childrenByPhoneNumber.size());
//
//            // Chuyển đổi danh sách Pupil thành PupilDto
//            List<PupilRes> childrenDtos = allChildren.stream()
//            return ResponseEntity.ok(allChildren.stream()
//
//                    .map(pupilMapper::toDto)
//                    .collect(Collectors.toList()));
//        }
//
////    @GetMapping("/{id}/vaccinations/{historyId}")
////    public ResponseEntity<VaccinationHistoryResponse> getById(@PathVariable int historyId) {
////        return ResponseEntity.ok(vaccinationHistoryService.getById(historyId));
////    }
//    }
    @GetMapping("/listPupils")
    public ResponseEntity<?> getLatestPupil(HttpServletRequest request) {
        String parentId = userService.getCurrentUserId(request);
        List<PupilRes> pupil = pupilService.getAllPupilsByParent(parentId);
        return ResponseEntity.ok(pupil);
    }
    @Autowired
    private UserService userService;
    @GetMapping("/all")
    public ResponseEntity<?> test(HttpServletRequest request) {
        return ResponseEntity.ok( userService.getCurrentUserId( request));
    }

}
