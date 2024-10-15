package org.factoriaf5.digital_academy.funko_shop.order;

import org.factoriaf5.digital_academy.funko_shop.category.Category;
import org.factoriaf5.digital_academy.funko_shop.category.CategoryDTO;
import org.factoriaf5.digital_academy.funko_shop.order.order_exceptions.OrderNotFoundException;
import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItem;
import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItemDTO;
import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItemRepository;
import org.factoriaf5.digital_academy.funko_shop.product.Product;
import org.factoriaf5.digital_academy.funko_shop.product.ProductDTO;
import org.factoriaf5.digital_academy.funko_shop.product.ProductRepository;
import org.factoriaf5.digital_academy.funko_shop.product.product_exceptions.ProductNotFoundException;
import org.factoriaf5.digital_academy.funko_shop.tracking.TrackingRepository;
import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.ArrayList;
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

    @Test
    void mapToOrderItem_Success() {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(1L);
        dto.setQuantity(2);
        Product product = new Product();
        product.setId(1L);
        dto.setProduct(new ProductDTO(1L, "Funko Pop", null, null, null, 20.0f, 0.0f, 10, null, null, 0, 0, 0));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Order order = new Order();
        OrderItem result = orderService.mapToOrderItem(dto, order);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(2, result.getQuantity());
        assertEquals(order, result.getOrder());
        assertEquals(product, result.getProduct());
    }

    @Test
    void mapToOrderItem_ProductNotFound() {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(1L);
        dto.setQuantity(2);
        dto.setProduct(new ProductDTO(1L, "Funko Pop", null, null, null, 20.0f, 0.0f, 10, null, null, 0, 0, 0));

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Order order = new Order();

        assertThrows(ProductNotFoundException.class, () -> orderService.mapToOrderItem(dto, order));
    }

    @Test
    void mapToDTO_Success() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("PENDING");
        order.setTotalPrice(100.0f);
        order.setTotalItems(2);
        order.setPaid(false);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(2);
        orderItem.setOrder(order);

        Product product = new Product();
        product.setId(1L);
        product.setOrderItems(new ArrayList<>());
        orderItem.setProduct(product);

        OrderDTO result = orderService.mapToDTO(order);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("PENDING", result.getStatus());
        assertEquals(100.0f, result.getTotalPrice());
        assertEquals(2, result.getTotalItems());
        assertFalse(result.isPaid());
        assertEquals(0, result.getOrderItems().size());
    }

    @Test
    void mapToOrderItemDTO_Success() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("PENDING");
        order.setTotalPrice(100.0f);
        order.setTotalItems(2);
        order.setPaid(false);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(2);
        orderItem.setOrder(order);

        Product product = new Product();
        product.setId(1L);
        product.setOrderItems(new ArrayList<>());
        orderItem.setProduct(product);

        OrderItemDTO result = orderService.mapToOrderItemDTO(orderItem);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(2, result.getQuantity());
        assertNotNull(result.getProduct());
        assertEquals(1L, result.getProduct().getId());
    }

    @Test
    void mapToProductDTO_Success() {

        Product product = new Product();
        product.setId(1L);
        product.setOrderItems(new ArrayList<>());
        product.setName("Funko Pop");
        product.setPrice(20.0f);
        product.setDiscount(50);
        product.setStock(10);

        ProductDTO result = orderService.mapToProductDTO(product);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Funko Pop", result.getName());
        assertEquals(20.0f, result.getPrice());
        assertEquals(10.0f, result.getDiscountedPrice());
        assertEquals(0, result.getTotalReviews());
        assertEquals(0.0, result.getAverageRating());
    }

    @Test
    void calculateDiscountedPrice_WithDiscount() {
        float result = orderService.calculateDiscountedPrice(100.0f, 20);
        assertEquals(80.0f, result);
    }

    @Test
    void calculateDiscountedPrice_NoDiscount() {
        float result = orderService.calculateDiscountedPrice(100.0f, 0);
        assertEquals(100.0f, result);
    }

    @Test
    void mapToCategoryDTO_Success() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category 1");
        category.setImageHash("imageHash");
        category.setHighlights(true);

        CategoryDTO result = orderService.mapToCategoryDTO(category);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Category 1", result.getName());
        assertEquals("imageHash", result.getImageHash());
        assertTrue(result.isHighlights());
    }

    @Test
    void mapToCategoryDTO_NullCategory() {
        CategoryDTO result = orderService.mapToCategoryDTO(null);
        assertNull(result);
    }
}
