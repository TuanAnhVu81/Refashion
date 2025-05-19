package exe201.Refashion.mapper;

import exe201.Refashion.dto.request.ProductRequest;
import exe201.Refashion.dto.response.ProductResponse;
import exe201.Refashion.entity.Products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seller", ignore = true) // set thủ công trong service
    @Mapping(target = "category", ignore = true) // set thủ công trong service
    // Chuyển String price sang BigDecimal đúng cú pháp
    @Mapping(target = "price", expression = "java(convertToBigDecimal(request.getPrice()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    Products toEntity(ProductRequest request);

    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(target = "imageUrls", ignore = true) // xử lý trong service
    ProductResponse toResponse(Products product);

    // helper method map danh sách image entity sang danh sách URL
    default List<String> mapImageUrls(List<exe201.Refashion.entity.ProductImages> images) {
        if (images == null) return null;
        return images.stream().map(exe201.Refashion.entity.ProductImages::getImageUrl).collect(Collectors.toList());
    }

    // helper method chuyển String sang BigDecimal
    default BigDecimal convertToBigDecimal(String price) {
        if (price == null || price.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(price);
    }
}
