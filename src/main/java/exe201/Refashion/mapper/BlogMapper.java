package exe201.Refashion.mapper;

import exe201.Refashion.dto.response.BlogResponse;
import exe201.Refashion.dto.response.ProductResponse;
import exe201.Refashion.entity.Blog;
import exe201.Refashion.entity.Products;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface BlogMapper {

    @Mapping(source = "author.username", target = "authorUsername")
    @Mapping(source = "products", target = "products")
    BlogResponse toBlogResponse(Blog blog);

    List<BlogResponse> toBlogResponses(List<Blog> blogs);
}
