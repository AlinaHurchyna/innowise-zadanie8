package com.innowise.marketplace.web;

import com.innowise.marketplace.data.AdvertisementRepository;
import com.innowise.marketplace.data.CategoryRepository;
import com.innowise.marketplace.model.AdPhoto;
import com.innowise.marketplace.model.Advertisement;
import com.innowise.marketplace.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/ads")
public class AdController {

    private final AdvertisementRepository advertisementRepository;
    private final CategoryRepository categoryRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public AdController(AdvertisementRepository advertisementRepository, CategoryRepository categoryRepository) {
        this.advertisementRepository = advertisementRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("ad", findOrThrow(id));
        return "adDetail";
    }

    @GetMapping("/new")
    public String newAdForm(Model model) {
        model.addAttribute("ad", new Advertisement());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("cities", Cities.ALL);
        return "adForm";
    }

    @PostMapping
    public String create(@ModelAttribute("ad") @Valid Advertisement ad, Errors errors,
                          @RequestParam(required = false) Long categoryId,
                          @RequestParam(required = false) MultipartFile[] photos,
                          @AuthenticationPrincipal User user,
                          Model model) throws IOException {

        if (errors.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("cities", Cities.ALL);
            return "adForm";
        }

        ad.setCategory(categoryRepository.findById(categoryId).orElse(null));
        ad.setSeller(user);
        savePhotos(ad, photos);
        advertisementRepository.save(ad);
        return "redirect:/ads/" + ad.getId();
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) {
        Advertisement ad = findOrThrow(id);
        requireOwner(ad, user);
        model.addAttribute("ad", ad);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("cities", Cities.ALL);
        return "adForm";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute("ad") @Valid Advertisement form, Errors errors,
                          @RequestParam(required = false) Long categoryId,
                          @RequestParam(required = false) MultipartFile[] photos,
                          @AuthenticationPrincipal User user,
                          Model model) throws IOException {

        Advertisement ad = findOrThrow(id);
        requireOwner(ad, user);

        if (errors.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("cities", Cities.ALL);
            return "adForm";
        }

        ad.setTitle(form.getTitle());
        ad.setDescription(form.getDescription());
        ad.setPrice(form.getPrice());
        ad.setCity(form.getCity());
        ad.setCategory(categoryRepository.findById(categoryId).orElse(null));
        savePhotos(ad, photos);
        advertisementRepository.save(ad);
        return "redirect:/ads/" + ad.getId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Advertisement ad = findOrThrow(id);
        requireOwner(ad, user);
        advertisementRepository.delete(ad);
        return "redirect:/";
    }

    private void savePhotos(Advertisement ad, MultipartFile[] photos) throws IOException {
        if (photos == null) {
            return;
        }
        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);
        for (MultipartFile photo : photos) {
            if (photo.isEmpty()) {
                continue;
            }
            String filename = UUID.randomUUID() + "-" + photo.getOriginalFilename();
            Files.copy(photo.getInputStream(), dir.resolve(filename));
            ad.getPhotos().add(new AdPhoto(filename, ad));
        }
    }

    private Advertisement findOrThrow(Long id) {
        return advertisementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Объявление не найдено: " + id));
    }

    private void requireOwner(Advertisement ad, User user) {
        if (user == null || !ad.getSeller().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Вы не автор этого объявления");
        }
    }
}
