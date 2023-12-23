package com.deptManager.Dept.model.bookmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TotalBooksResponse {
    HttpStatus status;
    Integer quantity;
}
