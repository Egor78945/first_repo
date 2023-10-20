package com.example.marketspring.services;

import com.example.marketspring.models.Product;
import com.example.marketspring.models.User;
import com.example.marketspring.repositories.ProductRepository;
import com.example.marketspring.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;

    public Product getProductById(Long id) {
        return productRepository.findProductById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public User getUserByProductId(Long id) {
        Product product = getProductById(id);
        return userService.getUserById(product.getOwnerId());
    }
    public void removeProductFromUser(User user, int id){
        user.getUserProducts().remove(id);
    }
    public void addProductToUser(User user, Product product){
        user.getUserProducts().add(product);
    }
}
