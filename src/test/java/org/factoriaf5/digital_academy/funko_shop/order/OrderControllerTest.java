package org.factoriaf5.digital_academy.funko_shop.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Autowired
    private Principal mockPrincipal;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() {
        OrderDTO order1 = new OrderDTO();
        OrderDTO order2 = new OrderDTO();
        List<OrderDTO> orders = Arrays.asList(order1, order2);

        Pageable pageable = PageRequest.of(0, 10);

        when(orderService.getAllOrders(any(Pageable.class)))
                .thenReturn(new PageImpl<>(orders, pageable, orders.size()));

        ResponseEntity<Page<OrderDTO>> response = orderController.getAllOrders(mockPrincipal, pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    void getOrderById_ShouldReturnOrder() {
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderId);

        when(orderService.getOrderById(orderId)).thenReturn(orderDTO);

        Principal principal = mock(Principal.class);

        ResponseEntity<OrderDTO> response = orderController.getOrderById(principal, orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderDTO, response.getBody());
        verify(orderService, times(1)).getOrderById(orderId);
    }

    @Test
    void updateOrder_ShouldUpdateAndReturnOrder() {

        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        when(orderService.updateOrder(orderId, orderDTO)).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderController.updateOrder(orderId, orderDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDTO, response.getBody());
        verify(orderService, times(1)).updateOrder(orderId, orderDTO);
    }

    @Test
    void deleteOrder_ShouldReturnNoContent() {

        Long orderId = 1L;
        doNothing().when(orderService).deleteOrder(orderId);

        ResponseEntity<Void> response = orderController.deleteOrder(orderId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(orderService, times(1)).deleteOrder(orderId);
    }
}
