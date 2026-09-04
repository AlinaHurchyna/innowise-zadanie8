package com.innowise.marketplace.model;

import jakarta.persistence.*;

@Entity
public class AdPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contentType;

    @Lob
    @Column(length = 10_000_000)
    private byte[] data;

    @ManyToOne
    private Advertisement advertisement;

    public AdPhoto() {
    }

    public AdPhoto(byte[] data, String contentType, Advertisement advertisement) {
        this.data = data;
        this.contentType = contentType;
        this.advertisement = advertisement;
    }

    public Long getId() {
        return id;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getData() {
        return data;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }
}
