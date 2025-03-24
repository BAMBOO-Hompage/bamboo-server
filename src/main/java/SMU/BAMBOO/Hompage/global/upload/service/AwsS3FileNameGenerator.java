package SMU.BAMBOO.Hompage.global.upload.service;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.UUID;

@Service
public class AwsS3FileNameGenerator {

    /** 파일명 생성 - UUID로 중복 방지 */
    public String createFileName(String folderName, String fileName, boolean isImage) {
        String extension = getFileExtension(fileName, isImage);

        int lastDotIndex = fileName.lastIndexOf(".");
        String baseName = (lastDotIndex == -1) ? fileName : fileName.substring(0, lastDotIndex);

        // 한글 및 특수문자 처리: Unicode 정규화 (NFC)
        String normalizedFileName = Normalizer.normalize(baseName, Normalizer.Form.NFC);

        // 공백 및 특수문자 제거 (한글, 영어, 숫자만 허용)
        String safeFileName = normalizedFileName.replaceAll("[^a-zA-Z0-9가-힣]", "_");

        String uniqueFileName = safeFileName + "_" + UUID.randomUUID() + "." + extension;

        return folderName + "/" + uniqueFileName;
    }

    /** 파일 유효성 검사 */
    public String getFileExtension(String fileName, boolean isImage) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        // 이미지 여부에 따라 다른 확장자 확인
        List<String> validExtensions = isImage ? List.of("jpg", "jpeg", "png")
                : List.of("pdf", "pptx", "hwp", "docx", "xlsx", "txt", "csv", "zip");

        if (!validExtensions.contains(extension)) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        return extension;
    }

}
