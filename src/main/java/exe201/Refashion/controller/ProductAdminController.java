package exe201.Refashion.controller;

import exe201.Refashion.dto.request.ProductAdminRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.ProductAdminResponse;
import exe201.Refashion.service.ProductAdminService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProductAdminController {

    ProductAdminService productAdminService;

    @PutMapping("/{productId}/review")
    public ApiResponse<ProductAdminResponse> reviewProduct(
            @PathVariable String productId,
            @RequestBody ProductAdminRequest request) {
        ProductAdminResponse response = productAdminService.reviewProduct(productId, request);
        return ApiResponse.<ProductAdminResponse>builder()
                .result(response)
                .build();
    }
}