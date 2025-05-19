package exe201.Refashion.service;

import exe201.Refashion.dto.request.ProductRequest;
import exe201.Refashion.dto.response.ProductResponse;
import exe201.Refashion.entity.Categories;
import exe201.Refashion.entity.ProductImages;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Users;
import exe201.Refashion.mapper.ProductMapper;
import exe201.Refashion.repository.CategoryRepository;
import exe201.Refashion.repository.ProductImagesRepository;
import exe201.Refashion.repository.ProductRepository;
import exe201.Refashion.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImagesRepository productImagesRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductResponse createProduct(ProductRequest request) {
        Products product = productMapper.toEntity(request);

        Users seller = userRepository.findById(request.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller không tồn tại"));
        product.setSeller(seller);

        Categories category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category không tồn tại"));
        product.setCategory(category);

        product.setId(UUID.randomUUID().toString());
        product.setCreatedAt(LocalDateTime.now());
        product.setIsActive(true);

        productRepository.save(product);

        List<ProductImages> images = request.getImageUrls().stream()
                .map(url -> {
                    ProductImages img = new ProductImages();
                    img.setId(UUID.randomUUID().toString());
                    img.setProduct(product);
                    img.setImageUrl(url);
                    return img;
                }).collect(Collectors.toList());
        productImagesRepository.saveAll(images);

        ProductResponse response = productMapper.toResponse(product);
        response.setImageUrls(images.stream().map(ProductImages::getImageUrl).collect(Collectors.toList()));

        return response;
    }

    public ProductResponse updateProduct(String productId, ProductRequest request) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product không tồn tại"));

        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setProductCondition(request.getProductCondition());
        product.setSize(request.getSize());
        product.setColor(request.getColor());
        product.setPrice(new BigDecimal(request.getPrice()));

        if (!product.getSeller().getId().equals(request.getSellerId())) {
            Users seller = userRepository.findById(request.getSellerId())
                    .orElseThrow(() -> new RuntimeException("Seller không tồn tại"));
            product.setSeller(seller);
        }

        if (!product.getCategory().getId().equals(request.getCategoryId())) {
            Categories category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category không tồn tại"));
            product.setCategory(category);
        }

        productRepository.save(product);

        List<ProductImages> oldImages = productImagesRepository.findByProductId(productId);
        productImagesRepository.deleteAll(oldImages);

        List<ProductImages> newImages = request.getImageUrls().stream()
                .map(url -> {
                    ProductImages img = new ProductImages();
                    img.setId(UUID.randomUUID().toString());
                    img.setProduct(product);
                    img.setImageUrl(url);
                    return img;
                }).collect(Collectors.toList());
        productImagesRepository.saveAll(newImages);

        ProductResponse response = productMapper.toResponse(product);
        response.setImageUrls(newImages.stream().map(ProductImages::getImageUrl).collect(Collectors.toList()));

        return response;
    }

    public ProductResponse getProductById(String productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product không tồn tại"));
        ProductResponse response = productMapper.toResponse(product);

        List<ProductImages> images = productImagesRepository.findByProductId(productId);
        response.setImageUrls(images.stream().map(ProductImages::getImageUrl).collect(Collectors.toList()));

        return response;
    }

    public List<ProductResponse> getAllProducts() {
        List<Products> products = productRepository.findAll();

        return products.stream().map(product -> {
            ProductResponse response = productMapper.toResponse(product);
            List<ProductImages> images = productImagesRepository.findByProductId(product.getId());
            response.setImageUrls(images.stream().map(ProductImages::getImageUrl).collect(Collectors.toList()));
            return response;
        }).collect(Collectors.toList());
    }

    public void deleteProduct(String productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product không tồn tại"));

        List<ProductImages> images = productImagesRepository.findByProductId(productId);
        productImagesRepository.deleteAll(images);

        productRepository.delete(product);
    }
}
