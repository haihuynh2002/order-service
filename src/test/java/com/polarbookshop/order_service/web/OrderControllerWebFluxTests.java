package com.polarbookshop.order_service.web;

import com.polarbookshop.order_service.domain.Order;
import com.polarbookshop.order_service.domain.OrderService;
import com.polarbookshop.order_service.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@WebFluxTest
public class OrderControllerWebFluxTests {

    @Autowired
    WebTestClient webClient;

    @MockBean
    private OrderService orderService;

    @Test
    void whenBookNotAvailableThenRejectOrder() {
        var orderRequest = new OrderRequest("1234567890", 3);
        var expectedOrder = orderService.buildRejectedOrder(
                orderRequest.isbn(), orderRequest.quantity()
        );

        given(orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity()))
                .willReturn(Mono.just(expectedOrder));

        webClient
                .post()
                .uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).value(order -> {
                    assertThat(order).isNotNull();
                    assertThat(order.status()).isEqualTo(OrderStatus.REJECTED);
                });

    }
}
