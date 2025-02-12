package SMU.BAMBOO.Hompage.mock.container;

import SMU.BAMBOO.Hompage.domain.libraryPost.controller.LibraryPostController;
import SMU.BAMBOO.Hompage.domain.libraryPost.repository.LibraryPostRepository;
import SMU.BAMBOO.Hompage.domain.libraryPost.service.LibraryPostServiceImpl;
import SMU.BAMBOO.Hompage.domain.mainActivites.controller.MainActivitiesController;
import SMU.BAMBOO.Hompage.domain.mainActivites.repository.MainActivitiesRepository;
import SMU.BAMBOO.Hompage.domain.mainActivites.service.MainActivitiesServiceImpl;
import SMU.BAMBOO.Hompage.domain.notice.controller.NoticeController;
import SMU.BAMBOO.Hompage.domain.notice.repository.NoticeRepository;
import SMU.BAMBOO.Hompage.domain.notice.service.NoticeServiceImpl;
import SMU.BAMBOO.Hompage.domain.tag.controller.TagController;
import SMU.BAMBOO.Hompage.domain.tag.repository.TagRepository;
import SMU.BAMBOO.Hompage.domain.tag.service.TagServiceImpl;
import SMU.BAMBOO.Hompage.global.upload.service.AwsS3Service;
import SMU.BAMBOO.Hompage.mock.repository.FakeLibraryPostRepository;
import SMU.BAMBOO.Hompage.mock.repository.FakeMainActivitiesRepository;
import SMU.BAMBOO.Hompage.mock.repository.FakeNoticeRepository;
import SMU.BAMBOO.Hompage.mock.repository.FakeTagRepository;
import lombok.Builder;
import org.mockito.Mockito;

public class TestContainer {

    public final LibraryPostRepository libraryPostRepository;
    public final TagRepository tagRepository;
    public final MainActivitiesRepository mainActivitiesRepository;
    public final NoticeRepository noticeRepository;

    public final LibraryPostServiceImpl libraryPostService;
    public final TagServiceImpl tagService;
    public final MainActivitiesServiceImpl mainActivitiesService;
    public final NoticeServiceImpl noticeService;

    public final LibraryPostController libraryPostController;
    public final TagController tagController;
    public final MainActivitiesController mainActivitiesController;
    public final NoticeController noticeController;

    public final AwsS3Service awsS3Service;

    @Builder
    public TestContainer() {
        this.libraryPostRepository = new FakeLibraryPostRepository();
        this.tagRepository = new FakeTagRepository();
        this.mainActivitiesRepository = new FakeMainActivitiesRepository();
        this.noticeRepository = new FakeNoticeRepository();

        this.awsS3Service = Mockito.mock(AwsS3Service.class);

        this.libraryPostService = LibraryPostServiceImpl.builder()
                .libraryPostRepository(this.libraryPostRepository)
                .tagRepository(this.tagRepository)
                .build();
        this.tagService = TagServiceImpl.builder()
                .tagRepository(this.tagRepository)
                .build();
        this.mainActivitiesService = new MainActivitiesServiceImpl(this.mainActivitiesRepository, this.awsS3Service);
        this.noticeService = new NoticeServiceImpl(this.noticeRepository, this.awsS3Service);

        this.libraryPostController = LibraryPostController.builder()
                .libraryPostService(this.libraryPostService)
                .build();
        this.tagController = TagController.builder()
                .tagService(this.tagService)
                .build();
        this.mainActivitiesController = MainActivitiesController.builder()
                .mainActivitiesService(this.mainActivitiesService)
                .awsS3Service(this.awsS3Service)
                .build();
        this.noticeController = NoticeController.builder()
                .noticeService(this.noticeService)
                .build();
    }
}
