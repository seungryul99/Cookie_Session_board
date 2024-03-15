package example.demo.service;


import example.demo.domain.Member;



public interface MemberService {


    // 회원가입 
    void saveMember(Member member);

    
    // 로그인
    void login(String loginId, String password);
}
