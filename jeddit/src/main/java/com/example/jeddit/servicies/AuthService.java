package com.example.jeddit.servicies;

import com.example.jeddit.exceptions.NotCorrectDataException;
import com.example.jeddit.exceptions.NotUniqueDataException;
import com.example.jeddit.exceptions.UserNotFoundException;
import com.example.jeddit.exceptions.WrongPasswordException;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.auth.UserRegistrationRequest;
import com.example.jeddit.models.models.auth.UserSignInRequest;
import com.example.jeddit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    public User registrationUser(UserRegistrationRequest request) throws NotUniqueDataException, NotCorrectDataException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        if(user.getLogin().length() < 5 || !isValidEmail(user.getEmail()) || request.getPassword().length() < 5){
            throw new NotCorrectDataException("Not correct data");
        }
        try {
            return userRepository.save(user);
        } catch (Exception ex){
            throw new NotUniqueDataException(ex.getMessage());
        }
    }

    public User signIn(UserSignInRequest request) throws UserNotFoundException, WrongPasswordException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<User> user = userRepository.findByLogin(request.getLogin());
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }
        return user.get();
    }
    private static boolean isValidEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }
}

