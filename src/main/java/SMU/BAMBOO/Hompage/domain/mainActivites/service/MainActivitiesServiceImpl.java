package SMU.BAMBOO.Hompage.domain.mainActivites.service;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesRequestDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesResponseDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.entity.MainActivities;
import SMU.BAMBOO.Hompage.domain.mainActivites.repository.MainActivitiesRepository;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.upload.AwsS3Service;
import com.sun.tools.javac.Main;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainActivitiesServiceImpl implements MainActivitiesService {

    private final MainActivitiesRepository mainActivitiesRepository;
    private final AwsS3Service awsS3Service;

    @Override
    public MainActivitiesResponseDTO.Create create(MainActivitiesRequestDTO.Create request, List<String> images) {

        // Hardcoded member
        Member hardcodedMember = Member.builder()
                .memberId(1L)
                .email("test@example.com")
                .pw("password")
                .name("John Doe")
                .studentId("202312345")
                .major("Computer Science")
                .phone("01012345678")
                .role(Role.ROLE_USER)
                .build();

        MainActivities mainActivities = MainActivities.from(request, hardcodedMember, images);

        MainActivities savedMainActivities = mainActivitiesRepository.save(mainActivities);

        return MainActivitiesResponseDTO.Create.from(savedMainActivities);
    }

    @Override
    public Page<MainActivitiesResponseDTO.ActivitiesByYearResponse> getMainActivitiesByYear(int year, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<MainActivities> activitiesPage = mainActivitiesRepository.findByYear(year, pageable);

        return activitiesPage.map(MainActivitiesResponseDTO.ActivitiesByYearResponse::from);
    }

    @Override
    @Transactional
    public void updateMainActivity(Long id, MainActivitiesRequestDTO.Update request, List<String> images){
        MainActivities activity = mainActivitiesRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.MAIN_ACTIVITIES_NOT_EXIST));

        // 기존 이미지 삭제
        List<String> oldImages = activity.getImages();
        oldImages.forEach(awsS3Service::deleteFile);

        activity.update(request, images);
    }

    @Override
    @Transactional
    public void deleteMainActivity(Long id){
        MainActivities activity = mainActivitiesRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.MAIN_ACTIVITIES_NOT_EXIST));

        List<String> imageUrls = activity.getImages();
        imageUrls.forEach(awsS3Service::deleteFile);

        mainActivitiesRepository.deleteById(id);
    }
}