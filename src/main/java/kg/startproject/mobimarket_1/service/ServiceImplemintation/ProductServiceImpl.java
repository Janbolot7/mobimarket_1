package kg.startproject.mobimarket_1.service.ServiceImplemintation;


import com.twilio.rest.api.v2010.Account;
import kg.startproject.mobimarket_1.dto.ProductFullDto;
import kg.startproject.mobimarket_1.dto.ProductListDto;
import kg.startproject.mobimarket_1.dto.request.ProductSaveRequestDto;
import kg.startproject.mobimarket_1.exceptions.NotFoundException;
import kg.startproject.mobimarket_1.exceptions.VerificationException;
import kg.startproject.mobimarket_1.model.Product;
import kg.startproject.mobimarket_1.model.User;
import kg.startproject.mobimarket_1.repository.ProductRepository;
import kg.startproject.mobimarket_1.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product saveProduct(ProductSaveRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName().toString());
        User user = (User) authentication.getPrincipal();

        if (!user.getVerified()) {
            System.out.println("Пользователь не проверен ");
            throw new VerificationException("Пользователь не верифицирован. Чтобы добавить новый продукт, необходимо предварительно подтвердить свой номер телефона.");
        }

        Product product = new Product();
        String fileName = StringUtils.cleanPath(requestDto.getImage().getOriginalFilename());

        if (fileName.contains("..")) {
            System.out.println("Недействительный файл");
        }

        try {
            product.setImage(Base64.getEncoder().encodeToString(requestDto.getImage().getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        product.setShortDescription(requestDto.getShortDescription());
        product.setFullDescription(requestDto.getFullDescription());
        product.setProductName(requestDto.getProductName());
        product.setPrice(requestDto.getPrice());
        product.setUser(user);

        return productRepository.save(product);
    }

    @Override
    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public Product updateProduct(int productId, Product updatedProduct) {
        System.out.println("Вход в метод updateProduct");

        Product existingProduct = productRepository.findById(productId).orElse(null);


        if (existingProduct == null) {
            return null;

        }

        BeanUtils.copyProperties(updatedProduct, existingProduct, "product_id");

        return productRepository.save(existingProduct);
    }

    @Override
    public List<ProductListDto> findAllProducts() {
        List<Product> products = productRepository.findAll();

        List<ProductListDto> productListDtos = products.stream()
                .map(product -> new ProductListDto(
                        product.getProduct_id(),
                        product.getImage(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getNumberOfLikes() // Assuming you have a getter for the like count
                ))
                .collect(Collectors.toList());

        return productListDtos;
    }

    @Override
    public ProductFullDto findSingleProduct(int id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return new ProductFullDto(
                    product.getProduct_id(),
                    product.getImage(),
                    product.getProductName(),
                    product.getPrice(),
                    product.getShortDescription(),
                    product.getFullDescription(),
                    product.getNumberOfLikes()
            );
        }

        throw new RuntimeException("ProductDto не найден " + id);
    }

    public boolean incrementLikeCount(Integer product_id) {
        Optional<Product> productOptional = productRepository.findById(product_id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.incrementLikeCount(); // Вызываем метод инкремента количества лайков в классе Product
            productRepository.save(product);
            return true;
        }
        return false;
    }

    public boolean decrementLikeCount(Integer product_id) {
        Optional<Product> productOptional = productRepository.findById(product_id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.decrementLikeCount(); // Вызываем метод декремента количества лайков в классе Product
            productRepository.save(product);
            return true;
        }
        return false;
    }
}