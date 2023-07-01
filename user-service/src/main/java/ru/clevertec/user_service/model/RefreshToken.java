package ru.clevertec.user_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "refresh_tokens")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_tokens_seq")
    @SequenceGenerator(name = "refresh_tokens_seq", sequenceName = "refresh_tokens_seq", allocationSize = 1)
    private Long id;
    private String token;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
