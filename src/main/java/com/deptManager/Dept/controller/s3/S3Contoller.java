package com.deptManager.Dept.controller.s3;

import com.deptManager.Dept.exception.s3Exception.S3Exception;
import com.deptManager.Dept.model.s3Model.S3ImageUploadOrDownloadResponseModel;
import com.deptManager.Dept.service.s3Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

@RestController
@RequestMapping("/api/v1/s3")
public class S3Contoller {

    @Autowired
    private S3Service s3Service;;

    @PostMapping("/upload/{userId}")
    public S3ImageUploadOrDownloadResponseModel upload(@RequestParam("file") MultipartFile file, @PathVariable Long userId) throws S3Exception {
        return s3Service.saveFile(file, userId);
    }

    @GetMapping("/download/{userId}")
    public ResponseEntity<byte[]> download( @PathVariable Long userId) throws S3Exception {
        return s3Service.downloadFile(userId);
    }


    @DeleteMapping("{filename}")
    public  String deleteFile(@PathVariable("filename") String filename){
        return s3Service.deleteFile(filename);
    }

    @GetMapping("list")
    public List<String> getAllFiles(){

        return s3Service.listAllFiles();

    }
}