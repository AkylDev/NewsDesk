package kz.aqyl.newsdesk.service.impl;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import kz.aqyl.newsdesk.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
  private final MinioClient minioClient;
  private final String bucketName;

  @Override
  public String uploadFile(MultipartFile file) {
    try {
      if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
      }

      String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
      minioClient.putObject(
              PutObjectArgs.builder()
                      .bucket(bucketName)
                      .object(fileName)
                      .stream(file.getInputStream(), file.getSize(), -1)
                      .contentType(file.getContentType())
                      .build()
      );
      return fileName;
    } catch (Exception e) {
      throw new RuntimeException("Error while uploading file to Minio", e);
    }
  }
}
