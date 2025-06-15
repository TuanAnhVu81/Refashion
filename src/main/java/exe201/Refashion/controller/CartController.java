package exe201.Refashion.controller;

import exe201.Refashion.dto.request.AddToCartRequest;
import exe201.Refashion.dto.request.CartRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.CartResponse;
import exe201.Refashion.service.CartService;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

//    @PostMapping
//    public ApiResponse<CartResponse> createCart(@RequestBody @Valid CartRequest request) {
//        return ApiResponse.<CartResponse>builder()
//                .result(cartService.createCart(request))
//                .build();
//    }
//
//    @GetMapping("/{cartId}")
//    public ApiResponse<CartResponse> getCartById(@PathVariable String cartId) {
//        return ApiResponse.<CartResponse>builder()
//                .result(cartService.getCartById(cartId))
//                .build();
//    }
//
//    @GetMapping("/user/{userId}")
//    public ApiResponse<CartResponse> getCartByUserId(@PathVariable String userId) {
//        return ApiResponse.<CartResponse>builder()
//                .result(cartService.getCartByUserId(userId))
//                .build();
//    }
//
//    @DeleteMapping("/{cartId}")
//    public ApiResponse<String> deleteCart(@PathVariable String cartId) {
//        cartService.deleteCart(cartId);
//        return ApiResponse.<String>builder()
//                .result("Cart deleted successfully")
//                .build();
//    }

    //User
    @PostMapping("/add")
    public ApiResponse<String> addToCart(@RequestBody AddToCartRequest request) {
        cartService.addProductToCart(request);
        return ApiResponse.<String>builder()
                .result("Đã thêm vào giỏ hàng.")
                .build();
    }

    //User
    @GetMapping("/{userId}")
    public ApiResponse<CartResponse> getCart(@PathVariable String userId) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.getCartForUser(userId))
                .build();
    }

    //User
    @DeleteMapping("/{userId}/remove/{productId}")
    public ApiResponse<String> removeFromCart(@PathVariable String userId, @PathVariable String productId) {
        cartService.removeProductFromCart(userId, productId);
        return ApiResponse.<String>builder()
                .result("Đã xóa sản phẩm khỏi giỏ hàng.")
                .build();
    }
}
