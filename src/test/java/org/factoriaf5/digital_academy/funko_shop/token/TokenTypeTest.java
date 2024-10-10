package org.factoriaf5.digital_academy.funko_shop.token;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TokenTypeTest {

    @Test
    public void testTokenTypeBearerExists() {
        assertEquals(TokenType.BEARER, TokenType.valueOf("BEARER"));
    }
}