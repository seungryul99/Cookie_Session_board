package example.demo.service;


import example.demo.domain.Article;
import example.demo.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;

import java.util.Optional;


public interface MemberService {


    // 회원가입 
    void saveMember(Member member);

    
    // 로그인
    Member login(String loginId, String password);


    Optional<Member> findById(Long memberId);


    void expireJsessionId(HttpServletRequest request, HttpServletResponse response);

    void checkMemberPermission(HttpServletRequest request, Article article);
}
