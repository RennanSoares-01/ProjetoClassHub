const API_BASE = '';

function parseJsonSeguro(valor) {
    if (valor === null || valor === undefined || valor === '') {
        return null;
    }

    try {
        return JSON.parse(valor);
    } catch (erro) {
        return null;
    }
}

function getToken() {
    return localStorage.getItem('ch_token');
}

function getUsuario() {
    const bruto = localStorage.getItem('ch_usuario');
    const usuario = parseJsonSeguro(bruto);

    if (!usuario) {
        localStorage.removeItem('ch_usuario');
        return null;
    }

    return usuario;
}

function setSessao(token, usuario) {
    localStorage.setItem('ch_token', token);
    localStorage.setItem('ch_usuario', JSON.stringify(usuario));
}

function limparSessao() {
    localStorage.removeItem('ch_token');
    localStorage.removeItem('ch_usuario');
}

async function apiFetch(path, options = {}) {
    const headers = Object.assign({ 'Content-Type': 'application/json' }, options.headers || {});
    const token = getToken();
    if (token) {
        headers['Authorization'] = 'Bearer ' + token;
    }

    const response = await fetch(API_BASE + path, Object.assign({}, options, { headers }));

    if (response.status === 204) {
        return null;
    }

    const texto = await response.text();
    const data = texto ? parseJsonSeguro(texto) : null;

    if (!response.ok) {
        const mensagem = (data && data.message) ? data.message : ('Erro ' + response.status);
        throw new Error(mensagem);
    }

    if (texto && data === null) {
        throw new Error('Resposta inválida do servidor.');
    }

    return data;
}
