package example.demo.service;


import example.demo.domain.Member;

import java.util.Optional;


public interface MemberService {


    // 회원가입 
    void saveMember(Member member);

    
    // 로그인
    Member login(String loginId, String password);


    Optional<Member> findById(Long memberId);
}
