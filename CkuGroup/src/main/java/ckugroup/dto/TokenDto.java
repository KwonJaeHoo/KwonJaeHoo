package ckugroup.dto;

import lombok.*;

//token 정보를 response 할 때 사용 할 tokenDto를 만들기

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto 
{
	private String token;
}

