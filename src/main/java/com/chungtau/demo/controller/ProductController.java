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
import com.chungtau.demo.model.orderDetail.OrderDetail;
import com.chungtau.demo.model.product.CreateProductInput;
import com.chungtau.demo.model.product.DeleteProductInput;
import com.chungtau.demo.model.product.Product;
import com.chungtau.demo.model.product.UpdateProductInput;
import com.chungtau.demo.model.review.Review;
import com.chungtau.demo.repository.CategoryRepository;
import com.chungtau.demo.repository.ProductRepository;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @QueryMapping
    public Optional<Product> getProductById(@Argument Long id) {
        return productRepository.findById(id);
    }

    @QueryMapping
    public Iterable<Product> getProducts() {
        return productRepository.findAll();
    }

    @SchemaMapping(typeName = "Product", field = "category")
    public Category getCategory(Product product) {
        Integer categoryId = product.getCategory().getId();
        if (categoryId != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            return optionalCategory.orElse(null);
        } else {
            return null;
        }
    }

    @SchemaMapping(typeName = "Product", field = "reviews")
    public List<Review> getReviews(Product product) {
        return product.getReviews();
    }

    @SchemaMapping(typeName = "Product", field = "orderDetails")
    public List<OrderDetail> getOrderDetails(Product product) {
        return product.getOrderDetails();
    }

    @MutationMapping
    public Product addProduct(@Argument CreateProductInput input) {
        Product product = new Product();
        
        product.setName(input.getName());
        product.setDescription(input.getDescription());
        product.setPrice(input.getPrice());

        Optional<Category> optionalCategory = categoryRepository.findById(input.getCategoryId());
        if (optionalCategory.isPresent()) {
            product.setCategory(optionalCategory.get());
            return productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Category not found for id: " + input.getCategoryId());
        }
    }

    @MutationMapping
    public Product updateProduct(@Argument UpdateProductInput input) {
        Optional<Product> optionalProduct = productRepository.findById(input.getId());
        
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(input.getName());
            product.setDescription(input.getDescription());
            product.setPrice(input.getPrice());
            Optional<Category> optionalCategory = categoryRepository.findById(input.getCategoryId());
            if(optionalCategory.isPresent()){
                product.setCategory(optionalCategory.get());
            }else{
                throw new IllegalArgumentException("Category not found for id: " + input.getCategoryId());
            }

            return productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Product not found for id: " + input.getId());
        }
    }

    @MutationMapping
    public boolean deleteProduct(@Argument DeleteProductInput input) {
        if (productRepository.existsById(input.getId())) {
            productRepository.deleteById(input.getId());
            return true;
        } else {
            throw new IllegalArgumentException("Product not found for id: " + input.getId());
        }
    }
}
