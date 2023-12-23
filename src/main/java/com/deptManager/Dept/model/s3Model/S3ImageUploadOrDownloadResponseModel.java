package com.deptManager.Dept.model.s3Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class S3ImageUploadOrDownloadResponseModel {
    private HttpStatus status;
    private String message;
}
