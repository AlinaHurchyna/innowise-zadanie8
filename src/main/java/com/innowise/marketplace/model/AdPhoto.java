package com.innowise.marketplace.model;

import jakarta.persistence.*;

@Entity
public class AdPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    @ManyToOne
    private Advertisement advertisement;

    public AdPhoto() {
    }

    public AdPhoto(String filename, Advertisement advertisement) {
        this.filename = filename;
        this.advertisement = advertisement;
    }

    public Long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }
}
