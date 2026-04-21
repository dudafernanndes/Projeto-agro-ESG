package br.com.vits.orc.vits_agrochain.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import br.com.vits.orc.vits_agrochain.dto.TokenResponse;
import br.com.vits.orc.vits_agrochain.model.User;

@Service
public class TokenService {

    private final JwtEncoder encoder;

    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public TokenResponse generateToken(Authentication authentication) {
        Instant now = Instant.now();

        User user = (User) authentication.getPrincipal();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("vits-agrochain-api")
                .issuedAt(now)
                .expiresAt(now.plus(24, ChronoUnit.HOURS))
                .subject(user.getEmail()) 
                .claim("id", user.getId()) 
                .build();
        
        String token = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new TokenResponse(
            token,
            user.getId(),
            user.getName(),            
            user.getEmail(),
            null
        );
    }
}
