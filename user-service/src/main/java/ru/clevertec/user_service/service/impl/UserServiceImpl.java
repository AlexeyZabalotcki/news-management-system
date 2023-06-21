package ru.clevertec.user_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.user_service.dao.UserRepository;
import ru.clevertec.user_service.dto.AuthenticationResponseDto;
import ru.clevertec.user_service.dto.InputUserDto;
import ru.clevertec.user_service.dto.LoginDto;
import ru.clevertec.user_service.dto.RegisterDto;
import ru.clevertec.user_service.dto.UserDto;
import ru.clevertec.user_service.exception.InvalidRoleException;
import ru.clevertec.user_service.exception.UsernameNotFoundException;
import ru.clevertec.user_service.mapper.Mapper;
import ru.clevertec.user_service.model.Role;
import ru.clevertec.user_service.model.User;
import ru.clevertec.user_service.security.JwtService;
import ru.clevertec.user_service.service.UserService;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponseDto register(RegisterDto request) {
        User user = getUser(request);
        String username = user.getUsername();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DataIntegrityViolationException("Username or email already exists");
        }
        userRepository.save(user);
        var jwtAccessToken = jwtService.generateAccessToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponseDto.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    @Override
    public AuthenticationResponseDto authenticate(LoginDto request) {
        log.info("authenticate {} ", request);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Check email " + request.getUsername()));
        log.info("find username {} ", user);
        var matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (matches) {
            var jwtAccessToken = jwtService.generateAccessToken(user);
            var jwtRefreshToken = jwtService.generateRefreshToken(user);
            return AuthenticationResponseDto.builder()
                    .accessToken(jwtAccessToken)
                    .refreshToken(jwtRefreshToken)
                    .build();
        } else {
            throw new IllegalArgumentException("Password is not valid");
        }
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(mapper::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(mapper::userToDto)
                .orElseThrow(() -> new UsernameNotFoundException("Check user's id " + id));
    }

    @Override
    @Transactional
    public UserDto save(Role role, InputUserDto user) {
        User entity = mapper.userToEntity(user);
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        entity.setRole(role);
        return mapper.userToDto(userRepository.save(entity));
    }

    @Override
    @Transactional
    public UserDto update(Long id, InputUserDto updates) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Check user's id " + id));
        try {
            Map<String, Object> map = userToMap(updates);

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Field field = User.class.getDeclaredField(key);
                field.setAccessible(true);

                if ("password".equals(key)) {
                    setPasswordField(userToUpdate, value);
                } else {
                    field.set(userToUpdate, value);
                }
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            throw new RuntimeException("Failed to update user", ex);
        }
        User savedUser = userRepository.save(userToUpdate);

        return mapper.userToDto(savedUser);
    }

    @Override
    public User findByUsername(String username) {
        log.info("find username {} ", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Check user's username " + username));

    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Check if user with id " + id + " exists"));
        userRepository.deleteById(id);
    }

    private void setPasswordField(User userToUpdate, Object value) {
        try {
            Field passwordField = User.class.getDeclaredField("password");
            passwordField.setAccessible(true);
            String encodedPassword = passwordEncoder.encode((String) value);
            passwordField.set(userToUpdate, encodedPassword);
            passwordField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            throw new RuntimeException("Failed to update password field", ex);
        }
    }

    private Map<String, Object> userToMap(InputUserDto updates) throws IllegalAccessException {
        Map<String, Object> values = new HashMap<>();

        Field[] fields = updates.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Optional.ofNullable(field.get(updates))
                    .ifPresent(value -> {
                        if ("role".equals(field.getName())) {
                            Role role = Role.valueOf(value.toString());
                            if (isValidRole(role)) {
                                values.put(field.getName(), role);
                            } else {
                                throw new InvalidRoleException("Invalid user role specified. Please choose a valid role: "
                                        + Arrays.toString(Role.values()));
                            }
                        } else {
                            values.put(field.getName(), value);
                        }
                    });
            field.setAccessible(false);
        }

        return values;
    }

    private boolean isValidRole(Role role) {
        return role == Role.SUBSCRIBER || role == Role.ADMIN || role == Role.JOURNALIST;
    }

    private User getUser(RegisterDto request) {
        return User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.SUBSCRIBER)
                .build();
    }
}
