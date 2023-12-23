package com.deptManager.Dept.model.pageModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertDebtSMSMessageModel {
        private String mobile;
        private String message;
}
