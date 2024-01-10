package com.example.socialweb.services;

import com.example.socialweb.errors.AttributeError;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.Cities;
import com.example.socialweb.models.enums.Countries;
import com.example.socialweb.models.enums.Role;
import com.example.socialweb.models.requestModels.Message;
import com.example.socialweb.models.requestModels.RegisterBody;
import com.example.socialweb.models.requestModels.ReportModel;
import com.example.socialweb.models.requestModels.SearchUserModel;
import com.example.socialweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
        user.setRegisterDate(new Date(System.currentTimeMillis()).toString());
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

    public void addErrorsToBindingResultForRegister(BindingResult bindingResult, RegisterBody registerBody) {
        if (!isValidNameOrSurname(registerBody.getName())) {
            bindingResult.addError(new AttributeError("name", "name", "Your name should to be less 16 and greater 1."));
        }
        if (!isValidNameOrSurname(registerBody.getSurname())) {
            bindingResult.addError(new AttributeError("surname", "surname", "Your name should to be less 16 and greater 1."));
        }
        if (!isValidCity(registerBody.getCity())) {
            bindingResult.addError(new AttributeError("city", "city", "Unknown city."));
        }
        if (!isValidCountry(registerBody.getCountry())) {
            bindingResult.addError(new AttributeError("country", "country", "Unknown country."));
        }
        if (!isValidEmail(registerBody.getEmail())) {
            bindingResult.addError(new AttributeError("email", "email", "Invalid format."));
        }
        if (!isValidPassword(registerBody.getPassword())) {
            bindingResult.addError(new AttributeError("password", "password", "Invalid password."));
        }
        if (!isValidStatus(registerBody.getStatus())) {
            bindingResult.addError(new AttributeError("status", "status", "Invalid status length."));
        }
        if (userRepository.existsUserByEmail(registerBody.getEmail())) {
            bindingResult.addError(new AttributeError("email", "email", "User with this email is already exists."));
        }
    }

    public void addErrorsToBindingResultForUpdate(BindingResult bindingResult, User user) {
        if (!isValidNameOrSurname(user.getName())) {
            bindingResult.addError(new AttributeError("name", "name", "Your name should to be less 16 and greater 1."));
        }
        if (!isValidNameOrSurname(user.getSurname())) {
            bindingResult.addError(new AttributeError("surname", "surname", "Your name should to be less 16 and greater 1."));
        }
        if (!isValidCity(user.getCity())) {
            bindingResult.addError(new AttributeError("city", "city", "Unknown city."));
        }
        if (!isValidCountry(user.getCountry())) {
            bindingResult.addError(new AttributeError("country", "country", "Unknown country."));
        }
        if (!isValidEmail(user.getEmail())) {
            bindingResult.addError(new AttributeError("email", "email", "Invalid format."));
        }
        if (!isValidStatus(user.getStatus())) {
            bindingResult.addError(new AttributeError("status", "status", "Invalid status length."));
        }
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

    public void updateUser(User updateModel, PasswordEncoder passwordEncoder, User user) {
        user.setName(updateModel.getName());
        user.setSurname(updateModel.getSurname());
        user.setAge(updateModel.getAge());
        user.setStatus(updateModel.getStatus());
        user.setEmail(updateModel.getEmail());
        user.setCity(updateModel.getCity());
        user.setCountry(updateModel.getCountry());
        user.setCloseProfile(updateModel.getCloseProfile());
        userRepository.save(user);
    }

    public boolean isRealPassword(String password, String email, PasswordEncoder passwordEncoder) {
        String userPassword = getUserByEmail(email).getPassword();
        return passwordEncoder.matches(password, userPassword);
    }

    public void changeUserPassword(String newPassword, String email, PasswordEncoder passwordEncoder) {
        User user = getUserByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public List<User> search(SearchUserModel model) {
        if (!model.getName().isEmpty() && !model.getSurname().isEmpty()) {
            return userRepository.findAllByNameAndSurname(model.getName(), model.getSurname());
        }
        if (!model.getName().isEmpty() && model.getSurname().isEmpty()) {
            return userRepository.findAllByName(model.getName());
        }
        if (model.getName().isEmpty() && !model.getSurname().isEmpty()) {
            return userRepository.findAllBySurname(model.getSurname());
        }
        return new ArrayList<>();
    }

    public boolean isFriend(User user1, User user2) {
        return user1.getFriendList().contains(user2);
    }

    public void friendRequest(User userFrom, User userTo) {
        ArrayList<String> list = new ArrayList<>();
        list.add("Wants to be your friend.");
        if (!userTo.getMessageList().containsKey(userFrom)) {
            userTo.getMessageList().put(userFrom, list);
            userRepository.save(userTo);
            log.info("friend request: user " + userFrom.getId() + " sends a friendship request to user " + userTo.getId() + ".");
        } else {
            userTo.getMessageList().get(userFrom).add("Wants to be your friend.");
            userRepository.save(userTo);
            log.info("friend request: user " + userFrom.getId() + " sends a friendship request to user " + userTo.getId() + ".");
        }
    }

    public void addErrorsToBindingResultForMessages(User from, User to, BindingResult bindingResult) {
        if (from.getId().equals(to.getId())) {
            bindingResult.addError(new AttributeError("user", "id", "You cant send messages to yourself."));
        }
    }

    public void sendMessage(User from, User to, Message message) {
        to.getMessageList().get(from).add(message.getMessage());
        userRepository.save(to);
        log.info("message: user " + from.getId() + " sends a message to user " + to.getId() + ".");
    }

    public void addToFriend(User user, User from) {
        log.info("friendship: attempt to add user " + from.getId() + " to user " + from.getId() + " friend list.");
        user.getFriendList().add(from);
        from.getFriendList().add(user);
        user.getMessageList().get(from).remove("Wants to be your friend.");
        userRepository.save(user);
        userRepository.save(from);
        log.info("friendship: user " + from.getId() + " has been added to user " + user.getId() + " friend list.");
    }

    public void deleteFriend(User user, User friend) {
        log.info("friendship: attempt to delete user " + friend.getId() + " from user " + user.getId() + " friend list.");
        user.getFriendList().remove(friend);
        friend.getFriendList().remove(user);
        userRepository.save(user);
        userRepository.save(friend);
        log.info("friendship: user " + friend.getId() + " has been deleted from user " + user.getId() + " friend list.");
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllBannedUsers() {
        return userRepository.findUserByIsBanTrue();
    }
    public List<User> getAllReportedUsers(){
        return userRepository.findAllByListOfReportsIsNotEmpty();
    }
}
