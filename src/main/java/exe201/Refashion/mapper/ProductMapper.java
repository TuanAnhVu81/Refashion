package exe201.Refashion.mapper;

import exe201.Refashion.dto.response.ProductResponse;
import exe201.Refashion.entity.ProductImages;
import exe201.Refashion.entity.Products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
//import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "seller.username", target = "sellerUsername")
    @Mapping(source = "isFeatured", target = "isFeatured")
    @Mapping(source = "featuredUntil", target = "featuredUntil")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "productCondition", target = "productCondition")
    @Mapping(source = "size", target = "size")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "price", target = "price")
    @Mapping(target = "imageUrls", expression = "java(mapImageUrls(product))") // ✅ map đúng sang DTO
    @Mapping(source = "status", target = "status")
    ProductResponse toProductResponse(Products product);

    default List<String> mapImageUrls(Products product) {
        if (product.getImages() == null) return new ArrayList<>();
        return product.getImages()
                .stream()
                .map(ProductImages::getImageUrl)
                .collect(Collectors.toList());
    }
}
