package com.example.RecipesList.recipe.service;

import com.example.RecipesList.recipe.exception.UserNotFoundException;
import com.example.RecipesList.recipe.model.User;
import com.example.RecipesList.recipe.model.UserRole;
import com.example.RecipesList.recipe.model.UserSignupDto;
import com.example.RecipesList.recipe.repository.UserRepository;
import com.example.RecipesList.util.validation.ValidatorUtil;
import jakarta.validation.ValidationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidatorUtil validatorUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ValidatorUtil validatorUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validatorUtil = validatorUtil;
    }

    public User findByLogin(String login){
        return userRepository.findOneByLoginIgnoreCase(login);
    }

    @Transactional
    public User addUser(String login, String password, String passwordConfirm){
        return createUser(login, password, passwordConfirm, UserRole.USER);
    }

    @Transactional
    public User addUser(UserSignupDto userSignupDto){
        if(findByLogin(userSignupDto.getLogin())!=null){
            throw new ValidationException(String.format("User '%s' already exists", userSignupDto.getLogin()));
        }
        if (!Objects.equals(userSignupDto.getPassword(), userSignupDto.getPasswordConfirm())) {
            throw new ValidationException("Passwords not equals");
        }
        final User user = new User(userSignupDto);
        validatorUtil.validate(user);
        return userRepository.save(user);
    }

    public User createUser(String login, String password, String passwordConfirm, UserRole role){
        if (findByLogin(login) != null) {
            throw new ValidationException(String.format("User '%s' already exists", login));
        }
        final User user = new User(login, passwordEncoder.encode(password), role);
        validatorUtil.validate(user);
        if (!Objects.equals(password, passwordConfirm)) {
            throw new ValidationException("Passwords not equals");
        }
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findUser(Long id) {
        final Optional<User> user = userRepository.findById(id);

        return user.orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(Long id, String login, String password) {
        final User currentUser = findUser(id);
        currentUser.setLogin(login);
        currentUser.setPassword(password);
        validatorUtil.validate(currentUser);
        return userRepository.save(currentUser);
    }

    @Transactional
    public User deleteUser(Long id) {
        final User user = findUser(id);
        userRepository.deleteById(id);
        return user;
    }

    @Transactional
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User userEntity = findByLogin(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(
                userEntity.getLogin(), userEntity.getPassword(), Collections.singleton(userEntity.getRole()));
    }
}
