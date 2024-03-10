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


    @GetMapping("/articles")
    public String articles(Model model){
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles",articles);

        return "articles";
    }


    @GetMapping("/article/{articleId}")
    public String readArticle(@PathVariable("articleId") Long articleId, Model model){
        Article article = articleService.getArticle(articleId);
        model.addAttribute("article",article);

        return "article";
    }


    @PostMapping("/article")
    public String createArticle(@ModelAttribute("article") Article article){
        articleService.writeArticle(article);

        return "redirect:articles";
    }


    // update는 나중에 내 글 수정하기 기능으로 만들예정, delete도 마찬가지, 로그인, 권한 부여를 해야 가능

}
