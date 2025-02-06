package SMU.BAMBOO.Hompage.domain.notice.controller;

import SMU.BAMBOO.Hompage.domain.member.annotation.CurrentMember;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeRequestDTO;
import SMU.BAMBOO.Hompage.domain.notice.dto.NoticeResponseDTO;
import SMU.BAMBOO.Hompage.domain.notice.service.NoticeService;
import SMU.BAMBOO.Hompage.global.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "공지사항 게시판 API")
@RequestMapping("/api/notices")
public class NoticeController {


    private final NoticeService noticeService;

    /** 공지사항 게시판 게시물 생성*/
    @RequestBody(content = @Content(
            encoding = @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE))) // request 내부에 Content Type 이 없으면 오류남. 그래서 application/json 설정
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "공지사항 게시판 게시물 생성")
    public SuccessResponse<NoticeResponseDTO.Detail> createNotice(
            @Valid @RequestPart(value = "request")NoticeRequestDTO.Create request,
            @RequestPart(required = false) List<MultipartFile> images,
            @RequestPart(required = false) List<MultipartFile> files,
            @CurrentMember Member member){

            // 빈 문자열로 들어온 경우 null 처리
            if (images != null && images.size() == 1 && images.get(0).isEmpty()) {
                  images = null;
            }
            if (files != null && files.size() == 1 && files.get(0).isEmpty()) {
                 files = null;
            }
            NoticeResponseDTO.Detail response = noticeService.create(request, member, images, files);

            return SuccessResponse.ok(response);
    }


    /** 공지사항 게시판 목록 조회 (전체, Type별) */


    /** 공지사항 게시판 게시물 단일 조회 */


    /** 공지사항 게시판 게시물 삭제 */


    /** 공지사항 게시판 게시물 수정 */



}
