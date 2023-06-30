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
public class NewsDto {

//    private Long id;
    private String title;
    private String text;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime time;
    private String username;
}
