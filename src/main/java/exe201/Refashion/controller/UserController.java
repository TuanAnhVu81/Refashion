package exe201.Refashion.controller;

import exe201.Refashion.dto.request.UserCreationRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.UserResponse;
import exe201.Refashion.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping(value = "/verify-email", produces = "text/html")
    public String verifyEmail(@RequestParam String token) {
        try {
            userService.verifyEmail(token);
            return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Xác Thực Email Thành Công</title>
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; background-color: #f4f4f4; }
                        .container { max-width: 600px; margin: 50px auto; padding: 20px; background: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); text-align: center; }
                        h1 { color: #28a745; }
                        .button { display: inline-block; padding: 10px 20px; background-color: #007BFF; color: #fff; text-decoration: none; border-radius: 5px; margin-top: 20px; }
                        .footer { font-size: 12px; color: #777; margin-top: 20px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>Xác Thực Email Thành Công!</h1>
                        <p>Cảm ơn bạn đã xác thực email. Tài khoản của bạn đã được kích hoạt.</p>
                        <p>Bạn có thể đăng nhập và bắt đầu sử dụng Refashion ngay bây giờ.</p>
                        <a href="http://localhost:5173/login" class="button">Đi đến Trang Đăng Nhập</a>
                        <div class="footer">
                            <p>Trân trọng,<br>Đội ngũ Refashion</p>
                            <p>Email: tbinhduong0101@gmail.com</p>
                        </div>
                    </div>
                </body>
                </html>
                """;
        } catch (Exception e) {
            return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Lỗi Xác Thực Email</title>
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; background-color: #f4f4f4; }
                        .container { max-width: 600px; margin: 50px auto; padding: 20px; background: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); text-align: center; }
                        h1 { color: #dc3545; }
                        .button { display: inline-block; padding: 10px 20px; background-color: #007BFF; color: #fff; text-decoration: none; border-radius: 5px; margin-top: 20px; }
                        .footer { font-size: 12px; color: #777; margin-top: 20px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>Lỗi Xác Thực Email</h1>
                        <p>Xin lỗi, chúng tôi không thể xác thực email của bạn. Token có thể đã hết hạn hoặc không hợp lệ.</p>
                        <p>Vui lòng yêu cầu một email xác thực mới hoặc liên hệ hỗ trợ nếu cần trợ giúp.</p>
                        <a href="http://localhost:5173/resend-verification" class="button">Gửi Lại Email Xác Thực</a>
                        <div class="footer">
                            <p>Trân trọng,<br>Đội ngũ Refashion</p>
                            <p>Email: tbinhduong0101@gmail.com</p>
                        </div>
                    </div>
                </body>
                </html>
                """;
        }
    }

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestParam String email) {
        userService.createPasswordResetToken(email);
        return ApiResponse.<String>builder()
                .result("Đã gửi email đặt lại mật khẩu nếu email tồn tại trong hệ thống.")
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword
    ) {
        userService.resetPassword(token, newPassword);
        return ApiResponse.<String>builder()
                .result("Mật khẩu đã được đặt lại thành công.")
                .build();
    }

}
