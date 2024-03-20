package example.demo.controller;

import example.demo.domain.Article;
import example.demo.domain.Member;
import example.demo.service.ArticleService;
import example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class ArticleController {

    @Autowired
    private final ArticleService articleService;

    @Autowired
    private final MemberService memberService;


    /**
     *   전체 게시물 모아 보기
     *   GET , /articles 라는 요청이 들어오면
     *   DB에서 모든 article이 담긴 List인 articles를 반환 받고
     *   이를 모델에 저장함, articles.html(뷰) 에서 articles 라는 이름으로 참조해서 사용할 수 있음
     */

    /**
     *      @CookieValue(name = "memberId", required = false) Long MemberId
     *      memberId 라는 쿠키를 꺼내와서 바인딩 해줌
     *      쿠키에 들어갈때 Long -> String 으로 넣어줬음
     */
    
    // 만약 쿠키를 가지고 있지 않은 사용자가 URL로 articles 에 뚫고 들어오려고 할 때 예외처리룰 위해서 required=false를 사용했는데 이게 맞는지는 모르겠다
    @GetMapping("/articles")
    public String articles(@CookieValue(name = "memberId", required = false) Long memberId, Model model){
        if (memberId==null){
            return "redirect:/";
        }
        ;
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
     * 로그인에 대한 권한을 주는거는 필터나 인터셉터를 사용해야하고 어차피 세션 로그인을 하게 되면
     * 다시 다 바꿔 줘야 하기때문에 일단은 임시로 만들어 주겠음
     */

    @GetMapping("/articleCreateForm")
    public String createArticleForm(@CookieValue(name = "memberId", required = false) Long memberId){
        if(memberId == null) return "redirect:/";

        return "articleCreateForm";
    }

    /**
     *   게시물 생성하기
     *   POST, /article 요청이 들어오면 새로운 article을 생성해서 DB에 저장하고 PRG 패턴으로
     *   해당 article 조회로 리다이렉트 시켜줌, 새로고침을 하면 readArticle로 가게됨
     *   여기 @ModelAttribute가 들어가는게 맞을지 고민중, Model을 사용하지 않는 것 같음
     */
    @PostMapping("/article")
    public String createArticle(@CookieValue(name = "memberId") Long memberId,
                                @RequestParam (name = "title") String title,
                                @RequestParam (name = "content") String content){

        Optional<Member> member = memberService.findById(memberId);
        Article article = Article.builder()
                .member(member.get())
                .title(title)
                .content(content)
                .build();

        articleService.writeArticle(article);

        return "redirect:/article/" + article.getId();
    }


    @GetMapping("/update/{articleId}")
    public String articleUpdateForm(@PathVariable(name = "articleId") Long articleId, Model model){

        Article article = articleService.getArticle(articleId);
        model.addAttribute(article);

        return "articleUpdateForm";
    }

    @PostMapping("/update")
    public String articleUpdate(@RequestParam (name = "articleId") Long articleId,
                                @RequestParam (name = "title")String title,
                                @RequestParam (name = "content") String content,
                                @CookieValue (name = "memberId") Long memberId,
                                Model model){

        Optional<Member> member = memberService.findById(memberId);
        Article article = Article.builder()
                .id(articleId)
                .title(title)
                .content(content)
                .member(member.get()).build();


        model.addAttribute(article);
        articleService.updateArticle(article);

        return "redirect:/article/"+article.getId();
    }


    @PostMapping("/delete")
    public String deleteArticle(@RequestParam(name = "articleId") Long articleId){

        articleService.deleteArticle(articleId);
        return "redirect:/articles";
    }


}