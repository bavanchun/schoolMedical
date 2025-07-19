package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Blog;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.model.dto.request.BlogRequestDTO;
import com.schoolhealth.schoolmedical.model.dto.response.AuthorDTO;
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
    @Mapping(target = "authorId", ignore = true)
    @Mapping(target = "verifierId", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    Blog toEntity(BlogRequestDTO dto);

    @Mapping(target = "author", expression = "java(createAuthorDTO(blog.getAuthorId()))")
    @Mapping(target = "verifierName", expression = "java(extractUserName(blog.getVerifierId()))")
    @Mapping(target = "createdAt", expression = "java(blog.getCreatedAt() != null ? blog.getCreatedAt().atStartOfDay() : null)")
    @Mapping(target = "lastUpdatedAt", expression = "java(blog.getLastUpdatedAt() != null ? blog.getLastUpdatedAt().atStartOfDay() : null)")
    BlogResponseDTO toResponse(Blog blog);

    List<BlogResponseDTO> toResponseList(List<Blog> blogs);

    /**
     * Helper method to extract user's full name.
     */
    default String extractUserName(User user) {
        if (user == null) {
            return null;
        }
        String last = user.getLastName() != null ? user.getLastName() : "";
        String first = user.getFirstName() != null ? user.getFirstName() : "";
        String fullName = (last + " " + first).trim();
        return fullName.isEmpty() ? null : fullName;
    }

    /**
     * Helper method to create author information DTO.
     */
    default AuthorDTO createAuthorDTO(User user) {
        if (user == null) {
            return null;
        }

        String name = extractUserName(user);

        return AuthorDTO.builder()
                .name(name)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @AfterMapping
    default void updateEntityFromDto(BlogRequestDTO dto, @MappingTarget Blog blog) {
        // Additional manual mappings can be placed here if needed in the future
    }
}
