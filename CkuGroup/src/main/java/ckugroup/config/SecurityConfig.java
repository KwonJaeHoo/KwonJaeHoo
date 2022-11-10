package ckugroup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import ckugroup.jwt.JwtAccessDeniedHandler;
import ckugroup.jwt.JwtAuthenticationEntryPoint;
import ckugroup.jwt.JwtSecurityConfig;
import ckugroup.jwt.TokenProvider;


//만들었던 5개의 jwt클래스들 적용하기

@EnableWebSecurity
//EnableGlobalMethodSecurity는 @PreAuthorize 어노테이션을 메소드 단위로 추가하기 위해서 적용
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig
{
	private final CorsFilter corsFilter;
	private final TokenProvider tokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	
	//securityConfig는 TokenProvider, JwtAuthenticationEntryPoint, JwtAccessDeniedHandler 주입
	public SecurityConfig(CorsFilter corsFilter, TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler)
	{
		this.corsFilter = corsFilter;
		this.tokenProvider = tokenProvider;		
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
	}
	
	//passwordEncoder는 BCryptPasswordEncoder사용
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() 
    {
    	//하위 요청 무시
        return (web) -> web.ignoring().antMatchers("/h2-console/**", "/favicon.ico", "/error");
    }
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception 
    {
    	httpSecurity
        //토큰을 사용하기 때문에 csrf 설정은 disable exception을 핸들링 할 때 만든 클래스 추가
        .csrf().disable()

        .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                
        //exceptionHandling할 때 authenticationEntryPoint,accessDeniedHandler를 만든 클래스들로 추가 
        .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDeniedHandler)

        .and()
        //h2-console을 위한 설정 추가
        .headers()
        .frameOptions()
        .sameOrigin()

        // 세션을 사용하지 않기 때문에 STATELESS로 설정
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // HttpServletRequest 사용 요청에 대한 접근제한
        .and()
        .authorizeRequests()
		// /hello에 대한 요청 인증없이 접근허용
		// 로그인, 회원가입 API는 토큰 없는 상태에서 요청 들어오기 때문에 permitAll
        .antMatchers("/api/hello").permitAll()
        .antMatchers("/api/authenticate").permitAll()
        .antMatchers("/api/signup").permitAll()
        
        // 이 요청들은 인증되어야 함
        .anyRequest().authenticated()

        //jwtfilter addfilterBefore로 등록했던 jwtSecurityConfig 클래스도 적용 
        .and()
        .apply(new JwtSecurityConfig(tokenProvider));

        return httpSecurity.build();
    }
}
