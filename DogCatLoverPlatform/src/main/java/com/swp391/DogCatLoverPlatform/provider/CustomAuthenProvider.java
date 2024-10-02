package com.swp391.DogCatLoverPlatform.provider;

import com.swp391.DogCatLoverPlatform.entity.UserEntity;
import com.swp391.DogCatLoverPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //logic xử lý đăng nhập
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity != null){
            //User không tồn tại kiểm tra lại mật khẩu
            if(passwordEncoder.matches(password,userEntity.getPassword())){
                // tạo chứng thực
                List<GrantedAuthority> roles = new ArrayList<>();
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userEntity.getRoleEntity().getName());
                roles.add(grantedAuthority);

                //Tạo chứng thực cho security
                 UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,userEntity.getPassword(),roles);
                 SecurityContextHolder.getContext().setAuthentication(token);
                 return token;


            }else{
                return null;
            }

        }else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
