package ckugroup.service;

import ckugroup.entity.User;
import ckugroup.repository.UserRepository;

import java.util.stream.Collectors;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//spring security에서 중요한 부분 UserDetailsService 구현한 CustomUserDetailsService 클래스 생성
@Component("userDetailsService")

//UserDetailsService를 implement하고 UserRepository를 주입받음
public class CustomUserDetailsService implements UserDetailsService
{
	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) 
	{
		this.userRepository = userRepository;
	}

	//loadUserByUsername메소드 오버라이드 해서 로그인 시 DB에서 유저정보와 권한정보를 가져오게 됨
	 @Override
	 @Transactional
	 public UserDetails loadUserByUsername(final String username) 
	 {
		 return userRepository.findOneWithAuthoritiesByUsername(username) .map(user -> createUser(username, user))
				 .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
	 }
	 
	 //해당 정보를 기반으로 userdetails, User객체를 생성해 리턴
	 private org.springframework.security.core.userdetails.User createUser(String username, User user) 
	 {
		 if(!user.isActivated()) 
		 {
			 throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
		 }
		 List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream() 
				 .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
				 .collect(Collectors.toList());

		 return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
	 }
}