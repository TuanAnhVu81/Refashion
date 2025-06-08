package exe201.Refashion.repository;

import exe201.Refashion.entity.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogCommentRepository extends JpaRepository<BlogComment, String> {
    List<BlogComment> findByProductIdOrderByCreatedAtAsc(String productId);
}
