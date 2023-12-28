package kg.startproject.mobimarket_1.service.ServiceImplemintation;

import kg.startproject.mobimarket_1.dto.FullInfoUserDto;
import kg.startproject.mobimarket_1.dto.ProductListDto;
import kg.startproject.mobimarket_1.dto.RegistrationUserDto;
import kg.startproject.mobimarket_1.dto.UserDto;
import kg.startproject.mobimarket_1.model.Product;
import kg.startproject.mobimarket_1.model.User;
import kg.startproject.mobimarket_1.exceptions.NotFoundException;
import kg.startproject.mobimarket_1.model.VerificationCode;
import kg.startproject.mobimarket_1.repository.ProductRepository;
import kg.startproject.mobimarket_1.repository.UserRepository;
import kg.startproject.mobimarket_1.repository.VerificationCodeRepository;
import kg.startproject.mobimarket_1.service.ProductService;
import kg.startproject.mobimarket_1.service.RoleService;
import kg.startproject.mobimarket_1.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    public List<User> getUser() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public FullInfoUserDto getSingleUser(int id) {
        return null;
    }

    public FullInfoUserDto getSingleUserByLogin(String username) {
        return null;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
//                String.format("Пользователь '%s' не найден", username)
//        ));
//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(),
//                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
//        );
//    }

    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User();
        user.setUsername(registrationUserDto.getUsername());
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);
    }



    public UserDto updateUser(UserDto userDto, long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id не существует!"));
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setId(user.getId());
        updatedUserDto.setEmail(user.getEmail());
        updatedUserDto.setPassword(user.getPassword());

        return updatedUserDto;
    }

    public List<UserDto> findAllUser() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : userList) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDtoList.add(userDto);
        }

        return userDtoList;
    }

    public void deleteUser(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id не существует!"));
        userRepository.deleteById(user.getId());
    }

    //New method
    public void updateFullDateOfUser(FullInfoUserDto fullInfoUserDto) {
        User user = new User();
        user.setFirstName(fullInfoUserDto.getFirstName());
        user.setLastName(fullInfoUserDto.getLastName());
        user.setAvatar(fullInfoUserDto.getAvatar());
        user.setUsername(fullInfoUserDto.getUsername());
        user.setEmail(fullInfoUserDto.getEmail());
        user.setPhoneNumber(fullInfoUserDto.getPhoneNumber());
        user.setBirthDate(fullInfoUserDto.getBirthDate());
        userRepository.save(user);
    }

    public FullInfoUserDto getUser(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            User userEntity = user.get();
            return new FullInfoUserDto(
                    userEntity.getId(),
                    userEntity.getFirstName(),
                    userEntity.getLastName(),
                    userEntity.getAvatar(),
                    userEntity.getUsername(),
                    userEntity.getEmail(),
                    userEntity.getPhoneNumber(),
                    userEntity.getBirthDate()
            );
        }
        throw new RuntimeException("Пользователь не найден " + id);
    }


    @Transactional
    public ResponseEntity<String> updatePhoneNumber(int userId, String newPhoneNumber) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (findByPhoneNumberAndVerified(newPhoneNumber)) {
                return ResponseEntity.badRequest().body("Номер телефона уже используется другим проверенным пользователем.");
            }

            user.setPhoneNumber(newPhoneNumber);
            user.setVerified(false);
            userRepository.save(user);

            return ResponseEntity.ok("Номер телефона был обновлен.");
        }

        return ResponseEntity.notFound().build();
    }

    public User updateUser(String username, User updatedUser) {
        System.out.println("Вход в метод updateUser");

        User existingUser = userRepository.findByUsername(username).orElse(null);

        if (existingUser == null) {
            return null;

        }

        BeanUtils.copyProperties(updatedUser, existingUser, "user_id", "email","role","login","password","verified","enabled","phone_number");
        return userRepository.save(existingUser);
    }

    public User getUserById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<ProductListDto> getUserProductList(int userId) {
        User user = getUserById(userId);

        if (user != null) {
            List<Product> userProducts = findAllUserProducts(user);

            // Map Product entities to ProductListDto
            return userProducts.stream()
                    .map(product -> new ProductListDto(
                            product.getProduct_id(),
                            product.getImage(),
                            product.getProductName(),
                            product.getPrice(),
                            product.getNumberOfLikes()
                    ))
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("Пользователь с идентификатором не найден: " + userId);
        }
    }

    public List<ProductListDto> getFavoriteProductList(int userId) {
        User user = getUserById(userId);

        if (user != null) {
            Set<Product> favoriteProducts = user.getFavoriteProducts();

            return favoriteProducts.stream()
                    .map(product -> new ProductListDto(
                            product.getProduct_id(),
                            product.getImage(),
                            product.getProductName(),
                            product.getPrice(),
                            product.getNumberOfLikes()
                    ))
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("Пользователь с идентификатором не найден: " + userId);
        }
    }

    public User findByLogin(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user;
        }
        return null;
    }

    public User findByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user;
        }
        return null;
    }

    public User addOrRemoveFavoriteProduct(int userId, int productId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        Product product = productService.getProductById(productId);

        if(product == null)
            return null;


        if (user.getFavoriteProducts().contains(product)) {
            user.getFavoriteProducts().remove(product);
            product.decrementLikeCount();

        } else {
            user.getFavoriteProducts().add(product);
            product.incrementLikeCount();
        }
        return userRepository.save(user);
    }

    public boolean verifyPhoneNumber(String phoneNumber, String code) {
        User user = userRepository.findByPhoneNumber(phoneNumber);

        if (user == null) {
            System.out.println("user is null");
            return false;
        }

        VerificationCode verificationCode = verificationCodeRepository.findByPhoneNumberAndUser(phoneNumber, user);

        if (verificationCode == null || !verificationCode.getCode().equals(code)) {
            System.out.println("Код недействителен !");
            return false;
        }

//        LocalDateTime now = LocalDateTime.now();
//        if (now.isAfter(verificationCode.getExpirationTime())) {
//            System.out.println("Code is already expired");
//            return false;
//        }

//        verificationCode.setPhoneConfirmedAt(now);

        user.setVerified(true);

        userRepository.save(user);

        verificationCodeRepository.delete(verificationCode);

        return true;
    }

    public boolean doesUserExistByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.isPresent();
    }

    public boolean doesUserExistByLogin(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent();
    }

    public List<Product> findAllUserProducts(User user) {
        return productRepository.findAllByUser(user);
    }

    public boolean findByPhoneNumberAndVerified(String newPhoneNumber) {
        User user = userRepository.findByPhoneNumber(newPhoneNumber);

        if(user == null)
            return false;

        return user.getVerified();
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}

