package com.bsuir.aviatours.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "document")
public class Document implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_data_id")
    private PersonalDatum personalData;

    @Column(name = "document_name", nullable = false, length = 100)
    private String documentName;

    @Column(name = "document_file", nullable = false)
    private byte[] documentFile;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PersonalDatum getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalDatum personalData) {
        this.personalData = personalData;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public byte[] getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(byte[] documentFile) {
        this.documentFile = documentFile;
    }

}