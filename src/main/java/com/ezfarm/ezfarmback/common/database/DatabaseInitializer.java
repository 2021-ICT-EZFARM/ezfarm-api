package com.ezfarm.ezfarmback.common.database;

import com.ezfarm.ezfarmback.common.farm.PublicFarmParser;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmGroup;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DatabaseInitializer {
    
    private final DatabaseCleanUp databaseCleanUp;

    private final PublicFarmParser publicFarmParser;

    private final UserRepository userRepository;

    private final FarmRepository farmRepository;

    private final PasswordEncoder passwordEncoder;

    //@PostConstruct
    @Transactional
    public void init() throws Exception {
        databaseCleanUp.afterPropertiesSet();
        databaseCleanUp.clearUp();
        saveUserAndNormalFarm();
        saveAdminAndBestFarm();
    }

    private void saveUserAndNormalFarm() {
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
    }

    private void saveAdminAndBestFarm() throws Exception {
        User admin = User.builder()
            .email("admin@email.com")
            .name("어드민")
            .password(passwordEncoder.encode("1234"))
            .role(Role.ROLE_ADMIN)
            .build();
        userRepository.save(admin);

        publicFarmParser.saveTomatoVinyl();
        publicFarmParser.saveTomatoGlass();
        publicFarmParser.saveStrawberryVinyl();
        publicFarmParser.savePaprikaVinyl();
        publicFarmParser.savePaprikaGlass();
    }
}
