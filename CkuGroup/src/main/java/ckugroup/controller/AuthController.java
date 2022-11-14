package ckugroup.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ckugroup.dto.LoginDto;
import ckugroup.dto.TokenDto;
import ckugroup.jwt.JwtFilter;
import ckugroup.jwt.TokenProvider;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
//login API 추가를 위해 AuthController 생성
public class AuthController 
{
	//AuthController는 TokenProvider, AuthenticationManagerBuilder를 주입받음 
	private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) 
    {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    //로그인 API경로는 /api/authenticate, post요청을 받음
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) //LoginDto의 username, password를 파라미터로 받고 이를 이용해서 UsernamePasswordAuthenticationToken생성
    {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        //authenticationToken 이용해서 authenticate가 실행 될 때 customUserDetailsService에서 loadUserByUsername 메소드 실행 
        
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken); 
        
        //이 결과값을 가지고 authentication 객체생성, 객체를 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        //인증정보를 기준으로 tokenProvider에서 만든 createToken메소드를 통해 jwt 토큰 생성 
        
        String jwt = tokenProvider.createToken(authentication);
        
        //jwt 토큰은 Response header에도 넣고, tokenDto 이용해서 Response body에도 넣어서 리턴  
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
