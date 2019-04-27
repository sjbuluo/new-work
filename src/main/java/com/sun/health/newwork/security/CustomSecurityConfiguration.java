package com.sun.health.newwork.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CustomSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 登录验证相关配置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**/*.js").permitAll() // 无需验证的资源
                .antMatchers("/sign/up/**").permitAll() // 无需验证的资源
                .antMatchers("/sign/in/info").permitAll() // 无需验证的资源
                .anyRequest().authenticated()
                .and()
                .formLogin() // 登录相关
                .usernameParameter("username") // 用户名的请求字段 默认为userName
                .passwordParameter("password") // 密码的请求字段 默认为password
//                .loginPage("") //  自定义登录页url,默认为/login
                .loginProcessingUrl("/sign/in").permitAll() // 登录请求拦截的url,也就是form表单提交时指定的action
                .successForwardUrl("/sign/in/success") // 验证成功对应action
                .failureForwardUrl("/sign/in/failure").permitAll() // 验证失败对应action
                .and()
                .logout() // 注销相关
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutUrl("/sign/out")
                .logoutSuccessUrl("/sign/out/success").permitAll()
                .and()
                .rememberMe() // 保持登录选项
                .tokenValiditySeconds(604800)
//                .alwaysRemember(false)
                .userDetailsService(userDetailsService)
                .and()
                .cors()
                .and()
                .csrf().disable();

    }

    /**
     * 自定义密码加密器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private CorsConfiguration buildCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
//        corsConfiguration.addExposedHeader("");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);
        return corsConfiguration;
    }

    /**
     * 跨域相关配置
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildCorsConfiguration());
        return new CorsFilter(source);
    }

}
