package com.deptManager.Dept.model.pageModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PageExceptionResponseModel {
    private HttpStatus status;
    private String message;
}
