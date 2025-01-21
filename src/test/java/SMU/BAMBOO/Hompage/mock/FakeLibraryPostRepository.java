package SMU.BAMBOO.Hompage.mock;

import SMU.BAMBOO.Hompage.domain.libraryPost.entity.LibraryPost;
import SMU.BAMBOO.Hompage.domain.libraryPost.repository.LibraryPostRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeLibraryPostRepository implements LibraryPostRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<LibraryPost> data = Collections.synchronizedList(new ArrayList<>());

    @Override
    public Optional<LibraryPost> findById(Long id) {
        return data.stream()
                .filter(post -> post.getLibraryPostId().equals(id))
                .findAny();
    }

    @Override
    public Optional<LibraryPost> findByPaperName(String paperName) {
        return data.stream()
                .filter(post -> post.getPaperName().equals(paperName))
                .findAny();
    }

    @Override
    public LibraryPost save(LibraryPost libraryPost) {
        if (libraryPost.getLibraryPostId() == null || libraryPost.getLibraryPostId() == 0) {
            LibraryPost newLibraryPost = LibraryPost.builder()
                    .libraryPostId(autoGeneratedId.incrementAndGet())
                    .member(libraryPost.getMember())
                    .speaker(libraryPost.getSpeaker())
                    .paperName(libraryPost.getPaperName())
                    .year(libraryPost.getYear())
                    .topic(libraryPost.getTopic())
                    .link(libraryPost.getLink())
                    .libraryPostTags(libraryPost.getLibraryPostTags())
                    .build();

            data.add(newLibraryPost);
            return newLibraryPost;
        } else {
            deleteById(libraryPost.getLibraryPostId());
            data.add(libraryPost);
            return libraryPost;
        }
    }

    @Override
    public List<LibraryPost> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(post -> post.getLibraryPostId().equals(id));
    }
}
