package com.tecnm.merida.market_backend.domain.service;

import com.tecnm.merida.market_backend.domain.Category;
import com.tecnm.merida.market_backend.domain.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.getAll();
    }

    public Optional<Category> getCategory(int categoryId) {
        return categoryRepository.getCategory(categoryId);
    }

    public Category save(Category category) {
        validate(category);
        category.setCategoryId(null); // en POST el id lo genera la base de datos
        return categoryRepository.save(category);
    }

    public Optional<Category> update(int categoryId, Category category) {
        validate(category);
        return getCategory(categoryId).map(existing -> {
            category.setCategoryId(categoryId);
            return categoryRepository.save(category);
        });
    }

    public boolean delete(int categoryId) {
        if (getCategory(categoryId).isPresent()) {
            categoryRepository.delete(categoryId);
            return true;
        }
        return false;
    }

    private void validate(Category category) {
        if (category.getCategory() == null || category.getCategory().isBlank()) {
            throw new IllegalArgumentException("The category description is required");
        }
    }
}