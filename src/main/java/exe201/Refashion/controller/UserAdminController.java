package exe201.Refashion.controller;

import exe201.Refashion.dto.request.UserAdminRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.UserAdminResponse;
import exe201.Refashion.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserAdminController {

    UserAdminService userAdminService;

    @PutMapping("/{userId}/manage")
    public ApiResponse<UserAdminResponse> manageUser(
            @PathVariable String userId,
            @RequestBody UserAdminRequest request) {
        UserAdminResponse response = userAdminService.manageUser(userId, request);
        return ApiResponse.<UserAdminResponse>builder()
                .result(response)
                .build();
    }
}