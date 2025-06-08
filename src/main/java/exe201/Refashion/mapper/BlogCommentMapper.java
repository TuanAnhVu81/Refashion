package exe201.Refashion.mapper;

import exe201.Refashion.dto.response.BlogCommentResponse;
import exe201.Refashion.entity.BlogComment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BlogCommentMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "product.id", target = "productId")
    BlogCommentResponse toResponse(BlogComment comment);
}
