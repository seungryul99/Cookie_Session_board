package example.demo.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// @Configuration이 붙은 클래스가 여러개가 있다면 이 클래스들의 모든 정보를 모아서
// 하나의 ApplicationContext가 됨, 고로 인터셉터 사용을 위해 만들어줘도 상관 없음
// 인터셉터는 체인으로 구성됨 order가 낮은 순서 부터 호출됨
// Http 요청 -> Was -> 필터 -> 서블릿 -> 인터셉터 1 -> 인터셉터 2 -> 컨트롤러

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LoginCheckInterceptor())    // 인터셉터 등록
                .order(1)   // 1순위
                .addPathPatterns("/**") //localhost8080:/ 아래 모든 애들에게 적용
                .excludePathPatterns("/error","/login", "/logout","/","/signup"); // 제외할거, 예외처리, 로그인 화면, localhost8080/
    }




}
