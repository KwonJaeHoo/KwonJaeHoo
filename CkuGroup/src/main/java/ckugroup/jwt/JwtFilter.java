package ckugroup.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

//jwt를 위한 커스텀 필터를 만들기 위해 jwtFilter 클래스 생성
// GenericFilterBean을 extends해서 doFilter Override, 실제 필터링 로직은 doFilter 내부에 작성

public class JwtFilter extends GenericFilterBean
{
	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	public static final String AUTHORIZATION_HEADER = "Authorization";
	//jwtFilter는 tokenprovider를 주입받음
	private TokenProvider tokenProvider;
	
	public JwtFilter(TokenProvider tokenProvider)
	{
		this.tokenProvider = tokenProvider;
	}
	
	//doFilter는 Jwt토큰의 인증정보를 현재 실행중인 SecurityContext에 저장하는 역할 수행
	//resolveToken을 통해 토큰을 받아와 유효성 검증을 하고 정상 토큰이면 SecurityContext에 저장
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
	{
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
			// request에서 토큰을 받아 jwt토큰을
	      String jwt = resolveToken(httpServletRequest);
	      String requestURI = httpServletRequest.getRequestURI();

	      // validateToken 유효성 검증을위한 메소드 통과 후 
	      if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) 
	      {
	    	 //토큰이 정상이면 토큰에서 Authentication 객체를 받아와서 
	         Authentication authentication = tokenProvider.getAuthentication(jwt);
	         //SecurityContext에 set 해줌
	         SecurityContextHolder.getContext().setAuthentication(authentication);
	         logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
	      } 
	      else 
	      {
	         logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
	      }

	      filterChain.doFilter(servletRequest, servletResponse);
	}
	//필터링을 하기위해
	//requestHeader 에서 토큰 정보를 꺼내오기 위한 resolveToken 메소드 추가
	private String resolveToken(HttpServletRequest request)
	{
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer"))
		{
			return bearerToken.substring(7);
		}
		return null;
	}	
}