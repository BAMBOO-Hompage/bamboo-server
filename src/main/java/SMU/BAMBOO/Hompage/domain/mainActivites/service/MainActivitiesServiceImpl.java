package SMU.BAMBOO.Hompage.domain.mainActivites.service;

import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesRequestDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesResponseDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.entity.MainActivities;
import SMU.BAMBOO.Hompage.domain.mainActivites.repository.MainActivitiesRepository;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.upload.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Hibernate;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainActivitiesServiceImpl implements MainActivitiesService {

    private final MainActivitiesRepository mainActivitiesRepository;
    private final AwsS3Service awsS3Service;

    @Override
    public MainActivitiesResponseDTO.Detail create(MainActivitiesRequestDTO.Create request, List<String> images, Member member) {

        if (images == null) {
            images = new ArrayList<>();
        }

        if (!"ROLE_ADMIN".equals(member.getRole().name()) && !"ROLE_OPS".equals(member.getRole().name())) {
            throw new CustomException(ErrorCode.USER_NO_PERMISSION);
        }

        MainActivities mainActivities = MainActivities.from(request, member, images);

        MainActivities savedMainActivities = mainActivitiesRepository.save(mainActivities);

        return MainActivitiesResponseDTO.Detail.from(savedMainActivities);
    }

    @Override
    public Page<MainActivitiesResponseDTO.ActivitiesByYearResponse> getMainActivitiesByYear(int year, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<MainActivities> activitiesPage = mainActivitiesRepository.findByYear(year, pageable);

        return activitiesPage.map(MainActivitiesResponseDTO.ActivitiesByYearResponse::from);
    }

    @Override
    public MainActivitiesResponseDTO.Detail getMainActivity(Long id){

        MainActivities mainActivity = mainActivitiesRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.MAIN_ACTIVITIES_NOT_EXIST));
        // Lazy Loading 초기화
        Hibernate.initialize(mainActivity.getMember());
        return MainActivitiesResponseDTO.Detail.from(mainActivity);

    }

    @Override
    @Transactional
    public void updateMainActivity(Long id, MainActivitiesRequestDTO.Update request, List<Object> images, Member member) {
        MainActivities activity = mainActivitiesRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MAIN_ACTIVITIES_NOT_EXIST));

        if (!"ROLE_ADMIN".equals(member.getRole().name()) && !"ROLE_OPS".equals(member.getRole().name())) {
            throw new CustomException(ErrorCode.USER_NO_PERMISSION);
        }

        List<String> finalImageUrls = new ArrayList<>();

        for (Object image : images) {
            if (image instanceof String url) { // 기존 이미지 URL이면 그대로 사용
                finalImageUrls.add(url);
            } else if (image instanceof MultipartFile file) { // 새 이미지 파일이면 업로드 후 URL 저장
                String uploadedUrl = awsS3Service.uploadFile("main-activities", file, true);
                finalImageUrls.add(uploadedUrl);
            }
        }

        activity.update(request, finalImageUrls);
    }

    @Override
    @Transactional
    public void deleteMainActivity(Long id, Member member){
        MainActivities activity = mainActivitiesRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.MAIN_ACTIVITIES_NOT_EXIST));

        if (!"ROLE_ADMIN".equals(member.getRole().name()) && !"ROLE_OPS".equals(member.getRole().name())) {
            throw new CustomException(ErrorCode.USER_NO_PERMISSION);
        }

        List<String> imageUrls = activity.getImages();
        imageUrls.forEach(awsS3Service::deleteFile);

        mainActivitiesRepository.deleteById(id);
    }
}