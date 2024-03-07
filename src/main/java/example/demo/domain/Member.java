package example.demo.domain;

import example.demo.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 *  @Entity가 붙으면 JPA가 관리함
 *  JPA를 이용해서 테이블과 매핑할 클래스에 필수로 붙여줌
 *
 *  @Entity가 붙은 클래스는 기본생성자가 필수로 필요함
 *
 *  @Column (name = "속성이름")
 *
 *  @Id 기본키
 *
 *  @GeneratedValue 테이블 기본키 생성 전략
 *  IDENTITY 데이터 베이스에 위임 (Auto Increase)
 *  
 *  Entity에는 Setter가 있으면 안됨
 *
 *  @NoArgsConstructor는 기본생성자를 생성함, 엔티티에는 기본생성자가 필수로 필요함
 *  아무 생성자가 없다면 자동으로 기본생성자가 생성됨
 *
 *  어떤 파라미터가 있는 생성자가 추후 추가된다면 이 기본생성자를 따로 생성해줄 필요가 있어서
 *  미리 해주는 거임
 *
 *  근데 access = AccessLevel.PROTECTED가 붙었는데
 *  이러면 기본생성자를 생성하되 다른 패키지에 소속된 클래스는 접근을 불허한다는 말이됨
 *
 *  즉 protected Member(){} 를 생성하는 것과 같은 말이 됨
 *
 *  접근권한이 protected인 이유는 어차피 우리는 지연로딩을 쓸거고 실제 엔티티가 아닌
 *  프록시 객체를 통해서 조회가 될거고 프록시 객체를 사용하기위해 JPA 구현체가 실제 엔티티의
 *  기본 생성자를 통해 프록시 객체를 생성하는데 이때 private이면 생성이 안됨
 *  근데 그럼 public은 왜 안됨?? 접근권한이 public이면 JPA Entity Class 요구사항으로
 *  생긴 기본생성자를 마구 가져다 쓴 후 객체를 생성하고 setter로 값을 넣어서 쓸 수 있는데
 *  이를 막은 것이다, 무분별한 기본생성자 + setter 로 객체 만들어쓰기를 방지하려고
 *
 *  @Builder는 빌더패턴을 사용하기 위함, 생성자에 들어갈 매개 변수를 메서드로 하나하나 받아들이고
 *  마지막에 통합 빌드해서 객체를 생성하는 방식이다
 *  그냥 생성자를 사용하면 매개변수로 조합 할 수 있는게 너무 경우가 많아서 너무 많은 생성자가 생기고
 *  그냥 기본생성자로 만들고 setter를 통해서 필요한 값 주입하자 하면 객체 생성 시점에 모든 값들을
 *  주입하지 않아 객체가 초기화 될때 반드시 설정 될 값이 들어가지 않는 일관성 문제나 setter가 무분별로 노출되어
 *  setter 무분별 사용으로 불변성 문제를 겪을 수 있어서 빌더 패턴이 나옴
 *
 *  써브웨이마냥 필요한거 골라서 완성 ~ 하자는 것
 *
 *  빌더 패턴에 대한 자세한 내용은 굉장히 많은데
 *  Lombok의 @Builder로 굉장히 편하게 심플 빌더패턴을 다룰 수 있다
 *
 *  @Builder : 빌더 클래스와 이를 반환하는 build() 메서드를 생성함
 *  @AllArgsConstructor(access=AccessLevel.PRIVATE) : 빌더 어노테이션을 선언시
 *  전체 인자를 갖는 생성자를 자동으로 만드는데 이를 private 생성자로 설정함
 *  
 *  위에서 언급한것과 같은이유로 지금 @Builder 를 위해서 만든 생성자지 다른 쪽에서
 *  쓰라고 만든 생성자가 아니니까
 *
 */

/**
 *  @Builder
 *  @AllArgsConstructor(access = AccessLevel.PRIVATE)
 *
 *  얘네가 아래 생성자를 만들어 준거임
 *
 *    @Builder
 *    public Member(String memberId, String password, String nickname){
 *          this.memberId=memberId;
 *          this.password=password;
 *          this.nickname=nickname;
 *   }
 *
 *  이제 필요할때 원하는거 줍줍 하고 build() 하기만 하면 되는데
 *  문제는 @Builder로 빌더패턴을 구현하면 필수 파라미터 적용을 지정 할 수가 없음
 *
 *  따라서 객체 안에 별도의 builder() 정적 메서드를 구현해서 빌더 객체를 생성하기전에
 *  필수 파라미터를 설정하게 유도하고, 파라미터 검증로직도 추가해야함
 *
 *  근데 이건 기본내용은 아닌거 같으니 나중에 해보고 일단은 @Builder로 빌더패턴을 쓰자
 *

 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member extends BaseEntity {

    @Id @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;


    /**
     *  빌더 패턴 사용시 필수 파라미터 예외처리를 해줘야하는데 이건 나중에
     *  예외처리는 예외처리 할 때 같이하자
     */

    // 양방향 매핑 예시를 넣어둠, 혹시라도 회원이 쓴 댓글을 몰아서 조회할 일이 있을 수 있음
    // Comment 의 Member member를 연관관계의 주인으로 주겠다는 소리, 주인이 아닌쪽은 mappedby를 사용
    @OneToMany(mappedBy = "member")
    List<Comment> comments = new ArrayList<>();

}