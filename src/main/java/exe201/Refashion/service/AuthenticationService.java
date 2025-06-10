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
import exe201.Refashion.entity.Role;
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

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXIST));

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new AppException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .userId(user.getId())
                .role(user.getRole())
                .build();
    }

    public String generateToken(Users user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer(user.getId())
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .jwtID(jwtId)
                .claim("scope", buildScope(user))
                .build();

        try {
            SignedJWT signedJWT = new SignedJWT(header, jwtClaimsSet);
            signedJWT.sign(new MACSigner(SIGNER_KEY.getBytes()));
            log.info("Generated token with JWT ID: {}", jwtId);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException("Token generation failed", e);
        }
    }

    private String buildScope(Users user) {
        StringJoiner joiner = new StringJoiner(" ");
        if (user.getRole() != null) {
            joiner.add(user.getRole().getRoleName());
        }
        return joiner.toString();
    }

    public IntrospectResponse introspect(IntrospectRequest request) {
        boolean isValid = true;
        try {
            verifyToken(request.getToken());
        } catch (Exception e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    private SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        log.debug("Verifying token...");
        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean verified = signedJWT.verify(verifier);

        Date expiry = signedJWT.getJWTClaimsSet().getExpirationTime();
        String jwtID = signedJWT.getJWTClaimsSet().getJWTID();

        if (!verified || expiry.before(new Date())) {
            log.warn("Token expired or invalid: verified={}, expiry={}", verified, expiry);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(jwtID)) {
            log.warn("Token is already invalidated: {}", jwtID);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    public void logout(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        try {
            String token = authorizationHeader.substring(7);
            SignedJWT signedJWT = verifyToken(token);

            String jwtID = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiry = signedJWT.getJWTClaimsSet().getExpirationTime();

            if (jwtID == null) {
                log.error("JWT ID is null, cannot invalidate token");
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            invalidatedTokenRepository.save(
                    InvalidatedToken.builder().id(jwtID).expiryTime(expiry).build()
            );
            log.info("Token invalidated successfully: {}", jwtID);
        } catch (Exception e) {
            log.error("Failed to invalidate token", e);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public AuthenticationResponse handleGoogleLogin(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();

        if (email == null) {
            throw new AppException(ErrorCode.EMAIL_NOT_PROVIDED);
        }

        Role role = roleRepository.findByRoleName("buyer")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Users user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        Users.builder().email(email).role(role).build()
                ));

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .userId(user.getId())
                .role(user.getRole())
                .build();
    }
}
