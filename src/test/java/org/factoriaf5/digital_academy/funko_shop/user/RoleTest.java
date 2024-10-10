package org.factoriaf5.digital_academy.funko_shop.user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleTest {

    @Test
    public void testAdminGetAuthorities() {
        Set<Permission> adminPermissions = Set.of(
                Permission.ADMIN_READ,
                Permission.ADMIN_UPDATE,
                Permission.ADMIN_DELETE,
                Permission.ADMIN_CREATE,
                Permission.MANAGER_READ,
                Permission.MANAGER_UPDATE,
                Permission.MANAGER_DELETE,
                Permission.MANAGER_CREATE
        );

        List<SimpleGrantedAuthority> expectedAuthorities = adminPermissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        expectedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        List<SimpleGrantedAuthority> actualAuthorities = Role.ADMIN.getAuthorities();

        assertEquals(expectedAuthorities, actualAuthorities);
    }

    @Test
    public void testUserGetAuthorities() {
        List<SimpleGrantedAuthority> expectedAuthorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        List<SimpleGrantedAuthority> actualAuthorities = Role.USER.getAuthorities();

        assertEquals(expectedAuthorities, actualAuthorities);
    }

    @Test
    public void testManagerGetAuthorities() {
        Set<Permission> managerPermissions = Set.of(
                Permission.MANAGER_READ,
                Permission.MANAGER_UPDATE,
                Permission.MANAGER_DELETE,
                Permission.MANAGER_CREATE
        );

        List<SimpleGrantedAuthority> expectedAuthorities = managerPermissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        expectedAuthorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));

        List<SimpleGrantedAuthority> actualAuthorities = Role.MANAGER.getAuthorities();

        assertEquals(expectedAuthorities, actualAuthorities);
    }
}