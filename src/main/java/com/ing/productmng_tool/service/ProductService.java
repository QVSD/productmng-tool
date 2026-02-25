package com.ing.productmng_tool.service;


import com.ing.productmng_tool.model.entity.dto.ChangePriceRequest;
import com.ing.productmng_tool.model.entity.dto.ProductRequest;
import com.ing.productmng_tool.model.entity.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProductById(Long id);

    List<ProductResponse> getAllProducts();

    ProductResponse changePrice(Long id, ChangePriceRequest request);

    void deleteProduct(Long id);
}
