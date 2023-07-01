package ru.clevertec.gateway_service.dto.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class NewsPageDto {

    private Integer number;
    private Integer size;
    private Integer allPages;
    private Long allElements;
    private Integer elementsCount;
    private Boolean first;
    private Boolean last;
    private List<NewsDto> content;
}
