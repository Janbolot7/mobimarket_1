package kg.startproject.mobimarket_1.controller;

import kg.startproject.mobimarket_1.dto.FullInfoUserDto;
import kg.startproject.mobimarket_1.dto.ProductListDto;
import kg.startproject.mobimarket_1.dto.UserDto;
import kg.startproject.mobimarket_1.model.User;
import kg.startproject.mobimarket_1.repository.ProductRepository;
import kg.startproject.mobimarket_1.repository.VerificationCodeRepository;
import kg.startproject.mobimarket_1.service.ProductService;
import kg.startproject.mobimarket_1.service.ServiceImplemintation.UserServiceImpl;
import io.swagger.annotations.Api;
import kg.startproject.mobimarket_1.configuration.SwaggerConfig;
import kg.startproject.mobimarket_1.service.SmsService;
import kg.startproject.mobimarket_1.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@Api(tags = SwaggerConfig.USER)
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final UserServiceImpl userServiceImpl;
    @Autowired
    private final UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    @Autowired
    private final SmsService smsService;


    @PutMapping("/updateUser")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable long id) {
        return userServiceImpl.updateUser(userDto, id);
    }

    @GetMapping("/findAll")
    public List<UserDto> findAll() {
        return userServiceImpl.findAllUser();
    }

//    @DeleteMapping("/deleteUser")
//    public void deleteUser(@RequestParam Long id) {
//        userServiceImpl.deleteUser(id);
//    }

    //new controller for Full Info User
    @PostMapping("/fullInfoOfUser")
    public void addUser(@RequestBody FullInfoUserDto fullInfoUserDto) {
        userServiceImpl.updateFullDateOfUser(fullInfoUserDto);
    }

    @GetMapping("/userProducts/{userId}")
    public ResponseEntity<List<ProductListDto>> getUserProductListDto(@PathVariable int userId) {
        List<ProductListDto> userProductList = userService.getUserProductList(userId);
        return ResponseEntity.ok(userProductList);
    }


    @GetMapping("/favorite-products/{userId}")
    public ResponseEntity<List<ProductListDto>> getFavoriteProductList(@PathVariable int userId) {
        List<ProductListDto> favoriteProductList = userService.getFavoriteProductList(userId);
        return ResponseEntity.ok(favoriteProductList);
    }

    @PutMapping("/{userId}/favorite-products/{productId}")
    public ResponseEntity<User> addOrRemoveFavoriteProduct(
            @PathVariable int userId,
            @PathVariable int productId
    ) {
        User updatedUser = userService.addOrRemoveFavoriteProduct(userId, productId);

        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/update-phone-number/{userId}")
    public ResponseEntity<String> updatePhoneNumber(
            @PathVariable int userId,
            @RequestParam String newPhoneNumber
    ) {
        return userService.updatePhoneNumber(userId, newPhoneNumber);
    }




    @PostMapping("/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String phoneNumber) {
        smsService.sendVerificationCode(phoneNumber);
        return ResponseEntity.ok("Код проверки отправлен успешно.");
    }


    @PostMapping("/verify-phone-number")
    public ResponseEntity<String> verifyPhoneNumber(
            @RequestParam String phoneNumber,
            @RequestParam String code
    ) {
        boolean isVerified = userService.verifyPhoneNumber(phoneNumber, code);

        if (isVerified) {
            return ResponseEntity.ok("Номер телефона успешно проверен.");
        } else {
            return ResponseEntity.badRequest().body("Проверка номера телефона не удалась.");
        }
    }
}
