package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Blog;
import com.schoolhealth.schoolmedical.entity.enums.BlogStatus;
import com.schoolhealth.schoolmedical.model.dto.request.BlogRequestDTO;
import com.schoolhealth.schoolmedical.model.dto.response.BlogResponseDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * MapStruct mapper for converting between {@link Blog} entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface BlogMapper {

    @Mapping(target = "blogId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastUpdatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "authorId", ignore = true)
    @Mapping(target = "verifierId", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    Blog toEntity(BlogRequestDTO dto);

    @Mapping(target = "status", expression = "java(mapStatus(blog.getStatus()))")
    @Mapping(target = "authorName", expression = "java(extractUserName(blog.getAuthorId()))")
    @Mapping(target = "verifierName", expression = "java(extractUserName(blog.getVerifierId()))")
    @Mapping(target = "createdAt", expression = "java(blog.getCreatedAt() != null ? blog.getCreatedAt().atStartOfDay() : null)")
    @Mapping(target = "lastUpdatedAt", expression = "java(blog.getLastUpdatedAt() != null ? blog.getLastUpdatedAt().atStartOfDay() : null)")
    BlogResponseDTO toResponse(Blog blog);

    List<BlogResponseDTO> toResponseList(List<Blog> blogs);

    /**
     * Convert the stored status string to the enum.
     */
    default BlogStatus mapStatus(String status) {
        return status != null ? BlogStatus.valueOf(status) : null;
    }

    /**
     * Helper method to extract user's full name.
     */
    default String extractUserName(com.schoolhealth.schoolmedical.entity.User user) {
        if (user == null) {
            return null;
        }
        String first = user.getFirstName() != null ? user.getFirstName() : "";
        String last = user.getLastName() != null ? user.getLastName() : "";
        String fullName = (first + " " + last).trim();
        return fullName.isEmpty() ? null : fullName;
    }

    @AfterMapping
    default void updateEntityFromDto(BlogRequestDTO dto, @MappingTarget Blog blog) {
        // Additional manual mappings can be placed here if needed in the future
    }
}
