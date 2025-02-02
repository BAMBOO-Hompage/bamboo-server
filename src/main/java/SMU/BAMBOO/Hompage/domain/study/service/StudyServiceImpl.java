package SMU.BAMBOO.Hompage.domain.study.service;

import SMU.BAMBOO.Hompage.domain.mapping.MemberStudy;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.member.repository.MemberRepository;
import SMU.BAMBOO.Hompage.domain.study.dto.StudyRequestDTO;
import SMU.BAMBOO.Hompage.domain.study.dto.StudyResponseDTO;
import SMU.BAMBOO.Hompage.domain.study.entity.Study;
import SMU.BAMBOO.Hompage.domain.study.repository.StudyRepository;
import SMU.BAMBOO.Hompage.domain.subject.entity.Subject;
import SMU.BAMBOO.Hompage.domain.subject.repository.SubjectRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final StudyRepository studyRepository;
    private final SubjectRepository subjectRepository;
    private final MemberRepository memberRepository;

    private Study getStudyById(Long id) {
        return studyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_EXIST));
    }

    private List<Member> getMembersFromIds(List<String> studentIds) {
        return memberRepository.findAllByStudentId(studentIds);
    }

    @Override
    @Transactional
    public StudyResponseDTO.Create create(StudyRequestDTO.Create dto) {

        Subject subject = subjectRepository.findById(dto.subjectId())
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_EXIST));

        Member studyMaster = memberRepository.findByStudentId(dto.studyMaster())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

        // 객체 생성
        Study study = Study.builder()
                .subject(subject)
                .cohort(dto.cohort())
                .isBook(dto.isBook())
                .section(dto.section())
                .studyMaster(dto.studyMaster())
                .build();

        // 스터디에 속한 회원들 조회/저장
        List<Member> members = getMembersFromIds(dto.studyMembers());
        members.add(studyMaster);

        // 회원들 스터디에 추가
        members.forEach(member -> {
            MemberStudy memberStudy = MemberStudy.builder()
                    .member(member)
                    .study(study)
                    .build();

            study.addMemberStudy(memberStudy);
            member.addMemberStudy(memberStudy);
        });

        // 저장
        Study savedStudy = studyRepository.save(study);
        return StudyResponseDTO.Create.from(savedStudy);
    }

    @Override
    public StudyResponseDTO.GetOne getById(Long id) {
        Study study = getStudyById(id);
        return StudyResponseDTO.GetOne.from(study);
    }

    @Override
    public List<StudyResponseDTO.GetOne> findAll() {
        List<Study> studies = studyRepository.findAll();

        return studies.stream()
                .map(StudyResponseDTO.GetOne::from)
                .toList();
    }

    @Override
    @Transactional
    public StudyResponseDTO.Update update(Long studyId, StudyRequestDTO.Update dto) {
        Study study = getStudyById(studyId);

        Subject subject = subjectRepository.findById(dto.subjectId())
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_EXIST));

        Member studyMaster = memberRepository.findByStudentId(dto.studyMaster())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

        // 기존 MemberStudy 데이터 제거 (NPE 방지)
        study.getMemberStudies().clear();

        // 새롭게 스터디에 속한 회원들 조회/저장
        List<Member> studyMembers = getMembersFromIds(dto.studyMembers());
        studyMembers.add(studyMaster);

        // 새로운 MemberStudy 리스트 생성
        List<MemberStudy> updatedMemberStudies = studyMembers.stream()
                .map(member -> MemberStudy.builder()
                        .member(member)
                        .study(study)
                        .build())
                .toList();

        study.updateStudy(subject, dto.cohort(), dto.isBook(), dto.section(), dto.studyMaster(), updatedMemberStudies);
        return StudyResponseDTO.Update.from(study);
    }

    @Override
    @Transactional
    public void delete(Long studyId) {
        getById(studyId);
        studyRepository.deleteById(studyId);
    }
}
