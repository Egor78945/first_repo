package com.example.socialweb.services;

import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.Cities;
import com.example.socialweb.models.enums.Countries;
import com.example.socialweb.models.enums.Role;
import com.example.socialweb.models.requestModels.RegisterBody;
import com.example.socialweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailsImpl userDetails = new UserDetailsImpl();
        return userDetails.build(userRepository.findUserByEmail(username));
    }

    public User getUserById(Long id) {
        return userRepository.findUserById(id);
    }

    public boolean containsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    public void saveUser(User user) {
        log.info("system: attempt to save or update the user...");
        userRepository.save(user);
        log.info("system: user has been saved or updated.");
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public void register(RegisterBody registerBody, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setName(registerBody.getName());
        user.setSurname(registerBody.getSurname());
        user.setAge(registerBody.getAge());
        user.setEmail(registerBody.getEmail());
        user.setStatus(registerBody.getStatus());
        user.setCity(registerBody.getCity());
        user.setRole(Role.USER_ROLE);
        user.setCountry(registerBody.getCountry());
        user.setPassword(passwordEncoder.encode(registerBody.getPassword()));
        userRepository.save(user);
    }

    public boolean isValidStatus(String status) {
        log.info("register: checking the status...");
        if (!status.isEmpty() && status.length() <= 20) {
            log.info("register: status is valid.");
            return true;
        }
        log.info("status is invalid.");
        return false;
    }

    public boolean isValidCountry(String country) {
        log.info("register: checking the country...");
        if (!country.isEmpty()) {
            Countries countries = new Countries();
            for (String i : countries.getList()) {
                if (country.equals(i))
                    log.info("register: country is valid.");
                return true;
            }
        }
        log.info("register: status is invalid.");
        return false;
    }

    public boolean isValidCity(String city) {
        log.info("register: checking the city...");
        if (!city.isEmpty()) {
            Cities cities = new Cities();
            for (String i : cities.getList()) {
                if (city.equals(i)) {
                    log.info("register: the city is valid.");
                    return true;
                }
            }
        }
        log.info("register: the city is invalid.");
        return false;
    }

    public boolean isValidNameOrSurname(String nameOrSurname) {
        log.info("register: checking name or surname...");
        if (nameOrSurname.length() > 2 && nameOrSurname.length() < 16 && isContainsOnlyLetters(nameOrSurname)) {
            log.info("register: name or surname is valid.");
            return true;
        }
        log.info("register: name or surname is invalid.");
        return false;
    }

    public boolean isContainsOnlyLetters(String word) {
        log.info("service: checking contains only letters...");
        for (char i : word.toCharArray()) {
            if (!Character.isLetter(i)) {
                log.info("service: is not contains only letters.");
                return false;
            }
        }
        log.info("service: is contains only letters.");
        return true;
    }

    public boolean isContainsOnlyLettersAndDigits(String word) {
        log.info("service: checking is contains only letters and digits...");
        for (char i : word.toCharArray()) {
            if (!Character.isLetter(i) && !Character.isDigit(i)) {
                log.info("service: is not contains only letters and digits.");
                return false;
            }
        }
        log.info("service: is contains only letters and digits.");
        return true;
    }

    public boolean isValidEmail(String email) {
        log.info("register: checking the email...");
        if (containsUserByEmail(email)) {
            log.info("register: user with this email already exists.");
        } else {
            if (email.length() < 39 && email.length() > 15) {
                if (email.endsWith("@mail.ru") && isContainsOnlyLettersAndDigits(email.substring(0, email.length() - 8))) {
                    log.info("register: email is valid.");
                    return true;
                } else if (email.endsWith("@gmail.com") && isContainsOnlyLettersAndDigits(email.substring(0, email.length() - 10))) {
                    log.info("register: email is valid.");
                    return true;
                }
            }
            log.info("register: email is invalid.");
        }
        return false;
    }

    public boolean isValidPassword(String password) {
        log.info("register: checking the password...");
        if (password.length() <= 35 && password.length() >= 10 && isContainsOnlyLettersAndDigits(password)) {
            int letters = 0;
            int digits = 0;
            for (char i : password.toCharArray()) {
                if (Character.isDigit(i))
                    digits++;
                else if (Character.isLetter(i))
                    letters++;
            }
            if (letters >= 4 && digits >= 4) {
                log.info("register: password is valid.");
                return true;
            } else {
                log.info("register: password is invalid.");
                return false;
            }
        } else {
            log.info("register: password is invalid.");
            return false;
        }
    }

    public List<User> getAllBannedUsers() {
        return userRepository.findUserByIsBanTrue();
    }

    public boolean isValidAge(Integer age) {
        log.info("register: checking the age...");
        if (age >= 14) {
            log.info("register: age is valid.");
            return true;
        } else {
            log.info("register: age is invalid.");
            return false;
        }
    }

    public boolean userDataIsValid(RegisterBody body) {
        log.info("register: checking user data...");
        boolean result = true;
        if (!isValidAge(body.getAge()))
            result = false;
        else if (!isValidNameOrSurname(body.getName()) || !isValidNameOrSurname(body.getSurname()))
            result = false;
        else if (!isValidCity(body.getCity()) || !isValidCountry(body.getCountry()))
            result = false;
        else if (!isValidEmail(body.getEmail()))
            result = false;
        else if (!isValidStatus(body.getStatus()))
            result = false;
        else if (!isValidPassword(body.getPassword()))
            result = false;
        if (result) {
            log.info("register: user data is valid.");
        } else {
            log.info("register: user data is invalid.");
        }
        return result;
    }
}
