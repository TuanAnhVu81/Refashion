package exe201.Refashion.controller;

import exe201.Refashion.dto.request.WishlistRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.WishlistResponse;
import exe201.Refashion.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlists")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistController {

    WishlistService wishlistService;

    @PostMapping
    public ApiResponse<WishlistResponse> addToWishlist(@RequestBody WishlistRequest request) {
        return ApiResponse.<WishlistResponse>builder()
                .result(wishlistService.addToWishlist(request))
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<WishlistResponse>> getWishlistByUserId(@PathVariable String userId) {
        return ApiResponse.<List<WishlistResponse>>builder()
                .result(wishlistService.getWishlistByUserId(userId))
                .build();
    }

    @DeleteMapping("/{userId}/{productId}")
    public ApiResponse<String> removeFromWishlist(@PathVariable String userId, @PathVariable String productId) {
        wishlistService.removeFromWishlist(userId, productId);
        return ApiResponse.<String>builder()
                .result("Removed from wishlist successfully")
                .build();
    }
}