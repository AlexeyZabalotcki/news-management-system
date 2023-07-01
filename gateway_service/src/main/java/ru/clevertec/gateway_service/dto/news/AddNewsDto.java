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
public class AddNewsDto {

    @Schema(description = "News title", example = "Amazing title")
    private String title;

    @Schema(description = "News text", example = "Some new news")
    private String text;

    @Schema(description = "Username", example = "journalist")
    private String username;
}
