package com.chungtau.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.chungtau.demo.exception.EntityRuntimeException;
import com.chungtau.demo.model.cart.Cart;
import com.chungtau.demo.model.cart.CreateCartInput;
import com.chungtau.demo.model.cart.DeleteCartInput;
import com.chungtau.demo.model.cart.UpdateCartInput;
import com.chungtau.demo.model.product.Product;
import com.chungtau.demo.model.user.User;
import com.chungtau.demo.repository.CartRepository;
import com.chungtau.demo.repository.ProductRepository;
import com.chungtau.demo.repository.UserRepository;

@Controller
public class CartController {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @QueryMapping
    public Optional<Cart> getCartById(@Argument Long id) {
        if(id != null){
            return cartRepository.findById(id);
        }else{
            return Optional.empty();
        }
    }

    @QueryMapping
    public Iterable<Cart> getCarts() {
        return cartRepository.findAll();
    }

    @SchemaMapping(typeName = "Cart", field = "user")
    public User getUser(Cart cart) {
        Long userId = cart.getUser().getId();
        if (userId != null) {
            Optional<User> optionalUser = userRepository.findById(userId);
            return optionalUser.orElse(null);
        } else {
            return null;
        }
    }

    @SchemaMapping(typeName = "Cart", field = "products")
    public List<Product> getProducts(Cart cart) {
        return cart.getProducts()
                .stream()
                .map(product -> {
                    Long productId = product.getId();
                    if (productId != null) {
                        Optional<Product> optionalProduct = productRepository.findById(productId);
                        return optionalProduct.orElse(null);
                    } else {
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    @MutationMapping
    public Cart createCart(@Argument CreateCartInput input) {
        Cart cart = new Cart();
        
        Long userId = input.getUserId();
        if(userId != null){
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                cart.setUser(optionalUser.get());
            } else {
                throw EntityRuntimeException.notFound(User.class.getName(), userId);
            }
        }else{
            throw EntityRuntimeException.entityIdNotNull(User.class.getName());
        }

        List<Product> products = new ArrayList<>();
        for (Long productId : input.getProductIds()) {
            if(productId != null){
                Optional<Product> optionalProduct = productRepository.findById(productId);
                optionalProduct.ifPresent(products::add);
            }else{
                throw EntityRuntimeException.entityIdNotNull(Product.class.getName());
            }
        }
        
        cart.setProducts(products);
        return cartRepository.save(cart);
    }

    @MutationMapping
    public Cart updateCart(@Argument UpdateCartInput input) {
        Long cartId = input.getId();

        if(cartId != null){
            Optional<Cart> optionalCart = cartRepository.findById(cartId);
        
            if (optionalCart.isPresent()) {
                Cart cart = optionalCart.get();

                List<Product> products = new ArrayList<>();
                for (Long productId : input.getProductIds()) {
                    if(productId != null){
                        Optional<Product> optionalProduct = productRepository.findById(productId);
                        optionalProduct.ifPresent(products::add);
                    }else{
                        throw EntityRuntimeException.entityIdNotNull(Product.class.getName());
                    }
                }

                cart.setProducts(products);
                return cartRepository.save(cart);
            } else {
                throw EntityRuntimeException.notFound(Cart.class.getName(), cartId);
            }
        }else{
            throw EntityRuntimeException.entityIdNotNull(Cart.class.getName());
        }
    }

    @MutationMapping
    public boolean deleteCart(@Argument DeleteCartInput input) {
        Long cartId = input.getId();

        if(cartId != null){
            if (cartRepository.existsById(cartId)) {
                cartRepository.deleteById(cartId);
                return true;
            } else {
                throw EntityRuntimeException.notFound(Cart.class.getName(), cartId);
            }
        }else{
            throw EntityRuntimeException.entityIdNotNull(Cart.class.getName());
        }
    }
}
