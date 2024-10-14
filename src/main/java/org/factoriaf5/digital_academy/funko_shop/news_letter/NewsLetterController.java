package org.factoriaf5.digital_academy.funko_shop.news_letter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api-endpoint}/news-letter")
public class NewsLetterController {
    
    @Autowired
    private NewsLetterService newsLetterService;

    @PostMapping
    public ResponseEntity<NewsLetterDTO> createnNewsLetter(@Valid @RequestBody NewsLetterDTO newsLetterDTO) {

        NewsLetterDTO newNewsLetterDTO = newsLetterService.createNewsLetter(newsLetterDTO);

        if (newNewsLetterDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newNewsLetterDTO);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String code) {
        newsLetterService.deleteProduct(code);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@RequestParam String code) {
        newsLetterService.deleteProduct(code);
        return ResponseEntity.noContent().build();
    }

}
