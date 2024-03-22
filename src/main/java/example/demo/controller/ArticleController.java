package example.demo.controller;

import example.demo.domain.Article;
import example.demo.domain.Member;
import example.demo.exception.MemberPermissionMismatchException;
import example.demo.service.ArticleService;
import example.demo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
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


    // 로그인 된 모두에게 허용, 권한 완료, 모든 게시글 리스트로 모아 보기
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



    // 로그인 된 모두에게 허용, 권한 완료, 특정 게시글 조회하기
    @GetMapping("/article/{articleId}")
    public String readArticle(@PathVariable("articleId") Long articleId, Model model,
                              RedirectAttributes redirectAttributes,
                              HttpServletRequest request){
        Article article = articleService.getArticle(articleId);
        model.addAttribute("article",article);

        
        // Service로 넘겨 줄 예정
        HttpSession session = request.getSession(false);
        String sessionId = session.getId();
        Member member = (Member) session.getAttribute(sessionId);

        if(Objects.equals(article.getMember().getId(), member.getId())) {
            model.addAttribute("status",true);
        }

        return "article";
    }

    /**
     * 로그인에 대한 권한을 주는거는 필터나 인터셉터를 사용해야하고 어차피 세션 로그인을 하게 되면
     * 다시 다 바꿔 줘야 하기때문에 일단은 임시로 만들어 주겠음
     */


    // 로그인 된 모두에게 허용, 권한 완료, 게시글 생성 폼으로 이동
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


    // 로그인 된 모두에게 허용, 권한 완료, 게시글 생성하기
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


    /**
     *   자신의 게시글만 수정, 삭제할 수 있도록 하는 기능은
     *   타임리프를 이용해서 게시글의 MemberId와 현재 사용자의 MemberId가 같은 경우에만 화면에 수정하기 버튼이 나타나게 할 생각이고
     *
     *   수정하기 버튼으로 들어가면 삭제하기가 포함되어 있게 할 예정인데
     *
     *   이렇게 처리해도 될지는 의문이다
     *
     *   누군가 URL 조작으로 버튼이 안보이지만 뚫고 들어갈 수도 있지 않을까 생각이 들었음
     *
     *   찾아보니 화면단에서 타임리프로 수정하기 버튼 공개 여부 설정 + 실제 수정이후에도 권한 검증로직이 필요할 것 같다
     */

    // 로그인 된 사용자 중 자신의 게시글 에서만 허용, 권한 미 완료, 자신의 글 수정 하기

    // RedirectAttributes
    @GetMapping("/update/{articleId}")
    public String articleUpdateForm(@PathVariable(name = "articleId") Long articleId, Model model,
                                    HttpServletRequest request){

        // articleId를 통해서 article을 찾아오고 그안에있는 member의 memberId를 찾아와서 권한 검증에 이용함
        Article article = articleService.getArticle(articleId);
        memberService.checkMemberPermission(request, article);
        model.addAttribute(article);

        return "articleUpdateForm";
    }

    // 로그인 된 사용자 중 자신의 게시글 에서만 허용, 권한 미 완료, 자신의 글 수정 요청
    @PostMapping("/update")
    public String articleUpdate(@RequestParam (name = "articleId") Long articleId,
                                @RequestParam (name = "title")String title,
                                @RequestParam (name = "content") String content,
                                HttpServletRequest request,
                                Model model){

        HttpSession session = request.getSession(false);
        String sessionId = session.getId();
        Member member = (Member) session.getAttribute(sessionId);

        Article article = Article.builder()
                .id(articleId)
                .title(title)
                .content(content)
                .member(member).build();


        model.addAttribute(article);
        articleService.updateArticle(article);

        return "redirect:/article/"+article.getId();
    }



    // 로그인 된 사용자중 자신의 게시글에서만 허용, 권한 미 완료, 자신의 글 삭제하기
    @PostMapping("/delete")
    public String deleteArticle(@RequestParam(name = "articleId") Long articleId,
                                HttpServletRequest request){

        Article article = articleService.getArticle(articleId);
        memberService.checkMemberPermission(request, article);
        articleService.deleteArticle(articleId);
        return "redirect:/articles";
    }


}