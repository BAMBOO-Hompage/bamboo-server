package SMU.BAMBOO.Hompage.domain.notice.dto;

import SMU.BAMBOO.Hompage.domain.enums.NoticeType;
import SMU.BAMBOO.Hompage.domain.member.dto.response.MemberResponse;
import SMU.BAMBOO.Hompage.domain.notice.entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "공지사항 게시판 응답 DTO")
public class NoticeResponseDTO {

    @Schema(description = "공지사항 게시판 단일 응답 DTO")
    public record Detail(
            @Schema(description = "공지사항 게시판 ID") Long noticeId,
            @Schema(description = "작성한 멤버 정보") MemberResponse member,
            @Schema(description = "제목") String title,
            @Schema(description = "내용") String content,
            @Schema(description = "게시글 유형(EVENTS, NOTICE)") NoticeType type,
            @Schema(description = "이미지 리스트") List<String> images,
            @Schema(description = "파일 리스트") List<String> files,
            @Schema(description = "생성일") LocalDateTime createdAt,
            @Schema(description = "수정일") LocalDateTime updatedAt
    ) {
        public static NoticeResponseDTO.Detail from(Notice notice) {
            return new NoticeResponseDTO.Detail(
                    notice.getNoticeId(),
                    MemberResponse.from(notice.getMember()),
                    notice.getTitle(),
                    notice.getContent(),
                    notice.getType(),
                    notice.getImages() != null ? notice.getImages() : new ArrayList<>(),
                    notice.getFiles() != null ? notice.getFiles() : new ArrayList<>(),
                    notice.getCreatedAt(),
                    notice.getModifiedAt()
            );
        }
    }

}