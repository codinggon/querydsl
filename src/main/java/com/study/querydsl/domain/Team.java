package com.study.querydsl.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //jpa 기본 생성자 필
@ToString(exclude ="members")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;
    private String name;
    private int age;

    @OneToMany(mappedBy = "team")//외래키 값을 update하지 않음
    private List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }





}















