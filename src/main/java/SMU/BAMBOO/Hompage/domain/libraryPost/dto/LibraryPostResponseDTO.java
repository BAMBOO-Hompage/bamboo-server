package SMU.BAMBOO.Hompage.domain.libraryPost.dto;

import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import SMU.BAMBOO.Hompage.domain.member.dto.MemberResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Optional;

@Schema(description = "알렉산드리아 도서관 응답 DTO")
public class LibraryPostResponseDTO {

    @Schema(description = "알렉산드리아 도서관 글 생성 응답 DTO")
    public record Create(
            @Schema(description = "알렉산드리아 글 ID")
            Long libraryPostId,

            @Schema(description = "멤버 정보")
            MemberResponseDTO.MemberInfo member,

            @Schema(description = "논문 이름")
            String paperName,

            @Schema(description = "연도")
            String year,

            @Schema(description = "주제")
            String topic,

            @Schema(description = "내용")
            String content,

            @Schema(description = "링크")
            String link,

            @Schema(description = "관련 태그들")
            List<String> tagNames
    ) {
        public static Create from(LibraryPost libraryPost) {
            return new Create(
                    libraryPost.getLibraryPostId(),
                    Optional.ofNullable(libraryPost.getMember())
                            .map(MemberResponseDTO.MemberInfo::from)
                            .orElse(null),
                    libraryPost.getPaperName(),
                    libraryPost.getYear(),
                    libraryPost.getTopic(),
                    libraryPost.getContent(),
                    libraryPost.getLink(),
                    Optional.ofNullable(libraryPost.getLibraryPostTags())
                            .orElse(List.of())
                            .stream()
                            .map(libraryPostTag -> libraryPostTag.getTag().getName())
                            .toList()
            );
        }
    }

    @Schema(description = "알렉산드리아 도서관 글 ID로 조회 응답 DTO")
    public record GetOne(
            @Schema(description = "알렉산드리아 글 ID")
            Long libraryPostId,

            @Schema(description = "멤버 정보")
            MemberResponseDTO.MemberInfo member,

            @Schema(description = "논문 이름")
            String paperName,

            @Schema(description = "연도")
            String year,

            @Schema(description = "주제")
            String topic,

            @Schema(description = "내용")
            String content,

            @Schema(description = "링크")
            String link,

            @Schema(description = "관련 태그들")
            List<String> tagNames,

            @Schema(description = "댓글 수")
            int commentCount
    ) {
        public static GetOne from(LibraryPost libraryPost) {
            return new GetOne(
                    libraryPost.getLibraryPostId(),
                    Optional.ofNullable(libraryPost.getMember())
                            .map(MemberResponseDTO.MemberInfo::from)
                            .orElse(null),
                    libraryPost.getPaperName(),
                    libraryPost.getYear(),
                    libraryPost.getTopic(),
                    libraryPost.getContent(),
                    libraryPost.getLink(),
                    Optional.ofNullable(libraryPost.getLibraryPostTags())
                            .orElse(List.of())
                            .stream()
                            .map(libraryPostTag -> libraryPostTag.getTag().getName())
                            .toList(),
                    0
            );
        }

        public GetOne withCommentCount(int count) {
            return new GetOne(
                    this.libraryPostId,
                    this.member,
                    this.paperName,
                    this.year,
                    this.topic,
                    this.content,
                    this.link,
                    this.tagNames,
                    count
            );
        }

    }
}

