package example.demo.repository;

import example.demo.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  SpringDataJPA 사용, 타입 + 기본키로 매칭
 *
 *  스프링 데이터 JPA가 기본적인 기능을 제공해 줄 수 있는건 JpaRepository에서 구현체를 제공해주는 걸 가져다 쓰기만 하면됨
 *
 *  만약 QueryDSL이 필요한 동적쿼리 처리가 필요하거나 제공하지 않는 기능들이 있다면
 *  ArticleRepositoryCustom 인터페이스를 만들어주고, ArticleRepositroy가 상속받게 한 후
 *  ArticleRepositoryCustomImpl 에서 구현해 주면 됨
 *
 *  지금 토이 프로젝트에서는 기본적인 기능만 사용할거라서 JpaRepository 선에서 커버가 될 듯하고
 *  어떤 기능을 구현해야하고 어떤 기능이 사용 가능한지는 추후 공부해보자
 *  
 *  
 *  JpaRepository에 @Repository가 들어있다 그래서 굳이 안붙여줘도됨
 *  하지만 쿼리Dsl을 위한 Impl을 만들었다면 꼭 붙여주자
 */

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
