package ckugroup.service;

import java.util.Collections;
import java.util.Optional;

import ckugroup.dto.UserDto;
import ckugroup.entity.Authority;
import ckugroup.entity.User;
import ckugroup.exception.DuplicateMemberException;
import ckugroup.exception.NotFoundMemberException;
import ckugroup.repository.UserRepository;
import ckugroup.util.SecurityUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//useService class는 userrepository, passwordEncoder를 주입받음, 
public class UserService 
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) 
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    //signup메소드는 회원가입 로직 실행
    public UserDto signup(UserDto userDto) 
    {	
    	//username이 DB에 존재하지 않으면 authority와 User정보를 생성하여 UserRepository의 save메소드를 통해 DB에 정보 저장 
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) 
        {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }
        
        //중요한점은 signup 메소드를 통해 가입한 회원은 ROLE_USER를 가지고있고 data.sql에서 자동 생성 되는 admin 계정은 USER, ADMIN ROLE을 가지고있음
        //회원가입을 통한 회원은 권한 하나를 가짐 
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return UserDto.from(userRepository.save(user));
    }

    //유저 권한정보를 가져오는 메소드 2개, getUserWithAuthorities는 username을 기준으로 정보를 가져옴
    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) 
    {
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }
    
    //getMyUserWithAuthorities는 SecurityContext에 저장된 username의 정보만 가져옴
    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() 
    {
        return UserDto.from(SecurityUtil.getCurrentUsername() .flatMap(userRepository::findOneWithAuthoritiesByUsername) .orElseThrow(() -> new NotFoundMemberException("Member not found")));
    }
}
