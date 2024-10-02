package com.swp391.DogCatLoverPlatform.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtHelper {

    //@Value: giúp lấy cấu hình bên file application.properties;
    @Value("${custom.token.key}")
    private String secKey;

    private long expiredTime = 8*60*60*1000;

    public String generateToken(String data){
        // lấy key đã lưu trử và sử dụng để tạo ra token
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secKey));
        //sinh ra thời gian hết hạn mới
        Date date = new Date();
        long newDateMilis = date.getTime()+expiredTime;
        Date newExpriedDate = new Date(newDateMilis);

        String token = Jwts.builder()
                .setSubject(data)
                .signWith(key)
                .setExpiration(newExpriedDate)
                .compact();
        return token;
    }

    //Giải mã token
    public String parseToken(String token){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secKey));
        String data = Jwts.parserBuilder()
                .setSigningKey(key).build() // truyền key cần để có thể giải mã token
                .parseClaimsJws(token)      // truyền vào token cần được giải mã
                .getBody().getSubject();    // Lấy nội dung lưu trữ trong token
        return data;
    }
}
