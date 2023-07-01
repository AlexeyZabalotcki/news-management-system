package ru.clevertec.gateway_service.dto.news;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {

    @Schema(description = "News title", example = "Amazing title")
    private String title;

    @Schema(description = "News text", example = "Amazing text")
    private String text;

    @Schema(description = "News publication time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime time;

    @Schema(description = "Username", example = "journalist")
    private String username;
}
