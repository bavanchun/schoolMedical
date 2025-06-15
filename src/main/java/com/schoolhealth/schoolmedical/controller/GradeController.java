package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.GradeDTO;
import com.schoolhealth.schoolmedical.service.grade.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/greades")
@Tag(name = "Grade Management", description = "API để quản lý các lớp học trong hệ thống")
public class GradeController {

    private GradeService service;

    @Autowired
    public GradeController(GradeService service) {
        this.service = service;
    }

    @Operation(summary = "Lấy danh sách tất cả các lớp học",
               description = "Trả về danh sách tất cả các lớp học có trong hệ thống")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách lớp học thành công",
                 content = @Content(schema = @Schema(implementation = GradeDTO.class)))
    @GetMapping
    public List<GradeDTO> listAll() {
        return service.getAllGrades();
    }

    @Operation(summary = "Lấy thông tin một lớp học theo ID",
               description = "Trả về chi tiết thông tin của một lớp học dựa trên ID được cung cấp")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tìm thấy lớp học",
                    content = @Content(schema = @Schema(implementation = GradeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy lớp học với ID đã cung cấp",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<GradeDTO> getOne(
            @Parameter(description = "ID của lớp học cần tìm") @PathVariable Long id) {
        return ResponseEntity.ok(service.getGradeById(id));
    }

    @Operation(summary = "Tạo lớp học mới",
               description = "Tạo một lớp học mới với thông tin được cung cấp")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Lớp học được tạo thành công",
                    content = @Content(schema = @Schema(implementation = GradeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<GradeDTO> create(
            @Parameter(description = "Thông tin lớp học cần tạo") @RequestBody GradeDTO grade) {
        GradeDTO created = service.createGrade(grade);
        return ResponseEntity
                .status(201)
                .body(created);
    }

    @Operation(summary = "Cập nhật thông tin lớp học",
               description = "Cập nhật thông tin của một lớp học dựa trên ID cung cấp")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cập nhật lớp học thành công",
                    content = @Content(schema = @Schema(implementation = GradeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy lớp học với ID đã cung cấp",
                    content = @Content),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<GradeDTO> update(
            @Parameter(description = "ID của lớp học cần cập nhật") @PathVariable Long id,
            @Parameter(description = "Thông tin lớp học mới") @RequestBody GradeDTO grade) {
        return ResponseEntity.ok(service.updateGrade(id, grade));
    }

    @Operation(summary = "Xóa một lớp học",
               description = "Xóa một lớp học khỏi hệ thống dựa trên ID cung cấp")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Xóa lớp học thành công"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy lớp học với ID đã cung cấp",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID của lớp học cần xóa") @PathVariable Long id) {
        service.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}
