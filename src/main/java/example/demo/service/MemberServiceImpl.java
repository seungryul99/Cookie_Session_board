package example.demo.service;

import example.demo.domain.Article;
import example.demo.domain.Member;
import example.demo.dto.request.LoginRequest;
import example.demo.dto.request.SignupRequest;
import example.demo.exception.MemberPermissionMismatchException;
import example.demo.exception.PasswordMismatchException;
import example.demo.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Autowired
    private final MemberRepository memberRepository;


    @Override
    public void saveMember(SignupRequest signupRequest) {
        Member member = Member.builder()
                .loginId(signupRequest.getLoginId())
                .password(signupRequest.getPassword())
                .nickname(signupRequest.getNickname())
                .build();

        memberRepository.save(member);
    }

    @Override
    public Member login(LoginRequest loginRequest) {

        if (memberRepository.existsByLoginId(loginRequest.getLoginId())) {
            Member realMember = memberRepository.findByLoginId(loginRequest.getLoginId());
            checkPassword(loginRequest.getPassword(), realMember.getPassword());
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


    public void checkMemberPermission(HttpServletRequest request, Article article){
        HttpSession session = request.getSession(false);
        String sessionId = session.getId();
        Member member = (Member) session.getAttribute(sessionId);
        // long 이 아닌 Long Wrapper클래스 비교

        // articleId를 통해서 찾아온 memberId 즉 해당 게시글을 작성한 member의 Id와
        // 현재 세션 로그인 한 사용자의 세션을 통해 찾아온 member의 memberId가 일치하는지를 확인해야함
        // 세션이 없었으면 이미 공통처리에서 튕겼을 거니까 예외없이 진행

        if(!Objects.equals(article.getMember().getId(), member.getId())) {
            throw new MemberPermissionMismatchException();
        }
    }
}
