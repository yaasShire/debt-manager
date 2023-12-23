package com.deptManager.Dept.model.deptItemModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DeptItemExceptionResponse {
    private HttpStatus status;
    private String message;
}
