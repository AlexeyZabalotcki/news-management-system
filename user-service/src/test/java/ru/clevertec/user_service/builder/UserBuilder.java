package ru.clevertec.user_service.builder;

import ru.clevertec.user_service.model.Role;
import ru.clevertec.user_service.model.User;

public class UserBuilder implements TestBuilder<User> {

    private Long id = 1L;
    private String username = "admin";
    private String password = "12345678";
    private Role role = Role.ADMIN;

    @Override
    public User build() {
        return User.builder()
                .id(this.id)
                .username(this.username)
                .password(this.password)
                .role(this.role)
                .build();
    }
}
