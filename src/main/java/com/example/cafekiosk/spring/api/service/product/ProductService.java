package com.example.cafekiosk.spring.api.service.product;

import com.example.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import com.example.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import com.example.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.example.cafekiosk.spring.domain.product.Product;
import com.example.cafekiosk.spring.domain.product.ProductRepository;
import com.example.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.example.cafekiosk.spring.domain.product.ProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * readOnly = true: 읽기 전용 트랜잭션이 생성됨
 * 따라서, CRUD 에서 CUD 가 동작하지 않음 - read 만 가능
 * JPA 에서의 이점: CUD 가 동작하지 않으므로
 *               스냅샷 저장, 변경 감지를 하지 않아 성능이 향상됨
 * CQRS - Command / Query 를 분리하는데도 도움이 됨
 *        책임을 분리해서 서로 연관이 없게 만들자
 *        Query 용 서비스, Read 용 서비스 구분이 가능해 짐
 *        DB에 대한 EndPoint 를 분리할 수 있음 - Read 용 DB + Write 용 DB
 *        장애 격리 포인트로도 활용 가능
 */
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 동시성 이슈가 존재
    @Transactional
    public ProductResponse createProduct(ProductCreateServiceRequest request) {
        // productNumber
        // 001 002 003 004 ...
        // DB에서 마지막 저장된 Product의 상품 번호를 읽어와서 +1
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    // 화면에서 상품 조회 시
    // 상품 판매 상태가 판매 중지가 아닌 상품들 조회
    public List<ProductResponse> getSellingProdcuts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if (latestProductNumber == null) {
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        return String.format("%03d", nextProductNumberInt);
    }
}
