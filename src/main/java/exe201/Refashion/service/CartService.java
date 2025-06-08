package exe201.Refashion.service;

import exe201.Refashion.dto.request.AddToCartRequest;
import exe201.Refashion.dto.request.CartRequest;
import exe201.Refashion.dto.response.CartResponse;
import exe201.Refashion.entity.Cart;
import exe201.Refashion.entity.CartItems;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Users;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.mapper.CartMapper;
import exe201.Refashion.repository.CartItemsRepository;
import exe201.Refashion.repository.CartRepository;
import exe201.Refashion.repository.ProductRepository;
import exe201.Refashion.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    CartRepository cartRepository;
    UserRepository userRepository;
    CartMapper cartMapper;
    ProductRepository productRepository;
    CartItemsRepository cartItemsRepository;

//    @Transactional
//    public CartResponse createCart(CartRequest request) {
//        Users user = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//
//        if (cartRepository.existsByUser(user)) {
//            throw new AppException(ErrorCode.CART_ALREADY_EXISTS);
//        }
//
//        Cart cart = cartMapper.toEntity(request, user);
//        cart.setId(UUID.randomUUID().toString());
//        return cartMapper.toResponse(cartRepository.save(cart));
//    }
//
//    public CartResponse getCartById(String cartId) {
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
//        return cartMapper.toResponse(cart);
//    }
//
//    public CartResponse getCartByUserId(String userId) {
//        Users user = userRepository.findById(userId)
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//        Cart cart = cartRepository.findByUser(user)
//                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
//        return cartMapper.toResponse(cart);
//    }
//
//    public void deleteCart(String cartId) {
//        if (!cartRepository.existsById(cartId)) {
//            throw new AppException(ErrorCode.CART_NOT_FOUND);
//        }
//        cartRepository.deleteById(cartId);
//    }

    @Transactional
    public void addProductToCart(AddToCartRequest request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .id(UUID.randomUUID().toString())
                            .user(user)
                            .createdAt(LocalDateTime.now())
                            .build();
                    return cartRepository.save(newCart);
                });

        Optional<CartItems> existingItemOpt = cartItemsRepository.findByCartAndProduct(cart, product);

        if (existingItemOpt.isPresent()) {
            CartItems item = existingItemOpt.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemsRepository.save(item);
        } else {
            CartItems newItem = CartItems.builder()
                    .id(UUID.randomUUID().toString())
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .addedAt(LocalDateTime.now())
                    .build();
            cartItemsRepository.save(newItem);
        }
    }

    // Xem giỏ hàng
    public CartResponse getCartForUser(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        List<CartItems> items = cartItemsRepository.findByCart(cart);
        return cartMapper.toCartResponse(cart, items);
    }

    // Xoá sản phẩm khỏi giỏ hàng
    public void removeProductFromCart(String userId, String productId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        cartItemsRepository.deleteByCartAndProduct(cart, product);
    }
}
