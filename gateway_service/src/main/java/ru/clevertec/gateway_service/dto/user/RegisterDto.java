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
@Schema(title = "RegisterDto", description = "User for registration")
public class RegisterDto {

    @Schema(description = "user username", example = "user1")
    private String username;
    @Schema(description = "user password", example = "12345678")
    private String password;
}
