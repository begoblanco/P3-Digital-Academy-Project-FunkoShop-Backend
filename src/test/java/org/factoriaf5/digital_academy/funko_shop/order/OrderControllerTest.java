package org.factoriaf5.digital_academy.funko_shop.order;

import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItemDTO;
import org.factoriaf5.digital_academy.funko_shop.product.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

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
        when(orderService.getAllOrders()).thenReturn(orders);

        ResponseEntity<List<OrderDTO>> response = orderController.getAllOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void getOrderById_ShouldReturnOrder() {
      
        Long orderId = 1L;
        Order order = new Order();
        when(orderService.getOrderById(orderId)).thenReturn(order);

        ResponseEntity<Order> response = orderController.getOrderById(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
        verify(orderService, times(1)).getOrderById(orderId);
    }

    @Test
    void addOrder_ShouldCreateAndReturnOrder() {
      
        OrderDTO orderDTO = new OrderDTO();
        when(orderService.addOrder(orderDTO)).thenReturn(orderDTO);
        
        ResponseEntity<OrderDTO> response = orderController.addOrder(orderDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderDTO, response.getBody());
        verify(orderService, times(1)).addOrder(orderDTO);
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

    @Test
    void getOrdersByUser_ShouldReturnListOfOrders() {
        Long userId = 1L;
        OrderDTO order1 = new OrderDTO();
        OrderDTO order2 = new OrderDTO();
        List<OrderDTO> orders = Arrays.asList(order1, order2);
        when(orderService.getOrdersByUser(userId)).thenReturn(orders);

        ResponseEntity<List<OrderDTO>> response = orderController.getOrdersByUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(orderService, times(1)).getOrdersByUser(userId);
    }

    @Test
    void addOrderItemToOrder_ShouldReturnCreated() {

        OrderItemDTO orderItemDTO = new OrderItemDTO(1L, 2, null, new ProductDTO(1L, "Funkoo", null, "cool funko", 10.0f, 10, true, null, null), null);

        when(orderService.addOrderItemToOrder(anyLong(), any(OrderItemDTO.class))).thenReturn(orderItemDTO);

        ResponseEntity<OrderItemDTO> response = orderController.addOrderItemToOrder(1L, orderItemDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        
        verify(orderService, times(1)).addOrderItemToOrder(anyLong(), any(OrderItemDTO.class));
    }

    @Test
    void removeOrderItemFromOrder_ShouldReturnNoContent() {
  
        ResponseEntity<Void> response = orderController.removeOrderItemFromOrder(1L, 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(orderService, times(1)).removeOrderItemFromOrder(anyLong(), anyLong());
    }
}
