package example.demo.controller;

import example.demo.domain.Article;
import example.demo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ArticleController {

    @Autowired
    private final ArticleService articleService;


    /**
     *   전체 게시물 모아 보기
     *   GET , /articles 라는 요청이 들어오면
     *   DB에서 모든 article이 담긴 List인 articles를 반환 받고
     *   이를 모델에 저장함, articles.html(뷰) 에서 articles 라는 이름으로 참조해서 사용할 수 있음
     */
    @GetMapping("/articles")
    public String articles(Model model){
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles",articles);

        return "articles";
    }


    /**
     *   특정 게시물 상세 보기
     *   GET, /article/1 이런 형식의 요청이 들어오면 DB에서 해당 id를 가진 article을 반환받고
     *   이를 모델에 저장, article.html(뷰) dptj article이라는 이름으로 이를 참조 가능
     */
    @GetMapping("/article/{articleId}")
    public String readArticle(@PathVariable("articleId") Long articleId, Model model){
        Article article = articleService.getArticle(articleId);
        model.addAttribute("article",article);

        return "article";
    }


    /**
     *   게시물 생성하기
     *   POST, /article 요청이 들어오면 새로운 article을 생성해서 DB에 저장하고 PRG 패턴으로
     *   해당 article 조회로 리다이렉트 시켜줌, 새로고침을 하면 readArticle로 가게됨
     *   여기 @ModelAttribute가 들어가는게 맞을지 고민중, Model을 사용하지 않는 것 같음
     */
    @PostMapping("/article")
    public String createArticle(@ModelAttribute("article") Article article){
        articleService.writeArticle(article);

        return "redirect:articles/" + article.getId();
    }


    // update는 나중에 내 글 수정하기 기능으로 만들예정, delete도 마찬가지, 로그인, 권한 부여를 해야 가능

}