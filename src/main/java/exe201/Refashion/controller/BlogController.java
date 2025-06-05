package exe201.Refashion.controller;

import exe201.Refashion.dto.request.BlogRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.BlogResponse;
import exe201.Refashion.service.BlogService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class BlogController {

    BlogService blogService;

    @PostMapping
    public ApiResponse<BlogResponse> createBlog(@RequestBody BlogRequest request) {
        return ApiResponse.<BlogResponse>builder()
                .result(blogService.createBlog(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<BlogResponse>> getAllBlogs() {
        return ApiResponse.<List<BlogResponse>>builder()
                .result(blogService.getAllBlogs())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BlogResponse> getBlog(@PathVariable String id) {
        return ApiResponse.<BlogResponse>builder()
                .result(blogService.getBlogById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteBlog(@PathVariable String id) {
        blogService.deleteBlog(id);
        return ApiResponse.<String>builder()
                .result("Xóa bài viết thành công")
                .build();
    }
}
