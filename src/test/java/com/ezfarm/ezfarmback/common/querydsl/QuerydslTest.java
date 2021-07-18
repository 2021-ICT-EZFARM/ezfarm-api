package com.ezfarm.ezfarmback.common.querydsl;

import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.common.acceptance.DbCleanUp;
import com.ezfarm.ezfarmback.user.domain.QUser;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Querydsl 테스트")
public class QuerydslTest {

    @Autowired
    EntityManager em;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DbCleanUp dbCleanUp;

    JPAQueryFactory queryFactory;

    User user;

    @BeforeEach
    void setUp() {
        dbCleanUp.afterPropertiesSet();
        dbCleanUp.clearUp();

        queryFactory = new JPAQueryFactory(em);

        user = User.builder()
            .name("테스트 유저1")
            .email("test1@email.com")
            .password("비밀번호")
            .role(Role.ROLE_USER)
            .build();
        userRepository.save(user);
    }

    @DisplayName("Q 타입 클래스가 생성된다.")
    @Test
    void createQType() {
        List<User> users = queryFactory.selectFrom(QUser.user)
            .fetch();

        Assertions.assertAll(
            () -> assertThat(users.size()).isEqualTo(1),
            () -> assertThat(users.get(0).getName()).isEqualTo(user.getName()),
            () -> assertThat(users.get(0).getEmail()).isEqualTo(user.getEmail())
        );
    }
}
