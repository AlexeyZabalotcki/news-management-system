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
public class CommentDto {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "Comment publication time")
    private LocalDateTime time;

    @Schema(description = "Comment text", example = "Some text")
    private String text;

    @Schema(description = "Username", example = "subscriber")
    private String username;

    @Schema(description = "News id")
    private Long newsId;
}
