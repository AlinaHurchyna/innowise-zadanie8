package com.innowise.marketplace.web;

import com.innowise.marketplace.data.AdvertisementRepository;
import com.innowise.marketplace.data.CategoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final AdvertisementRepository advertisementRepository;
    private final CategoryRepository categoryRepository;

    public HomeController(AdvertisementRepository advertisementRepository, CategoryRepository categoryRepository) {
        this.advertisementRepository = advertisementRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/")
    public String home(@RequestParam(required = false) String keyword,
                        @RequestParam(required = false) Long categoryId,
                        @RequestParam(defaultValue = "newest") String sort,
                        Model model) {

        Sort sortOrder = switch (sort) {
            case "priceAsc" -> Sort.by("price").ascending();
            case "priceDesc" -> Sort.by("price").descending();
            default -> Sort.by("createdAt").descending();
        };

        String keywordParam = (keyword == null || keyword.isBlank()) ? null : keyword;

        model.addAttribute("ads", advertisementRepository.search(keywordParam, categoryId, sortOrder));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sort", sort);
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
