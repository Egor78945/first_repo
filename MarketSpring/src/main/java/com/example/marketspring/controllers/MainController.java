package com.example.marketspring.controllers;

import com.example.marketspring.exceptions.BadSumException;
import com.example.marketspring.exceptions.NotEnoughMoneyException;
import com.example.marketspring.models.*;
import com.example.marketspring.services.ProductService;
import com.example.marketspring.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        ProfileModel profileModel = new ProfileModel(user.getId(), user.getName(), user.getSurname(), user.getBalance(), user.getDateOfRegister(), user.getUserProducts());
        return ResponseEntity.ok(profileModel);
    }

    @PostMapping("/public")
    public ResponseEntity<?> addProduct(Principal principal, @RequestBody PublicModelProduct modelProduct) {
        User user = userService.getUserByEmail(principal.getName());
        Product product = new Product(modelProduct.getName(), modelProduct.getPrice(), modelProduct.getDescription());
        product.setDateOfPublication(new Date(System.currentTimeMillis()).toString());
        user.getUserProducts().add(product);
        userService.saveUser(user);
        return ResponseEntity.ok("Product has been add to list of your products.");
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllProducts() {
        List<Product> list = productService.getAllProducts();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/replenish")
    public ResponseEntity<?> replenishBalance(Principal principal, @RequestBody ReplenishModel model) {
        User user = userService.getUserByEmail(principal.getName());
        try {
            userService.replenishBalance(user, model);
        } catch (BadSumException e) {
            return ResponseEntity.ok(new ExceptionResponse(e.getMessage()));
        }
        return ResponseEntity.ok("Balance has been replenished.");
    }

    @PostMapping("/buy/{id}")
    public ResponseEntity<?> buyProduct(Principal principal, @PathVariable("id") Long id) {
        User user = userService.getUserByEmail(principal.getName());
        try {
            userService.buyProduct(user, id);
        } catch (NotEnoughMoneyException e) {
            return ResponseEntity.ok(new ExceptionResponse(e.getMessage()));
        } catch (BadSumException e) {
            return ResponseEntity.ok(new BadSumException(e.getMessage()));
        }
        return ResponseEntity.ok("You bought this product.");
    }

}
