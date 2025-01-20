package SMU.BAMBOO.Hompage.domain.libraryPost.dto;

import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "알렉산드리아 도서관 응답 DTO")
public class LibraryPostResponseDTO {

    @Schema(description = "알렉산드리아 도서관 글 생성 응답 DTO")
    public static record Create(
            Long libraryPostId,
            Long memberId,
            String speaker,
            String paperName,
            int year,
            String topic,
            String link
    ) {
        public static Create from(LibraryPost libraryPost) {
            return new Create(
                    libraryPost.getLibraryPostId(),
                    libraryPost.getMember().getMemberId(),
                    libraryPost.getSpeaker(),
                    libraryPost.getPaperName(),
                    libraryPost.getYear(),
                    libraryPost.getTopic(),
                    libraryPost.getLink()
            );
        }
    }

    @Schema(description = "알렉산드리아 도서관 글 ID로 조회 응답 DTO")
    public static record GetOne(
            Long libraryPostId,
            Long memberId,
            String speaker,
            String paperName,
            int year,
            String topic,
            String link
    ) {
        public static GetOne from(LibraryPost libraryPost) {
            return new GetOne(
                    libraryPost.getLibraryPostId(),
                    libraryPost.getMember().getMemberId(),
                    libraryPost.getSpeaker(),
                    libraryPost.getPaperName(),
                    libraryPost.getYear(),
                    libraryPost.getTopic(),
                    libraryPost.getLink()
            );
        }
    }
}

