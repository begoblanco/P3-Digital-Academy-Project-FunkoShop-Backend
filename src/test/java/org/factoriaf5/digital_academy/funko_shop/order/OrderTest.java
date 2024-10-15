package org.factoriaf5.digital_academy.funko_shop.order;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
    }

    @Test
    void testOnCreate() {
        order.onCreate();
        
        assertNotNull(order.getCreatedAt());
        
        Date now = new Date();
        long difference = Math.abs(now.getTime() - order.getCreatedAt().getTime());
        assertTrue(difference < 1000);
    }
}

