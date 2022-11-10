package ckugroup.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



//TokenProvider, JwtFilter를 SecurityConfig에 적용 할 때 사용 할 JwtSecurityCOnfig 클래스 생성

//JwtSecurityConfig는 SecurityConfigurerAdapter를 extends를 받고,
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> 
{
	//TokenProvider를 주입받음
    private TokenProvider tokenProvider;
    
    public JwtSecurityConfig(TokenProvider tokenProvider) 
    {
        this.tokenProvider = tokenProvider;
    }
    // configure 메소드를 override하고, 
    @Override
    public void configure(HttpSecurity http) 
    {
    	// Security로직에 필터로 등록하는 역할을 함
        http.addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}