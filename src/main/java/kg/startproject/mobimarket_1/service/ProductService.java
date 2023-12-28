package kg.startproject.mobimarket_1.service;

import kg.startproject.mobimarket_1.dto.ProductDto;
import kg.startproject.mobimarket_1.dto.ProductFullDto;
import kg.startproject.mobimarket_1.dto.ProductListDto;
import kg.startproject.mobimarket_1.dto.request.ProductSaveRequestDto;
import kg.startproject.mobimarket_1.dto.response.ProductResponse;
import kg.startproject.mobimarket_1.model.Product;

import java.util.List;

public interface ProductService {
    public Product saveProduct(ProductSaveRequestDto requestDto);
    public Product updateProduct(int productId, Product updatedProduct);
    public List<ProductListDto> findAllProducts();
    public ProductFullDto findSingleProduct(int id);
    public Product getProductById(int id);
}