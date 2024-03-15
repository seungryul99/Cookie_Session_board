package example.demo.controller;


import example.demo.domain.Member;
import example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;



    // 검증, 예외 처리는 나중에
    @PostMapping("/signup")
    public String signup(@ModelAttribute Member member){
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
