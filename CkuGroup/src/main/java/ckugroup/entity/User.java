package ckugroup.entity;

import lombok.*;
import lombok.Getter;
import javax.persistence.*;
import java.util.Set;

@Entity 				//db 테이블과 1:1매핑객체
@Table(name = "`user`")	//테이블명 user
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class User 
{

	//자동증가되는 PK
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;		
	
	@Column(name = "username", length = 8, unique = true)
	private String username;
	
	@Column(name = "password", length = 256)
	private String password;
	
	@Column(name = "nickname", length = 16)
	private String nickname;
	
	@Column(name = "activated")
	private boolean activated;
	
	// @ManyToMany, @JoinTable은 USER 객체, 권환객체의 다:다 관계 -> 1:다 or 다:1 관계의 join table로 정의 
	@ManyToMany 
	@JoinTable(name = "user_authority", 
	joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "user_id")},
	inverseJoinColumns = { @JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
	private Set<Authority> authorities; //권한관계	

}