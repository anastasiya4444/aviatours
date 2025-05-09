package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.TourAdditionalService;
import com.bsuir.aviatours.model.TourAdditionalServiceId;
import com.bsuir.aviatours.repository.TourAdditionalServiceRepository;
import com.bsuir.aviatours.service.interfaces.MEntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourAdditionalServiceImpl implements MEntityService<TourAdditionalService, TourAdditionalServiceId> {

    private final TourAdditionalServiceRepository tourHotelRepository;

    public TourAdditionalServiceImpl(TourAdditionalServiceRepository tourHotelRepository) {
        this.tourHotelRepository = tourHotelRepository;
    }

    @Override
    public TourAdditionalService saveEntity(TourAdditionalService obj) {
        return tourHotelRepository.save(obj);
    }

    @Override
    public List<TourAdditionalService> getAllEntities() {
        return tourHotelRepository.findAll();
    }

    @Override
    public TourAdditionalService updateEntity(TourAdditionalService obj) {
        if (!tourHotelRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Entity with ID " + obj.getId() + " not found");
        }
        return tourHotelRepository.save(obj);
    }

    @Override
    public void deleteEntity(TourAdditionalService obj) {
        tourHotelRepository.delete(obj);
    }

    @Override
    public TourAdditionalService findEntityById(int id) {
        return null;
    }

    @Override
    public TourAdditionalService findEntityById(TourAdditionalServiceId id) {
        return tourHotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + id + " not found"));
    }
}
