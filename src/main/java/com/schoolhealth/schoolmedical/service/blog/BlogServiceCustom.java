package com.schoolhealth.schoolmedical.service.blog;

import com.schoolhealth.schoolmedical.model.dto.request.BlogRequestDTO;
import com.schoolhealth.schoolmedical.model.dto.response.BlogResponseDTO;

import java.util.List;

/**
 * Defines blog management operations.
 */
public interface BlogServiceCustom {

    /**
     * Create a new blog post. The creator will be set as both author and verifier.
     *
     * @param dto     blog data
     * @param userId  identifier of the admin/manager creating the blog
     * @return created blog
     */
    BlogResponseDTO createBlog(BlogRequestDTO dto, String userId);

    /**
     * Update an existing blog.
     */
    BlogResponseDTO updateBlog(Long blogId, BlogRequestDTO dto, String userId);

    /**
     * Soft delete a blog.
     */
    void deleteBlog(Long blogId, String userId);

    /**
     * Get all active blogs for public view.
     */
    List<BlogResponseDTO> getPublishedBlogs();

    /**
     * Get a single active blog by ID.
     */
    BlogResponseDTO getPublishedBlog(Long blogId);
}