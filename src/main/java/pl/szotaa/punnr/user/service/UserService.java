package pl.szotaa.punnr.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.szotaa.punnr.user.domain.Role;
import pl.szotaa.punnr.user.domain.User;
import pl.szotaa.punnr.user.exception.UserAlreadyExistsException;
import pl.szotaa.punnr.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setIsActive(Boolean.TRUE);
        userRepository.save(user);
    }
}
