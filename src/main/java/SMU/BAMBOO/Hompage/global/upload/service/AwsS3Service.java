package SMU.BAMBOO.Hompage.global.upload.service;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 s3Client;

    // 여러 개의 파일 업로드
    public List<String> uploadFiles(String folderName, List<MultipartFile> files, boolean isImage) {
        return uploadFilesToFolder(folderName, files, isImage);
    }

    // 단일 파일 업로드
    public String uploadFile(String folderName, MultipartFile file, boolean isImage) {
        return uploadFileToFolder(folderName, file, isImage);
    }

    // 공통 - 여러개의 파일 업로드
    private List<String> uploadFilesToFolder(String folderName, List<MultipartFile> files, boolean isImage) {
        List<String> fileUrlList = new ArrayList<>();

        files.forEach(file -> {

            String fileName = createFileName(folderName, file.getOriginalFilename(), isImage);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new CustomException(ErrorCode.UPLOAD_FAILED);
            }

            String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
            fileUrlList.add(fileUrl);
        });

        return fileUrlList;
    }

    // 공통 - 파일 하나 업로드
    private String uploadFileToFolder(String folderName, MultipartFile file, boolean isImage) {

        String fileName = createFileName(folderName, file.getOriginalFilename(), isImage);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.UPLOAD_FAILED);
        }

        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
    }


    // 파일 삭제
    public void deleteFile(String fileName) {
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
        } catch (Exception e){
            throw new CustomException(ErrorCode.DELETE_FAILED);
        }
    }


    // 파일명 중복 방지 (UUID)
    private String createFileName(String folderName, String fileName, boolean isImage) {
        String uniqueFileName = UUID.randomUUID().toString() + getFileExtension(fileName, isImage);
        return folderName + "/" + uniqueFileName;
    }

    // 파일 유효성 검사
    public String getFileExtension(String fileName, boolean isImage) {
        fileName = fileName.trim();

        // 확장자 추출
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        List<String> imageExtensions = new ArrayList<>(List.of("jpg", "jpeg", "png"));
        List<String> documentExtensions = new ArrayList<>(List.of("pdf", "docx", "xlsx", "txt", "csv", "zip"));

        // 리스트에서 확장자 포함 여부 확인
        boolean isValidImage = imageExtensions.contains(fileExtension);
        boolean isValidDocument = documentExtensions.contains(fileExtension);

        if (isImage) {
            if (!isValidImage) {
                throw new CustomException(ErrorCode.INVALID_REQUEST);
            }
            return fileExtension;
        } else {
            if (!isValidDocument) {
                throw new CustomException(ErrorCode.INVALID_REQUEST);
            }
        }

        return fileExtension;
    }

    // S3에서 파일 키 추출
    public String extractS3Key(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        String prefix = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/";
        if (!fileUrl.startsWith(prefix)) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        return fileUrl.substring(prefix.length());
    }
}