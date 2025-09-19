package com.snapp.billsplitter.infrastructure.service.auth;


import com.snapp.billsplitter.infrastructure.service.messages.MessageHelper;
import com.snapp.billsplitter.infrastructure.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MessageHelper messageHelper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new UserDetails(user.getId(), user.getUsername(), user.getPassword()))
                .orElseThrow(() -> new UsernameNotFoundException(messageHelper.getMessage("error.auth.bad.data.username.not.found", new Object[]{
                        username
                })));
    }
}