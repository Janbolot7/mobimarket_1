package kg.startproject.mobimarket_1.service;

import kg.startproject.mobimarket_1.dto.FullInfoUserDto;
import kg.startproject.mobimarket_1.dto.ProductListDto;
import kg.startproject.mobimarket_1.model.Product;
import kg.startproject.mobimarket_1.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> getUser();
    User saveUser (User user);
    public FullInfoUserDto getSingleUser(int id);
    public FullInfoUserDto getSingleUserByLogin(String username);
//    void deleteUser (int id);
    public ResponseEntity<String> updatePhoneNumber(int userId, String newPhoneNumber);
    public User updateUser(String username, User updatedUser);
    public User getUserById(int userId);
    public boolean verifyPhoneNumber(String phoneNumber, String code);
    public User addOrRemoveFavoriteProduct(int userId, int productId);
    public boolean doesUserExistByEmail(String email);
    public boolean doesUserExistByLogin(String username);
    public List<ProductListDto> getUserProductList(int userId);
    public List<ProductListDto> getFavoriteProductList(int userId);
    public User findByLogin(String username);

    List<Product> findAllUserProducts(User user);

    public boolean findByPhoneNumberAndVerified(String newPhoneNumber);
}
