package org.factoriaf5.digital_academy.funko_shop.news_letter;

import java.security.SecureRandom;
import java.util.stream.Collectors;

import org.factoriaf5.digital_academy.funko_shop.email.EmailService;
import org.factoriaf5.digital_academy.funko_shop.product.product_exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NewsLetterService {

    private final NewsLetterRepository newsLetterRepository;
    private final EmailService emailService;

    public NewsLetterService(NewsLetterRepository newsLetterRepository, EmailService emailService) {
        this.newsLetterRepository = newsLetterRepository;
        this.emailService = emailService;
    }

    public NewsLetterDTO createNewsLetter(NewsLetterDTO newsLetterDTO) {

        if (newsLetterRepository.existsByEmail(newsLetterDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        NewsLetter newsLetter = mapToEntity(newsLetterDTO);

        NewsLetter savedNewsLetter = newsLetterRepository.save(newsLetter);

        String unsubscribeLink = "http://localhost:5173/unsubscribe?code=" + savedNewsLetter.getCode();

        emailService.sendSubscriptionEmail(newsLetterDTO.getEmail(), unsubscribeLink);

        return mapToDTO(savedNewsLetter);

    }

    public void deleteProduct(String code) {
        NewsLetter newsLetter = newsLetterRepository.findByCode(code);
        if (newsLetter == null) {
            throw new ProductNotFoundException("Product not found with code: " + code);
        }
        newsLetterRepository.deleteById(newsLetter.getId());
    }

    private NewsLetter mapToEntity(NewsLetterDTO dto) {

        String code = new SecureRandom().ints(8, 0, 36)
                .mapToObj(i -> Integer.toString(i, 36))
                .collect(Collectors.joining()).toUpperCase(

                );
        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setCode(code);
        newsLetter.setEmail(dto.getEmail());

        return newsLetter;
    }

    private NewsLetterDTO mapToDTO(NewsLetter newsLetter) {
        return new NewsLetterDTO(
                newsLetter.getId(),
                newsLetter.getCode(),
                newsLetter.getEmail(),
                newsLetter.getCreatedAt());
    }
}
