package com.chungtau.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.chungtau.demo.exception.EntityRuntimeException;
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
        if(id != null){
            return categoryRepository.findById(id);
        }else{
            return Optional.empty();
        }
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
        Integer categoryId = input.getId();

        if(categoryId != null){
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                category.setName(input.getName());
                category.setDescription(input.getDescription());

                return categoryRepository.save(category);
            } else {
                throw EntityRuntimeException.notFound(Category.class.getName(), categoryId);
            }
        }else{
            throw EntityRuntimeException.entityIdNotNull(Category.class.getName());
        }
    }

    @MutationMapping
    public boolean deleteCategory(@Argument DeleteCategoryInput input) {
        Integer categoryId = input.getId();

        if(categoryId != null){
            if (categoryRepository.existsById(categoryId)) {
                categoryRepository.deleteById(categoryId);
                return true;
            } else {
                throw EntityRuntimeException.notFound(Category.class.getName(), categoryId);
            }
        }else{
            throw EntityRuntimeException.entityIdNotNull(Category.class.getName());
        }
    }
}
