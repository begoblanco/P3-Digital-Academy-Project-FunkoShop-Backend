package org.factoriaf5.digital_academy.funko_shop.news_letter;

import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor
@Table(name = "news_letter")
public class NewsLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_letter_id")
    private Long id;
    private String code;
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE) 
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}
