package exe201.Refashion.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileUploadService {

    String UPLOAD_DIR = "uploads/";

    public String uploadImage(MultipartFile file) {
        try {
            // Kiểm tra xem file có hợp lệ không
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty");
            }

            // Kiểm tra định dạng file (chỉ chấp nhận JPG, PNG)
            String contentType = file.getContentType();
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                throw new IllegalArgumentException("Only JPG and PNG files are allowed");
            }

            // Tạo thư mục uploads nếu chưa tồn tại
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Tạo tên file duy nhất
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // Lưu file
            Files.write(filePath, file.getBytes());

            // Trả về URL tương đối (có thể điều chỉnh thành URL tuyệt đối nếu cần)
            return UPLOAD_DIR + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}