package kg.startproject.mobimarket_1.service.ServiceImplemintation;

import kg.startproject.mobimarket_1.dto.ProductDto;
import kg.startproject.mobimarket_1.dto.response.ProductResponse;
import kg.startproject.mobimarket_1.exceptions.NotFoundException;
import kg.startproject.mobimarket_1.model.Product;
import kg.startproject.mobimarket_1.repository.ProductRepository;
import kg.startproject.mobimarket_1.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

//    @Override
//    public ProductDto saveProduct(ProductDto productDto) {
//
//        Product product = ProductMapper.INSTANCE.toEntity(productDto);
//
//        try {
//            Product productSave = productRepository.save(product);
//            return ProductMapper.INSTANCE.toDTO(productSave);
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Не удалось сохранить продукт в базе!", e);
//        }
//    }
    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(productDto.getPrice()));
        product.setDescription(productDto.getDescription());

        try {
            Product savedProduct = productRepository.save(product);
            ProductDto savedProductDto = new ProductDto();
            savedProductDto.setPrice(savedProduct.getPrice().doubleValue());
            savedProductDto.setDescription(savedProduct.getDescription());
            return savedProductDto;
        } catch (RuntimeException e) {
            throw new RuntimeException("Не удалось сохранить продукт в базе!", e);
        }
    }


    private String fileDownload(MultipartFile file) {
        try {
            File path = new File("C:\\" + file.getOriginalFilename());
            path.createNewFile();
            FileOutputStream output = new FileOutputStream(path);
            output.write(file.getBytes());
            output.close();
            return path.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, long id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Продукта с таким id не существует!"));

        product.setPrice(BigDecimal.valueOf(productDto.getPrice()));
        product.setDescription(productDto.getDescription());

        try {
            Product productSave = productRepository.save(product);
            ProductDto updatedProductDto = new ProductDto();
            updatedProductDto.setPrice(productSave.getPrice().doubleValue());
            updatedProductDto.setDescription(productSave.getDescription());
            return updatedProductDto;
        } catch (RuntimeException e) {
            throw new RuntimeException("Не удалось обновить продукт в базе!", e);
        }
    }


//    @Override
//    public ProductDto updateProduct(ProductDto productDto, long id) {
//        Product product = this.productRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Продукта с таким id не существует!"));
//
//        ProductMapper.INSTANCE.update(product, productDto);
//
//        try {
//            Product productSave = productRepo.save(product);
//            return ProductMapper.INSTANCE.toDTO(productSave);
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Не удалось обновить продукт в базе!", e);
//        }
//    }

    @Override
    public List<ProductResponse> findAllProduct() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setPrice(product.getPrice().doubleValue());
            productResponse.setDescription(product.getDescription());
            productResponses.add(productResponse);
        }

        return productResponses;
    }

//    @Override
//    public List<ProductResponse> findAllProduct() {
//        return ProductMapper.INSTANCE.toResponseList(productRepository.findAll());
//    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Продукта с таким id не существует!"));

        ProductDto productDto = new ProductDto();
        productDto.setPrice(product.getPrice().doubleValue());
        productDto.setDescription(product.getDescription());

        return productDto;
    }

//    @Override
//    public ProductDto getProductById(Long id) {
//        Product product = this.productRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Продукта с таким id не существует!"));
//        return ProductMapper.INSTANCE.toDTO(product);
//    }

    @Override
    public void deleteProduct(Long id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Продукта с таким id не существует!"));
        productRepository.deleteById(product.getId());
    }
}