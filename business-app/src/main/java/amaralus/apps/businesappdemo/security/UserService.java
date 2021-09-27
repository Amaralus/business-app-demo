package amaralus.apps.businesappdemo.security;

import amaralus.apps.businesappdemo.security.database.models.UserModel;
import amaralus.apps.businesappdemo.security.database.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static amaralus.apps.businesappdemo.security.Role.USER;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Поиск пользователя [{}]", username);
        return userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("user [" + username + "] not found!"));
    }

    public boolean saveUser(UserModel user) {
        log.info("Сохранение пользователя [{}]", user.getUsername());
        if (userRepository.existsById(user.getUsername())) {
            log.warn("Пользователь [{}] уже существует", user.getUsername());
            return false;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(USER);

        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(String username) {
        log.info("Удаление пользователя [{}]", username);
        try {
            userRepository.deleteById(username);
            return true;
        } catch (EmptyResultDataAccessException ignored) {
            return false;
        }
    }
}
