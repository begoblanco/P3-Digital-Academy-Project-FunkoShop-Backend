package org.factoriaf5.digital_academy.funko_shop.review;

import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
//import java.util.List;

@RestController
@RequestMapping("${api-endpoint}/reviews")
@PreAuthorize("hasRole('USER')")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> addReview(Principal connectedUser, @RequestBody ReviewDTO reviewDTO) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            reviewService.addReview(reviewDTO, user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<ReviewDTO> updateReview(Principal connectedUser, @RequestBody ReviewDTO reviewDTO) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        ReviewDTO updatedReview = reviewService.updateReview(reviewDTO, user);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @GetMapping("/{orderItemId}")
    public ResponseEntity<ReviewDTO> getReviewByIdAndUser(Principal connectedUser, @PathVariable Long orderItemId) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        ReviewDTO reviewDTO = reviewService.getReviewByOrderItemIdAndUser(orderItemId, user);
        return new ResponseEntity<ReviewDTO>(reviewDTO, HttpStatus.OK);
    }

}
