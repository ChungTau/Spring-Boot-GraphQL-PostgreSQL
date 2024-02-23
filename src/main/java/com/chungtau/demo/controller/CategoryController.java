package com.chungtau.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.chungtau.demo.model.category.Category;
import com.chungtau.demo.model.category.CreateCategoryInput;
import com.chungtau.demo.model.category.DeleteCategoryInput;
import com.chungtau.demo.model.category.UpdateCategoryInput;
import com.chungtau.demo.model.product.Product;
import com.chungtau.demo.repository.CategoryRepository;

@Controller
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @QueryMapping
    public Optional<Category> getCategoryById(@Argument Integer id) {
        return categoryRepository.findById(id);
    }

    @QueryMapping
    public Iterable<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @SchemaMapping(typeName = "Category", field = "products")
    public List<Product> getProduct(Category category) {
        return category.getProducts();
    }

    @MutationMapping
    public Category addCategory(@Argument CreateCategoryInput input) {
        Category category = new Category();
        category.setName(input.getName());
        category.setDescription(input.getDescription());
        return categoryRepository.save(category);
    }

    @MutationMapping
    public Category updateCategory(@Argument UpdateCategoryInput input) {
        Optional<Category> optionalCategory = categoryRepository.findById(input.getId());
        
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setName(input.getName());
            category.setDescription(input.getDescription());

            return categoryRepository.save(category);
        } else {
            throw new IllegalArgumentException("Category not found for id: " + input.getId());
        }
    }

    @MutationMapping
    public boolean deleteCategory(@Argument DeleteCategoryInput input) {
        if (categoryRepository.existsById(input.getId())) {
            categoryRepository.deleteById(input.getId());
            return true;
        } else {
            throw new IllegalArgumentException("Category not found for id: " + input.getId());
        }
    }
}
