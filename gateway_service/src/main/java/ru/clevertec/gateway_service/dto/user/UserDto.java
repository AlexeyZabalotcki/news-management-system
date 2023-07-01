package ru.clevertec.gateway_service.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User DTO")
public class UserDto {

    @Schema(description = "Username", example = "johndoe")
    private String username;

    @Schema(description = "User role")
    private Role role;
}
