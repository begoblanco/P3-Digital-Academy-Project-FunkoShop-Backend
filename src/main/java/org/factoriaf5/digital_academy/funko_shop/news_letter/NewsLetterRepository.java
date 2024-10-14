package org.factoriaf5.digital_academy.funko_shop.news_letter;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface NewsLetterRepository extends JpaRepository<NewsLetter, Long> {
    NewsLetter findByCode(String code);
    boolean existsByEmail(String email);
}
