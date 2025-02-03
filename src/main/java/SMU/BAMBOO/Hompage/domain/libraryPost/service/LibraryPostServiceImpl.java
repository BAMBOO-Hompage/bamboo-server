package SMU.BAMBOO.Hompage.domain.libraryPost.service;

import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostRequestDTO;
import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostResponseDTO;
import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import SMU.BAMBOO.Hompage.domain.libraryPost.repository.LibraryPostRepository;
import SMU.BAMBOO.Hompage.domain.mapping.LibraryPostTag;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.domain.tag.entity.Tag;
import SMU.BAMBOO.Hompage.domain.tag.repository.TagRepository;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Builder
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LibraryPostServiceImpl implements LibraryPostService {

    private final LibraryPostRepository libraryPostRepository;
    private final TagRepository tagRepository;

    private LibraryPost getLibraryPostById(Long id) {
        return libraryPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.LIBRARY_POST_NOT_EXIST));
    }

    @Override
    @Transactional
    public LibraryPostResponseDTO.Create create(LibraryPostRequestDTO.Create dto, Member member) {

        // 객체 생성
        LibraryPost libraryPost = LibraryPost.builder()
                .member(member)
                .speaker(member.getName())
                .paperName(dto.paperName())
                .year(dto.year())
                .topic(dto.topic())
                .content(dto.content())
                .link(dto.link())
                .build();

        // 태그 추가
        dto.tagNames().forEach(tagName -> {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_EXIST));

            LibraryPostTag libraryPostTag = LibraryPostTag.builder()
                    .tag(tag)
                    .libraryPost(libraryPost)
                    .build();

            libraryPost.addLibraryPostTag(libraryPostTag);
        });

        // 저장
        LibraryPost savedLibraryPost = libraryPostRepository.save(libraryPost);
        return LibraryPostResponseDTO.Create.from(savedLibraryPost);
    }

    @Override
    public LibraryPostResponseDTO.GetOne getById(Long id) {
        LibraryPost libraryPost = getLibraryPostById(id);
        return LibraryPostResponseDTO.GetOne.from(libraryPost);
    }

    @Override
    public Page<LibraryPostResponseDTO.GetOne> getLibraryPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return libraryPostRepository.findByPage(pageable)
                .map(LibraryPostResponseDTO.GetOne::from);
    }

    @Override
    @Transactional
    public void update(Long id, LibraryPostRequestDTO.Update request) {
        LibraryPost libraryPost = getLibraryPostById(id);

        // 태그 이름을 기반으로 Tag 객체 리스트 생성
        List<Tag> tags = request.tagNames().stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_EXIST)))
                .toList();

        libraryPost.setTags(tags);
        libraryPost.updateBasicFields(request);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getLibraryPostById(id);
        libraryPostRepository.deleteById(id);
    }

    /**
     * 태그를 변경하는 방식이 확정되면 수정 or 사용       //FIXME
     */
    @Override
    @Transactional
    public LibraryPostResponseDTO.GetOne addTags(Long libraryPostId, LibraryPostRequestDTO.ResetTag request) {
        LibraryPost libraryPost = getLibraryPostById(libraryPostId);

        // 태그 조회
        List<Tag> tags = request.tagNames().stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_EXIST)))
                .toList();

        // 태그 추가
        libraryPost.addTags(tags);

        return LibraryPostResponseDTO.GetOne.from(libraryPost);
    }

    @Override
    @Transactional
    public LibraryPostResponseDTO.GetOne resetTags(Long libraryPostId, LibraryPostRequestDTO.ResetTag request) {
        LibraryPost libraryPost = getLibraryPostById(libraryPostId);

        // 태그 조회
        List<Tag> tags = request.tagNames().stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_EXIST)))
                .toList();

        // 태그 리스트 초기화 및 설정
        libraryPost.setTags(tags);

        return LibraryPostResponseDTO.GetOne.from(libraryPost);
    }
}
