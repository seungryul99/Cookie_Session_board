package example.demo.service;

import example.demo.domain.Article;
import example.demo.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


/**
 *      @Transactional : 트랜잭션을 사용해서 예외가 발생하면 롤백해줌
 *      지금 프로젝트에서는 굳이 필요없을 수도 있음, 왜냐하면 어떤 메소드에 이 어노테이션이 붙으면 순차적으로 실행중
 *      에러가 발생하면 그 메소드 실행한 부분을 전체를 롤백해 버리겠다는 거임, 근데 지금은 다 한줄로 이루어져 있음
 *
 *      
 *      readOnly = true를 클래스 전체로 걸어주면 좋음
 *      조회 기능만 트랜잭션으로 사용하면 비용이 많이 줄어들어 성능이 높아짐
 *      생성, 수정 같은 관리 기능을 사용할 메소드에만 추가적으로 @Transactional을 붙여주자
 *
 *
 *      @Service : 서비스
 *
 *      @RequiredArgsConstructor
 *      public ArticleServiceImpl(ArticleRepository articlerepository){
 *          this.articlerepository = articlerepository
 *      }
 *
 *      생성자쓰기 귀찮으면 이 어노테이션을 사용하자
 *      
 *      final이 붙은 필드의 생성자를 자동으로 생성해줌
 *
 *
 *      @Autowired가 생성자에 붙어있으면 생성자에 있는 객체 정보를 바탕으로 
 *      해당하는 스프링 빈이 있는지 타입레벨로 조회하고 찾은 후 DI 해줌
 *      같은 레벨의 여러가지 빈이 조회되면 @Qualify, @Primary로 조절
 *
 *      컴포넌트 스캔방식과 자동 DI를 사용하는 거임
 *      
 *      @Component가 붙은 스프링빈 클래스에 생성자가 단 1개라면 @Autowired가 생략되지만
 *      일단 지금은 다 명시해 주도록 하자
 *
 *      Spring Data JPA의 findById()는 Optional<T> 형태로 반환을 해주는데 Optional은 값이 있을수도 없을 수도 있음을 나타냄
 *     일단은 Null이 없을거라고 가정하고 임시방편으로 무조건 .get()으로 가져오겠다 처리해둔 후 공부후 수정할 예정
 *
 */



@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    @Autowired
    private final ArticleRepository articleRepository;

    @Transactional
    @Override
    public void writeArticle(Article article) {
        articleRepository.save(article);
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    // 임시 방편후 Optional + 람다식 공부해서 다시 처리할 예정
    // 그냥 이렇게해버리면 Null인경우 서버 다운됨, NullPointException
    @Override
    public Article getArticle(Long id){
        return articleRepository.findById(id).get();
    }

    @Transactional
    @Override
    public void updateArticle(Article article) {
        articleRepository.save(article);
    }

    @Transactional
    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}
