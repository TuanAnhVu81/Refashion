package exe201.Refashion.controller;

import exe201.Refashion.dto.request.BlogCommentRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.BlogCommentResponse;
import exe201.Refashion.service.BlogCommentService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/blogs/comments")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class BlogCommentController {

    BlogCommentService blogCommentService;

    @PostMapping
    public ApiResponse<BlogCommentResponse> addComment(@RequestBody BlogCommentRequest request) {
        return ApiResponse.<BlogCommentResponse>builder()
                .result(blogCommentService.addComment(request))
                .build();
    }

    @GetMapping("/{blogId}")
    public ApiResponse<List<BlogCommentResponse>> getComments(@PathVariable String blogId) {
        return ApiResponse.<List<BlogCommentResponse>>builder()
                .result(blogCommentService.getCommentsByBlog(blogId))
                .build();
    }
}
