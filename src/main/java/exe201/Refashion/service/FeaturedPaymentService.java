package exe201.Refashion.service;

import exe201.Refashion.dto.request.FeatureProductRequest;
import exe201.Refashion.dto.response.FeaturedPaymentResponse;
import exe201.Refashion.entity.FeaturedPayment;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Users;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.mapper.FeaturedPaymentMapper;
import exe201.Refashion.repository.FeaturedPaymentRepository;
import exe201.Refashion.repository.ProductRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeaturedPaymentService {

    FeaturedPaymentRepository featuredPaymentRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    FeaturedPaymentMapper featuredPaymentMapper;

    @Transactional
    public FeaturedPaymentResponse featureProduct(FeatureProductRequest request) {
        // Kiểm tra user và product
        Users seller = userRepository.findById(request.getSellerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Kiểm tra quyền sở hữu
        if (!product.getSeller().getId().equals(request.getSellerId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Giả lập kiểm tra thanh toán (thực tế cần tích hợp cổng thanh toán)
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.INVALID_PAYMENT_AMOUNT);
        }

        // Cập nhật sản phẩm thành nổi bật
        product.setIsFeatured(true);
        LocalDateTime featuredUntil = LocalDateTime.now().plusDays(request.getDurationDays());
        product.setFeaturedUntil(featuredUntil);
        productRepository.save(product);

        // Lưu giao dịch thanh toán
        FeaturedPayment payment = FeaturedPayment.builder()
                .seller(seller)
                .product(product)
                .amount(request.getAmount())
                .durationDays(request.getDurationDays())
                .build();

        return featuredPaymentMapper.toFeaturedPaymentResponse(featuredPaymentRepository.save(payment));
    }
}