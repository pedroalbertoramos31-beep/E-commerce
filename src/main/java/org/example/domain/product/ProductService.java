package org.example.domain.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.category.Category;
import org.example.domain.category.CategoryQuery;
import org.example.domain.category.CategoryRepository;
import org.example.domain.product.dto.request.ProductRegisterRequest;
import org.example.domain.product.dto.request.ProductStockIncreaseRequest;
import org.example.domain.product.dto.response.ProductCardResponse;
import org.example.domain.product.dto.response.ProductFoundResponse;
import org.example.domain.product.dto.response.ProductStatusResponse;
import org.example.domain.product.dto.response.ProductStockResponse;
import org.example.domain.product_category.ProductCategory;
import org.example.domain.product_category.ProductCategoryQuery;
import org.example.domain.product_category.ProductCategoryRepository;
import org.example.domain.product_stats.ProductStats;
import org.example.domain.product_stats.ProductStatsQuery;
import org.example.domain.product_stats.ProductStatsRepository;
import org.example.domain.user.UserRepository;
import org.example.infrastructure.exception.error.ProductException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStatsRepository productStatsRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategoryQuery productCategoryQuery;
    private final ProductQuery productQuery;
    private final ProductStatsQuery productStatsQuery;
    private final CategoryQuery categoryQuery;

    private final ProductMapper productMapper;

    // GET

    @Cacheable(value = "product", key = "#productId")
    public ProductFoundResponse findProduct(Long productId){

        ProductStats productStats = productStatsQuery.findByProductIdWithProduct(productId);

        List<ProductCategory> productCategories = productCategoryQuery.getByProductIdWithCategory(productId);

        Product product = productStats.getProduct();

        return productMapper.toProductFoundResponse(product, productStats, productCategories);
    }

    public Page<ProductCardResponse> getProductLikeName(String name, Pageable page){

        Page<ProductStats> productStats = productStatsQuery.getByProductNameWithProduct(name, page);

        return productStats.map(productMapper::toProductCardResponse);
    }

    // CREATE

    @Transactional
    public ProductFoundResponse registerProduct(ProductRegisterRequest request, Long user){

        productQuery.verifyExistsByName(request.name());

        Set<Category> categories = categoryQuery.findByIds(request.categoriesId());

        Product product = productRepository.save(
                Product.create(request.name(), request.price(), request.stock(), userRepository.getReferenceById(user)));

        ProductStats productStats = productStatsRepository.save(
                ProductStats.create(product));

        List<ProductCategory> productCategories = categories.stream()
                .map(category -> ProductCategory.create(product, category))
                .collect(Collectors.toList());

        productCategoryRepository.saveAll(productCategories);

        return productMapper.toProductFoundResponse(product, productStats, productCategories);
    }

    // MODIFY

    @CacheEvict(value = "product", key = "#productId")
    @Transactional
    public ProductStatusResponse toggleProductAvailabilityStatus(Long productId, Long userId){

        Product product = productQuery.findById(productId);

        productQuery.verifyUserIsOwner(product.getVendor().getId(), userId);

        if (product.getStatus() == ProductStatus.AVAILABLE){

            product.changeState(ProductStatus.UNAVAILABLE);

        } else {

            product.changeState(ProductStatus.AVAILABLE);
        }

        return productMapper.toProductStatusResponse(product);
    }

    @CacheEvict(value = "product", key = "#productId")
    @Transactional
    public void deleteProduct(Long productId, Long userId){

        Product product = productQuery.findById(productId);

        productQuery.verifyUserIsOwner(product.getVendor().getId(), userId);

        product.changeState(ProductStatus.DELETED);
    }

    @CacheEvict(value = "product", key = "#productId")
    @Transactional
    public ProductStockResponse increaseProductStock(Long productId, Long userId, ProductStockIncreaseRequest request){

        Product product = productQuery.findById(productId);

        productQuery.verifyUserIsOwner(product.getVendor().getId(), userId);

        product.increaseStock(request.quantity());

        return productMapper.toProductIncreasedStockResponse(product);
    }

    // ==================== ADMIN METHODS ====================

    @CacheEvict(value = "product", key = "#productId")
    @Transactional
    public ProductStatusResponse approveProduct(Long productId){

        Product product = productQuery.findById(productId);

        if (product.getStatus() != ProductStatus.WAITING_APPROVAL) {
            throw new ProductException.InvalidState();
        }

        if (product.getStock() < 1){
            product.changeState(ProductStatus.OUT_OF_STOCK);

        } else {
            product.changeState(ProductStatus.AVAILABLE);
        }

        return productMapper.toProductStatusResponse(product);
    }


}
