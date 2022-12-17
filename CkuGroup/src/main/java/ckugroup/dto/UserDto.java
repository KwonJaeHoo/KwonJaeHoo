package ckugroup.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import ckugroup.entity.User;
import lombok.*;


//회원가입시 사용할 UserDto클래스도 만듬

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto 
{

   @NotNull
   @Size(min = 8, max = 8)
   private String username;

   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   @NotNull
   @Size(min = 16, max = 128)
   private String password;

   @NotNull
   @Size(min = 3, max = 32)
   private String nickname;
   
   private Set<AuthorityDto> authorityDtoSet;

   public static UserDto from(User user) 
   {
      if(user == null) return null;

      return UserDto.builder()
              .username(user.getUsername())
              .nickname(user.getNickname())
              .authorityDtoSet(user.getAuthorities().stream()
              .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
              .collect(Collectors.toSet()))
              .build();
   }
}

//Repository들을 만들기 위해 repository 패키지 생성
//이전에 만든 User엔티티에 매핑되는 UserRepository 인터페이스 생성