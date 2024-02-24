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
        if(id != null){
            return productRepository.findById(id);
        }else{
            return Optional.empty();
        }
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

        Integer categoryId = input.getCategoryId();
        if(categoryId != null){
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            if (optionalCategory.isPresent()) {
                product.setCategory(optionalCategory.get());
                return productRepository.save(product);
            } else {
                throw EntityRuntimeException.notFound(Category.class.getName(), categoryId);
            }
        }else{
            throw EntityRuntimeException.entityIdNotNull(Category.class.getName());
        }
    }

    @MutationMapping
    public Product updateProduct(@Argument UpdateProductInput input) {
        Long productId = input.getId();

        if(productId != null){
            Optional<Product> optionalProduct = productRepository.findById(productId);
        
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setName(input.getName());
                product.setDescription(input.getDescription());
                product.setPrice(input.getPrice());
                
                Integer categoryId = input.getCategoryId();
                if(categoryId != null){
                    Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
                    if(optionalCategory.isPresent()){
                        product.setCategory(optionalCategory.get());
                    }else{
                        throw EntityRuntimeException.notFound(Category.class.getName(), categoryId);
                    }
                }else{
                    throw EntityRuntimeException.entityIdNotNull(Category.class.getName());
                }
                return productRepository.save(product);
            } else {
                throw EntityRuntimeException.notFound(Product.class.getName(), productId);
            }
        }else{
            throw EntityRuntimeException.entityIdNotNull(Product.class.getName());
        }  
    }

    @MutationMapping
    public boolean deleteProduct(@Argument DeleteProductInput input) {
        Long productId = input.getId();

        if(productId != null){
            if (productRepository.existsById(productId)) {
                productRepository.deleteById(productId);
                return true;
            } else {
                throw EntityRuntimeException.notFound(Product.class.getName(), productId);
            }
        }else{
            throw EntityRuntimeException.entityIdNotNull(Product.class.getName());
        }
    }
}
