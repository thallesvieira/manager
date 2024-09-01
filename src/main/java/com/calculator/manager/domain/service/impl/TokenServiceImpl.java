package com.calculator.manager.domain.service.impl;

import com.calculator.manager.domain.model.user.User;
import com.calculator.manager.domain.service.ITokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Class responsible to create and validate token
 */
@Service
public class TokenServiceImpl implements ITokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Value("${token.jwt.expiration}")
    private String expiration;

    @Value("${token.jwt.secret}")
    private String secret;

    private final static String AUTHORIZATION = "Authorization";
    private final static String BEARER = "Bearer ";

    /** Method to create a token using expiration and secrete configureds in application.properties.
     * Save user id in subject to identify user when necessary.
     * @param authentication
     * @return
     */
    @Override
    public String createJwt(Authentication authentication) {
        logger.info("Get user from authentication principal");
        User user = (User) authentication.getPrincipal();

        logger.info("Get expiration date");
        Date expirationDate = getExpirationTime();

        logger.info("Crating a new token JWT");
        return Jwts.builder()
                .setIssuer("Api calculator")
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * Convert expiration date for a date.
     * @return
     */
    private Date getExpirationTime() {
        return new Date(new Date().getTime() + Long.parseLong(expiration));
    }

    /**
     * Retrieve token from request and check if is a bearer.
     *
     * @param request
     * @return - the token without bearer
     */
    @Override
    public String retrieve(HttpServletRequest request) {
        logger.info("Recover token from request");
        String token = request.getHeader(AUTHORIZATION);

        logger.info("Check if token is null or don't have Bearer");
        if (Strings.isEmpty(token) || !token.startsWith(BEARER)) {
            logger.info("Token is null or don't have Bearer");
            return null;
        }

        logger.info("Return token");
        return token.substring(BEARER.length());
    }

    /**
     * Check if the token is valid
     *
     * @param token
     * @return
     */
    @Override
    public Boolean isValid(String token) {
        try {
            logger.info("Check if token is valid");
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);

            logger.info("Token is valid");
            return true;
        } catch (Exception e) {
            logger.warn("Token is invalid");
            return false;
        }
    }

    /**
     * Get user id configured in subject from token
     * @param token
     * @return
     */
    @Override
    public Long getUserId(String token) {
        logger.info("Getting user from token");
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();

        logger.info("Return user found");
        return Long.parseLong(claims.getSubject());
    }

}
