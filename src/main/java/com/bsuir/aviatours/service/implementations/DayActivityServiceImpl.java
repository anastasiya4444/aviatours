package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.DayActivity;
import com.bsuir.aviatours.model.DayActivityId;
import com.bsuir.aviatours.repository.DayActivityRepository;
import com.bsuir.aviatours.repository.TourHotelRepository;
import com.bsuir.aviatours.service.interfaces.MEntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DayActivityServiceImpl implements MEntityService<DayActivity, DayActivityId> {

    private final DayActivityRepository tourHotelRepository;

    public DayActivityServiceImpl(DayActivityRepository tourHotelRepository) {
        this.tourHotelRepository = tourHotelRepository;
    }

    @Override
    public DayActivity saveEntity(DayActivity obj) {
        return tourHotelRepository.save(obj);
    }

    @Override
    public List<DayActivity> getAllEntities() {
        return tourHotelRepository.findAll();
    }

    @Override
    public DayActivity updateEntity(DayActivity obj) {
        if (!tourHotelRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Entity with ID " + obj.getId() + " not found");
        }
        return tourHotelRepository.save(obj);
    }

    @Override
    public void deleteEntity(DayActivity obj) {
        tourHotelRepository.delete(obj);
    }

    @Override
    public DayActivity findEntityById(int id) {
        return null;
    }

    @Override
    public DayActivity findEntityById(DayActivityId id) {
        return tourHotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + id + " not found"));
    }
}
