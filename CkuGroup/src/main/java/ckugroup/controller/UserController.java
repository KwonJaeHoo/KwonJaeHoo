package ckugroup.controller;

import ckugroup.dto.UserDto;

import ckugroup.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
//import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController 
{
    private final UserService userService;

    public UserController(UserService userService) 
    {
        this.userService = userService;
    }
    
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    
    @PostMapping("/logout")
    public void testCookie(HttpServletResponse response)
    {
        //원래 쿠키의 이름이 userInfo 이었다면, value를 null로 처리.
        Cookie myCookie = new Cookie("token", null);
        myCookie.setMaxAge(0); // 쿠키의 expiration 타임을 0으로 하여 없앤다.
        myCookie.setPath("/"); // 모든 경로에서 삭제 됬음을 알린다.
        response.addCookie(myCookie);
    }
    
    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException 
    {
        response.sendRedirect("/api/user");
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    //getMyUserInfo 메소드는  @PreAuthorize 어노테이션을 통해 User, Admin 두 가지 권한 모두 호출 할 수 있는 api
    public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request) 
    {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    //getUserInfo 메소드는 @PreAuthorize 어노테이션을 통해 Admin 권한만 호출 할 수 있는 api
    //그리고 UserService에서 만들었던 username 파라미터 기준으로 유저 정보와 권한정보를 리턴하는 api
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) 
    {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }
    

    
//    //UserController에는 signup api 존재, Userdto 객체 파라미터로 받아 UserService signup 메소드 수행
//	@PostMapping("/signup")
//	public ResponseEntity<UserDto> signup(@Valid @RequestBody UserDto userDto) 
//	{
//		return ResponseEntity.ok(userService.signup(userDto));
//	}
}
