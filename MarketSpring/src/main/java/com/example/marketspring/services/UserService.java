package com.example.marketspring.services;

import com.example.marketspring.exceptions.*;
import com.example.marketspring.models.*;
import com.example.marketspring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductService productService;

    @Autowired
    public UserService(@Lazy UserRepository userRepository, PasswordEncoder passwordEncoder, @Lazy ProductService productService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.productService = productService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found."));
        return UserDetailsImpl.build(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found."));
    }

    public boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    public void registration(RegBody regBody) throws BadEmailException, BadNameSizeException, BadSurnameSizeException, ForbiddenSymbolsException {
        if (existsUserByEmail(regBody.getEmail())) {
            throw new BadEmailException("User with email " + regBody.getEmail() + " already exists. change the email.");
        } else if (!regBody.getEmail().contains("@") || regBody.getEmail().length() > 25 || regBody.getEmail().length() < 13) {
            throw new BadEmailException("Your email is not equals a standard form of email's or your email is to long. Maximum length of email - 25, min - 13");
        } else if (regBody.getName().length() < 2 || regBody.getName().length() > 16) {
            throw new BadNameSizeException("Your name is to long or to short. Maximum size - 16, minimum - 2.");
        } else if (regBody.getSurname().length() < 2 || regBody.getSurname().length() > 24) {
            throw new BadSurnameSizeException("Your surname is to long or to short. Maximum size - 24, minimum - 2.");
        } else if (regBody.getName().contains(" ") || regBody.getSurname().contains(" ") || regBody.getEmail().contains(" ")) {
            throw new ForbiddenSymbolsException("Your name or surname or email must to not have an space symbols.");
        } else {
            User user = new User();
            user.setName(regBody.getName());
            user.setSurname(regBody.getSurname());
            user.setEmail(regBody.getEmail());
            user.setDateOfRegister(new Date(System.currentTimeMillis()).toString());
            user.setPassword(passwordEncoder.encode(regBody.getPassword()));
            user.setRole(Role.USER_ROLE);
            user.setBalance(0L);
            user.setUserProducts(new ArrayList<>());
            user.setBlocking(false);
            userRepository.save(user);
        }
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void replenishBalance(User user, ReplenishModel replenishModel) throws BadSumException {
        if (replenishModel.getSum() < 1L || replenishModel.getSum() > 300000L) {
            throw new BadSumException("Replenish sum must to not be less 1 and must to be not bigger 300000.");
        } else {
            user.setBalance(user.getBalance() + replenishModel.getSum());
            saveUser(user);
        }
    }

    /*public void replenishBalanceAfterBuy(User user, int sum) throws BadSumException {
        if (sum < 1) {
            throw new BadSumException("Sum must to be bigger 1.");
        } else {
            user
            saveUser(user);
        }
    }*/

    public void withDrawMoney(User user, int sum) throws BadSumException {
        if (sum > user.getBalance() || sum < 1) {
            throw new BadSumException("User have not this money, or sum is to less. Minimum size of sum - 1.");
        } else {
            user.setBalance(user.getBalance() - sum);
        }
    }

    public User getUserById(Long id) {
        return userRepository.findUserById(id);
    }

    public void buyProduct(User user, Long id) throws NotEnoughMoneyException, BadSumException {
        User productUser = productService.getUserByProductId(id);
        Product product = productService.getProductById(id);
        if (user.getBalance() < product.getPrice()) {
            throw new NotEnoughMoneyException("You have not money for buy this product. Replenish the balance.");
        } else {
            for (int i = 0; i < productUser.getUserProducts().size(); i++) {
                Product product1 = productUser.getUserProducts().get(i);
                if (product1.getId().equals(id)) {
                    productService.removeProductFromUser(productUser, i);
                    productService.addProductToUser(user, product);
                    break;
                }
            }
            ReplenishModel model = new ReplenishModel();
            model.setSum(Integer.toUnsignedLong(product.getPrice()));
            withDrawMoney(user, product.getPrice());
            replenishBalance(productUser, model);
            saveUser(user);
            saveUser(productUser);
        }
    }
}
