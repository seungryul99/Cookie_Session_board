package example.demo.repository;

import example.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByLoginId(String LoginId);

    Member findByLoginId(String LoginId);

}
