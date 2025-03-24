package SMU.BAMBOO.Hompage.mock.container;

import SMU.BAMBOO.Hompage.domain.knowledge.controller.KnowledgeController;
import SMU.BAMBOO.Hompage.domain.knowledge.repository.KnowledgeRepository;
import SMU.BAMBOO.Hompage.domain.knowledge.service.KnowledgeServiceImpl;
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
import SMU.BAMBOO.Hompage.global.upload.service.AwsS3Facade;
import SMU.BAMBOO.Hompage.mock.repository.*;
import lombok.Builder;
import org.mockito.Mockito;

public class TestContainer {

    public final LibraryPostRepository libraryPostRepository;
    public final TagRepository tagRepository;
    public final MainActivitiesRepository mainActivitiesRepository;
    public final NoticeRepository noticeRepository;
    public final KnowledgeRepository knowledgeRepository;

    public final LibraryPostServiceImpl libraryPostService;
    public final TagServiceImpl tagService;
    public final MainActivitiesServiceImpl mainActivitiesService;
    public final NoticeServiceImpl noticeService;
    public final KnowledgeServiceImpl knowledgeService;

    public final LibraryPostController libraryPostController;
    public final TagController tagController;
    public final MainActivitiesController mainActivitiesController;
    public final NoticeController noticeController;
    public final KnowledgeController knowledgeController;

    public final AwsS3Facade awsS3Facade;

    @Builder
    public TestContainer() {
        this.libraryPostRepository = new FakeLibraryPostRepository();
        this.tagRepository = new FakeTagRepository();
        this.mainActivitiesRepository = new FakeMainActivitiesRepository();
        this.noticeRepository = new FakeNoticeRepository();
        this.knowledgeRepository = new FakeKnowledgeRepository();

        this.awsS3Facade = Mockito.mock(AwsS3Facade.class);

        this.libraryPostService = LibraryPostServiceImpl.builder()
                .libraryPostRepository(this.libraryPostRepository)
                .tagRepository(this.tagRepository)
                .build();
        this.tagService = TagServiceImpl.builder()
                .tagRepository(this.tagRepository)
                .build();
        this.mainActivitiesService = new MainActivitiesServiceImpl(this.mainActivitiesRepository, this.awsS3Facade);
        this.noticeService = new NoticeServiceImpl(this.noticeRepository, this.awsS3Facade);
        this.knowledgeService = new KnowledgeServiceImpl(this.knowledgeRepository, this.awsS3Facade);

        this.libraryPostController = LibraryPostController.builder()
                .libraryPostService(this.libraryPostService)
                .build();
        this.tagController = TagController.builder()
                .tagService(this.tagService)
                .build();
        this.mainActivitiesController = MainActivitiesController.builder()
                .mainActivitiesService(this.mainActivitiesService)
                .awsS3Facade(this.awsS3Facade)
                .build();
        this.noticeController = NoticeController.builder()
                .noticeService(this.noticeService)
                .build();
        this.knowledgeController = KnowledgeController.builder()
                .knowledgeService(this.knowledgeService)
                .build();
    }
}
