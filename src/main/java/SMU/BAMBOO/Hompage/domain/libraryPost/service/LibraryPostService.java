package SMU.BAMBOO.Hompage.domain.libraryPost.service;

import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostRequestDTO;
import SMU.BAMBOO.Hompage.domain.libraryPost.dto.LibraryPostResponseDTO;
import SMU.BAMBOO.Hompage.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface LibraryPostService {

    LibraryPostResponseDTO.Create create(LibraryPostRequestDTO.Create dto, Member member, MultipartFile file);
    LibraryPostResponseDTO.GetOne getById(Long id);
    Page<LibraryPostResponseDTO.GetOne> getLibraryPosts(String tab, String keyword, int page, int size);
    void update(Long id, LibraryPostRequestDTO.Update dto, MultipartFile file);
    void delete(Long id);
    LibraryPostResponseDTO.GetOne addTags(Long libraryPostId, LibraryPostRequestDTO.ResetTag dto);
    LibraryPostResponseDTO.GetOne resetTags(Long libraryPostId, LibraryPostRequestDTO.ResetTag dto);
    LibraryPostResponseDTO.GetOne getLibraryPostWithCommentCount(Long postId);
}
