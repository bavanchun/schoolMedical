package com.schoolhealth.schoolmedical.model.dto.request;

import com.opencsv.bean.CsvBindByPosition;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PupilCsvRowDto {

    @CsvBindByPosition(position = 0)
    private String pupilId;
    @CsvBindByPosition(position = 1)
    private String lastName;
    @CsvBindByPosition(position = 2)
    private String firstName;
    @CsvBindByPosition(position = 3)
    private String birthDate; // yyyy-MM-dd format
    @CsvBindByPosition(position = 4)
    private String gender;  // M/F
    @CsvBindByPosition(position = 5)
    private String parentPhoneNumber;
    @CsvBindByPosition(position = 6)
    private String gradeLevel;  // 1, 2, 3, 4, 5
    @CsvBindByPosition(position = 7)
    private String className;   // Class 1A, Class 2B, etc.
    @CsvBindByPosition(position = 8)
    private String isActive;    // 0/1 or true/false
}
