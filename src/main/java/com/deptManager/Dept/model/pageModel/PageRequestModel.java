package com.deptManager.Dept.model.pageModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PageRequestModel {
    private Date endDate;
    private Double currentTotalDept;
}
