package exe201.Refashion.mapper;

import exe201.Refashion.dto.response.ProductResponse;
import exe201.Refashion.entity.Products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "seller.username", target = "sellerUsername")
    ProductResponse toProductResponse(Products product);
}
