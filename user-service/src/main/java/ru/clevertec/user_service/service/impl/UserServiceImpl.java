package ru.clevertec.user_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.user_service.dao.UserRepository;
import ru.clevertec.user_service.dto.AuthenticationResponseDto;
import ru.clevertec.user_service.dto.InputUserDto;
import ru.clevertec.user_service.dto.LoginDto;
import ru.clevertec.user_service.dto.RegisterDto;
import ru.clevertec.user_service.dto.UserDto;
import ru.clevertec.user_service.mapper.Mapper;
import ru.clevertec.user_service.model.Role;
import ru.clevertec.user_service.model.User;
import ru.clevertec.user_service.security.JwtService;
import ru.clevertec.user_service.service.UserService;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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

    private User getUser(RegisterDto request) {
        return User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.SUBSCRIBER)
                .build();
    }

    @Override
    public AuthenticationResponseDto authenticate(LoginDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Check email " + request.getUsername()));
        var jwtAccessToken = jwtService.generateAccessToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponseDto.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(mapper::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long id) {
        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Check user's id " + id)))
                .map(mapper::userToDto)
                .orElse(null);
    }

    @Override
    public UserDto save(Role role, InputUserDto user) {
        User entity = mapper.userToEntity(user);
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        entity.setRole(role);
        return mapper.userToDto(userRepository.save(entity));
    }

    @Override
    public UserDto update(Long id, InputUserDto updates) {
        log.info("UserService update INPUT {} {} ", id, updates);
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Check user's id " + id));

        try {
            Map<String, String> map = userToMap(updates);

            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Field field = User.class.getDeclaredField(key);
                field.setAccessible(true);

                if (key.equals("password")) {
                    String encodedPassword = passwordEncoder.encode(value);
                    field.set(userToUpdate, encodedPassword);
                } else {
                    field.set(userToUpdate, value);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            throw new RuntimeException("Failed to update user", ex);
        }
        User savedUser = userRepository.save(userToUpdate);

        return mapper.userToDto(savedUser);
    }

    private Map<String, String> userToMap(InputUserDto updates) throws IllegalAccessException {
        Map<String, String> values = new HashMap<>();

        Field[] fields = updates.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(updates);
            if (value != null) {
                values.put(field.getName(), value.toString());
            }
        }
        return values;
    }

    @Override
    public User findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Check user's username " + username)))
                .orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
