package example.demo.controller;


import example.demo.domain.Member;
import example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;



    @PostMapping("/signup")
    public String signup(@ModelAttribute Member member){
        memberService.saveMemberInDB(member);

        return "redirect:/";
    }

}
