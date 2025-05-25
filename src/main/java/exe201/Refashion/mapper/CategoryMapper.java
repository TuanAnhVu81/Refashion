package exe201.Refashion.mapper;

import exe201.Refashion.dto.request.CategoryRequest;
import exe201.Refashion.dto.response.CategoryResponse;
import exe201.Refashion.entity.Categories;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Categories toCategory(CategoryRequest request);
    CategoryResponse toCategoryResponse(Categories category);
}
