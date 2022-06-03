package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data //lombok setter getter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private Integer id;
    private String description;

    @JsonIgnore//외부에 데이터 노출 안하기
    // User : Post 관계는 1 : 0~N, User가 Main : Post가 Sub
    //주 테이블이 User, 참조하는 테이블이 Post
    @ManyToOne(fetch = FetchType.LAZY)
    //LAZY 지연로딩방식으로 사용자 Entity를 조회할때 매번 Post Entity가 같이 로딩되는 것이 아니라
    // Post 데이터가 로딩되는 시점에 필요한 사용자 데이터를 가져온다
    private User user;
}
