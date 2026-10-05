const API_BASE = '';

function getToken() {
    return localStorage.getItem('ch_token');
}

function getUsuario() {
    const bruto = localStorage.getItem('ch_usuario');
    return bruto ? JSON.parse(bruto) : null;
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
    const data = texto ? JSON.parse(texto) : null;
    if (!response.ok) {
        const mensagem = (data && data.message) ? data.message : ('Erro ' + response.status);
        throw new Error(mensagem);
    }
    return data;
}
