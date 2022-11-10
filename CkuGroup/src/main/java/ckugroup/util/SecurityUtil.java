package ckugroup.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtil 
{
   private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
   private SecurityUtil() {}
   								
   //getCurrentUsername 메소드 역할 - Security context의 Authentication 객체 이용해 username 리턴해주는 간단유틸성 메소드 
   public static Optional<String> getCurrentUsername() 
   {
	   				//SecurityContext에 Authentication객체가 저장되는 시점은 JwtFilter의 doFilter메소드에서 request가 들어올 때 SecurityContext에 Authentication객체를 저장해서 사용 
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null) 
      {
         logger.debug("Security Context에 인증 정보가 없습니다.");
         return Optional.empty();
      }

      String username = null;
      if (authentication.getPrincipal() instanceof UserDetails) 
      {
         UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
         username = springSecurityUser.getUsername();
      }
      else if (authentication.getPrincipal() instanceof String) 
      {
         username = (String) authentication.getPrincipal();
      }

      return Optional.ofNullable(username);
   }
}
