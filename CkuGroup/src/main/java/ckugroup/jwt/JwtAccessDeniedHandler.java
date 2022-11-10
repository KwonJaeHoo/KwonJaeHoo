package ckugroup.jwt;

import org.springframework.stereotype.Component;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 필요한 권한이 존재하지 않는 경우에 403 Forbidden 에러 리턴 할 JwtAccessDeniedHandler 클래스

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler 
{
	
   @Override
   public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException 
   {
      //필요한 권한이 없이 접근하려 할때 403
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
   }
}