package exe201.Refashion.controller;

import exe201.Refashion.dto.request.ProductAdminRequest;
import exe201.Refashion.dto.request.ProductRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.ProductAdminResponse;
import exe201.Refashion.dto.response.ProductResponse;
import exe201.Refashion.service.ProductAdminService;
import exe201.Refashion.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProductAdminController {

    ProductAdminService productAdminService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ProductResponse> createProduct(
            @RequestBody @Valid ProductRequest request
    ) {
        return ApiResponse.<ProductResponse>builder()
                .result(productAdminService.createProductByAdmin(request))
                .build();
    }
    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productAdminService.getAllProducts())
                .build();
    }
    // ✅ Duyệt sản phẩm
    @PutMapping("/{productId}/review")
    public ApiResponse<ProductAdminResponse> reviewProduct(
            @PathVariable String productId,
            @RequestBody ProductAdminRequest request) {
        ProductAdminResponse response = productAdminService.reviewProduct(productId, request);
        return ApiResponse.<ProductAdminResponse>builder()
                .result(response)
                .build();
    }

    // ✅ Lấy tất cả sản phẩm đang chờ duyệt
    @GetMapping("/pending")
    public ApiResponse<List<ProductResponse>> getAllPendingProducts() {
        List<ProductResponse> responses = productAdminService.getAllPendingProducts();
        return ApiResponse.<List<ProductResponse>>builder()
                .result(responses)
                .build();
    }

    // ✅ Lấy chi tiết 1 sản phẩm
    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable String productId) {
        ProductResponse response = productAdminService.getProductById(productId);
        return ApiResponse.<ProductResponse>builder()
                .result(response)
                .build();
    }
}
