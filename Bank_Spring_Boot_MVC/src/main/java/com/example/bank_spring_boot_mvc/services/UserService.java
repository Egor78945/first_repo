package com.example.bank_spring_boot_mvc.services;

import com.example.bank_spring_boot_mvc.entities.Transaction;
import com.example.bank_spring_boot_mvc.entities.User;
import com.example.bank_spring_boot_mvc.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with email: '%s', not found.", username)
        ));
        return UserDetailsImpl.build(user);
    }

    public boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).get();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void payMoney(Transaction transaction, User user) throws BadCredentialsException {
        if (transaction.getSum() <= 0 || transaction.getSum() > 1000000) {
            throw new BadCredentialsException("Sum must to be bigger 0 and less or equals 1000000.");
        } else {
            user.setBalance(user.getBalance() + transaction.getSum());
            userRepository.save(user);
        }
    }
    public void takeMoney(Transaction transaction, User user) throws BadCredentialsException{
        if(transaction.getSum() <= 0 || transaction.getSum() > user.getBalance()){
            throw new BadCredentialsException("Sum must to be bigger 0 and less or equals user balance.");
        } else {
            user.setBalance(user.getBalance() - transaction.getSum());
            userRepository.save(user);
        }
    }
    public void transferMoney(User from,User to, Transaction transaction) throws BadCredentialsException{
        if(transaction.getSum() > from.getBalance()){
            throw new BadCredentialsException("You dont have any money");
        } else {
            payMoney(transaction, to);
            takeMoney(transaction, from);
        }
    }
}
