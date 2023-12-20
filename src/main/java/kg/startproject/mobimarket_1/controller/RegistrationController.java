package kg.startproject.mobimarket_1.controller;

import kg.startproject.mobimarket_1.dto.request.JwtRequest;
import kg.startproject.mobimarket_1.dto.RegistrationUserDto;
import kg.startproject.mobimarket_1.service.RegistrationService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static kg.startproject.mobimarket_1.configuration.SwaggerConfig.REGISTR;


@Api(tags = REGISTR)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RegistrationController {
    private final RegistrationService authService;

    @PostMapping("/authentication")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }

    @PostMapping("/checkAvailability")
    public ResponseEntity<?> checkUserAvailability(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.checkUserAvailability(registrationUserDto);
    }

}
