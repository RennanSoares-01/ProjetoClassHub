package com.classhub.common.security;

import java.time.Instant;

record SessaoToken(String token, UsuarioAutenticado usuario, Instant criadoEm) {
}
