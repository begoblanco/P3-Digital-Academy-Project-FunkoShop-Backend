package org.factoriaf5.digital_academy.funko_shop.review;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long id;
    private int rating;
    private Long orderItem;
}
