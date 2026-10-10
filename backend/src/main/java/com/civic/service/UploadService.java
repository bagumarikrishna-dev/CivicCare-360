package com.civic.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;
@Service
public class UploadService {
 @Value("${app.upload-dir}") private String uploadDir;
 private static final Set<String> TYPES=Set.of("image/jpeg","image/png","image/webp","image/gif");
 public String save(MultipartFile file)throws Exception{
  if(file==null||file.isEmpty()) return null;
  if(file.getSize()>5*1024*1024) throw new IllegalArgumentException("Image must be 5 MB or smaller.");
  if(file.getContentType()==null||!TYPES.contains(file.getContentType())) throw new IllegalArgumentException("Only JPG, PNG, WEBP, and GIF images are allowed.");
  String original=file.getOriginalFilename()==null?"image":file.getOriginalFilename();
  String ext=original.lastIndexOf(".")>=0?original.substring(original.lastIndexOf(".")).replaceAll("[^a-zA-Z0-9.]",""):"";
  String name=UUID.randomUUID()+ext;
  Path dir=Paths.get(uploadDir).toAbsolutePath().normalize(); Files.createDirectories(dir);
  Files.copy(file.getInputStream(),dir.resolve(name),StandardCopyOption.REPLACE_EXISTING);
  return name;
 }
}
