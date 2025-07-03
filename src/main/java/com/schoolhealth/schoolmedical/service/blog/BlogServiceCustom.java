package com.schoolhealth.schoolmedical.service.blog;

import com.schoolhealth.schoolmedical.entity.enums.BlogStatus;
import com.schoolhealth.schoolmedical.model.dto.request.BlogRequestDTO;
import com.schoolhealth.schoolmedical.model.dto.request.BlogStatusUpdateDTO;
import com.schoolhealth.schoolmedical.model.dto.response.BlogResponseDTO;

import java.util.List;

/**
 * Defines blog management operations.
 */
public interface BlogServiceCustom {

    /**
     * Create a new blog post with status {@link BlogStatus#DRAFT}.
     *
     * @param dto         blog data
     * @param nurseUserId author identifier
     * @return created blog
     */
    BlogResponseDTO createBlog(BlogRequestDTO dto, String nurseUserId);

    /**
     * Update an existing blog owned by the nurse.
     */
    BlogResponseDTO updateBlog(Long blogId, BlogRequestDTO dto, String nurseUserId);

    /**
     * Soft delete a blog owned by the nurse.
     */
    void deleteBlog(Long blogId, String nurseUserId);

    /**
     * Submit a blog for manager review.
     */
    BlogResponseDTO submitBlog(Long blogId, String nurseUserId);

    /**
     * Retrieve blogs by status.
     */
    List<BlogResponseDTO> getBlogsByStatus(BlogStatus status);

    /**
     * Retrieve blogs created by the given nurse.
     */
    List<BlogResponseDTO> getMyBlogs(String nurseUserId);

    /**
     * Approve and publish a pending blog.
     */
    BlogResponseDTO approveBlog(Long blogId, String managerUserId, BlogStatusUpdateDTO dto);

    /**
     * Reject a pending blog.
     */
    BlogResponseDTO rejectBlog(Long blogId, String managerUserId, BlogStatusUpdateDTO dto);

    /**
     * Get all published blogs for public view.
     */
    List<BlogResponseDTO> getPublishedBlogs();

    /**
     * Get a single published blog.
     */
    BlogResponseDTO getPublishedBlog(Long blogId);
}