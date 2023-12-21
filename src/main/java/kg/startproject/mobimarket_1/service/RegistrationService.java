package kg.startproject.mobimarket_1.service;

import kg.startproject.mobimarket_1.dto.CheckUserDto;
import kg.startproject.mobimarket_1.dto.request.JwtRequest;
import kg.startproject.mobimarket_1.dto.RegistrationUserDto;
import kg.startproject.mobimarket_1.dto.response.CheckUserResponse;
import kg.startproject.mobimarket_1.dto.response.RegistrationResponse;
import kg.startproject.mobimarket_1.model.User;
import kg.startproject.mobimarket_1.exceptions.AppError;
import kg.startproject.mobimarket_1.repository.UserRepository;
import kg.startproject.mobimarket_1.service.ServiceImplemintation.UserServiceImpl;
import kg.startproject.mobimarket_1.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserServiceImpl userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNAUTHORIZED.value(),
                            "Неправильный логин или пароль"),
                    HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.ok(new RegistrationResponse(null, userDetails.getUsername(), token));
    }

    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        if (userRepository.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким именем уже существует"), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findByEmail(registrationUserDto.getEmail()).isPresent()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким email уже существует"), HttpStatus.BAD_REQUEST);
        }

        User user = userService.createNewUser(registrationUserDto);

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.ok(new RegistrationResponse(user.getId(), user.getUsername(), token));

    }

    public ResponseEntity<?> checkUserAvailability(@RequestBody CheckUserDto checkUserDto) {
        boolean isUsernameExists = userRepository.findByUsername(checkUserDto.getUsername()).isPresent();
        boolean isEmailExists = userRepository.findByEmail(checkUserDto.getEmail()).isPresent();

        if (isUsernameExists || isEmailExists) {
            String errorMessage = "";
            if (isUsernameExists && isEmailExists) {
                errorMessage = "Пользователь с таким именем и email уже существует";
            } else if (isUsernameExists) {
                errorMessage = "Пользователь с таким именем уже существует";
            } else if (isEmailExists) {
                errorMessage = "Пользователь с таким email уже существует";
            }
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), errorMessage), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok("Имя пользователя и email доступны для регистрации");
    }

}
