package com.classhub.usuario;

import com.classhub.common.exception.ConflictException;
import com.classhub.common.exception.NotFoundException;
import com.classhub.common.exception.UnauthorizedException;
import com.classhub.common.security.CurrentUser;
import com.classhub.common.security.TokenStore;
import com.classhub.common.security.UsuarioAutenticado;
import com.classhub.usuario.dto.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper,
                           PasswordEncoder passwordEncoder, TokenStore tokenStore) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
    }

    @Transactional
    public UsuarioResponse registrarAluno(RegistroRequest request) {
        return usuarioMapper.toResponse(criar(request.matricula(), request.nome(), request.email(), request.senha(), Role.ALUNO));
    }

    @Transactional
    public UsuarioResponse criarUsuarioPrivilegiado(CriarUsuarioRequest request) {
        CurrentUser.exigirRole(Role.ADMIN);
        return usuarioMapper.toResponse(criar(request.matricula(), request.nome(), request.email(), request.senha(), request.role()));
    }

    private Usuario criar(String matricula, String nome, String email, String senha, Role role) {
        if (usuarioRepository.existsByMatricula(matricula)) {
            throw new ConflictException("Já existe um usuário cadastrado com esta matrícula.");
        }
        Usuario usuario = new Usuario(matricula, nome, email, passwordEncoder.encode(senha), role);
        return usuarioRepository.save(usuario);
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByMatricula(request.matricula())
                .orElseThrow(() -> new UnauthorizedException("Matrícula ou senha inválidas."));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new UnauthorizedException("Matrícula ou senha inválidas.");
        }

        UsuarioAutenticado autenticado = new UsuarioAutenticado(usuario.getId(), usuario.getMatricula(), usuario.getNome(), usuario.getRole());
        String token = tokenStore.criar(autenticado);
        return new LoginResponse(token, usuarioMapper.toResponse(usuario));
    }

    public List<UsuarioResponse> listar() {
        CurrentUser.exigirRole(Role.ADMIN);
        return usuarioRepository.findAll().stream().map(usuarioMapper::toResponse).toList();
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, AtualizarUsuarioRequest request) {
        CurrentUser.exigirRole(Role.ADMIN);
        Usuario usuario = buscarEntidade(id);
        if (request.nome() != null && !request.nome().isBlank()) {
            usuario.setNome(request.nome());
        }
        if (request.role() != null) {
            usuario.setRole(request.role());
        }
        return usuarioMapper.toResponse(usuario);
    }

    public Usuario buscarEntidade(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }

    public Usuario buscarPorMatricula(String matricula) {
        return usuarioRepository.findByMatricula(matricula)
                .orElseThrow(() -> new NotFoundException("Usuário com matrícula '" + matricula + "' não encontrado."));
    }
}
