package example.demo.controller;


import example.demo.domain.Member;
import example.demo.service.MemberService;
import example.demo.service.MemberServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupForm(){
        return "signup";
    }

    /**
     *   이 부분은 @ModelAttribute로 해결 할 수도 있다
     *   예를들어 @ModelAttribute Member member로 처리 할 수도 있는데
     *
     *   이 기능은 HTTP Body에서 알아서 필드들을 setter를 이용해서 Member에 세팅해주고 Model에 Member를 넣어준다
     *
     *   문제는 회원가입을 하고 인덱스 화면으로 넘어가는데 사실 Model이 쓰이지 않는다
     *   이러면 의도치 않은 SideEffect가 발생할 수 있다, 그래서 그냥 @RequestParam으로 필요한것만 받아주는 걸로 결정
     */
    @PostMapping("/signup")
    public String signup(@RequestParam("loginId") String loginId,
                         @RequestParam("password") String password,
                         @RequestParam("nickname") String nickname) {

        Member member = Member.builder()
                .loginId(loginId)
                .password(password)
                .nickname(nickname)
                .build();

        memberService.saveMember(member);

        return "redirect:/";
    }


    @PostMapping("/login")
    public String login(@RequestParam("loginId") String loginId,
                        @RequestParam("password") String password,
                        HttpServletResponse response){

        Member loginMember = memberService.login(loginId,password);
        
        // 쿠키에 시간에 대한 정보를 주지 않으면 브라우저 종료시 종료되는 세션쿠키가 됨
        Cookie idCookie = new Cookie("memberId", loginMember.getId().toString());
        response.addCookie(idCookie);

        return "redirect:/articles";
    }


    /**
     *   쿠키는 클라이언트 쪽에 있는데 id를 통해서 찾아올 수 있는 방법도 없음, 세션은 서버가 관리하는데 쿠키는 아님
     *
     *   즉, 쿠키는 서버에서 관리 할 수 없다, 쿠키는 클라이언트 쪽에서 key value형태로 관리되는데 memberId = 1 이런식으로
     *   우선 response 가 쓰이는 이유는 서버에서 클라이언트의 쿠키를 세팅해주고 싶기 때문임
     *
     *   쿠키의 이름은 우리가 쓰던 memberId로 넣고 expireCookie를 돌리면
     *
     *   새로운 쿠키를 만들어서 클라이언트의 쿠키에 넣어버리면 키:밸류에서 밸류가 교체가됨
     *   왜 새로 쿠키를 만들어서 처리함?? 이럴 수 있긴한데
     *
     *   클라이언트의 쿠키를 서버에서 관리 할 방법이 없어서 이렇게 하는거고
     *   cookie.setMaxAge(0)으로 바로 만료 되게 만들어 두는 거임
     */
    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expireCookie(response,"memberId");

        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName){
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}