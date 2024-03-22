package example.demo.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// 엔티티에 직접 세터가 있으면 절대 안되고 DTO를 통해서 데이터를 이동하고 세팅해줘야 한다, 따라세 게터, 세터가 있고
// 실제 DTO를 사용할때 기본생성자로 생성해 두고 setting을 해도되고
// 모든 매개변수 생성자로 생성후 바로 생성자 파라미터에 넣어줘도 된다 취향 차이

// DTO 있는데 굳이 빌더 써야할까??
// DTO를 상황에 맞게 잘 나눠두면 빌더가 필요할까 라는생각이 들었는데 DTO 자체에는 @Entity가 붙어있지 않아서 DTO는 DB에 접속할 수 없어서
// 언젠가는 꼭 다시 Entity로 변환을 시켜줘야해서 빌더가 사용됨
// 추가적으로 가독성 문제도 있음, repository에 save(Entity) 할걸 save(parameter1, parameter2, .... ) 이래 되면 안됨

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    private String loginId;

    private String password;

    private String nickname;
}