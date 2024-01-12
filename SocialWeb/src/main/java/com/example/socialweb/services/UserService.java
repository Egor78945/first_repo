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

    public User getUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailsImpl userDetails = new UserDetailsImpl();
        return userDetails.build(userRepository.findUserByEmail(username));
    }
    public void saveUser(User user) {
        userRepository.save(user);
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
        user.setIsLock(false);
        user.setIsBan(false);
        user.setCloseProfile(false);
        userRepository.save(user);
    }

    public boolean isValidStatus(String status) {
        if (status.isEmpty() || status.length() > 20) {
            log.info("isValidStatus: status is invalid: " + status);
            return false;
        }
        log.info("isValidStatus: status is valid: " + status);
        return true;
    }

    public boolean isValidCountry(String country) {
        if (country.isEmpty()) {
            return false;
        }
        Countries countries = new Countries();
        List<String> countryList = countries.getList();
        for (int i = 0; i < countryList.size(); i++) {
            if (countryList.get(i).equalsIgnoreCase(country))
                log.info("isValidCountry: country is valid: " + country);
            return true;
        }
        log.info("isValidCountry: country is unknown: " + country);
        return false;
    }

    public boolean isValidCity(String city) {
        if (city.isEmpty()) {
            return false;
        }
        Cities cities = new Cities();
        List<String> cityList = cities.getList();
        for (int i = 0; i < cityList.size(); i++) {
            if (cityList.get(i).equalsIgnoreCase(city))
                log.info("isValidCity: city is valid: " + city);
            return true;
        }
        log.info("isValidCity: city is unknown: " + city);
        return false;
    }

    public boolean isValidNameOrSurname(String nameOrSurname) {
        if (nameOrSurname.length() > 16 || nameOrSurname.length() < 2) {
            log.info("isValidNameOrSurname: name or surname is invalid: " + nameOrSurname);
            return false;
        }
        if (!isContainsOnlyLetters(nameOrSurname)) {
            log.info("isValidNameOrSurname: name or surname is invalid: " + nameOrSurname);
            return false;
        }
        log.info("isValidNameOrSurname: name or surname is valid: " + nameOrSurname);
        return true;
    }

    public boolean isContainsOnlyLetters(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (!Character.isLetter(word.charAt(i))) {
                log.info("isContainsOnlyLetters: false: " + word);
                return false;
            }
        }
        log.info("isContainsOnlyLetters: true: " + word);
        return true;
    }

    public boolean isValidEmail(String email) {
        if (email.endsWith("@gmail.com") || email.endsWith("@mail.ru")) {
            int dogIndex = email.indexOf("@");
            String beforeDog = email.substring(0, dogIndex);
            if (beforeDog.length() >= 50) {
                log.info("register(isValidEmail): email is invalid: " + email);
                return false;
            } else {
                for (int i = 0; i < beforeDog.length(); i++) {
                    if (!Character.isDigit(beforeDog.charAt(i)) && !Character.isLetter(beforeDog.charAt(i))) {
                        log.info("register(isValidEmail): email is invalid: " + email);
                        return false;
                    }
                }
                log.info("register(isValidEmail): email is valid: " + email);
                return true;
            }
        }
        log.info("register(isValidEmail): email is invalid: " + email);
        return false;
    }

    public boolean isValidPassword(String password) {
        System.out.println(password);
        if (password.length() > 6 && password.length() < 15) {
            int letters = 0;
            int digits = 0;
            for (int i = 0; i < password.length(); i++) {
                if (!Character.isDigit(password.charAt(i)) && !Character.isLetter(password.charAt(i))) {
                    log.info("register(isValidPassword): password is invalid.");
                    return false;
                } else {
                    if (Character.isLetter(password.charAt(i))) {
                        letters++;
                    }
                    if (Character.isDigit(password.charAt(i))) {
                        digits++;
                    }
                }
            }
            if (letters < 3 || digits < 3) {
                log.info("register(isValidPassword): password is invalid.");
                return false;
            } else {
                log.info("register(isValidPassword): password is invalid.");
                return true;
            }
        }
        log.info("register(isValidPassword): password is invalid.");
        return false;
    }


    public List<User> getAllBannedUsers() {
        return userRepository.findUserByIsBanTrue();
    }
    public List<User> getAllReportedUsers(){
        return userRepository.findAllByListOfReportsIsNotEmpty();
    }
}
