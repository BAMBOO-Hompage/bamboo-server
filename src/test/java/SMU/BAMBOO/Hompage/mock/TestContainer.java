package SMU.BAMBOO.Hompage.mock;

import SMU.BAMBOO.Hompage.domain.libraryPost.controller.LibraryPostController;
import SMU.BAMBOO.Hompage.domain.libraryPost.repository.LibraryPostRepository;
import SMU.BAMBOO.Hompage.domain.libraryPost.service.LibraryPostServiceImpl;
import SMU.BAMBOO.Hompage.domain.tag.controller.TagController;
import SMU.BAMBOO.Hompage.domain.tag.repository.TagRepository;
import SMU.BAMBOO.Hompage.domain.tag.service.TagServiceImpl;
import lombok.Builder;

public class TestContainer {

    public final LibraryPostRepository libraryPostRepository;
    public final TagRepository tagRepository;

    public final LibraryPostServiceImpl libraryPostService;
    public final TagServiceImpl tagService;

    public final LibraryPostController libraryPostController;
    public final TagController tagController;

    @Builder
    public TestContainer() {
        this.libraryPostRepository = new FakeLibraryPostRepository();
        this.tagRepository = new FakeTagRepository();

        this.libraryPostService = LibraryPostServiceImpl.builder()
                .libraryPostRepository(this.libraryPostRepository)
                .tagRepository(this.tagRepository)
                .build();
        this.tagService = TagServiceImpl.builder()
                .tagRepository(this.tagRepository)
                .build();

        this.libraryPostController = LibraryPostController.builder()
                .libraryPostService(this.libraryPostService)
                .build();
        this.tagController = TagController.builder()
                .tagService(this.tagService)
                .build();
    }
}
