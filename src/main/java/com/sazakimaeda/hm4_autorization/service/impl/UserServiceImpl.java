package com.sazakimaeda.hm4_autorization.service.impl;

import com.sazakimaeda.hm4_autorization.entity.Role;
import com.sazakimaeda.hm4_autorization.model.User;
import com.sazakimaeda.hm4_autorization.repository.UserRepository;
import com.sazakimaeda.hm4_autorization.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    @Transactional
    public User upgradeToPremium(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getRole() == Role.GUEST) {
            user.setRole(Role.PREMIUM_USER);
            userRepository.save(user);
        } else if (user.getRole() == Role.PREMIUM_USER) {
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }

        return user;
    }
}
