package example.demo.service;


import example.demo.domain.Article;
import example.demo.domain.Member;
import example.demo.dto.request.LoginRequest;
import example.demo.dto.request.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;

import java.util.Optional;


public interface MemberService {


    // 회원가입
    void saveMember(SignupRequest signupRequest);

    // 로그인
    Member login(LoginRequest loginRequest);

    Optional<Member> findById(Long memberId);


    void expireJsessionId(HttpServletRequest request, HttpServletResponse response);

    void checkMemberPermission(HttpServletRequest request, Article article);
}
