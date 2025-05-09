package com.bsuir.aviatours;

import com.bsuir.aviatours.controller.ActivityController;
import com.bsuir.aviatours.dto.ActivityDTO;
import com.bsuir.aviatours.model.Activity;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivityControllerTest {

    @Mock
    private EntityService<Activity> activityEntityService;

    @InjectMocks
    private ActivityController activityController;

    private ActivityDTO testActivityDTO;
    private Activity testActivity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testActivityDTO = new ActivityDTO();
        testActivityDTO.setId(1);
        testActivityDTO.setDescription("Test Activity");

        testActivity = new Activity();
        testActivity.setId(1);
        testActivity.setDescription("Test Activity");
    }

    @Test
    void getAllActivities_ShouldReturnListOfActivities() {
        List<Activity> activities = Arrays.asList(
                testActivity,
                new Activity(2, "Another Activity")
        );

        when(activityEntityService.getAllEntities()).thenReturn(activities);

        List<ActivityDTO> result = activityController.getAll();

        assertEquals(2, result.size());
        assertEquals("Test Activity", result.get(0).getDescription());
        assertEquals("Another Activity", result.get(1).getDescription());
        verify(activityEntityService, times(1)).getAllEntities();
    }

    @Test
    void getActivityById_WhenExists_ShouldReturnActivity() {
        when(activityEntityService.findEntityById(1)).thenReturn(testActivity);

        ResponseEntity<ActivityDTO> response = activityController.getById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        assertEquals("Test Activity", response.getBody().getDescription());
        verify(activityEntityService, times(1)).findEntityById(1);
    }

    @Test
    void getActivityById_WhenNotExists_ShouldThrowException() {
        when(activityEntityService.findEntityById(99)).thenThrow(new RuntimeException("Activity not found"));

        assertThrows(RuntimeException.class, () -> activityController.getById(99));
        verify(activityEntityService, times(1)).findEntityById(99);
    }

    @Test
    void deleteActivity_WhenExists_ShouldReturnSuccessMessage() {
        when(activityEntityService.findEntityById(1)).thenReturn(testActivity);
        doNothing().when(activityEntityService).deleteEntity(testActivity);

        ResponseEntity<String> response = activityController.delete(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Activity deleted successfully", response.getBody());
        verify(activityEntityService, times(1)).findEntityById(1);
        verify(activityEntityService, times(1)).deleteEntity(testActivity);
    }
}
