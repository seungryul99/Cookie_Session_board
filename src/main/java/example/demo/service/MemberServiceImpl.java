package example.demo.service;

import example.demo.domain.Member;
import example.demo.exception.PasswordMismatchException;
import example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Autowired
    private final MemberRepository memberRepository;


    @Override
    public void saveMember(Member member){
        memberRepository.save(member);
    }

    @Override
    public void login(String loginId, String password) {

        if(memberRepository.existsByLoginId(loginId)){
            Member realMember = memberRepository.findByLoginId(loginId);
            checkPassword(password, realMember.getPassword());
        }
    }

    public void checkPassword(String password, String realPassword){
        if(!realPassword.equals(password)){
            throw new PasswordMismatchException();
        }
        
        
        // 로그인 성공한 사용자에게 권한을 주는 방법을 따로 공부해야함, 세션 로그인으로
    }


}
