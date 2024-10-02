package com.swp391.DogCatLoverPlatform.config;

import com.swp391.DogCatLoverPlatform.filter.JwtFilter;
import com.swp391.DogCatLoverPlatform.provider.CustomAuthenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Class sẽ được quét khi spring boot chạy ở thầng config
@EnableWebSecurity //custom spring security
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    CustomAuthenProvider customAuthenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(customAuthenProvider)
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                    .antMatchers("/css/**", "/js/**", "/images/**", "/webfonts/**", "/fonts/**", "/video/**", "/vendors/**", "/buttons.github.io/**").permitAll()
                    .antMatchers("/service/**").permitAll()     //Dũng thêm để fix bug
                    .antMatchers("/service/view").permitAll()
                    .antMatchers("/service/detail/**").permitAll()
                    .antMatchers("/index/**").permitAll()
                    .antMatchers("/cdn-cgi/**").permitAll()

                    .antMatchers("/blog/view").permitAll()
                    .antMatchers("/blog/byType").permitAll()
                    .antMatchers("/blog/**").permitAll()        //Dũng thêm để fix bug

                    .antMatchers("/blog/**").permitAll()
                    .antMatchers("/invoice/**").permitAll()
                    .antMatchers("/paymethod/**").permitAll()


                    .antMatchers("/").permitAll()
                    .antMatchers(HttpMethod.POST,"/blog/**").permitAll()
                  //  .antMatchers("/paymethod/**").hasRole("USER")
                    .antMatchers(HttpMethod.POST,"/services/**").permitAll()
                    .antMatchers("/booking/**").permitAll()


                    .antMatchers("/staff").hasAnyRole("ADMIN", "STAFF")
                    .antMatchers("/staff/**").hasAnyRole("ADMIN", "STAFF")

//                .antMatchers("/staff").permitAll()
//                .antMatchers("/staff/**").permitAll()

                    .antMatchers("/deposite-history/**").permitAll()




                    .anyRequest().authenticated()
                    .and()

                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                .formLogin()
//                .loginPage("/index/login")
//                .loginProcessingUrl("/j_spring_security_check")
//                .defaultSuccessUrl("/index/home")
//                .and()
//                .logout()
//                    .permitAll()
//                .and()
                .build();
    }

}
