package com.deptManager.Dept.model.deptTransactionModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DebtTransactionalTotal {
    private HttpStatus status;
    private Double total;
}
