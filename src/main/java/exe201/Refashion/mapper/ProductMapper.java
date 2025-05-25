package exe201.Refashion.mapper;

import exe201.Refashion.dto.response.ProductResponse;
import exe201.Refashion.entity.Products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "seller.username", target = "sellerUsername")
    @Mapping(source = "isFeatured", target = "isFeatured")
    @Mapping(source = "featuredUntil", target = "featuredUntil")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "product.id", target = "id")
    @Mapping(source = "product.title", target = "title")
    @Mapping(source = "product.description", target = "description")
    @Mapping(source = "product.brand", target = "brand")
    @Mapping(source = "product.productCondition", target = "productCondition")
    @Mapping(source = "product.size", target = "size")
    @Mapping(source = "product.color", target = "color")
    @Mapping(source = "product.price", target = "price")
    ProductResponse toProductResponse(Products product);

//    @Named("mapImageUrls")
//    default List<String> mapImageUrls(Products product) {
//        return product.getImages() != null ? product.getImages().stream()
//                .map(image -> image.getImageUrl())
//                .collect(Collectors.toList()) : new ArrayList<>();
//    }
}