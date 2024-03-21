package example.demo.service;

import example.demo.domain.Member;
import example.demo.exception.PasswordMismatchException;
import example.demo.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Autowired
    private final MemberRepository memberRepository;


    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member login(String loginId, String password) {

        if (memberRepository.existsByLoginId(loginId)) {
            Member realMember = memberRepository.findByLoginId(loginId);
            checkPassword(password, realMember.getPassword());
            return realMember;
        }
        return null;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }


    public void checkPassword(String password, String realPassword) {
        if (!realPassword.equals(password)) {
            // 수정할 예정
            throw new PasswordMismatchException();

        }
    }


    @Override
    public void expireJsessionId(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break; // 원하는 쿠키를 찾았으므로 루프 종료
                }
            }
        }

    }
}
