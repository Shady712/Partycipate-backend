package com.sasd.eventor.services.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.entities.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;

@Service
@AllArgsConstructor
public class JwtService {
    private static final String SECRET = "AVShtanahTiDvaHeraDerzhish";

    private final Algorithm algorithm = HMAC256(SECRET);
    private final JWTVerifier verifier = JWT.require(algorithm).build();
    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public String createJwtToken(User user) {
        logger.info("Request for creation of jwt token for user '{}' received", user.getLogin());
        try {
            return JWT.create().withClaim("userId", user.getId()).sign(algorithm);
        } catch (JWTCreationException exception) {
            logger.error("Jwt token creation failed for user '{}'", user.getLogin());
            throw new EventorException("JWT token creation failed due to '" + exception + "'");
        }
    }

    public Long decodeJwtToId(String jwt) {
        logger.debug("Received decoding request for jwt token");
        try {
            return verifier.verify(jwt).getClaim("userId").asLong();
        } catch (JWTVerificationException exception) {
            throw new EventorException("Provided JWT token is invalid");
        }
    }
}
