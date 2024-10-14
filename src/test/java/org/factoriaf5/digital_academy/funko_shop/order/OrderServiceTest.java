package org.factoriaf5.digital_academy.funko_shop.order;

import org.factoriaf5.digital_academy.funko_shop.order.order_exceptions.OrderNotFoundException;
import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItemRepository;
import org.factoriaf5.digital_academy.funko_shop.product.Product;
import org.factoriaf5.digital_academy.funko_shop.product.ProductRepository;
import org.factoriaf5.digital_academy.funko_shop.tracking.TrackingRepository;
import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private TrackingRepository trackingRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addOrder_Success() {
        Order order = new Order();
        order.setStatus("PENDING");
        order.setTotalPrice(100.0f);
        order.setTotalItems(2);
        order.setPaid(false);

        User user = new User();
        user.setId(1L);

        Order savedOrder = new Order();
        savedOrder.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setName("Funko Pop");

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        OrderDTO result = orderService.addOrder(order, user);

        verify(orderRepository, times(2)).save(any(Order.class));

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void updateOrder_Success() {
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setStatus("PENDING");

        OrderDTO updatedOrderDTO = new OrderDTO();
        updatedOrderDTO.setStatus("COMPLETED");
        updatedOrderDTO.setTotalPrice(150.0f);
        updatedOrderDTO.setTotalItems(3);
        updatedOrderDTO.setPaid(true);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(existingOrder);

        OrderDTO result = orderService.updateOrder(1L, updatedOrderDTO);

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getOrderById_Success() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("PENDING");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDTO result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("PENDING", result.getStatus());
        verify(orderRepository).findById(1L);
    }

    @Test
    void getOrderById_OrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));
        verify(orderRepository).findById(1L);
    }

    @Test
    void deleteOrder_Success() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.deleteOrder(1L);

        verify(orderRepository).delete(order);
    }

    @Test
    void deleteOrder_OrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(1L));
        verify(orderRepository).findById(1L);
    }

    @Test
    void getOrdersByUser_Success() {
        User user = new User();
        user.setId(1L);
        Page<Order> ordersPage = new PageImpl<>(Collections.singletonList(new Order()));
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(orderRepository.findByUser(user, pageRequest)).thenReturn(ordersPage);

        Page<OrderDTO> result = orderService.getOrdersByUser(user, pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(orderRepository).findByUser(user, pageRequest);
    }
}
