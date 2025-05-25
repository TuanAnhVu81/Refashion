package exe201.Refashion.service;

import exe201.Refashion.dto.request.WishlistRequest;
import exe201.Refashion.dto.response.WishlistResponse;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Users;
import exe201.Refashion.entity.Wishlists;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.mapper.WishlistMapper;
import exe201.Refashion.repository.ProductRepository;
import exe201.Refashion.repository.UserRepository;
import exe201.Refashion.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistService {

    WishlistRepository wishlistRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    WishlistMapper wishlistMapper;

    @Transactional
    public WishlistResponse addToWishlist(WishlistRequest request) {
        // Kiểm tra user và product tồn tại
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Kiểm tra xem sản phẩm đã trong wishlist chưa
        wishlistRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId())
                .ifPresent(w -> { throw new AppException(ErrorCode.PRODUCT_ALREADY_IN_WISHLIST); });

        // Tạo mới wishlist
        Wishlists wishlist = Wishlists.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .product(product)
                .createdAt(LocalDateTime.now())
                .build();

        return wishlistMapper.toWishlistResponse(wishlistRepository.save(wishlist));
    }

    public List<WishlistResponse> getWishlistByUserId(String userId) {
        List<Wishlists> wishlists = wishlistRepository.findByUserId(userId);
        return wishlists.stream()
                .map(wishlistMapper::toWishlistResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFromWishlist(String userId, String productId) {
        Wishlists wishlist = wishlistRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new AppException(ErrorCode.WISHLIST_NOT_FOUND));
        wishlistRepository.delete(wishlist);
    }
}