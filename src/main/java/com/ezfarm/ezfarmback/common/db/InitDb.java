package com.ezfarm.ezfarmback.common.db;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Profile("dev")
@Service
@RequiredArgsConstructor
public class InitDb {

    private final EntityManager em;

    private final UserRepository userRepository;

    private final FarmRepository farmRepository;

    private final PasswordEncoder passwordEncoder;

    //@PostConstruct
    @Transactional
    public void init() {
        for (int i = 1; i <= 20; i++) {
            User user = User.builder()
                .email("test" + i + "@email.com")
                .name("테스트 유저" + i)
                .password(passwordEncoder.encode("1234"))
                .role(Role.ROLE_USER)
                .build();
            User savedUser = userRepository.save(user);
            for (int j = 1; j <= 10; j++) {
                Farm farm = Farm.builder()
                    .user(savedUser)
                    .isMain(false)
                    .name(user.getName() + "의 테스트 농가" + j)
                    .build();
                farmRepository.save(farm);
            }
        }
    }
}
