package SMU.BAMBOO.Hompage.global.upload;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final AmazonS3 s3Client;

    // 여러개의 파일 업로드
    public List<String> uploadFiles(String folderName, List<MultipartFile> multipartFile) {
        List<String> fileNameList = new ArrayList<>();

        multipartFile.forEach(file -> {
            String fileName = createFileName(folderName, file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new CustomException(ErrorCode.UPLOAD_FAILED);
            }

            String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
            fileNameList.add(fileUrl);
        });

        return fileNameList;
    }

    // 파일 하나 업로드
    public String uploadFile(String folderName, MultipartFile multipartFile) {
        String fileName = createFileName(folderName, multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try(InputStream inputStream = multipartFile.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
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
    private String createFileName(String folderName, String fileName) {
        String uniqueFileName = UUID.randomUUID().toString() + getFileExtension(fileName);
        return folderName + "/" + uniqueFileName;
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

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