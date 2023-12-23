package com.deptManager.Dept.model.deptTransactionModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DebtTransactionRequestModel {
    private Double ammount;
    private Date date;
}