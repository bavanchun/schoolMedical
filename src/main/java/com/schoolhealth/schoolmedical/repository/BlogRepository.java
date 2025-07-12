package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Blog} entities with custom query methods
 * used throughout the blog workflow.
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    /**
     * Find active blogs belonging to an author.
     */
    @Query("SELECT b FROM Blog b WHERE b.authorId.userId = :authorId AND b.isActive = true")
    List<Blog> findByAuthorIdAndIsActiveTrue(@Param("authorId") String authorId);

    /**
     * Retrieve all blogs that are active.
     */
    @Query("SELECT b FROM Blog b WHERE b.isActive = true")
    List<Blog> findByIsActiveTrue();

    /**
     * Find a blog by id that is active.
     */
    @Query("SELECT b FROM Blog b WHERE b.blogId = :blogId AND b.isActive = true")
    Optional<Blog> findByBlogIdAndIsActiveTrue(@Param("blogId") Long blogId);
}