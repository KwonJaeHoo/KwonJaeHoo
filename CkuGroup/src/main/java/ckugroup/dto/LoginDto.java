package ckugroup.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;


//외부와의 통신에 사용할 DTO 클래스 생성
//Repository 관련 코드 생성
//로그인 API관련 로직 생성

//외부 통신에 사용할 DTO 클래스들을 만들기위해 DTO 패키지 생성 
//login시 사용 할 login Dto클래스 생성 

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

//Lombok 어노테이션 추가,
//@valid관련 어노테이션 추가, username, password

public class LoginDto 
{
	@NotNull
	@Size(min = 5, max = 8)
	private String username;
	
	@NotNull
	@Size(min = 3, max = 32)
	private String password;
}
