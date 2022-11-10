package ckugroup.repository;

import ckugroup.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Repository들을 만들기 위해 repository 패키지 생성
//이전에 만든 User엔티티에 매핑되는 UserRepository 인터페이스 생성

//JpaRepository를 extends하면 findAll, save등의 메소드를 기본적으로 사용가능
public interface UserRepository extends JpaRepository<User, Long> 
{
	//EntityGraph 어노테이션은 쿼리가 수행 될 때 Lazy조회가 아닌 Eager조회로 authorities정보를 같이 가져옴
	@EntityGraph(attributePaths = "authorities")
   
	//findOneWithAuthoritiesByUsername 메소드는 Username을 기준으로 User 정보를 가져올 때 권한정보도 같이 가져옴
	Optional<User> findOneWithAuthoritiesByUsername(String username);
}

