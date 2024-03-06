package example.demo.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


/**
 *   @CreatedDate : JPA에게 이 필드가 엔터티가 생성될때 자동으로 값이 설정됨을 알려줌
 *   @Column(updatable = false) : 생성일자가 수정되면 안됨
 *
 *   @LastModifiedDate : JPA에게 이 필드가 엔티티가 수정될때 자동으로 값이 설정되어야 함을 알려줌
 *
 *   BaseEntity는 실제 DB 상에는 올라가지 않음
 *   모든 클래스의 부모라 추상클래스로 구현함
 *
 *   @MappedSuperClass : 이 어노테이션은 JPA에게 이 클래스가 엔티티 클래스가 아니라고 알려줌
 *                       그래서 데이터베이스에 매핑되지 않음, 대신 BaseEntity의 필드들은 하위 엔티티 클래스들에게 상속됨
 *
 *   스프링 데이터 JPA에서는 Auditing이라는 기능을 제공하는데
 *   이를 사용하여 엔티티가 생성되고 변경되는 시점을 감지해서 생성, 수정시각이나 누가 수정했는지 기록 가능함
 *
 *   @EntityListeners를 적용해야 Auditing을 사용할 수 있음
 *   이 어노테이션이 엔티티의 변화를 감지하여 엔티티와 매핑된 테이블의 데이터를 조작함
 *   이 어노테이션의 파라미터에 이벤트 리스너를 넣어줘야하는데 여기에 AuditingEntityListener라는 클래스를 넣어줌
 *   이 클래스는 Spring Data JPA 에서 제공하는 이벤트 리스너로 엔티티의 영속, 수정 이벤트를 감지함
 *   
 */


@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)        
    LocalDateTime createdAt;

    @LastModifiedDate
    LocalDateTime updatedAt;
}