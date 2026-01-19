package com.nexus.product_service.impl;

import com.nexus.product_service.dto.ProductDto;
import com.nexus.product_service.entity.Category;
import com.nexus.product_service.entity.Product;
import com.nexus.product_service.mapper.ProductMapper;
import com.nexus.product_service.repository.ProductRepository;
import com.nexus.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final String uploadDir = System.getProperty("user.dir")+"/uploads/products/";

    @Override
    public ProductDto createProduct(ProductDto dto) {
        Category category=null;
        if(dto.getCategoryId() !=null){
            category = new Category();
            category.setId(dto.getCategoryId());
        }
        Product product = productMapper.toEntity(dto, category);
        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("No Such Product Found"));
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscountPrice(dto.getDiscountPrice());
        if(dto.getCategoryId()!=null){
            Category category = new Category();
            category.setId(dto.getCategoryId());
            product.setCategory(category);
        }
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long Id) {
        productRepository.deleteById(Id);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not Found"));
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto uploadImage(Long productId, MultipartFile file) throws IOException {
        if(file.isEmpty()) {
            throw new RuntimeException("Image file is Empty");
        }

        long maxSize= 2*1024*1024;
        if(file.getSize()>maxSize){
            throw  new RuntimeException("File size must be less than 2MB");
        }

        List<String> allowedType = List.of("image/jpeg", "image/jpg", "image/png");
        if(!allowedType.contains(file.getContentType())){
            throw new RuntimeException("Only jpg, png and jpeg are allowed");
        }

        String originalName = file.getOriginalFilename();
        if(originalName==null || originalName.contains(".")){
            throw new RuntimeException("Invalid file name");
        }

        String ext = originalName.substring(originalName.lastIndexOf(".")+1).toLowerCase();
        List<String> allowedExtension= List.of("jpg", "png", "jpeg");
        if(!allowedExtension.contains(ext)){
            throw new RuntimeException("Invalid image extension");
        }

        Product product = productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product not Found"));

        File folder = new File(uploadDir);
        if(!folder.exists()){
            folder.mkdir();
        }

        String fileName = UUID.randomUUID().toString()+"."+ext;
        Path filePath = Paths.get(uploadDir+fileName);
        Files.write(filePath, file.getBytes());

        String imageUrl = "/products/images/"+fileName;
        product.setImageUrl(imageUrl);
        Product saved = productRepository.save(product);

        return productMapper.toDto(saved);
    }

    @Override
    public Page<ProductDto> getALlProduct(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> searchProduct(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.searchProducts(keyword, pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> filterProducts(Long categoryId, Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.advanceFilter(null, categoryId, minPrice, maxPrice, pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> advanceFilter(String keyword, Long categoryId, Double minPrice, Double maxPrice, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.advanceFilter(keyword, categoryId,  minPrice, maxPrice, pageable);
        return productPage.map(productMapper::toDto);
    }
}
