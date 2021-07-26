package com.ezfarm.ezfarmback.common.db;

import com.ezfarm.ezfarmback.common.bestfarm.BestFarmService;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmGroup;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InitDb {

    private final EntityManager em;

    private final DbCleanUp dbCleanUp;

    private final BestFarmService bestFarmService;

    private final UserRepository userRepository;

    private final FarmRepository farmRepository;

    private final PasswordEncoder passwordEncoder;

    //@PostConstruct
    @Transactional
    public void init() throws Exception {

        dbCleanUp.afterPropertiesSet();
        dbCleanUp.clearUp();

        for (int i = 1; i <= 20; i++) {
            User user = User.builder()
                .email("user" + i + "@email.com")
                .name("유저" + i)
                .password(passwordEncoder.encode("1234"))
                .role(Role.ROLE_USER)
                .build();
            User savedUser = userRepository.save(user);
            for (int j = 1; j <= 5; j++) {
                Farm farm = Farm.builder()
                    .user(savedUser)
                    .isMain(false)
                    .farmGroup(FarmGroup.NORMAL)
                    .name(user.getName() + "의 농가" + j)
                    .build();
                farmRepository.save(farm);
            }
        }

        User admin = User.builder()
            .email("admin@email.com")
            .name("어드민")
            .password(passwordEncoder.encode("1234"))
            .role(Role.ROLE_ADMIN)
            .build();
        userRepository.save(admin);

        bestFarmService.saveTomatoVinyl();
        bestFarmService.saveTomatoGlass();
        bestFarmService.saveStrawberryVinyl();
        bestFarmService.savePaprikaVinyl();
        bestFarmService.savePaprikaGlass();
    }
}
