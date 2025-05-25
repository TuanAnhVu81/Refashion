package exe201.Refashion.service;

import exe201.Refashion.dto.request.ProductRequest;
import exe201.Refashion.dto.response.ProductResponse;
import exe201.Refashion.entity.*;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.mapper.ProductMapper;
import exe201.Refashion.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    ProductMapper productMapper;

//    String uploadDir = "E:\\SEMESTER 8\\EXE201\\BE\\uploads\\products\\";
    String uploadDir = System.getProperty("user.dir") + "/uploads/products/";

    public ProductResponse createProduct(ProductRequest request) {
        try {
            Categories category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

            Users seller = userRepository.findById(request.getSellerId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            // KHÔNG cần tự gán id thủ công ở đây vì đã dùng @GeneratedValue
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
                    .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                    .images(new ArrayList<>())
                    .build();

            // Lưu product trước để có ID (nếu cần dùng cho ảnh)
            product = productRepository.save(product);

            // Upload ảnh
            uploadProductImage(request.getImageFile(), product);

            // Lưu lại product với ảnh
            product = productRepository.save(product);

            return productMapper.toProductResponse(product);

        } catch (Exception e) {
            System.err.println("Error creating product: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error creating product: " + e.getMessage(), e);
        }
    }

    public ProductResponse updateProduct(String id, ProductRequest request) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setProductCondition(request.getProductCondition());
        product.setSize(request.getSize());
        product.setColor(request.getColor());
        product.setPrice(request.getPrice());
        product.setIsActive(request.getIsActive());

        uploadProductImage(request.getImageFile(), product);

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public List<ProductResponse> getAllProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse getProductById(String id) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toProductResponse(product);
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        productRepository.deleteById(id);
    }
    
    /**
     * Tách logic upload ảnh ra riêng.
     */
    private void uploadProductImage(MultipartFile imageFile, Products product) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile, product.getId());

            ProductImages productImage = ProductImages.builder()
                    .imageUrl(imageUrl)
                    .product(product) // Gán product đang được quản lý
                    .build();

            product.getImages().add(productImage);
        }
    }

    /**
     * Lưu ảnh vào thư mục và trả về URL tương đối
     */
    private String saveImage(MultipartFile imageFile, String productId) {
        try {
            String sanitizedFileName = imageFile.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
            String fileName = productId + "_" + sanitizedFileName;
            File dest = new File(uploadDir + fileName);
            dest.getParentFile().mkdirs(); // tạo thư mục nếu chưa có
            System.out.println("File name: " + imageFile.getOriginalFilename());
            System.out.println("File size: " + imageFile.getSize());
            imageFile.transferTo(dest);
            return "/uploads/products/" + fileName; // URL tương đối
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPLOAD_IMAGE_FAIL);
        }
    }

    public List<ProductResponse> searchAndSortProducts(String keyword, String sortBy, String sortDirection) {
        Set<String> validSortFields = Set.of("category.name", "price", "size", "color", "productCondition");
        if (sortBy == null || !validSortFields.contains(sortBy)) {
            sortBy = "title"; // Default to title if sortBy is invalid
        }

        return productRepository.findAllCustom(keyword, sortBy, sortDirection).stream()
                .map(productMapper::toProductResponse)
                .toList();
    }
}
