package ckugroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ckugroup.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> 
{
}
