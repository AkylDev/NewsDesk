package kz.aqyl.newsdesk.service;

import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
  String uploadFile(MultipartFile file);
}
