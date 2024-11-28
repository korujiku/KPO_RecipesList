package com.example.RecipesList.recipe.service;

import com.example.RecipesList.recipe.exception.UserNotFoundException;
import com.example.RecipesList.recipe.model.UserModel;
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

    public UserModel findByLogin(String login){
        return userRepository.findOneByLoginIgnoreCase(login);
    }

    @Transactional
    public UserModel addUser(String login, String password, String passwordConfirm){
        return createUser(login, password, passwordConfirm, UserRole.USER);
    }

    @Transactional
    public UserModel addUser(UserSignupDto userSignupDto){
        if(findByLogin(userSignupDto.getLogin())!=null){
            throw new ValidationException(String.format("UserModel '%s' already exists", userSignupDto.getLogin()));
        }
        if (!Objects.equals(userSignupDto.getPassword(), userSignupDto.getPasswordConfirm())) {
            throw new ValidationException("Passwords not equals");
        }
        final UserModel userModel = new UserModel(userSignupDto);
        validatorUtil.validate(userModel);
        return userRepository.save(userModel);
    }

    public UserModel createUser(String login, String password, String passwordConfirm, UserRole role){
        if (findByLogin(login) != null) {
            throw new ValidationException(String.format("UserModel '%s' already exists", login));
        }
        final UserModel userModel = new UserModel(login, passwordEncoder.encode(password), role);
        validatorUtil.validate(userModel);
        if (!Objects.equals(password, passwordConfirm)) {
            throw new ValidationException("Passwords not equals");
        }
        return userRepository.save(userModel);
    }

    @Transactional(readOnly = true)
    public UserModel findUser(Long id) {
        final Optional<UserModel> user = userRepository.findById(id);

        return user.orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<UserModel> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserModel updateUser(Long id, String login, String password) {
        final UserModel currentUserModel = findUser(id);
        currentUserModel.setLogin(login);
        currentUserModel.setPassword(password);
        validatorUtil.validate(currentUserModel);
        return userRepository.save(currentUserModel);
    }

    @Transactional
    public UserModel deleteUser(Long id) {
        final UserModel userModel = findUser(id);
        userRepository.deleteById(id);
        return userModel;
    }

    @Transactional
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserModel userModelEntity = findByLogin(username);
        if ( userModelEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(
                userModelEntity.getLogin(), userModelEntity.getPassword(), Collections.singleton(userModelEntity.getRole()));
    }
}
