package org.factoriaf5.digital_academy.funko_shop.order;

import org.factoriaf5.digital_academy.funko_shop.order.order_exceptions.OrderNotFoundException;
import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItem;
import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItemDTO;
import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItemRepository;
import org.factoriaf5.digital_academy.funko_shop.product.Product;
import org.factoriaf5.digital_academy.funko_shop.product.ProductDTO;
import org.factoriaf5.digital_academy.funko_shop.product.ProductRepository;
import org.factoriaf5.digital_academy.funko_shop.tracking.TrackingRepository;
import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.factoriaf5.digital_academy.funko_shop.user.UserDTO;
import org.factoriaf5.digital_academy.funko_shop.user.UserRepository;
import org.factoriaf5.digital_academy.funko_shop.user.user_exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

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
    void addOrder_ShouldCreateOrderSuccessfully() {

        User user = new User();
        user.setId(1L);
        
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUser(new UserDTO(user.getId(), null, null, null, null, null, null, null, null));
        orderDTO.setStatus("PENDING");
        orderDTO.setTotalPrice(100.0f);
        orderDTO.setTotalItems(2);
        orderDTO.setPaid(true);
        
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L); 
            return order;
        });

        OrderDTO result = orderService.addOrder(orderDTO);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertEquals(100.0f, result.getTotalPrice());
        assertEquals(2, result.getTotalItems());
        assertTrue(result.isPaid());
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void updateOrder_ShouldUpdateOrderSuccessfully() {
       
        Long orderId = 1L;
        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setStatus("PENDING");
        
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus("SHIPPED");
        orderDTO.setTotalPrice(150.0f);
        orderDTO.setTotalItems(3);
        orderDTO.setPaid(true);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

      
        OrderDTO result = orderService.updateOrder(orderId, orderDTO);

        assertNotNull(result);
        assertEquals("SHIPPED", result.getStatus());
        assertEquals(150.0f, result.getTotalPrice());
        assertEquals(3, result.getTotalItems());
        assertTrue(result.isPaid());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getAllOrders_ShouldReturnEmptyList() {
       
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        var result = orderService.getAllOrders();

        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getOrderById_ShouldThrowExceptionWhenNotFound() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(orderId));
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void deleteOrder_ShouldDeleteOrderSuccessfully() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void getOrdersByUser_ShouldThrowExceptionWhenUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> orderService.getOrdersByUser(userId));
        verify(userRepository, times(1)).findById(userId);
    }


    @Test
    void addOrderItemToOrder_ShouldReturnOrderItemDTO() {

        Order order = new Order();
        order.setId(1L);

        Product product = new Product();
        product.setId(1L);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        OrderItemDTO orderItemDTO = new OrderItemDTO(1L, 2, null, new ProductDTO(1L, "Product", null, null, 0.0f, 0, true, null, null), null);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItemDTO result = orderService.addOrderItemToOrder(1L, orderItemDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(2, result.getQuantity());

        verify(orderRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).findById(anyLong());
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void removeOrderItemFromOrder_ShouldDeleteOrderItem() {
 
        Order order = new Order();
        order.setId(1L);
        List<OrderItem> orderItems = new ArrayList<>(); 
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setOrder(order);
        orderItems.add(orderItem); 
        order.setOrderItems(orderItems); 
    
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));    
        
        orderService.removeOrderItemFromOrder(1L, 1L);
  
        verify(orderItemRepository).delete(orderItem);
        verify(orderRepository).save(order);
    }
}
