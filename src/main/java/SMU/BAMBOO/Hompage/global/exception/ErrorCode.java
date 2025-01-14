package SMU.BAMBOO.Hompage.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 에러코드 규약
     * HTTP Status Code는 에러에 가장 유사한 코드를 부여한다.
     * 사용자정의 에러코드는 중복되지 않게 배정한다.
     * 사용자정의 에러코드는 각 카테고리 이름과 숫자를 조합하여 명확성을 더한다.
     */

    /**
     * 400 : 잘못된 요청
     * 401 : 인증되지 않은 요청
     * 403 : 권한의 문제가 있을때
     * 404 : 요청한 리소스가 존재하지 않음
     * 409 : 현재 데이터와 값이 충돌날 때(ex. 아이디 중복)
     * 412 : 파라미터 값이 뭔가 누락됐거나 잘못 왔을 때
     * 422 : 파라미터 문법 오류
     * 424 : 뭔가 단계가 꼬였을때, 1번안하고 2번하고 그런경우
     */

    // Common
    SERVER_UNTRACKED_ERROR("COMMON500", "미등록 서버 에러입니다. 서버 팀에 연락주세요.",HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST("COMMON400", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("COMMON401", "인증되지 않은 요청입니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("COMMON403", "권한이 부족합니다.", HttpStatus.FORBIDDEN),
    OBJECT_NOT_FOUND("COMMON404", "조회된 객체가 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_PARAMETER("COMMON422", "잘못된 파라미터입니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    PARAMETER_VALIDATION_ERROR("COMMON422", "파라미터 검증 에러입니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    PARAMETER_GRAMMAR_ERROR("COMMON422", "파라미터 문법 에러입니다.", HttpStatus.UNPROCESSABLE_ENTITY),

    // Token
    ACCESS_TOKEN_EXPIRED("TOKEN401", "Access Token 이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_INVALID("TOKEN401", "유효하지 않은 Access Token 입니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND("TOKEN404", "해당 사용자에 대한 Refresh Token 을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    REFRESH_TOKEN_MISMATCH("TOKEN401", "Refresh Token 이 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED("TOKEN401", "Refresh Token 이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID("TOKEN401", "유효하지 않은 Refresh Token 입니다.", HttpStatus.UNAUTHORIZED),

    // User (회원)
    USER_ALREADY_EXIST("USER400", "이미 회원가입된 유저입니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST("USER404", "존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND),
    USER_WRONG_PASSWORD("USER401", "비밀번호가 틀렸습니다.", HttpStatus.UNAUTHORIZED),

    // Study (스터디)
    STUDY_ALREADY_EXIST("STUDY400", "이미 존재하는 스터디입니다.", HttpStatus.BAD_REQUEST),
    STUDY_NOT_EXIST("STUDY404", "존재하지 않는 스터디입니다.", HttpStatus.NOT_FOUND),

    // Inventory (스터디 정리본)
    INVENTORY_ALREADY_EXIST("INVENTORY400", "이미 스터디 정리본이 존재합니다.", HttpStatus.BAD_REQUEST),
    INVENTORY_NOT_EXIST("INVENTORY404", "존재하지 않는 스터디 정리본입니다.", HttpStatus.NOT_FOUND),

    // Award (명예의 전당)
    AWARD_ALREADY_EXIST("AWARD400", "이미 명예의 전당에 존재합니다.", HttpStatus.BAD_REQUEST),
    AWARD_NOT_EXIST("AWARD404", "명예의 전당에 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    // Subject (과목)
    SUBJECT_ALREADY_EXIST("SUBJECT400", "이미 존재하는 과목입니다.", HttpStatus.BAD_REQUEST),
    SUBJECT_NOT_EXIST("SUBJECT404", "존재하지 않는 과목입니다.", HttpStatus.NOT_FOUND),

    // WeeklyContent (주차별 내용)
    WEEKLY_CONTENT_NOT_EXIST("WEEKLY_CONTENT404", "존재하지 않는 주차별 내용입니다.", HttpStatus.NOT_FOUND),

    // Notice (공지사항)
    NOTICE_NOT_EXIST("NOTICE404", "존재하지 않는 공지글입니다.", HttpStatus.NOT_FOUND),

    // NoticeComment (공지사항 댓글)
    NOTICE_COMMENT_NOT_EXIST("NOTICE_COMMENT404", "존재하지 않는 공지 댓글입니다.", HttpStatus.NOT_FOUND),

    // Knowledge (지식 공유)
    KNOWLEDGE_NOT_EXIST("KNOWLEDGE404", "존재하지 않는 지식 공유글입니다.", HttpStatus.NOT_FOUND),

    // Knowledge_Comment (지식 공유 게시판 댓글)
    KNOWLEDGE_COMMENT_NOT_EXIST("KNOWLEDGE_COMMENT404", "존재하지 않는 공지 공유 게시판 댓글입니다.", HttpStatus.NOT_FOUND),

    // Main_Activities (주요 활동)
    MAIN_ACTIVITIES_NOT_EXIST("MAIN_ACTIVITIES404", "존재하지 않는 주요 활동입니다.", HttpStatus.NOT_FOUND),

    // Library_Post (알렉산드리아 글)
    LIBRARY_POST_NOT_EXIST("LIBRARY_POST404", "알렉산드리아 도서관에 존재하지 않는 글입니다.", HttpStatus.NOT_FOUND),

    // TAG (태그)
    TAG_ALREADY_EXIST("TAG400", "이미 존재하는 태그입니다.", HttpStatus.BAD_REQUEST),
    TAG_NOT_EXIST("TAG404", "존재하지 않는 태그입니다.", HttpStatus.NOT_FOUND);


    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
