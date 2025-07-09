package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkVaccinationHistoryResponse {

    private String pupilId;
    private String pupilName;
    private int totalCreated;
    private int totalFailed;
    private List<VaccinationHistoryResponse> successfulRecords;
    private List<BulkError> errors;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BulkError {
        private Long vaccineId;
        private Long diseaseId;
        private String errorMessage;
    }
}