package SMU.BAMBOO.Hompage.global.upload.service;

import SMU.BAMBOO.Hompage.global.upload.util.AwsS3FileValidator;
import SMU.BAMBOO.Hompage.global.upload.util.AwsS3KeyExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AwsS3Facade {

    private final AwsS3Uploader uploader;
    private final AwsS3Deleter deleter;
    private final AwsS3FileValidator validator;
    private final AwsS3KeyExtractor keyExtractor;

    public String uploadFile(String folderName, MultipartFile file, boolean isImage) {
        validator.validateFile(file);
        return uploader.uploadFile(folderName, file, isImage);
    }

    public List<String> uploadFiles(String folderName, List<MultipartFile> files, boolean isImage) {
        files.forEach(validator::validateFile);
        return uploader.uploadFiles(folderName, files, isImage);
    }

    public void deleteFile(String fileUrl) {
        String fileKey = keyExtractor.extractKey(fileUrl);
        deleter.deleteFile(fileKey);
    }
}
