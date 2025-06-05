package exe201.Refashion.service;

import exe201.Refashion.dto.request.BlogRequest;
import exe201.Refashion.dto.response.BlogResponse;
import exe201.Refashion.entity.Blog;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Users;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.mapper.BlogMapper;
import exe201.Refashion.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class BlogService {

    BlogRepository blogRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    BlogMapper blogMapper;

    public BlogResponse createBlog(BlogRequest request) {
        Users author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Products> products = request.getProductIds().stream()
                .map(id -> productRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)))
                .filter(Products::getIsActive)
                .toList();

        Blog blog = Blog.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .products(products)
                .createdAt(LocalDateTime.now())
                .build();

        return blogMapper.toBlogResponse(blogRepository.save(blog));
    }

    public List<BlogResponse> getAllBlogs() {
        return blogMapper.toBlogResponses(blogRepository.findAll());
    }

    public BlogResponse getBlogById(String id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));
        return blogMapper.toBlogResponse(blog);
    }

    public void deleteBlog(String id) {
        if (!blogRepository.existsById(id)) {
            throw new AppException(ErrorCode.BLOG_NOT_FOUND);
        }
        blogRepository.deleteById(id);
    }
}
