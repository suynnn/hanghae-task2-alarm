package org.hanghae.hanghaetask2alarm.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.hanghae.hanghaetask2alarm.domain.product.entity.Product;
import org.hanghae.hanghaetask2alarm.domain.product.exception.ProductNotFoundException;
import org.hanghae.hanghaetask2alarm.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void updateProductStock(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("해당 상품 아이디에 맞는 상품을 찾을 수 없습니다."));

        Long stock = 100L;
        product.updateStock(stock);
    }

    @Transactional
    public void updateProductRestockRound(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("해당 상품 아이디에 맞는 상품을 찾을 수 없습니다."));
        product.updateRestockRound();
    }
}
