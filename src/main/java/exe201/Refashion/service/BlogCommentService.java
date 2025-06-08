package exe201.Refashion.service;

import exe201.Refashion.dto.request.BlogCommentRequest;
import exe201.Refashion.dto.response.BlogCommentResponse;
import exe201.Refashion.entity.*;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.mapper.BlogCommentMapper;
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
public class BlogCommentService {

    BlogRepository blogRepository;
    UserRepository userRepository;
    BlogCommentRepository blogCommentRepository;
    BlogCommentMapper commentMapper;
    ProductRepository productRepository;

    public BlogCommentResponse addComment(BlogCommentRequest request) {
        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        BlogComment comment = BlogComment.builder()
                .id(UUID.randomUUID().toString())
                .content(request.getContent())
                .product(product)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        return commentMapper.toResponse(blogCommentRepository.save(comment));
    }

    public List<BlogCommentResponse> getCommentsByBlog(String productId) {
        return blogCommentRepository.findByProductIdOrderByCreatedAtAsc(productId).stream()
                .map(commentMapper::toResponse)
                .toList();
    }
}
