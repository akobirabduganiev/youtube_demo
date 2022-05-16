package com.company.util;

import com.company.dto.ProfileJwtDTO;
import com.company.enums.ProfileRole;
import com.company.exceptions.AppForbiddenException;
import com.company.exceptions.TokenNotValidException;
import io.jsonwebtoken.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtil {
    private final static String SECRET_KEY = "kalitso'z";

    public static String encode(Integer id, String email) {
        return doEncode(id, null, email, 60);
    }

    public static String encode(Integer id, ProfileRole role) {
        return doEncode(id, role, null, 60);
    }

    public static String encode(Integer id) {
        return doEncode(id, null, null, 30);
    }

    public static String doEncode(Integer id, ProfileRole role, String email, long minute) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setSubject(String.valueOf(id));
        jwtBuilder.setIssuedAt(new Date()); // 10:15
        jwtBuilder.signWith(SignatureAlgorithm.HS256, SECRET_KEY);
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (minute * 60 * 1000)));
        jwtBuilder.setIssuer("youtube");

        if (role != null) jwtBuilder.claim("role", role);
        if (email != null) jwtBuilder.claim("email", email);

        return jwtBuilder.compact();
    }

    public static ProfileJwtDTO decode(String jwt) {
        JwtParser jwtParser = Jwts.parser();

        jwtParser.setSigningKey(SECRET_KEY);
        Jws<Claims> jws = jwtParser.parseClaimsJws(jwt);

        Claims claims = jws.getBody();
        String id = claims.getSubject();
        String role = String.valueOf(claims.get("role"));
        String email = String.valueOf(claims.get("email"));

        if (role.equals("null"))
            return new ProfileJwtDTO(Integer.parseInt(id), email);

        return new ProfileJwtDTO(Integer.parseInt(id), ProfileRole.valueOf(role), email);
    }

    public static Integer decodeAndGetId(String jwt) {
        JwtParser jwtParser = Jwts.parser();

        jwtParser.setSigningKey(SECRET_KEY);
        Jws<Claims> jws = jwtParser.parseClaimsJws(jwt);

        Claims claims = jws.getBody();
        String id = claims.getSubject();

        return Integer.parseInt(id);
    }


    public static Integer getIdFromHeader(HttpServletRequest request, ProfileRole... requiredRoles) {
        try {
            ProfileJwtDTO dto = (ProfileJwtDTO) request.getAttribute("profileJwtDto");
            if (requiredRoles == null || requiredRoles.length == 0) {
                return dto.getId();
            }
            for (ProfileRole role : requiredRoles) {
                if (role.equals(dto.getRole())) {
                    return dto.getId();
                }
            }
        } catch (RuntimeException e) {
            throw new TokenNotValidException("Not Authorized");
        }
        throw new AppForbiddenException("Not Access");
    }

    public static ProfileJwtDTO getProfileFromHeader(HttpServletRequest request, ProfileRole... requiredRoles) {
        try {
            ProfileJwtDTO dto = (ProfileJwtDTO) request.getAttribute("profileJwtDto");
            if (requiredRoles == null || requiredRoles.length == 0) {
                return dto;
            }
            for (ProfileRole role : requiredRoles) {
                if (role.equals(dto.getRole())) {
                    return dto;
                }
            }
        } catch (RuntimeException e) {
            throw new TokenNotValidException("Not Authorized");
        }
        throw new AppForbiddenException("Not Access");
    }
}
