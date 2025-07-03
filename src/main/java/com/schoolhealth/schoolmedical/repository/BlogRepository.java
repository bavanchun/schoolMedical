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
     * Find blogs by status regardless of active flag.
     *
     * @param status status string
     * @return list of blogs
     */
    @Query("SELECT b FROM Blog b WHERE b.status = :status")
    List<Blog> findByStatus(@Param("status") String status);

    /**
     * Find blogs by status that are active.
     */
    @Query("SELECT b FROM Blog b WHERE b.status = :status AND b.isActive = true")
    List<Blog> findByStatusAndIsActiveTrue(@Param("status") String status);

    /**
     * Find blogs by author and status.
     */
    @Query("SELECT b FROM Blog b WHERE b.authorId.userId = :authorId AND b.status = :status")
    List<Blog> findByAuthorIdAndStatus(@Param("authorId") String authorId, @Param("status") String status);

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
     * Find pending blogs awaiting manager review.
     */
    @Query("SELECT b FROM Blog b WHERE b.status = 'PENDING' AND b.isActive = true")
    List<Blog> findPendingBlogs();

    /**
     * Find a published blog by id.
     */
    @Query("SELECT b FROM Blog b WHERE b.blogId = :blogId AND b.status = :status AND b.isActive = true")
    Optional<Blog> findByBlogIdAndStatusAndIsActiveTrue(@Param("blogId") Long blogId,
                                                        @Param("status") String status);
}
