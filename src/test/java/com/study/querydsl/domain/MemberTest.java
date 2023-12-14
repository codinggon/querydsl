package com.study.querydsl.domain;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.querydsl.dto.MemberDto;
import com.study.querydsl.dto.MemberTeamDto;
import com.study.querydsl.dto.QMemberDto;
import com.study.querydsl.repository.MemberRepository;
import com.study.querydsl.repository.MemberSearchCondition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import java.beans.Expression;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static com.study.querydsl.domain.QMember.*;
import static com.study.querydsl.domain.QTeam.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Commit
class MemberTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    JPAQueryFactory queryFactory;


    @BeforeEach
    public void before(){
         queryFactory = new JPAQueryFactory(em);




//            Team teamA = new Team("teamA");
//            Team teamB = new Team("teamB");
//            em.persist(teamA);
//            em.persist(teamB);
//
//            Member member1 = new Member("member1", 10, teamA);
//            Member member2 = new Member("member2", 20, teamA);
//            Member member3 = new Member("member3", 30, teamB);
//            Member member4 = new Member("member4", 40, teamB);
//            em.persist(member1);
//            em.persist(member2);
//            em.persist(member3);
//            em.persist(member4);
//
//            //init
//            em.flush();
//            em.clear();
    }

    @DisplayName("")
    @Test
    public void _테스트11() throws Exception{

        MemberSearchCondition condition = MemberSearchCondition.builder()
                .username(null)
                .teamName("teamA")
                .ageGoe(10)
                .ageLoe(null)
                .build();

        Pageable pageRequest = PageRequest.of(0, 3);
        Page<MemberTeamDto> memberTeamDtos = memberRepository.searchPageSimple(condition, pageRequest);
        for (MemberTeamDto memberTeamDto : memberTeamDtos) {
            System.out.println("memberTeamDto = " + memberTeamDto);
        }

    }

    @DisplayName("")
    @Test
    public void jpql_테스트() throws Exception{

        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @DisplayName("")
    @Test
    public void startQuerydsl() throws Exception{
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember m = new QMember("m");

        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @DisplayName("")
    @Test
    public void _테스트1() throws Exception{
        Member findMember = queryFactory.selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.between(10, 30)
                )
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /**
     * 1. 회원 나이 desc
     * 2. 회원 이름 asc
     * 단 2에서 회원이름이 없으면 마지막 출력(nulls last)
     */
    @DisplayName("")
    @Test
    public void resultFetch() throws Exception{

        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsFirst()
                )
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();

    }

    @DisplayName("")
    @Test
    public void _테스트() throws Exception{

        List<Member> list = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        assertThat(list.size()).isEqualTo(2);

    }


    @PersistenceUnit
    EntityManagerFactory emf;


    @DisplayName("")
    @Test
    public void group_테스트() throws Exception{

//        List<Tuple> result = queryFactory
//                .select(member.count(),
//                        member.age.sum(),
//                        member.age.avg(),
//                        member.age.max())
//                .from(member)
//                .fetch();
//
//        Tuple tuple = result.get(0);
//        tuple.get(member.count());

//        List<Tuple> result = queryFactory
//                .select(team.name, member.age.avg())
//                .from(member)
//                .join(member.team, team)
//                .groupBy(team.name)
//                .fetch();
//
//        Tuple teamA = result.get(0);
//        Tuple teamB = result.get(1);
//
//        assertThat(teamA.get(team.name)).isEqualTo("teamA");
//        assertThat(teamA.get(team.age.avg())).isEqualTo(15);


//        List<Member> result = queryFactory
//                .selectFrom(member)
//                .join(member.team, team)
//                .where(team.name.eq("teamA"))
//                .fetch();
//
//        assertThat(result)
//                .extracting("username")
//                .containsExactly("member1","member2");


//        List<Tuple> teamA = queryFactory
//                .select(member, team)
//                .from(member)
//                .leftJoin(member.team, team)
//                .on(team.name.eq("teamA"))
//                .fetch();
//
//        for (Tuple tuple : teamA) {
//            System.out.println("tuple = " + tuple);
//        }

//        Member findMember = queryFactory
//                .selectFrom(member)
//                .join(member.team, team).fetchJoin()
//                .where(member.username.eq("member1"))
//                .fetchOne();
//
//
//        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());

        QMember memberSub = new QMember("memberSub");

//        List<Member> result = queryFactory
//                .selectFrom(member)
//                .where(member.age.goe(
//                        JPAExpressions
//                                .select(memberSub.age.avg())
//                                .from(memberSub)
//                                .where(memberSub.age.gt(10))
//                ))
//                .fetch();

//        List<Tuple> result = queryFactory
//                .select(member.username,
//                        select(memberSub.age.avg())
//                                .from(memberSub)
//                )
//                .from(member)
//                .fetch();
//
//        for (Tuple tuple : result) {
//            System.out.println("tuple = " + tuple);
//        }


//        List<String> list = queryFactory
//                .select(member.age
//                        .when(10).then("10살")
//                        .when(20).then("20살")
//                        .otherwise("기타")
//                )
//                .from(member)
//                .fetch();
//
////        for (String s : list) {
////            System.out.println("s = " + s);
////        }
//
//        //조건 값이 when절 안에 있을때
//        List<String> fetch = queryFactory
//                .select(new CaseBuilder()
//                        .when(member.age.between(0, 20)).then("0-20")
//                        .when(member.age.between(21, 30)).then("나머지")
//                        .otherwise("기타")
//                )
//                .from(member)
//                .fetch();
//        for (String s : fetch) {
//            System.out.println("s = " + s);
//        }
//

//        List<String> fetch = queryFactory
//                .select(member.username.concat("_").concat(member.age.stringValue()))
//                .from(member)
//                .fetch();
//
//        for (String s : fetch) {
//            System.out.println("s = " + s);
//        }


//        List<MemberDto> list = queryFactory
//                .select(Projections.constructor(MemberDto.class, member.username, member.age))
//                .from(member)
//                .fetch();
//
//        for (MemberDto memberDto : list) {
//            System.out.println("memberDto = " + memberDto);
//        }


//        List<Tuple> tuples = queryFactory
//                .select(member.username, member.age)
//                .from(member)
//                .fetch();
//
//        for (Tuple tuple : tuples) {
//            System.out.println("tuple.age = " + tuple.get(member.age));
//        }


        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);


    }

    private List<Member> searchMember1(String usernameParam, Integer ageParam) {
        return null;
    }


}















