package com.example.socialweb.services;

import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.Cities;
import com.example.socialweb.models.enums.Countries;
import com.example.socialweb.models.enums.Role;
import com.example.socialweb.models.requestModels.PasswordSettingsModel;
import com.example.socialweb.models.requestModels.ProfileSettingsModel;
import com.example.socialweb.models.requestModels.RegisterBody;
import com.example.socialweb.models.requestModels.UserSearchModel;
import com.example.socialweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Set;

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
    public User getCurrentUser(Principal principal){
        return getUserByEmail(principal.getName());
    }

    public User getUserById(Long id) {
        return userRepository.findUserById(id);
    }

    private boolean containsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    private void saveUser(User user) {
        log.info("system: attempt to save or update the user...");
        userRepository.save(user);
        log.info("system: user has been saved or updated.");
    }

    private User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Transactional
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

    private boolean isValidStatus(String status) {
        log.info("register: checking the status...");
        if (!status.isEmpty() && status.length() <= 80) {
            log.info("register: status is valid.");
            return true;
        }
        log.info("status is invalid.");
        return false;
    }

    private boolean isValidCountry(String country) {
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

    private boolean isValidCity(String city) {
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

    private boolean isValidNameOrSurname(String nameOrSurname) {
        log.info("register: checking name or surname...");
        if (nameOrSurname.length() > 2 && nameOrSurname.length() < 16 && isContainsOnlyLetters(nameOrSurname)) {
            log.info("register: name or surname is valid.");
            return true;
        }
        log.info("register: name or surname is invalid.");
        return false;
    }

    private boolean isContainsOnlyLetters(String word) {
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

    private boolean isContainsOnlyLettersAndDigits(String word) {
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

    private boolean isValidEmail(String email) {
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

    private boolean isValidEmailFormat(String email) {
        log.info("register: checking the email...");
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
        return false;
    }

    private boolean isValidPassword(String password) {
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

    private boolean isValidAge(Integer age) {
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

    private boolean userDataIsValid(ProfileSettingsModel body) {
        log.info("register: checking user data...");
        boolean result = true;
        if (!isValidAge(body.getAge()))
            result = false;
        else if (!isValidNameOrSurname(body.getName()) || !isValidNameOrSurname(body.getSurname()))
            result = false;
        else if (!isValidCity(body.getCity()) || !isValidCountry(body.getCountry()))
            result = false;
        else if (!isValidEmailFormat(body.getEmail()))
            result = false;
        else if (!isValidStatus(body.getStatus()))
            result = false;
        if (result) {
            log.info("register: user data is valid.");
        } else {
            log.info("register: user data is invalid.");
        }
        return result;
    }

    @Transactional
    public boolean changePassword(PasswordSettingsModel model, User user, PasswordEncoder passwordEncoder) {
        log.info("settings: attempt to change password...");
        if (passwordEncoder.matches(model.getOldPassword(), user.getPassword())) {
            log.info("settings: password confirmed.");
            if (isValidPassword(model.getNewPassword())) {
                log.info("settings: new password is valid, changing the password...");
                user.changePassword(model.getNewPassword(), passwordEncoder);
                log.info("settings: password successfully changed.");
                log.info("settings: saving the user...");
                saveUser(user);
                log.info("settings: user successfully saved.");
                return true;
            } else {
                log.info("settings: new password is invalid.");
                return false;
            }
        } else {
            log.info("settings: password is not confirmed.");
            return false;
        }
    }

    @Transactional
    public boolean updateProfile(ProfileSettingsModel model, User user) {
        log.info("settings: attempt to update user profile...");
        if (userDataIsValid(model)) {
            log.info("settings: updating user profile...");
            user.updateProfile(model);
            log.info("settings: user profile successfully updated.");
            log.info("settings: saving the user...");
            saveUser(user);
            log.info("settings: user successfully saved.");
            return true;
        } else {
            log.info("settings: user profile is not updated.");
            return false;
        }
    }

    private List<User> getAllByName(String name) {
        return userRepository.findAllByName(name);
    }

    private List<User> getAllBySurname(String surname) {
        return userRepository.findAllBySurname(surname);
    }

    private List<User> getAllByNameAndSurname(String name, String surname) {
        return userRepository.findAllByNameAndSurname(name, surname);
    }

    private boolean containsByNameAndSurname(String name, String surname) {
        return userRepository.existsUserByNameAndSurname(name, surname);
    }

    private boolean containsByName(String name) {
        return userRepository.existsUserByName(name);
    }

    private boolean containsBySurname(String surname) {
        return userRepository.existsUserBySurname(surname);
    }

    public List<User> search(UserSearchModel model) throws Exception {
        log.info("search: attempt to find user...");
        if (!model.getName().isEmpty() && !model.getSurname().isEmpty() && containsByNameAndSurname(model.getName(), model.getSurname())) {
            return getAllByNameAndSurname(model.getName(), model.getSurname());
        } else if (!model.getName().isEmpty() && model.getSurname().isEmpty() && containsByName(model.getName())) {
            return getAllByName(model.getName());
        } else if (model.getName().isEmpty() && !model.getSurname().isEmpty() && containsBySurname(model.getSurname())) {
            return getAllBySurname(model.getSurname());
        } else {
            throw new Exception("search: user is not found.");
        }
    }

    @Transactional
    public void addFriend(User user, User friend) {
        log.info("service: attempt to add user with id " + friend.getId() + " to user with id " + user.getId() + " friend list...");
        if(!user.getId().equals(friend.getId())) {
            if (!user.getFriends().contains(friend)) {
                user.addFriend(friend);
                log.info("service: user with id " + friend.getId() + " added to user with id " + user.getId() + " friend list, saving the users...");
                saveUser(user);
                log.info("service: user with id " + user.getId() + " has been saved.");
                saveUser(friend);
                log.info("service: user with id " + friend.getId() + " has been saved.");
            } else {
                log.info("service: users is already friends.");
            }
        } else {
            log.info("service: you cant add yourself to your friend list.");
        }
    }

    @Transactional
    public void removeFriend(User user, User friend) {
        log.info("service: attempt to remove user with id " + friend.getId() + " from user with id " + user.getId() + " friend list...");
        System.out.println(user.getFriends().get(0).equals(friend));
        if(!user.getId().equals(friend.getId())) {
            if (user.getFriends().contains(friend)) {
                user.removeFriend(friend);
                log.info("service: user with id " + friend.getId() + " removed from user with id " + user.getId() + " friend list, saving the users...");
                saveUser(user);
                log.info("service: user with id " + user.getId() + " has been saved.");
                saveUser(friend);
                log.info("service: user with id " + friend.getId() + " has been saved.");
            } else {
                log.info("service: users is not friends.");
            }
        } else {
            log.info("service: you cant remove yourself from your friend list.");
        }
    }
}
