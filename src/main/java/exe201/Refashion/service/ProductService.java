package exe201.Refashion.service;

import exe201.Refashion.dto.request.ProductRequest;
import exe201.Refashion.dto.response.ProductResponse;
import exe201.Refashion.entity.*;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.mapper.ProductMapper;
import exe201.Refashion.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    ProductMapper productMapper;

//    String uploadDir = "E:\\SEMESTER 8\\EXE201\\BE\\uploads\\products\\";
    String uploadDir = System.getProperty("user.dir") + "/uploads/products/";

    public ProductResponse createProduct(ProductRequest request) {
        Categories category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Users seller = userRepository.findById(request.getSellerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Products product = Products.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .brand(request.getBrand())
                .productCondition(request.getProductCondition())
                .size(request.getSize())
                .color(request.getColor())
                .price(request.getPrice())
                .category(category)
                .seller(seller)
                .isFeatured(Boolean.TRUE.equals(request.getIsFeatured()))
                .featuredUntil(request.getFeaturedUntil())
                .isActive(Boolean.TRUE.equals(request.getIsActive()))
                .images(new ArrayList<>())
                .build();

        // Thêm ảnh từ URL (FE upload Cloudinary → gửi URL về)
        if (request.getImageUrls() != null) {
            for (String url : request.getImageUrls()) {
                ProductImages image = ProductImages.builder()
                        .imageUrl(url)
                        .product(product)
                        .build();
                product.getImages().add(image);
            }
        }

        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    public ProductResponse updateProduct(String id, ProductRequest request) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setProductCondition(request.getProductCondition());
        product.setSize(request.getSize());
        product.setColor(request.getColor());
        product.setPrice(request.getPrice());
        product.setIsActive(request.getIsActive());
        product.setIsFeatured(request.getIsFeatured());
        product.setFeaturedUntil(request.getFeaturedUntil());

        // Cập nhật ảnh mới
        if (request.getImageUrls() != null) {
            product.getImages().clear(); // Xóa ảnh cũ
            for (String url : request.getImageUrls()) {
                ProductImages image = ProductImages.builder()
                        .imageUrl(url)
                        .product(product)
                        .build();
                product.getImages().add(image);
            }
        }

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public List<ProductResponse> getAllProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse getProductById(String id) {
        Products product = productRepository.findWithImagesById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toProductResponse(product);
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        productRepository.deleteById(id);
    }

    public List<ProductResponse> searchAndSortProducts(String keyword, String sortBy, String sortDirection) {
        Set<String> validSortFields = Set.of("category.name", "price", "size", "color", "productCondition");
        if (sortBy == null || !validSortFields.contains(sortBy)) {
            sortBy = "title"; // Default to title if sortBy is invalid
        }

        return productRepository.findAllCustom(keyword, sortBy, sortDirection).stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    //Cho phép người bán xem danh sách sản phẩm của mình.
    public List<ProductResponse> getProductsBySeller(String sellerId) {
        // Kiểm tra xem seller có tồn tại không
        userRepository.findById(sellerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Lấy danh sách sản phẩm của seller
        List<Products> products = productRepository.findBySellerId(sellerId);

        // Ánh xạ sang ProductResponse
        return products.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
