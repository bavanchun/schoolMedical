package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.BlogRequestDTO;
import com.schoolhealth.schoolmedical.model.dto.response.BlogResponseDTO;
import com.schoolhealth.schoolmedical.service.blog.BlogServiceCustom;
import com.schoolhealth.schoolmedical.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
 * REST controller exposing blog management APIs. Only admins and managers can
 * create, update or delete blogs. All users can view published blogs.
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
     * Create a new published blog post.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create blog", description = "Managers and admins create a blog post")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Blog created",
                    content = @Content(schema = @Schema(implementation = BlogResponseDTO.class)))
    })
    public ResponseEntity<BlogResponseDTO> createBlog(@Valid @RequestBody BlogRequestDTO dto,
                                                      HttpServletRequest request) {

        String userId = userService.getCurrentUserId(request);
        BlogResponseDTO response = blogService.createBlog(dto, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing blog.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update blog", description = "Update an existing blog")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog updated",
                    content = @Content(schema = @Schema(implementation = BlogResponseDTO.class)))
    })
        public ResponseEntity<BlogResponseDTO> updateBlog(@PathVariable("id") Long blogId,
                @Valid @RequestBody BlogRequestDTO dto,
                HttpServletRequest request) {
            String userId = userService.getCurrentUserId(request);
            BlogResponseDTO response = blogService.updateBlog(blogId, dto, userId);
            return ResponseEntity.ok(response);
        }

        /**
         * Soft delete a blog post.
         */
        @DeleteMapping("/{id}")
        @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
        @SecurityRequirement(name = "bearerAuth")
        @Operation(summary = "Delete blog", description = "Soft delete a blog")
        @ApiResponse(responseCode = "204", description = "Blog deleted")
            public ResponseEntity<Void> deleteBlog(@PathVariable("id") Long blogId,
                    HttpServletRequest request) {
                String userId = userService.getCurrentUserId(request);
                blogService.deleteBlog(blogId, userId);
                return ResponseEntity.noContent().build();
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