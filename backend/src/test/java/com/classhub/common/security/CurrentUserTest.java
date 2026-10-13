package com.classhub.common.security;

import com.classhub.common.exception.ForbiddenException;
import com.classhub.common.exception.UnauthorizedException;
import com.classhub.usuario.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CurrentUserTest {

    private CurrentUser currentUser;

    @BeforeEach
    void setUp() {
        currentUser = new CurrentUser();
    }

    @Test
    void getWithoutAuthenticatedUserThrowsUnauthorized() {
        assertThrows(UnauthorizedException.class, currentUser::get);
    }

    @Test
    void exigirRoleAllowsOnlyPermittedRoles() {
        currentUser.set(new UsuarioAutenticado(1L, "aluno001", "Aluno", Role.ALUNO));

        assertDoesNotThrow(() -> currentUser.exigirRole(Role.ALUNO));
        assertThrows(ForbiddenException.class, () -> currentUser.exigirRole(Role.ADMIN));
    }

    @Test
    void clearRemovesAuthenticatedUser() {
        currentUser.set(new UsuarioAutenticado(1L, "aluno001", "Aluno", Role.ALUNO));

        currentUser.clear();

        assertThrows(UnauthorizedException.class, currentUser::get);
    }
}