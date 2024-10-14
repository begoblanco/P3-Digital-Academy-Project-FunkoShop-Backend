package org.factoriaf5.digital_academy.funko_shop.news_letter;

import java.util.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsLetterDTO {

    private Long id;
    private String code;
    private String email;
    private Date createdAt;
    
}