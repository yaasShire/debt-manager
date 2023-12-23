package com.deptManager.Dept.service.s3Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.deptManager.Dept.entity.AppUser;
import com.deptManager.Dept.exception.s3Exception.S3Exception;
import com.deptManager.Dept.model.s3Model.S3ImageUploadOrDownloadResponseModel;
import com.deptManager.Dept.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.net.HttpURLConnection.HTTP_OK;

@Service
@RequiredArgsConstructor
public class S3Service implements FileServiceImpl{

    private final AmazonS3 s3;
    private final AppUserRepository appUserRepository;

    @Value("${bucketName}")
    private String bucketName;

    @Override
    public S3ImageUploadOrDownloadResponseModel saveFile(MultipartFile file, Long userId) throws S3Exception {
        String originalFilename = file.getOriginalFilename();
        int count = 0;
        int maxTries = 3;
        while(true) {
            try {
                Optional<AppUser> user = appUserRepository.findAppUserById(userId);
                if (user.isPresent()){
                deleteFile(user.get().getProfileImage());
                File file1 = convertMultiPartToFile(file);
                PutObjectResult putObjectResult = s3.putObject(bucketName, originalFilename, file1);
                user.get().setProfileImage(originalFilename);
                appUserRepository.save(user.get());
                S3ImageUploadOrDownloadResponseModel responseModel = S3ImageUploadOrDownloadResponseModel.builder()
                        .status(HttpStatus.CREATED)
                        .message("profile image saved successfully")
                        .build();
                return responseModel;
                }else {
                    throw new S3Exception("User with id " + userId + " doest not exist");
                }
            } catch (Exception e) {
                if (++count == maxTries) throw new S3Exception(e.getMessage());
            }
        }

    }

    @Override
    public ResponseEntity<byte[]> downloadFile(Long userId) throws S3Exception {

        try {
            Optional<AppUser> user = appUserRepository.findAppUserById(userId);
            if (user.isPresent()){
                S3Object object = s3.getObject(bucketName, user.get().getProfileImage());
                S3ObjectInputStream objectContent = object.getObjectContent();

                HttpHeaders headers=new HttpHeaders();
                headers.add("Content-type", String.valueOf(MediaType.IMAGE_JPEG));
                headers.add("Content-Disposition", "attachment; filename="+user.get().getProfileImage());
                return  ResponseEntity.status(HTTP_OK).contentType(MediaType.IMAGE_JPEG).headers(headers).body(IOUtils.toByteArray(objectContent));
            }else {
                throw new S3Exception("User with id " + userId + " does not exit");
            }
        } catch (Exception e) {
            throw  new S3Exception(e.getMessage());
        }


    }

    @Override
    public String deleteFile(String filename) {

        s3.deleteObject(bucketName,filename);
        return "File deleted";
    }

    @Override
    public List<String> listAllFiles() {

        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(bucketName);
        return  listObjectsV2Result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());

    }


    private File convertMultiPartToFile(MultipartFile file ) throws IOException
    {
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        return convFile;
    }
}