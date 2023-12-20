package kg.startproject.mobimarket_1.service;

import kg.startproject.mobimarket_1.dto.ProductDto;
import kg.startproject.mobimarket_1.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductDto saveProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto, long id);
    List<ProductResponse> findAllProduct();
    ProductDto getProductById(Long id);
    void deleteProduct(Long id);
}