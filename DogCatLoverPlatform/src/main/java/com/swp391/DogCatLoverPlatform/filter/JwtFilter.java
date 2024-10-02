package com.swp391.DogCatLoverPlatform.filter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swp391.DogCatLoverPlatform.util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


//Tạo fillter để hứng token mỗi khi người dùng gọi request
@Service
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    private Gson goGson = new Gson();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Lấy token mà client truyền trên header()

        String headerValue = request.getHeader("Authorization");
        if(headerValue != null &&headerValue.startsWith("Bearer ")){
            //cắt chữ Bearer để lấy được Token
            String token = headerValue.substring(7);
            String data = jwtHelper.parseToken(token);
            System.out.println("Kiểm tra " + data   );
            if(data!=null&&!data.isEmpty()){
                //Chứng thực hợp lệ cho security
                Type listType = new TypeToken<ArrayList<SimpleGrantedAuthority>>(){}.getType();
                List<SimpleGrantedAuthority> roles = goGson.fromJson(data,listType);
//                List<GrantedAuthority> roles = new ArrayList<>();
//                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
//                roles.add(grantedAuthority);


                UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken("","",roles);
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(user);
            }
        }else {
            //không hợp lệ

        }

        filterChain.doFilter(request,response);
    }
}
