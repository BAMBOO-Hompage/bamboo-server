package SMU.BAMBOO.Hompage.domain.libraryPost.dto;

import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "알렉산드리아 도서관 응답 DTO")
public class LibraryPostResponseDTO {

    @Schema(description = "알렉산드리아 도서관 글 생성 응답 DTO")
    public record Create(
            Long libraryPostId,
            Long memberId,
            String speaker,
            String paperName,
            int year,
            String topic,
            String link,
            List<String> tagNames
    ) {
        public static Create from(LibraryPost libraryPost) {
            return new Create(
                    libraryPost.getLibraryPostId(),
                    libraryPost.getMember().getMemberId(),
                    libraryPost.getSpeaker(),
                    libraryPost.getPaperName(),
                    libraryPost.getYear(),
                    libraryPost.getTopic(),
                    libraryPost.getLink(),
                    libraryPost.getLibraryPostTags().stream()
                            .map(libraryPostTag -> libraryPostTag.getTag().getName())
                            .toList()
            );
        }
    }

    @Schema(description = "알렉산드리아 도서관 글 ID로 조회 응답 DTO")
    public record GetOne(
            Long libraryPostId,
            Long memberId,
            String speaker,
            String paperName,
            int year,
            String topic,
            String link,
            List<String> tagNames
    ) {
        public static GetOne from(LibraryPost libraryPost) {
            return new GetOne(
                    libraryPost.getLibraryPostId(),
                    libraryPost.getMember().getMemberId(),
                    libraryPost.getSpeaker(),
                    libraryPost.getPaperName(),
                    libraryPost.getYear(),
                    libraryPost.getTopic(),
                    libraryPost.getLink(),
                    libraryPost.getLibraryPostTags().stream()
                            .map(libraryPostTag -> libraryPostTag.getTag().getName())
                            .toList()
            );
        }
    }
}

