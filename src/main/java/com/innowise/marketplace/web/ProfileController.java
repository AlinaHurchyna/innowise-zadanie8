package com.innowise.marketplace.web;

import com.innowise.marketplace.data.AdvertisementRepository;
import com.innowise.marketplace.data.UserRepository;
import com.innowise.marketplace.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;

    public ProfileController(UserRepository userRepository, AdvertisementRepository advertisementRepository) {
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;
    }

    @GetMapping("/users/{username}")
    public String profile(@PathVariable String username, Model model) {
        User seller = userRepository.findByUsername(username);
        if (seller == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден: " + username);
        }
        model.addAttribute("seller", seller);
        model.addAttribute("ads", advertisementRepository.findBySellerOrderByCreatedAtDesc(seller));
        return "profile";
    }
}
