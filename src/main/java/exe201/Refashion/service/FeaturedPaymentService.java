package exe201.Refashion.service;

import exe201.Refashion.dto.request.FeatureProductRequest;
import exe201.Refashion.dto.response.FeaturedPaymentResponse;
import exe201.Refashion.entity.FeaturedPayment;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Users;
import exe201.Refashion.enums.PaymentStatus;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeaturedPaymentService {

    FeaturedPaymentRepository featuredPaymentRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    FeaturedPaymentMapper featuredPaymentMapper;
    EmailService emailService;

    // User requests to feature a product
    @Transactional
    public FeaturedPaymentResponse requestFeatureProduct(FeatureProductRequest request) {
        // Validate user and product
        Users seller = userRepository.findById(request.getSellerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Check ownership
        if (!product.getSeller().getId().equals(request.getSellerId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Validate amount and duration
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.INVALID_PAYMENT_AMOUNT);
        }
        if (request.getDurationDays() <= 0) {
            throw new AppException(ErrorCode.INVALID_DURATION);
        }

        // Validate transfer proof image
        if (request.getTransferProofImageUrl() == null || request.getTransferProofImageUrl().trim().isEmpty()) {
            throw new AppException(ErrorCode.IMAGE_URL_REQUIRED);
        }

        // Create payment record with PENDING status
        FeaturedPayment payment = FeaturedPayment.builder()
                .seller(seller)
                .product(product)
                .amount(request.getAmount())
                .durationDays(request.getDurationDays())
                .transferProofImageUrl(request.getTransferProofImageUrl())
                .status(PaymentStatus.PENDING)
                .build();

        return featuredPaymentMapper.toFeaturedPaymentResponse(featuredPaymentRepository.save(payment));
    }

    // Admin confirms or rejects feature payment
    @Transactional
    public FeaturedPaymentResponse confirmFeaturePayment(String paymentId, String adminId, boolean isConfirmed) {
        // Validate admin
        Users admin = userRepository.findById(adminId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!admin.getRole().equals("ADMIN")) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        // Validate payment
        FeaturedPayment payment = featuredPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        // Check if payment is still pending
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
        }

        // Update payment status and product if confirmed
        if (isConfirmed) {
            payment.setStatus(PaymentStatus.PAID);
            Products product = payment.getProduct();
            product.setIsFeatured(true);
            product.setFeaturedUntil(LocalDateTime.now().plusDays(payment.getDurationDays()));
            productRepository.save(product);

            // Send confirmation email to seller
            try {
                emailService.sendFeaturePaymentConfirmation(payment.getSeller().getEmail(), payment.getProduct().getTitle(), payment.getAmount().toString(), payment.getDurationDays());
            } catch (Exception e) {
                // Log error but don't interrupt transaction
                System.err.println("Failed to send confirmation email: " + e.getMessage());
            }
        } else {
            payment.setStatus(PaymentStatus.UNPAID);

            // Send rejection email to seller
            try {
                emailService.sendFeaturePaymentRejection(payment.getSeller().getEmail(), payment.getProduct().getTitle());
            } catch (Exception e) {
                System.err.println("Failed to send rejection email: " + e.getMessage());
            }
        }

        return featuredPaymentMapper.toFeaturedPaymentResponse(featuredPaymentRepository.save(payment));
    }

    // Admin: Get all featured payments
    public List<FeaturedPaymentResponse> getAllFeaturedPayments(String adminId) {
        // Validate admin
        Users admin = userRepository.findById(adminId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!admin.getRole().equals("ADMIN")) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        return featuredPaymentRepository.findAll().stream()
                .map(featuredPaymentMapper::toFeaturedPaymentResponse)
                .collect(Collectors.toList());
    }

    // Admin: Get featured payment by ID
    public FeaturedPaymentResponse getFeaturedPaymentById(String paymentId, String adminId) {
        // Validate admin
        Users admin = userRepository.findById(adminId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!admin.getRole().equals("ADMIN")) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        FeaturedPayment payment = featuredPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        return featuredPaymentMapper.toFeaturedPaymentResponse(payment);
    }

    // Admin: Update featured payment
    @Transactional
    public FeaturedPaymentResponse updateFeaturedPayment(String paymentId, FeatureProductRequest request, String adminId) {
        // Validate admin
        Users admin = userRepository.findById(adminId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!admin.getRole().equals("ADMIN")) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        FeaturedPayment payment = featuredPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        // Validate user and product
        Users seller = userRepository.findById(request.getSellerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Check ownership
        if (!product.getSeller().getId().equals(request.getSellerId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Update payment details
        payment.setSeller(seller);
        payment.setProduct(product);
        payment.setAmount(request.getAmount());
        payment.setDurationDays(request.getDurationDays());
        if (request.getTransferProofImageUrl() != null && !request.getTransferProofImageUrl().trim().isEmpty()) {
            payment.setTransferProofImageUrl(request.getTransferProofImageUrl());
        }

        return featuredPaymentMapper.toFeaturedPaymentResponse(featuredPaymentRepository.save(payment));
    }

    // Admin: Delete featured payment
    @Transactional
    public void deleteFeaturedPayment(String paymentId, String adminId) {
        // Validate admin
        Users admin = userRepository.findById(adminId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!admin.getRole().equals("ADMIN")) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        FeaturedPayment payment = featuredPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        featuredPaymentRepository.delete(payment);
    }

    // Admin: Get pending featured payments
    public List<FeaturedPaymentResponse> getPendingFeaturedPayments(String adminId) {
        // Validate admin
        Users admin = userRepository.findById(adminId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!admin.getRole().equals("ADMIN")) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        return featuredPaymentRepository.findByStatus(PaymentStatus.PENDING).stream()
                .map(featuredPaymentMapper::toFeaturedPaymentResponse)
                .collect(Collectors.toList());
    }
}