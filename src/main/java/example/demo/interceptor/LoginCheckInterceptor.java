package example.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 *  스프링 인터셉터를 사용하기 위해서는 HandlerInterceptor를 구현해 줘야 한다
 *  HandlerInterceptor 안에 preHandle, postHandle, afterCompletion 이 들어있음
 *
 *  로그인을 성공해서 세션 아이디를 쿠키에 가지고 있는지 체크
 */

// 로그인 되지 않은 사용자는 로그인 화면으로 튕겨버리는 가장 1번째 인터셉터
// 컨트롤러를 거칠 필요도 없으니 PreHandle만 구현해서 세션 없으면 입장불가




// 인터셉터의 역할은 컨트롤러에서 공통적으로 사용해야하는 로직이 너무 많을 때 공통처리를 위해서 사용하는 거라
// 굳이 1~2개 메소드가 이용하는 공통처리라고 보기 어려운 경우에는 그냥 컨트롤러 계층에서 처리해 버려도 상관이 없음    
public class LoginCheckInterceptor implements HandlerInterceptor {

    
    // 로그인을 했는지 안했는지만 체크할거라서 preHandle에서 체크 후 
    // 세션을 가지고 있지 않으면 index로 다시 보내버림
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response
                             , Object handler) throws Exception{

        HttpSession session = request.getSession(false);

        if(session == null){

            // redirect와 똑같은 기능이지만 인터셉터에서는 sendRedirect를 사용해줘야만 리다이렉트 가능
            // 내 게시판에서는 사용자가 로그인없이는 아무것도 할 수가 없어서 굳이 사용자가 어떤 활동을 하고 있던 곳으로 리다이렉트 해주지 않고
            // 그냥 로그인 판별화면으로 보내줌
            response.sendRedirect("/");
            return false;
        }


        // preHandle의 return이 true이면 진행, false면 중단
        return true;
    }

}
