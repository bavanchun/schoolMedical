package com.schoolhealth.schoolmedical.model.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Payload for manager actions when approving or rejecting a blog post.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogStatusUpdateDTO {

    @Pattern(regexp = "PUBLISHED|REJECTED", message = "Status must be either PUBLISHED or REJECTED")
    private String status;

    private String note;
}