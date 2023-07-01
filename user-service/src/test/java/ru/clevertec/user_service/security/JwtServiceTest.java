package ru.clevertec.user_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clevertec.user_service.builder.TestBuilder;
import ru.clevertec.user_service.builder.UserBuilder;
import ru.clevertec.user_service.model.User;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private String SECRET_KEY = "404E635266556A586E3272357538782F413F442A472D4B6150645367566B5970";

    private static User user;
    private static SecretKey secretKey;
    private static String token;


    @BeforeEach
    void setUp() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
        token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsInN1YiI6Iml2YW5faXZhbm92QGdtYWlsLmNvbSIsImlh" +
                "dCI6MTY4NTU0MTI0MSwiZXhwIjoxNjg1NTQ0ODQxfQ.pqvNChUaeEC11GU0_bWS5yKFj8nwF_-uiH2XmSibPCM";
        TestBuilder<User> userBuilder = new UserBuilder();
        user = userBuilder.build();
    }

    @Test
    void checkGenerateAccessTokenShouldNotBeNull() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        String token = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(60)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();


        assertNotNull(token);
    }

    @Test
    void generateRefreshToken() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        SecretKey refreshSecretKey = Keys.hmacShaKeyFor(keyBytes);
        String refreshToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(60)))
                .signWith(refreshSecretKey, SignatureAlgorithm.HS256)
                .compact();
        assertNotNull(refreshToken);
    }
}
