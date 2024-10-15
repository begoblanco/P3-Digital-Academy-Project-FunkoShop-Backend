package org.factoriaf5.digital_academy.funko_shop.account_settings;

import org.factoriaf5.digital_academy.funko_shop.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountSettingsDTOTest {

    @Test
    void testAccountSettingsDTOConstructorAndGetters() {
        Long id = 1L;
        String language = "English";
        UserDTO user = new UserDTO(); 

        AccountSettingsDTO accountSettingsDTO = new AccountSettingsDTO(id, language, user);

        assertEquals(id, accountSettingsDTO.getId());
        assertEquals(language, accountSettingsDTO.getLanguage());
        assertEquals(user, accountSettingsDTO.getUser());
    }

    @Test
    void testAccountSettingsDTOSetters() {
        AccountSettingsDTO accountSettingsDTO = new AccountSettingsDTO();
        UserDTO user = new UserDTO();

        accountSettingsDTO.setId(2L);
        accountSettingsDTO.setLanguage("Spanish");
        accountSettingsDTO.setUser(user);

        assertEquals(2L, accountSettingsDTO.getId());
        assertEquals("Spanish", accountSettingsDTO.getLanguage());
        assertEquals(user, accountSettingsDTO.getUser());
    }

    @Test
    void testEmptyAccountSettingsDTO() {
        AccountSettingsDTO accountSettingsDTO = new AccountSettingsDTO();

        assertNull(accountSettingsDTO.getId());
        assertNull(accountSettingsDTO.getLanguage());
        assertNull(accountSettingsDTO.getUser());
    }
}
