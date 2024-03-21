package example.demo.controller;

import example.demo.domain.Article;
import example.demo.domain.Member;
import example.demo.service.ArticleService;
import example.demo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public String articles(Model model){

        // /articles라는 요청이 왔을 때, request에 session이 있는지 확인
        // 없으면 새로 만들어 주지 않고 null을 넣어줌
        // 만약 null이 들어있으면
        // 이게 다 권한이 없는 사용자가 URL 조작으로

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
    public String createArticleForm(){

        return "articleCreateForm";
    }

    /**
     *   게시물 생성하기
     *   POST, /article 요청이 들어오면 새로운 article을 생성해서 DB에 저장하고 PRG 패턴으로
     *   해당 article 조회로 리다이렉트 시켜줌, 새로고침을 하면 readArticle로 가게됨
     *   여기 @ModelAttribute가 들어가는게 맞을지 고민중, Model을 사용하지 않는 것 같음
     */
    
    /*
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
    */
    @PostMapping("/article")
    public String createArticle(@RequestParam (name = "title") String title,
                                @RequestParam (name = "content") String content,
                                HttpServletRequest request){

        // 지금 세션 저장소에 세션 id : member 레퍼런스로 저장이 되어 있다
        // 따라서 세션에서 세션 아이디를 통해 getAttribute를 해오면? 원하는 멤버의 객체가 들어있다
        // 문제는 페이지 마다 모든 권한을 이런식으로 하나하나 줄 수는 없다. 그래서 스프링의 인터셉터와 필터를 공부해 봐야 한다
        // 공통된 문제를 처리하기 위해서는 AOP를 알아야 하는데 로그인 권한 같은 웹 관련해서는 인터셉터나 필터가 있기 때문에 AOP는 아직 몰라도 됨
        // 필터는 서블릿이 제공하는 기능이고 인터셉터는 스프링 MVC가 제공하는 기능임
        // 필터는 프론트 컨트롤러 (디스패처 서블릿)으로 가기전에 적용된다면, 인터셉터는 그 후 적용됨, 스프링 MVC의 시작점이 디스패처 서블릿이기 때문
        // 인터셉터가 필터보다 훤씬 정밀한 설정이 가능함
        // 인터셉터에는 컨트롤러 실행 전에 수행되는 preHandle이 있고, 컨트롤러 실행후에 수행되는 postHandle도 있고 afterCompletion (뷰 랜더링 후)
        // 이렇게 단계적으로 수행할 수 있게 나눠둠

        HttpSession session=request.getSession();
        String sessionId = session.getId();
        Member member = (Member) session.getAttribute(sessionId);

        Article article = Article.builder()
                .member(member)
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