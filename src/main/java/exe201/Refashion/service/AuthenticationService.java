package exe201.Refashion.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import exe201.Refashion.dto.request.AuthenticationRequest;
import exe201.Refashion.dto.request.IntrospectRequest;
import exe201.Refashion.dto.response.AuthenticationResponse;
import exe201.Refashion.dto.response.IntrospectResponse;
import exe201.Refashion.entity.InvalidatedToken;
import exe201.Refashion.entity.Users;
import exe201.Refashion.exception.AppException;
import exe201.Refashion.exception.ErrorCode;
import exe201.Refashion.repository.InvalidatedTokenRepository;
import exe201.Refashion.repository.RoleRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import exe201.Refashion.entity.Role;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;
    RoleRepository roleRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXIST));

        if (!user.getEmailVerified()) {
            throw new AppException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        boolean authenticated = passwordEncoder.matches(request.getPassword(),
                user.getPassword());

        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .userId(user.getId())
                .role(user.getRole())
                .build();
    }

    public String generateToken(Users user) {

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        //Thong tin co ban de build 1 token
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer(user.getId())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString()) //JWT ID cho login token
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        //Ki 1 token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }
    }

    private String buildScope(Users user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (user.getRole() != null) {
            stringJoiner.add(user.getRole().getRoleName());
        }

        return stringJoiner.toString();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    private SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        log.info("Parsing JWT token: {}", token);
        SignedJWT signedJWT = SignedJWT.parse(token);
        log.info("JWT parsed successfully");

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);
        log.info("Verifying token: {}", token);

        if (!(verified && expiryTime.after(new Date()))) {
            log.error("Token is invalid or expired: verified={}, expiryTime={}", verified, expiryTime);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String jwtID = signedJWT.getJWTClaimsSet().getJWTID();
        boolean exists = invalidatedTokenRepository.existsById(jwtID);
        log.info("Checking if token exists in invalidated list: {}", exists);

        if (exists)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }

    public void logout(String authorizationHeader) throws ParseException, JOSEException {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        log.info("Authorization header received: {}", authorizationHeader);

        try {
            String token = authorizationHeader.substring(7); // Loại bỏ "Bearer "
            var signToken = verifyToken(token);
            log.info("JWT Claims: {}", signToken.getJWTClaimsSet().toJSONObject());

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
            if (jit == null) {
                log.error("JWT ID is null, cannot invalidate token");
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();
            log.info("Invalidated token saved with ID: {}", jit);

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception) {
            log.info("Token already expired");
            throw exception;
        }
    }

    public AuthenticationResponse handleGoogleLogin(Authentication authentication) {
        // Lấy thông tin từ Google
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();

        if (email == null) {
            throw new AppException(ErrorCode.EMAIL_NOT_PROVIDED);
        }

        // Tìm hoặc tạo role
        Role role = roleRepository.findByRoleName("buyer")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Tìm hoặc tạo user
        Users user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    Users newUser = Users.builder()
                            .email(email)
                            .role(role)
                            .build();
                    return userRepository.save(newUser);
                });

        // Sinh token
        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .userId(user.getId())
                .build();
    }
}
