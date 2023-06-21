package ru.clevertec.gateway_service.user_ms;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User{

    private String username;
    private String role;
}
