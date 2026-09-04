package com.innowise.marketplace.web;

import com.innowise.marketplace.data.AdPhotoRepository;
import com.innowise.marketplace.model.AdPhoto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class PhotoController {

    private final AdPhotoRepository adPhotoRepository;

    public PhotoController(AdPhotoRepository adPhotoRepository) {
        this.adPhotoRepository = adPhotoRepository;
    }

    @GetMapping("/photos/{id}")
    public ResponseEntity<byte[]> photo(@PathVariable Long id) {
        AdPhoto photo = adPhotoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Фото не найдено: " + id));

        MediaType mediaType = photo.getContentType() != null
                ? MediaType.parseMediaType(photo.getContentType())
                : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok().contentType(mediaType).body(photo.getData());
    }
}
