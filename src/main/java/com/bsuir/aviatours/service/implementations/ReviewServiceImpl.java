package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Review;
import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.repository.ReviewRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements EntityService<Review> {

    private final ReviewRepository entityRepository;

    public ReviewServiceImpl(ReviewRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public Review saveEntity(Review obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<Review> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public Review updateEntity(Review obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Review with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(Review obj) {
        entityRepository.delete(obj);
    }

    @Override
    public Review findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review with ID " + id + " not found"));
    }

}
