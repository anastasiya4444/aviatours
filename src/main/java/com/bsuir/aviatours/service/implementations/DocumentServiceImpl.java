package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Document;
import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.repository.DocumentRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements EntityService<Document> {

    private final DocumentRepository entityRepository;

    public DocumentServiceImpl(DocumentRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public Document saveEntity(Document obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<Document> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public Document updateEntity(Document obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Document with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(Document obj) {
        entityRepository.delete(obj);
    }

    @Override
    public Document findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document with ID " + id + " not found"));
    }

}
