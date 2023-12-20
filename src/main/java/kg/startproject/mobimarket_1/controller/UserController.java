package kg.startproject.mobimarket_1.controller;

import kg.startproject.mobimarket_1.dto.UserDto;
import kg.startproject.mobimarket_1.service.ServiceImplemintation.UserServiceImpl;
import io.swagger.annotations.Api;
import kg.startproject.mobimarket_1.configuration.SwaggerConfig;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@Api(tags = SwaggerConfig.USER)
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userServiceImpl;

//    @PostMapping("/save")
//    public UserDto saveUser(@RequestBody UserDto userDto) {
//
//        return userServiceImpl.saveUser(userDto);
//    }

    @PutMapping("/updateUser")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable long id) {
        return userServiceImpl.updateUser(userDto, id);
    }

    @GetMapping("/findAll")
    public List<UserDto> findAll() {
        return userServiceImpl.findAllUser();
    }

    @DeleteMapping("/deleteUser")
    public void deleteUser(@RequestParam Long id) {
        userServiceImpl.deleteUser(id);

    }
}
