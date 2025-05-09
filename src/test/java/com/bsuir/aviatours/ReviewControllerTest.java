package com.bsuir.aviatours;

import com.bsuir.aviatours.controller.ReviewController;
import com.bsuir.aviatours.dto.ReviewDTO;
import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.repository.RoleRepository;
import com.bsuir.aviatours.service.implementations.ReviewServiceImpl;
import com.bsuir.aviatours.service.implementations.RoleServiceImpl;
import com.bsuir.aviatours.service.implementations.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import com.bsuir.aviatours.model.Review;
import com.bsuir.aviatours.model.User;
import com.bsuir.aviatours.dto.UserDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @Mock
    private ReviewServiceImpl reviewEntityService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReviewController reviewController;

    private ReviewDTO testReviewDTO;
    private Review testReview;
    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");
        testUser.setRole(new Role());

        testUserDTO = UserDTO.fromEntity(testUser);

        testReviewDTO = new ReviewDTO();
        testReviewDTO.setId(1);
        testReviewDTO.setRating((byte) 5);
        testReviewDTO.setReviewText("Great experience");
        testReviewDTO.setUser(testUserDTO);

        testReview = new Review();
        testReview.setId(1);
        testReview.setRating((byte) 5);
        testReview.setReviewText("Great experience");
        testReview.setUser(testUser);
    }

    @Test
    void getMyReviews_AuthenticatedUser_ShouldReturnReviews() {
        when(authentication.getName()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(reviewEntityService.findByUserId(1)).thenReturn(List.of(testReview));

        ResponseEntity<List<ReviewDTO>> response = reviewController.getMyReviews(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Great experience", response.getBody().get(0).getReviewText());
    }

    @Test
    void getMyReviews_UnauthenticatedUser_ShouldReturnUnauthorized() {
        when(authentication.getName()).thenReturn("unknown");
        when(userService.findByUsername("unknown")).thenReturn(null);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getMyReviews(authentication);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void createReview_UnauthenticatedUser_ShouldReturnUnauthorized() {
        when(authentication.getName()).thenReturn("unknown");
        when(userService.findByUsername("unknown")).thenReturn(null);

        ResponseEntity<ReviewDTO> response = reviewController.createReview(testReviewDTO, authentication);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void updateReview_NotOwner_ShouldReturnForbidden() {
        User otherUser = new User();
        otherUser.setId(2);
        otherUser.setUsername("otheruser");

        when(authentication.getName()).thenReturn("otheruser");
        when(userService.findByUsername("otheruser")).thenReturn(otherUser);
        when(reviewEntityService.findEntityById(1)).thenReturn(testReview);

        ResponseEntity<ReviewDTO> response = reviewController.updateReview(1, testReviewDTO, authentication);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void getAllReviews_ShouldReturnAllReviews() {
        Review secondReview = new Review();
        secondReview.setId(2);
        secondReview.setRating((byte) 3);
        secondReview.setReviewText("Average experience");
        secondReview.setUser(testUser);

        when(reviewEntityService.getAllEntities()).thenReturn(Arrays.asList(testReview, secondReview));

        ResponseEntity<List<ReviewDTO>> response = reviewController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getReviewById_ExistingId_ShouldReturnReview() {
        when(reviewEntityService.findEntityById(1)).thenReturn(testReview);

        ResponseEntity<ReviewDTO> response = reviewController.getById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void getReviewById_NonExistingId_ShouldReturnNotFound() {
        when(reviewEntityService.findEntityById(99)).thenReturn(null);

        ResponseEntity<ReviewDTO> response = reviewController.getById(99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
