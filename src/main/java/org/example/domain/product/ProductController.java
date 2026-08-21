package org.example.domain.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.domain.product.dto.request.ProductRegisterRequest;
import org.example.domain.product.dto.request.ProductStockIncreaseRequest;
import org.example.domain.product.dto.response.ProductCardResponse;
import org.example.domain.product.dto.response.ProductFoundResponse;
import org.example.domain.product.dto.response.ProductStatusResponse;
import org.example.domain.product.dto.response.ProductStockResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {

    final private ProductService productService;

    /* SEARCH PRODUCTS */

    @GetMapping("/{productId}")
    public ResponseEntity<ProductFoundResponse> findProduct(
            @Positive @PathVariable Long productId
    ) {

        ProductFoundResponse product = productService.findProduct(productId);

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/like")
    public ResponseEntity<Page<ProductCardResponse>> getProductLikeName(
            @RequestParam String name,
            @PageableDefault(page = 0, size = 10) Pageable page
    )
    {
        Page<ProductCardResponse> products = productService.getProductLikeName(name, page);

        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ProductFoundResponse> registerProduct(
            @Valid @RequestBody ProductRegisterRequest request,
            @AuthenticationPrincipal(expression = "id") Long user
    ){

        ProductFoundResponse response = productService.registerProduct(request, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductStatusResponse> toggleProductAvailabilityStatus(
            @PathVariable Long productId,
            @AuthenticationPrincipal(expression = "id") Long userId){

        ProductStatusResponse product = productService.toggleProductAvailabilityStatus(productId, userId);

        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal(expression = "id") Long userId){

        productService.deleteProduct(productId, userId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/stock/{productId}")
    public ResponseEntity<ProductStockResponse> increaseProductStock(
            @PathVariable @Positive Long productId,
            @AuthenticationPrincipal(expression = "id") Long userId,
            @Valid @RequestBody ProductStockIncreaseRequest request
    ) {

        ProductStockResponse product = productService.increaseProductStock(productId, userId, request);

        return ResponseEntity.ok(product);
    }



    /* ADMIN METHODS */

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/approve/{productId}")
    public ResponseEntity<ProductStatusResponse> approveProduct(@PathVariable @Positive Long productId){

        ProductStatusResponse response = productService.approveProduct(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
