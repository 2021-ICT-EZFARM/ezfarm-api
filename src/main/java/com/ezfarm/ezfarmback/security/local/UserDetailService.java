package com.ezfarm.ezfarmback.security.local;

import com.ezfarm.ezfarmback.security.UserPrincipal;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("유저를 찾을 수 없습니다. email: " + email)
                );
        return UserPrincipal.create(findUser);
    }
}
