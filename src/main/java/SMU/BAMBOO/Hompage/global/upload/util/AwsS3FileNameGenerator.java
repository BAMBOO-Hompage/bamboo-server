package SMU.BAMBOO.Hompage.global.upload.util;

import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
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
        if (fileName == null) {
            log.error("파일명이 null입니다");
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            log.error("파일명에 확장자가 없습니다: {}", fileName);
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        String extension = fileName.substring(dotIndex + 1).toLowerCase();

        List<String> validExtensions = isImage ? List.of("jpg", "jpeg", "png")
                : List.of("pdf", "pptx", "hwp", "docx", "xlsx", "txt", "csv", "zip");

        if (!validExtensions.contains(extension)) {
            log.error("허용되지 않은 확장자: {}", extension);
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        return extension;
    }

}
