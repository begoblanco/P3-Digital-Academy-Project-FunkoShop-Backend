package org.factoriaf5.digital_academy.funko_shop.order;

import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItemDTO;
import org.factoriaf5.digital_academy.funko_shop.tracking.TrackingDTO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OrderDTOTest {

    @Test
    void testOrderDTOConstructorAndGetters() {
        Long id = 1L;
        String status = "Pending";
        float totalPrice = 100.5f;
        int totalItems = 5;
        boolean isPaid = false;

        OrderItemDTO item1 = new OrderItemDTO();
        OrderItemDTO item2 = new OrderItemDTO();
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(item1);
        orderItems.add(item2);

        TrackingDTO tracking = new TrackingDTO();

        OrderDTO orderDTO = new OrderDTO(id, status, totalPrice, totalItems, isPaid, orderItems, tracking);

        assertEquals(id, orderDTO.getId());
        assertEquals(status, orderDTO.getStatus());
        assertEquals(totalPrice, orderDTO.getTotalPrice());
        assertEquals(totalItems, orderDTO.getTotalItems());
        assertEquals(isPaid, orderDTO.isPaid());
        assertEquals(orderItems, orderDTO.getOrderItems());
        assertEquals(tracking, orderDTO.getTracking());
    }

    @Test
    void testOrderDTOSetters() {
        OrderDTO orderDTO = new OrderDTO();

        OrderItemDTO item1 = new OrderItemDTO();
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(item1);

        TrackingDTO tracking = new TrackingDTO();

        orderDTO.setId(2L);
        orderDTO.setStatus("Shipped");
        orderDTO.setTotalPrice(200.0f);
        orderDTO.setTotalItems(10);
        orderDTO.setPaid(true);
        orderDTO.setOrderItems(orderItems);
        orderDTO.setTracking(tracking);

        assertEquals(2L, orderDTO.getId());
        assertEquals("Shipped", orderDTO.getStatus());
        assertEquals(200.0f, orderDTO.getTotalPrice());
        assertEquals(10, orderDTO.getTotalItems());
        assertTrue(orderDTO.isPaid());
        assertEquals(orderItems, orderDTO.getOrderItems());
        assertEquals(tracking, orderDTO.getTracking());
    }

    @Test
    void testEmptyOrderDTO() {
        OrderDTO orderDTO = new OrderDTO();

        assertNull(orderDTO.getId());
        assertNull(orderDTO.getStatus());
        assertEquals(0.0f, orderDTO.getTotalPrice());
        assertEquals(0, orderDTO.getTotalItems());
        assertFalse(orderDTO.isPaid());
        assertNull(orderDTO.getOrderItems());
        assertNull(orderDTO.getTracking());
    }
}
