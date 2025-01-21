package SMU.BAMBOO.Hompage.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private Exception originException;
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 넣지 않으면 상위 객체인 Throwable 클래스에서 message가 null로 초기화 - CustomExcepton은 getter가 있어서 괜찮지만 ErrorCode에 직접 접근할 때 null 반환
        this.errorCode = errorCode;
    }

    public CustomException(Exception originException, ErrorCode errorCode) {
        this.originException = originException;
        this.errorCode = errorCode;
    }
}