package ru.clevertec.user_service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clevertec.user_service.builder.TestBuilder;
import ru.clevertec.user_service.builder.UserBuilder;
import ru.clevertec.user_service.builder.dto.*;
import ru.clevertec.user_service.dao.UserRepository;
import ru.clevertec.user_service.dto.*;
import ru.clevertec.user_service.exception.UsernameNotFoundException;
import ru.clevertec.user_service.mapper.Mapper;
import ru.clevertec.user_service.model.User;
import ru.clevertec.user_service.security.JwtService;
import ru.clevertec.user_service.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Mapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private static User user;
    private static UserDto userDto;
    private static AuthenticationResponseDto authenticationResponseDto;
    private static RegisterDto registerDto;
    private static LoginDto loginDto;
    private static InputUserDto inputUserDto;


    @BeforeEach
    void setUp() {
        TestBuilder<User> userBuilder = new UserBuilder();
        user = userBuilder.build();
        TestBuilder<UserDto> userDtoBuilder = new UserDtoBuilder();
        userDto = userDtoBuilder.build();
        TestBuilder<AuthenticationResponseDto> authenticationResponseDtoBuilder = new AuthenticationResponseDtoBuilder();
        authenticationResponseDto = authenticationResponseDtoBuilder.build();
        TestBuilder<RegisterDto> registerDtoBuilder = new RegisterDtoBuilder();
        registerDto = registerDtoBuilder.build();
        TestBuilder<LoginDto> loginDtoBuilder = new LoginDtoBuilder();
        loginDto = loginDtoBuilder.build();
        TestBuilder<InputUserDto> inputUserDtoBuilder = new InputUserDtoBuilder();
        inputUserDto = inputUserDtoBuilder.build();
    }

    @ParameterizedTest
    @ValueSource(strings = "RU")
    void checkRegisterShouldReturnAuthenticationResponse() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateAccessToken(any(User.class))).thenReturn(authenticationResponseDto.getAccessToken());
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn(authenticationResponseDto.getRefreshToken());

        AuthenticationResponseDto actual = userService.register(registerDto);

        assertEquals(authenticationResponseDto, actual);
    }

    @Test
    void checkRegisterShouldThrowIllegalArgumentException() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        assertThrows(IllegalArgumentException.class, () -> userService.register(registerDto));
    }

    @Test
    void checkAuthenticateShouldReturnAuthenticationResponse() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateAccessToken(any(User.class))).thenReturn(authenticationResponseDto.getAccessToken());
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn(authenticationResponseDto.getRefreshToken());

        AuthenticationResponseDto actual = userService.authenticate(loginDto);

        assertEquals(authenticationResponseDto, actual);
    }

    @Test
    void checkAuthenticateShouldThrowUsernameNotFoundException() {
        when(userRepository.findByUsername(loginDto.getUsername())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.authenticate(loginDto));
    }

    @Test
    void checkAuthenticateShouldThrowIllegalArgumentException() {
        when(userRepository.findByUsername(loginDto.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> userService.authenticate(loginDto));
    }

    @Test
    void checkFindAllShouldReturnAllUsers() {
        List<User> expectedList = new ArrayList<>(Collections.singletonList(user));
        List<UserDto> expectedDtoList = new ArrayList<>(Collections.singletonList(userDto));

        when(userRepository.findAll()).thenReturn(expectedList);
        when(mapper.userToDto(user)).thenReturn(userDto);

        List<UserDto> actual = userService.findAll();

        assertEquals(expectedDtoList, actual);
    }

    static Stream<Arguments> params() {
        return Stream.of(
                Arguments.of(1L),
                Arguments.of(2L),
                Arguments.of(3L),
                Arguments.of(4L)
        );
    }


    @ParameterizedTest
    @MethodSource("params")
    void checkFindByIdShouldReturnUser(Long id) {
        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));
        when(mapper.userToDto(user)).thenReturn(userDto);

        UserDto actual = userService.findById(id);

        assertNotNull(actual);
    }

    @ParameterizedTest
    @MethodSource("params")
    void checkFindByIdShouldThrowUsernameNotFoundException(Long id) {
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> userService.findById(id));
    }

    @Test
    void checkSaveShouldSaveUser() {
        when(mapper.userToEntity(inputUserDto)).thenReturn(user);
        when(passwordEncoder.encode(inputUserDto.getPassword())).thenReturn(inputUserDto.getPassword());
        when(userRepository.save(user)).thenReturn(user);
        when(mapper.userToDto(user)).thenReturn(userDto);

        UserDto actual = userService.save(inputUserDto);

        assertEquals(userDto.getUsername(), actual.getUsername());
        assertEquals(userDto.getRole(), actual.getRole());
    }

    @ParameterizedTest
    @MethodSource("params")
    void checkUpdateShouldUpdateUser(Long id) {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        user.setUsername("asdfg");

        UserDto actual = userService.update(id, inputUserDto);

        assertNotEquals(user, actual);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(user);

    }

    @Test
    void checkFindByUsernameShouldReturnExpectedUser() {
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(user));

        User actual = userService.findByUsername(userDto.getUsername());

        assertEquals(user, actual);

        verify(userRepository, times(1)).findByUsername(userDto.getUsername());
    }

    @Test
    void checkFindByUsernameShouldThrowUsernameNotFoundException() {
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> userService.findByUsername(userDto.getUsername()));
    }

    @ParameterizedTest
    @MethodSource("params")
    void checkDeleteByIdShouldDeleteUser(Long id) {
        doNothing().when(userRepository).deleteById(id);
        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));
        userService.deleteById(id);

        verify(userRepository, times(1)).deleteById(id);
    }
}
