package SMU.BAMBOO.Hompage.global.upload.util;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AwsS3KeyExtractor {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String extractKey(String fileUrl) {
        if (!fileUrl.startsWith("https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/")) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        return fileUrl.substring(fileUrl.indexOf(".com/") + 5);
    }

}
