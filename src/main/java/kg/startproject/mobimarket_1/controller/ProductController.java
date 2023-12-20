package kg.startproject.mobimarket_1.controller;

import io.swagger.annotations.Api;
import kg.startproject.mobimarket_1.dto.ProductDto;
import kg.startproject.mobimarket_1.dto.response.ProductResponse;
import kg.startproject.mobimarket_1.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kg.startproject.mobimarket_1.configuration.SwaggerConfig.PRODUCT;

@Api(tags = PRODUCT)
@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    @PostMapping("/saveProduct")
    //,@RequestPart MultipartFile file
    public ProductDto saveProduct(@RequestBody ProductDto productDto){
        return productService.saveProduct(productDto);
    }

    @PutMapping("/updateProduct/{id}")
    public ProductDto updateProduct(@RequestBody ProductDto productDto, @PathVariable long id){
        return productService.updateProduct(productDto, id);
    }
    @GetMapping("/findAllProducts")
    public List<ProductResponse> findAll(){
        return productService.findAllProduct();
    }
    @DeleteMapping("/deleteProduct/{id}")
    public void deleteProduct(@RequestParam Long id){
        productService.deleteProduct(id);
    }
}