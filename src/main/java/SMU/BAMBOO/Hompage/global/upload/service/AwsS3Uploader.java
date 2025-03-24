package SMU.BAMBOO.Hompage.global.upload.service;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.upload.util.AwsS3FileNameGenerator;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AwsS3Uploader {

    private final AmazonS3 s3Client;
    private final AwsS3FileNameGenerator fileNameGenerator;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /** 단일 파일 업로드 */
    public String uploadFile(String folderName, MultipartFile file, boolean isImage) {
        String fileName = fileNameGenerator.createFileName(folderName, file.getOriginalFilename(), isImage);
        uploadToS3(fileName, file);
        return generateFileUrl(fileName);
    }

    /** 여러 개의 파일 업로드 */
    public List<String> uploadFiles(String folderName, List<MultipartFile> files, boolean isImage) {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            fileUrls.add(uploadFile(folderName, file, isImage));
        }
        return fileUrls;
    }

    private void uploadToS3(String fileName, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.UPLOAD_FAILED);
        }
    }

    private String generateFileUrl(String fileName) {
        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
    }
}
