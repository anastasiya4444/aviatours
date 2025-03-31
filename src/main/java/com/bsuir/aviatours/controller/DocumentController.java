package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.Document;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/document")
@CrossOrigin
public class DocumentController {

    private final EntityService<Document> documentEntityService;

    public DocumentController(EntityService<Document> documentEntityService) {
        this.documentEntityService = documentEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Document document) {
        documentEntityService.saveEntity(document);
        return ResponseEntity.ok("Document saved successfully");
    }

    @GetMapping("/getAll")
    public List<Document> getAll() {
        return documentEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getById(@PathVariable int id) {
        Document document = documentEntityService.findEntityById(id);
        return ResponseEntity.ok(document);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Document document = documentEntityService.findEntityById(id);
        documentEntityService.deleteEntity(document);
        return ResponseEntity.ok("Document deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<Document> update(@RequestBody Document document1) {
        Document document = documentEntityService.updateEntity(document1);
        return ResponseEntity.ok(document);
    }
}
