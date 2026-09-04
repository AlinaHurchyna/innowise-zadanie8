package com.innowise.marketplace;

import com.innowise.marketplace.data.CategoryRepository;
import com.innowise.marketplace.model.Category;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CategorySeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategorySeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return;
        }
        categoryRepository.save(new Category(1L, "Электроника"));
        categoryRepository.save(new Category(2L, "Транспорт"));
        categoryRepository.save(new Category(3L, "Недвижимость"));
        categoryRepository.save(new Category(4L, "Одежда"));
        categoryRepository.save(new Category(5L, "Мебель"));
        categoryRepository.save(new Category(6L, "Другое"));
    }
}
