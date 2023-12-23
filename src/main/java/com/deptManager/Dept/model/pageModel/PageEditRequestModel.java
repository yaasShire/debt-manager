package com.deptManager.Dept.model.pageModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PageEditRequestModel {
    private Date endDate;
    private Boolean status;
    private Double currentTotalDept;
}
