package ru.clevertec.news_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@ToString
@Table(name = "news")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "newsSeqGenerator")
    @SequenceGenerator(name = "newsSeqGenerator", sequenceName = "news_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    private String title;
    private String text;
    private LocalDateTime time;
}
