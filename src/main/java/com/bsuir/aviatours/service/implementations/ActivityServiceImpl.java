package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Activity;
import com.bsuir.aviatours.repository.ActivityRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements EntityService<Activity> {

    private final ActivityRepository activityRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public Activity saveEntity(Activity obj) {
        return activityRepository.save(obj);
    }

    @Override
    public List<Activity> getAllEntities() {
        return activityRepository.findAll();
    }

    @Override
    public Activity updateEntity(Activity obj) {
        if (!activityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Activity with ID " + obj.getId() + " not found");
        }
        return activityRepository.save(obj);
    }

    @Override
    public void deleteEntity(Activity obj) {
        activityRepository.delete(obj);
    }

    @Override
    public Activity findEntityById(int id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity with ID " + id + " not found"));
    }

}
