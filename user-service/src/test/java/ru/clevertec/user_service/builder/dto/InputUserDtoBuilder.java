package ru.clevertec.user_service.builder.dto;

import ru.clevertec.user_service.builder.TestBuilder;
import ru.clevertec.user_service.dto.InputUserDto;
import ru.clevertec.user_service.model.Role;


public class InputUserDtoBuilder implements TestBuilder<InputUserDto> {

//    private Long id = 1L;
    private String username = "new name";
    private String password = "12345678";
    private Role role = Role.SUBSCRIBER;

    @Override
    public InputUserDto build() {
        return InputUserDto.builder()
//                .id(this.id)
                .username(this.username)
                .password(this.password)
                .role(this.role)
                .build();
    }
}
