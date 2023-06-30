package ru.clevertec.news_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

//    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime time;
    private String text;
    private String username;
    private Long newsId;
}
