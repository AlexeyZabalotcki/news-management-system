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
@Schema(title = "LoginDto", description = "Dto for login")
public class LoginDto {

    @Schema(description = "User email", example = "admin")
    String username;

    @Schema(description = "User password", example = "12345678")
    String password;
}
