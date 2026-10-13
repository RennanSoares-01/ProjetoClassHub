package com.classhub.common.security;

import com.classhub.common.exception.UnauthorizedException;
import com.classhub.usuario.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticationFilterTest {

    private CurrentUser currentUser;
    private AuthenticationFilter filter;
    private UsuarioAutenticado authenticatedUser;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        currentUser = new CurrentUser();
        TokenStore tokenStore = new TokenStore();
        authenticatedUser = new UsuarioAutenticado(1L, "aluno001", "Aluno", Role.ALUNO);
        String token = tokenStore.criar(authenticatedUser);
        filter = new AuthenticationFilter(tokenStore, new ObjectMapper(), currentUser);
        request = new MockHttpServletRequest("GET", "/tarefas");
        request.addHeader("Authorization", "Bearer " + token);
        response = new MockHttpServletResponse();
    }

    @Test
    void setsUserDuringRequestAndClearsItAfterward() throws Exception {
        filter.doFilter(request, response, (servletRequest, servletResponse) ->
                assertEquals(authenticatedUser, currentUser.get()));

        assertThrows(UnauthorizedException.class, currentUser::get);
    }

    @Test
    void clearsUserWhenRequestProcessingThrows() {
        assertThrows(ServletException.class, () -> filter.doFilter(request, response,
                (servletRequest, servletResponse) -> { throw new ServletException("failure"); }));

        assertThrows(UnauthorizedException.class, currentUser::get);
    }
}