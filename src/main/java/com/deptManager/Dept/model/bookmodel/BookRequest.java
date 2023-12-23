package com.deptManager.Dept.model.bookmodel;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookRequest {
    private String ownerName;
    private String ownerNumber;
    private String backUpNumber;
}
