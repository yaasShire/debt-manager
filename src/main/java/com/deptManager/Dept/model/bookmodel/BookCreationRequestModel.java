package com.deptManager.Dept.model.bookmodel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookCreationRequestModel {
    private String ownerName;
    private String ownerNumber;
    private String backUpNumber;
}
