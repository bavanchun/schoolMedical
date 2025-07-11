package com.schoolhealth.schoolmedical.service.blog;

import com.schoolhealth.schoolmedical.entity.Blog;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.exception.BlogNotFoundException;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.BlogRequestDTO;
import com.schoolhealth.schoolmedical.model.dto.response.BlogResponseDTO;
import com.schoolhealth.schoolmedical.model.mapper.BlogMapper;
import com.schoolhealth.schoolmedical.repository.BlogRepository;
import com.schoolhealth.schoolmedical.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link BlogServiceCustom} for a simplified workflow where
 * managers and admins directly create published blogs.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BlogServiceCustomImpl implements BlogServiceCustom {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;
    private final UserRepository userRepository;

    @Override
    public BlogResponseDTO createBlog(BlogRequestDTO dto, String userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Blog blog = blogMapper.toEntity(dto);
        blog.setAuthorId(author);
        blog.setVerifierId(author);

        Blog saved = blogRepository.save(blog);
        return blogMapper.toResponse(saved);
    }

    @Override
    public BlogResponseDTO updateBlog(Long blogId, BlogRequestDTO dto, String userId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("Blog not found with id: " + blogId));

        blog.setTitle(dto.getTitle());
        blog.setContent(dto.getContent());
        blog.setImageUrl(dto.getImageUrl());
        Blog updated = blogRepository.save(blog);
        return blogMapper.toResponse(updated);
    }

    @Override
    public void deleteBlog(Long blogId, String userId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("Blog not found with id: " + blogId));

        blog.setActive(false);
        blogRepository.save(blog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogResponseDTO> getPublishedBlogs() {
        List<Blog> blogs = blogRepository.findByIsActiveTrue();
        return blogMapper.toResponseList(blogs);
    }

    @Override
    @Transactional(readOnly = true)
    public BlogResponseDTO getPublishedBlog(Long blogId) {
        Blog blog = blogRepository
                .findByBlogIdAndIsActiveTrue(blogId)
                .orElseThrow(() -> new BlogNotFoundException("Published blog not found with id: " + blogId));
        return blogMapper.toResponse(blog);
    }
}