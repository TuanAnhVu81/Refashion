package exe201.Refashion.controller;

import com.nimbusds.jose.JOSEException;
import exe201.Refashion.dto.request.AuthenticationRequest;
import exe201.Refashion.dto.request.IntrospectRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.AuthenticationResponse;
import exe201.Refashion.dto.response.IntrospectResponse;
import exe201.Refashion.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request); // Sử dụng login từ AuthenticationService
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestHeader("Authorization") String authorizationHeader) throws ParseException, JOSEException {
        authenticationService.logout(authorizationHeader);
        return ApiResponse.<Void>builder()
                .message("Logout successfully")
                .build();
    }

    @GetMapping("/google/login")
    ApiResponse<AuthenticationResponse> handleGoogleLogin() {
        // Lấy thông tin authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Gọi service để xử lý và sinh token
        AuthenticationResponse authResponse = authenticationService.handleGoogleLogin(authentication);

        // Trả về response theo định dạng ApiResponse
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authResponse)
                .message("Google login successful")
                .build();
    }
}
