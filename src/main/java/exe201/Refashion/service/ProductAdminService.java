package exe201.Refashion.service;

import exe201.Refashion.dto.request.ProductAdminRequest;
import exe201.Refashion.dto.request.ProductRequest;
import exe201.Refashion.dto.response.ProductAdminResponse;
import exe201.Refashion.dto.response.ProductResponse;
import exe201.Refashion.entity.*;
import exe201.Refashion.enums.ProductStatus;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.mapper.ProductMapper;
import exe201.Refashion.repository.CategoryRepository;
import exe201.Refashion.repository.ProductRepository;
import exe201.Refashion.repository.ReportsRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductAdminService {

    ProductRepository productRepository;
    ReportsRepository reportsRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    EmailService emailService;

    public ProductResponse createProductByAdmin(ProductRequest request) {
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
                .status(ProductStatus.APPROVED) // 👈 Thêm dòng này
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

    public List<ProductResponse> getAllProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .map(productMapper::toProductResponse)
                .toList();
    }

    public List<ProductResponse> getAllPendingProducts() {
        List<Products> pendingProducts = productRepository.findByStatus(ProductStatus.PENDING);
        return pendingProducts.stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse getProductById(String id) {
        Products product = productRepository.findWithImagesById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toProductResponse(product);
    }

    @Transactional
    public ProductAdminResponse reviewProduct(String productId, ProductAdminRequest request) {
        // Lấy sản phẩm từ DB
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Chỉ cho phép review nếu đang ở trạng thái PENDING
        if (product.getStatus() != ProductStatus.PENDING) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_REVIEWED);
        }

        // Kiểm tra trạng thái hợp lệ
        if (request.getStatus() != ProductStatus.APPROVED && request.getStatus() != ProductStatus.REJECTED) {
            throw new AppException(ErrorCode.INVALID_PRODUCT_STATUS);
        }

        // Cập nhật trạng thái sản phẩm
        product.setStatus(request.getStatus());

        // Cập nhật trạng thái các báo cáo liên quan (nếu có)
        List<Reports> pendingReports = reportsRepository.findPendingReportsByProductId(productId);
        for (Reports report : pendingReports) {
            report.setStatus(request.getStatus() == ProductStatus.APPROVED
                    ? "RESOLVED"
                    : "VIOLATION_CONFIRMED");
        }

        // Lưu lại thay đổi
        productRepository.save(product);
        reportsRepository.saveAll(pendingReports);

        // Gửi email nếu sản phẩm được duyệt
        if (request.getStatus() == ProductStatus.APPROVED) {
            String sellerEmail = product.getSeller().getEmail();
            String productName = product.getTitle();
            emailService.sendProductApprovalEmail(sellerEmail, productName);
        }

        // Trả về phản hồi cho admin
        return ProductAdminResponse.builder()
                .productId(product.getId())
                .status(product.getStatus().name())
                .message("Product has been " + product.getStatus().name().toLowerCase())
                .build();
    }

}