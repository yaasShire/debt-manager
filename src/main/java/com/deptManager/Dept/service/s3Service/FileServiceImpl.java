package com.deptManager.Dept.service.s3Service;

import com.deptManager.Dept.exception.s3Exception.S3Exception;
import com.deptManager.Dept.model.s3Model.S3ImageUploadOrDownloadResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileServiceImpl {


    S3ImageUploadOrDownloadResponseModel saveFile(MultipartFile file, Long userId) throws S3Exception;


    ResponseEntity<byte[]> downloadFile(Long userId) throws S3Exception;


    String deleteFile(String filename);



    List<String> listAllFiles();
}