package com.patrones.u1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService - orquestación de órdenes con dependencias inyectadas (DIP)")
class OrderServiceTest {

    @Mock
    private TaxCalculator taxCalculator;

    @Mock
    private OrderRepository repository;

    @Mock
    private EmailNotifier notifier;

    @Mock
    private DiscountStrategy discountStrategy;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(taxCalculator, repository, notifier, discountStrategy);
    }

    @Test
    @DisplayName("Debe procesar la orden calculando total, aplicando descuento, persistiendo y notificando")
    void shouldProcessOrderSuccessfully() {
        List<Double> prices = List.of(100.0, 200.0);
        when(taxCalculator.calculateTotal(prices)).thenReturn(357.0);
        when(discountStrategy.apply(357.0)).thenReturn(303.45);

        orderService.processOrder("ORD-100", "cliente@mail.com", prices);

        verify(taxCalculator).calculateTotal(prices);
        verify(discountStrategy).apply(357.0);
        verify(repository).save("ORD-100", 303.45);
        verify(notifier).sendConfirmation("cliente@mail.com", "ORD-100");
    }

    @Test
    @DisplayName("Debe admitir estrategia VIP aplicando 15% de descuento")
    void shouldApplyVipDiscount() {
        DiscountStrategy vip = new VipDiscount();
        OrderService service = new OrderService(taxCalculator, repository, notifier, vip);

        List<Double> prices = List.of(100.0);
        when(taxCalculator.calculateTotal(prices)).thenReturn(100.0);

        service.processOrder("ORD-101", "vip@mail.com", prices);

        verify(repository).save("ORD-101", 85.0);
        verify(notifier).sendConfirmation("vip@mail.com", "ORD-101");
    }

    @Test
    @DisplayName("Debe admitir estrategia Regular aplicando 5% de descuento")
    void shouldApplyRegularDiscount() {
        DiscountStrategy regular = new RegularDiscount();
        OrderService service = new OrderService(taxCalculator, repository, notifier, regular);

        List<Double> prices = List.of(100.0);
        when(taxCalculator.calculateTotal(prices)).thenReturn(100.0);

        service.processOrder("ORD-102", "regular@mail.com", prices);

        verify(repository).save("ORD-102", 95.0);
        verify(notifier).sendConfirmation("regular@mail.com", "ORD-102");
    }

    @Test
    @DisplayName("Debe admitir estrategia NoDiscount sin alterar monto")
    void shouldApplyNoDiscount() {
        DiscountStrategy noDiscount = new NoDiscount();
        OrderService service = new OrderService(taxCalculator, repository, notifier, noDiscount);

        List<Double> prices = List.of(100.0);
        when(taxCalculator.calculateTotal(prices)).thenReturn(100.0);

        service.processOrder("ORD-103", "nodisc@mail.com", prices);

        verify(repository).save("ORD-103", 100.0);
        verify(notifier).sendConfirmation("nodisc@mail.com", "ORD-103");
    }
}
