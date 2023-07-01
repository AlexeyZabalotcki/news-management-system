package ru.clevertec.gateway_service.dto.news;

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
public class AddUsersCommentDto {

    @Schema(description = "Comment text", example = "Some text")
    private String text;

    @Schema(description = "Username", example = "subscriber")
    private String username;
}
