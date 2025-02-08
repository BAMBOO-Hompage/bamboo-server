package SMU.BAMBOO.Hompage.domain.weeklyContent.service;

import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.repository.MemberRepository;
import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.domain.subject.repository.SubjectRepository;
import SMU.BAMBOO.Hompage.domain.weeklyContent.dto.WeeklyContentRequestDTO;
import SMU.BAMBOO.Hompage.domain.weeklyContent.dto.WeeklyContentResponseDTO;
import SMU.BAMBOO.Hompage.domain.weeklyContent.entity.WeeklyContent;
import SMU.BAMBOO.Hompage.domain.weeklyContent.repository.WeeklyContentRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WeeklyContentServiceImpl implements WeeklyContentService {

    private final WeeklyContentRepository weeklyContentRepository;
    private final SubjectRepository subjectRepository;
    private final MemberRepository memberRepository;

    /**
     * 주차별 내용 생성
     */
    @Override
    @Transactional
    public WeeklyContentResponseDTO.Create create(Long memberId, Long subjectId, WeeklyContentRequestDTO.Create request) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_EXIST));

        memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

        // 같은 과목의 같은 주차에 WeeklyContent가 존재하는지 검증
        weeklyContentRepository.findBySubjectAndWeek(subject, request.week())
                .ifPresent(existingContent -> {
                    throw new CustomException(ErrorCode.WEEKLY_CONTENT_ALREADY_EXISTS); // 중복 예외 발생
                });

        WeeklyContent weeklyContent = WeeklyContent.builder()
                .subject(subject)
                .content(request.content())
                .week(request.week())
                .build();

        // 연관관계 설정
        subject.addWeeklyContent(weeklyContent);

        WeeklyContent savedWeeklyContent = weeklyContentRepository.save(weeklyContent);
        return WeeklyContentResponseDTO.Create.from(savedWeeklyContent);
    }

    /**
     * ID로 주차별 내용 단일 조회
     */
    @Override
    public WeeklyContentResponseDTO.GetOne getById(Long id) {
        WeeklyContent weeklyContent = weeklyContentRepository.getById(id);
        return WeeklyContentResponseDTO.GetOne.from(weeklyContent);
    }

    /**
     * 과목에 속한 주차별 내용 조회
     */
    @Override
    public List<WeeklyContentResponseDTO.GetOne> getWeeklyContentBySubjectId(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_EXIST));
        List<WeeklyContent> weeklyContents = weeklyContentRepository.findBySubject(subject);

        return weeklyContents.stream()
                .map(WeeklyContentResponseDTO.GetOne::from)
                .toList();
    }

    /**
     * 주차별 내용 수정
     */
    @Override
    @Transactional
    public WeeklyContentResponseDTO.Update update(Long id, Long subjectId, WeeklyContentRequestDTO.Update request) {
        subjectRepository.findById(subjectId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_EXIST));
        WeeklyContent weeklyContent = weeklyContentRepository.getById(id);
        weeklyContent.update(request);

        return WeeklyContentResponseDTO.Update.from(weeklyContent);
    }

    /**
     * 주차별 내용 삭제
     */
    @Override
    @Transactional
    public void delete(Long id, Long subjectId) {
        subjectRepository.findById(subjectId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_EXIST));
        weeklyContentRepository.getById(id);
        weeklyContentRepository.delete(id);
    }
}
