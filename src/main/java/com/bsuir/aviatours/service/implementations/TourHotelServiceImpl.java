package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.TourHotel;
import com.bsuir.aviatours.model.TourHotelId;
import com.bsuir.aviatours.repository.TourHotelRepository;
import com.bsuir.aviatours.service.interfaces.MEntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourHotelServiceImpl implements MEntityService<TourHotel, TourHotelId> {

    private final TourHotelRepository tourHotelRepository;

    public TourHotelServiceImpl(TourHotelRepository tourHotelRepository) {
        this.tourHotelRepository = tourHotelRepository;
    }

    @Override
    public TourHotel saveEntity(TourHotel obj) {
        return tourHotelRepository.save(obj);
    }

    @Override
    public List<TourHotel> getAllEntities() {
        return tourHotelRepository.findAll();
    }

    @Override
    public TourHotel updateEntity(TourHotel obj) {
        if (!tourHotelRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Entity with ID " + obj.getId() + " not found");
        }
        return tourHotelRepository.save(obj);
    }

    @Override
    public void deleteEntity(TourHotel obj) {
        tourHotelRepository.delete(obj);
    }

    @Override
    public TourHotel findEntityById(int id) {
        return null;
    }

    @Override
    public TourHotel findEntityById(TourHotelId id) {
        return tourHotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + id + " not found"));
    }
}
