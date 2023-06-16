package ru.clevertec.news_service.dto;

import jakarta.validation.constraints.NotBlank;
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
public class AddCommentDto {

    @NotBlank(message = "Text cannot be blank")
    private String text;

    @NotBlank(message = "Username cannot be blank")
    private String username;
}
