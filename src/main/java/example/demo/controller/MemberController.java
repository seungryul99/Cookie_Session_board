package example.demo.controller;


import example.demo.domain.Member;
import example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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


    // 검증, 예외 처리는 나중에

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
                        @RequestParam("password") String password){

        memberService.login(loginId,password);

        return "redirect:/";
    }

}