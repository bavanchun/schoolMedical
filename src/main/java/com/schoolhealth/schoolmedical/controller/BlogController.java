package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.entity.enums.BlogStatus;
import com.schoolhealth.schoolmedical.model.dto.request.BlogRequestDTO;
import com.schoolhealth.schoolmedical.model.dto.request.BlogStatusUpdateDTO;
import com.schoolhealth.schoolmedical.model.dto.response.BlogResponseDTO;
import com.schoolhealth.schoolmedical.service.blog.BlogServiceCustom;
import com.schoolhealth.schoolmedical.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing blog management APIs for nurses, managers and public users.
 */
@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Blog Management", description = "APIs for managing blog posts")
public class BlogController {

    private final BlogServiceCustom blogService;
    private final UserService userService;

    /**
     * Create a new blog post with status DRAFT.
     */
    @PostMapping
    @PreAuthorize("hasRole('SCHOOL_NURSE')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create blog", description = "School nurse creates a blog post in DRAFT status")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Blog created",
                    content = @Content(schema = @Schema(implementation = BlogResponseDTO.class)))
    })
    public ResponseEntity<BlogResponseDTO> createBlog(@Valid @RequestBody BlogRequestDTO dto,
                                                      HttpServletRequest request) {
        String nurseId = userService.getCurrentUserId(request);
        BlogResponseDTO response = blogService.createBlog(dto, nurseId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing blog owned by the nurse.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SCHOOL_NURSE')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update blog", description = "Update a DRAFT or PENDING blog owned by the nurse")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog updated",
                    content = @Content(schema = @Schema(implementation = BlogResponseDTO.class)))
    })
    public ResponseEntity<BlogResponseDTO> updateBlog(
            @Parameter(description = "Blog ID") @PathVariable("id") Long blogId,
            @Valid @RequestBody BlogRequestDTO dto,
            HttpServletRequest request) {
        String nurseId = userService.getCurrentUserId(request);
        BlogResponseDTO response = blogService.updateBlog(blogId, dto, nurseId);
        return ResponseEntity.ok(response);
    }

    /**
     * Soft delete a blog post.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SCHOOL_NURSE')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete blog", description = "Soft delete a DRAFT or PENDING blog")
    @ApiResponse(responseCode = "204", description = "Blog deleted")
    public ResponseEntity<Void> deleteBlog(@PathVariable("id") Long blogId, HttpServletRequest request) {
        String nurseId = userService.getCurrentUserId(request);
        blogService.deleteBlog(blogId, nurseId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Submit a blog for manager approval.
     */
    @PutMapping("/{id}/submit")
    @PreAuthorize("hasRole('SCHOOL_NURSE')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Submit blog", description = "Submit blog for manager review")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog submitted",
                    content = @Content(schema = @Schema(implementation = BlogResponseDTO.class)))
    })
    public ResponseEntity<BlogResponseDTO> submitBlog(@PathVariable("id") Long blogId,
                                                      HttpServletRequest request) {
        String nurseId = userService.getCurrentUserId(request);
        BlogResponseDTO response = blogService.submitBlog(blogId, nurseId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get blogs created by the authenticated nurse.
     */
    @GetMapping("/mine")
    @PreAuthorize("hasRole('SCHOOL_NURSE')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "My blogs", description = "List blogs created by the authenticated nurse")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of blogs",
                    content = @Content(schema = @Schema(implementation = BlogResponseDTO.class)))
    })
    public ResponseEntity<List<BlogResponseDTO>> getMyBlogs(HttpServletRequest request) {
        String nurseId = userService.getCurrentUserId(request);
        List<BlogResponseDTO> response = blogService.getMyBlogs(nurseId);
        return ResponseEntity.ok(response);
    }

    /**
     * List all pending blogs for manager review.
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Pending blogs", description = "List all blogs awaiting approval")
    public ResponseEntity<List<BlogResponseDTO>> getPendingBlogs() {
        List<BlogResponseDTO> response = blogService.getBlogsByStatus(BlogStatus.PENDING);
        return ResponseEntity.ok(response);
    }

    /**
     * Approve and publish a blog.
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Approve blog", description = "Approve a pending blog and publish it")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog approved",
                    content = @Content(schema = @Schema(implementation = BlogResponseDTO.class)))
    })
    public ResponseEntity<BlogResponseDTO> approveBlog(
            @PathVariable("id") Long blogId,
            @Valid @RequestBody(required = false) BlogStatusUpdateDTO dto,
            HttpServletRequest request) {
        String managerId = userService.getCurrentUserId(request);
        BlogResponseDTO response = blogService.approveBlog(blogId, managerId, dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Reject a pending blog.
     */
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Reject blog", description = "Reject a pending blog")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog rejected",
                    content = @Content(schema = @Schema(implementation = BlogResponseDTO.class)))
    })
    public ResponseEntity<BlogResponseDTO> rejectBlog(
            @PathVariable("id") Long blogId,
            @Valid @RequestBody(required = false) BlogStatusUpdateDTO dto,
            HttpServletRequest request) {
        String managerId = userService.getCurrentUserId(request);
        BlogResponseDTO response = blogService.rejectBlog(blogId, managerId, dto);
        return ResponseEntity.ok(response);
    }

    /**
     * List all published blogs for public view.
     */
    @GetMapping("/public")
    @Operation(summary = "Published blogs", description = "List all published blogs")
    public ResponseEntity<List<BlogResponseDTO>> getPublishedBlogs() {
        List<BlogResponseDTO> response = blogService.getPublishedBlogs();
        return ResponseEntity.ok(response);
    }

    /**
     * Get a single published blog by its ID.
     */
    @GetMapping("/public/{id}")
    @Operation(summary = "Get published blog", description = "Get details of a published blog")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found",
                    content = @Content(schema = @Schema(implementation = BlogResponseDTO.class)))
    })
    public ResponseEntity<BlogResponseDTO> getPublishedBlog(@PathVariable("id") Long blogId) {
        BlogResponseDTO response = blogService.getPublishedBlog(blogId);
        return ResponseEntity.ok(response);
    }
}
