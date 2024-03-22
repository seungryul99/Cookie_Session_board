package example.demo.controller;


import example.demo.domain.Member;
import example.demo.dto.request.LoginRequest;
import example.demo.dto.request.SignupRequest;
import example.demo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;


    // 모두에게 허용, localhost8080:/ 에대한 누구나 날리는 요청
    @GetMapping("/")
    public String index(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if (session == null) return "redirect:/login";
        else return "redirect:/articles";
    }


    // 모두에게 허용, 회원가입폼으로 이동
    @GetMapping("/signup")
    public String signupForm(){
        return "signup";
    }

    /**
     *   이 부분은 @ModelAttribute로 해결 할 수도 있다
     *   예를들어 @ModelAttribute Member member로 처리 할 수도 있는데
     *
     *   이 기능은 HTTP Body에서 알아서 필드들을 setter를 이용해서 Member에 세팅해주고 Model에 Member를 넣어준다
     *
     *   문제는 회원가입을 하고 인덱스 화면으로 넘어가는데 사실 Model이 쓰이지 않는다
     *   이러면 의도치 않은 SideEffect가 발생할 수 있다, 그래서 그냥 @RequestParam으로 필요한것만 받아주는 걸로 결정
     */

    // 모두에게 허용, 권한 완료, 회원가입 요청
    @PostMapping("/signup")
    public String signup(@RequestParam("loginId") String loginId,
                         @RequestParam("password") String password,
                         @RequestParam("nickname") String nickname) {

        SignupRequest signupRequest = new SignupRequest(loginId,password,nickname);
        memberService.saveMember(signupRequest);

        return "redirect:/";
    }


    /**
     *  세션쿠키 (시간을 명시하지 않아 브라우저 종료시 로그아웃 되는 쿠키)를 사용하면
     *  보안 문제에 취약해서 쿠키 로그인 -> 세션 로그인으로 바꿔 줘야한다
     *
     *  클라이언트에서 로그인 정보를 주면 서버에서 관리하는 세션에서
     *  sessionId : value = {무작위값, Member 1} 이런식으로
     *  매칭을 해서 세션 저장소에 정보를 저장하고
     *
     *  클라이언트에게는 쿠키로 sessionId를 사용할 수 있게 해준다
     *
     *  그럼 클라이언트가 무작위 UUID인 세션 Id를 넘겨주면
     *  서버의 세션저장소에서 해당 세션 ID로 접근할 수 있는 개인 정보들에 접근해주는 형식
     *  세션 id가 털려도 그 자체로는 의미없는 UUID라서 이제 상관이 없음
     *
     *  참고로 쿠키는 String, 세션은 Object
     *
     *  세션은 쿠키를 사용하긴 하는데 중요한 정보를 서버에서 관리하겠다는 간단한 얘기임
     *  HttpSession 제공되는거 쓰면 됨
     *  쿠키 이름은 JSESSIONID, 값은 추정불가능한 랜덤값 (UUID 같은거)
     *  
     */

/*
    // 기존의 세션 쿠키 로그인 방식
    @PostMapping("/login")
    public String login(@RequestParam("loginId") String loginId,
                        @RequestParam("password") String password,
                        HttpServletRequest request,
                        HttpServletResponse response){

        // 일단 로그인 실패는 고려하지 않음
        Member loginMember = memberService.login(loginId,password);

        // 쿠키에 시간에 대한 정보를 주지 않으면 브라우저 종료시 종료되는 세션쿠키가 됨
        Cookie idCookie = new Cookie("memberId", loginMember.getId().toString());
        response.addCookie(idCookie);
        return "redirect:/articles";
    }
*/

    // 모두에게 허용, 권한 완료, 로그인 페이지로 이동
    @GetMapping("/login")
    public String loginview(){
        return "login";
    }


    // 모두에게 허용, 권한 완료, 로그인 요청
    @PostMapping("/login")
    public String login(@RequestParam("loginId") String loginId,
                        @RequestParam("password") String password,
                        HttpServletRequest request){

        /**
         * HTTPServletRequest의 내장 메소드
         * getSession(true) : 세션이 있으면 반환해주고 없으면 신규 세션을 생성해줌, default = true
         *
         * 로그인된 멤버를 세션 저장소에 저장해줌
         *
         * loginMember에서 로그인 된 Member 객체를 찾아오면 사용자가 날린 request에서 getSession을 통해서 세션이 있나 확인하고
         * 세션이 없으면 서버에서 새로 만들어줌, 이 때 HttpServletSession을 이용해서 세션을 생성해줌
         * 세션은 그냥 클래스 같은 거라고 생각해보자, 세션에도 세션을 식별하는 고유 아이디가 있는데 이게 JSESSIONID가 되고 UUID 마냥 랜덤 값으로 들어가니까,
         * 클라이언트에게 이 JSESSIONID를 넘겨주면됨, 근데 이거 서블릿을 사용하는 거라서 알아서 처리해줌 내가 response.addCookie 하면서 전달해 줄 필요는 없음
         * 서블릿이란 내가 핵심로직에만 집중하고 귀찮은건 대신해줌
         *
         * 그 후 session.setAttribute를 통해서 서버의 key:value로 되어있는 세션 저장소에  key로 쿠키에 있는 JsessionId를 통해서 지금의 경우
         * loginmember라는 객체를 넣어줬는데 실제로는 value에 해당 객체의 레퍼런스가 저장됨, 세션 자체는 메모리에 있음
         *
         * 여튼 세션아이디를 세션저장소의 키로, 원하는 값이나 객체를 value로 넣어줌
         */

        LoginRequest loginRequest = new LoginRequest(loginId,password);


        Member loginMember = memberService.login(loginRequest);

        HttpSession session = request.getSession();
        String sessionId = session.getId();
        session.setAttribute(sessionId,loginMember);
        // 로그인 실패는 우선 고려하지 않음

        if(loginMember==null) return "redirect:/login";
        else return "redirect:/articles";
    }


    /**
     *   쿠키는 클라이언트 쪽에 있는데 id를 통해서 찾아올 수 있는 방법도 없음, 세션은 서버가 관리하는데 쿠키는 아님
     *
     *   즉, 쿠키는 서버에서 관리 할 수 없다, 쿠키는 클라이언트 쪽에서 key value형태로 관리되는데 memberId = 1 이런식으로
     *   우선 response 가 쓰이는 이유는 서버에서 클라이언트의 쿠키를 세팅해주고 싶기 때문임
     *
     *   쿠키의 이름은 우리가 쓰던 memberId로 넣고 expireCookie를 돌리면
     *
     *   새로운 쿠키를 만들어서 클라이언트의 쿠키에 넣어버리면 키:밸류에서 밸류가 교체가됨
     *   왜 새로 쿠키를 만들어서 처리함?? 이럴 수 있긴한데
     *
     *   클라이언트의 쿠키를 서버에서 관리 할 방법이 없어서 이렇게 하는거고
     *   cookie.setMaxAge(0)으로 바로 만료 되게 만들어 두는 거임
     *
     *  HTTP세션은 원래 웹브라우저가 종료되면 자동으로 없어짐
     * 
     *   세션의 종료 시점은 언제로 두는게 좋을까??
     *   세션을 처음 생성한 시점부터 ~~분 이면, 멀쩡히 로그인 상태로 무언가 작업을 하다가
     *   30분 마다 재 로그인을 해줘야하는 상황이 된다
     *
     *   그래서 사용자가 서버에 최근에 요청한 시간을 기준으로 ~~분을 유지해주는 방식을 사용한다
     *
     *   근데 HttpSession을 이용하면 결국 세션 DB(저장소)를 사용하게 된다 세션은 메모리에 있지만
     *   세션 DB를 이용할 때마다 DB 자원을 사용하게 된다. 그래서 그 다음으로 나온게 토큰 로그인
     *   
     *   토큰 로그인 다음으로 나온게 JWT 로그인
     */
    
    /*
    // 기존 로그아웃 코드
    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expireCookie(response,"memberId");

        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName){
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    */


    // 권한 완료, 로그아웃은 따로 추가적인 처리가 필요해서 공통처리에서 안함
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        
        // false일 경우 세션이 없으면 새로 만들지 않음
        HttpSession session = request.getSession(false);
        
        // 세션이 있는 경우 세션을 제거함
        if(session != null){
            session.invalidate();
        }


        /**
         *  기존에 세션을 만들어서 사용하던 방식에서는 직접 세션저장소에 세션을 추가한 후
         *  addCookie를 이용해서 사용자의 쿠키저장소의 쿠키를 갱신해줬다
         *  근데 HttpSession이 들어오면서 이 과정을 자동으로 SetCookie를 해줬는데
         *  해제된 세션과 관련된 쿠키의 MaxAge를 0으로 설정함으로써
         *  쿠키를 없애버리는 과정을 자동으로 해주지는 않아서 쿠키를 모두 삭제해 버렸다
         *
         *  애초에 그런기능 없이 만들어 졌다고 하니까 구현해서 사용하자
         */

        memberService.expireJsessionId(request, response);

        return "redirect:/";
    }

}