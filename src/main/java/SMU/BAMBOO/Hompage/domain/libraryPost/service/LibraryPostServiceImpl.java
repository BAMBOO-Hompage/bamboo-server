package SMU.BAMBOO.Hompage.domain.libraryPost.service;

import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostRequestDTO;
import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostResponseDTO;
import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import SMU.BAMBOO.Hompage.domain.libraryPost.repository.LibraryPostRepository;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import SMU.BAMBOO.Hompage.global.exception.CustomException;
import SMU.BAMBOO.Hompage.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LibraryPostServiceImpl implements LibraryPostService {

    private final LibraryPostRepository libraryPostRepository;

    private LibraryPost getLibraryPostById(Long id) {
        return libraryPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.LIBRARY_POST_NOT_EXIST));
    }

    @Override
    @Transactional
    public LibraryPostResponseDTO.Create create(LibraryPostRequestDTO.Create dto, Member member) {

        LibraryPost libraryPost = LibraryPost.from(dto, member);
        LibraryPost saveLibraryPost = libraryPostRepository.save(libraryPost);

        return LibraryPostResponseDTO.Create.from(saveLibraryPost);
    }

    @Override
    public LibraryPostResponseDTO.GetOne getById(Long id) {
        LibraryPost libraryPost = getLibraryPostById(id);
        return LibraryPostResponseDTO.GetOne.from(libraryPost);
    }

    @Override
    @Transactional
    public void update(Long id, LibraryPostRequestDTO.Update request) {
        LibraryPost libraryPost = getLibraryPostById(id);
        libraryPost.update(request);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getLibraryPostById(id);
        libraryPostRepository.deleteById(id);
    }
}
