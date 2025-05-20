package SMU.BAMBOO.Hompage.domain.mainActivites.service;

import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesRequestDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.dto.MainActivitiesResponseDTO;
import SMU.BAMBOO.Hompage.domain.mainActivites.entity.MainActivities;
import SMU.BAMBOO.Hompage.domain.mainActivites.repository.MainActivitiesRepository;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import SMU.BAMBOO.Hompage.global.upload.service.AwsS3Facade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainActivitiesServiceImpl implements MainActivitiesService {

    private final MainActivitiesRepository mainActivitiesRepository;
    private final AwsS3Facade awsS3Facade;

    @Override
    public MainActivitiesResponseDTO.Detail create(MainActivitiesRequestDTO.Create request, List<String> images, Member member) {

        if (images == null) {
            images = new ArrayList<>();
        }

        if (!"ROLE_ADMIN".equals(member.getRole().name()) && !"ROLE_OPS".equals(member.getRole().name())) {
            throw new CustomException(ErrorCode.USER_NO_PERMISSION);
        }

        MainActivities mainActivities = MainActivities.from(request, images);

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

        return MainActivitiesResponseDTO.Detail.from(mainActivity);

    }

    @Override
    @Transactional
    public void updateMainActivity(Long id, MainActivitiesRequestDTO.Update request, List<MultipartFile> newImages, Member member) {
        if (!"ROLE_ADMIN".equals(member.getRole().name()) && !"ROLE_OPS".equals(member.getRole().name())) {
            throw new CustomException(ErrorCode.USER_NO_PERMISSION);
        }

        MainActivities activity = mainActivitiesRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MAIN_ACTIVITIES_NOT_EXIST));

        List<String> finalImageUrls = new ArrayList<>();

        // 기존 유지할 이미지 URL
        if (request.getKeptImageUrls() != null) {
            finalImageUrls.addAll(request.getKeptImageUrls());
        }

        // 삭제 대상 이미지 삭제
        List<String> existingUrls = activity.getImages();
        List<String> toDelete = existingUrls.stream()
                .filter(url -> !finalImageUrls.contains(url))
                .toList();

        toDelete.forEach(url -> {
            try {
                awsS3Facade.deleteFile(url);
            } catch (Exception e) {
                throw new CustomException(ErrorCode.DELETE_FAILED);
            }
        });

        // 새 이미지 업로드
        if (newImages != null) {
            for (MultipartFile image : newImages) {
                try {
                    String uploadedUrl = awsS3Facade.uploadFile("main-activities", image, true);
                    finalImageUrls.add(uploadedUrl);
                } catch (Exception e) {
                    log.error("S3 업로드 중 예외 발생: {}", e.getMessage(), e);
                    throw new CustomException(ErrorCode.UPLOAD_FAILED);
                }
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
        imageUrls.forEach(awsS3Facade::deleteFile);

        mainActivitiesRepository.deleteById(id);
    }

}