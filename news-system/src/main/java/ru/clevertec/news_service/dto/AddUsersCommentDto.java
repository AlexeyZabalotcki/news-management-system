package ru.clevertec.news_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AddUsersCommentDto {

    @NotBlank(message = "Text cannot be blank")
    private String text;
    private String username;
}
