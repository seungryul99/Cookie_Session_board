package example.demo.domain;

import example.demo.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


/**
 *   @Lob에는 지정할수 있는 속성이 없음
 *   매핑하는 필드 타입이 문자면 CLOB
 *   나머지는 BLOB
 */

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor (access = AccessLevel.PRIVATE)
public class Article extends BaseEntity {

    @Id @Column (name = "article_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "title")
    private String title;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


}
