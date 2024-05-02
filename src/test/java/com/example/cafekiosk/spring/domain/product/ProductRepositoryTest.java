package com.example.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static com.example.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ActiveProfiles("test")
//@SpringBootTest
@DataJpaTest
class ProductRepositoryTest {
    
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);

        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);

        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // then
        assertThat(products).hasSize(2) // 리스트에 대한 검증은 우선 사이즈 검증을 주로 한다.
                .extracting("productNumber", "name", "sellingStatus") // extractin은 검증하고자 하는 필드만 추출하는데 쓰인다.
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @DisplayName("상품 번호 리스트로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);

        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);

        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then
        assertThat(products).hasSize(2) // 리스트에 대한 검증은 우선 사이즈 검증을 주로 한다.
                .extracting("productNumber", "name", "sellingStatus") // extractin은 검증하고자 하는 필드만 추출하는데 쓰인다.
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
    void findLatestProductNumber() {
        // given
        String targetProductNumber = "003";

        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);

        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);

        Product product3 = createProduct(targetProductNumber, HANDMADE, STOP_SELLING, "팥빙수", 7000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(latestProductNumber).isEqualTo("003");

    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
    void findLatestProductNumberWhenProductNumberIsNull() {
        // given
        // when
        String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(latestProductNumber).isNull();

    }

    private Product createProduct(String number, ProductType productType, ProductSellingStatus productSellingStatus, String name, int price) {
        return Product.builder()
                .productNumber(number)
                .type(productType)
                .sellingStatus(productSellingStatus)
                .name(name)
                .price(price)
                .build();
    }


}
