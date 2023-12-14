package com.study.querydsl.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //jpa 기본 생성자 필
@ToString(exclude ="team")
public class Member {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)//외래키 값 관리
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Member(String username) {
        this.username = username;
    }

    @Builder
    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    @Builder
    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    //== 연과관계편의매서드 ==
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}















