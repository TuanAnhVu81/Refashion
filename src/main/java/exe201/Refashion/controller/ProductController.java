package exe201.Refashion.controller;

import exe201.Refashion.dto.request.ProductRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.ProductResponse;
import exe201.Refashion.enums.ProductCondition;
import exe201.Refashion.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @PostMapping(consumes = "multipart/form-data")
    public ApiResponse<ProductResponse> createProduct(
            @ModelAttribute @Valid ProductRequest request,
            @RequestPart(name = "imageFile", required = false) MultipartFile imageFile
    ) {
        request.setImageFile(imageFile);
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllProducts())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable String id) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProductById(id))
                .build();
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable String id,
            @ModelAttribute @Valid ProductRequest request,
            @RequestPart(name = "imageFile", required = false) MultipartFile imageFile
    ) {
        request.setImageFile(imageFile);
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ApiResponse.<String>builder()
                .result("Xóa sản phẩm thành công.")
                .build();
    }

    @GetMapping("/conditions")
    public ApiResponse<List<String>> getProductConditions() {
        List<String> conditions = Arrays.stream(ProductCondition.values())
                .map(Enum::name)
                .toList();

        return ApiResponse.<List<String>>builder()
                .result(conditions)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductResponse>> searchAndSortProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "title") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection
    ) {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.searchAndSortProducts(keyword, sortBy, sortDirection))
                .build();
    }

    @GetMapping("/seller/{sellerId}")
    public ApiResponse<List<ProductResponse>> getProductsBySeller(@PathVariable String sellerId) {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getProductsBySeller(sellerId))
                .build();
    }

}
