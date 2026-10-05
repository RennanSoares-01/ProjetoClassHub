const formLogin = document.getElementById('form-login');
const formRegistro = document.getElementById('form-registro');

if (formLogin) {
    formLogin.addEventListener('submit', async (evento) => {
        evento.preventDefault();
        const matricula = document.getElementById('login-matricula').value.trim();
        const senha = document.getElementById('login-senha').value;
        const erroEl = document.getElementById('login-erro');
        erroEl.textContent = '';
        try {
            const dados = await apiFetch('/auth/login', {
                method: 'POST',
                body: JSON.stringify({ matricula, senha })
            });
            setSessao(dados.token, dados.usuario);
            window.location.href = 'app.html';
        } catch (erro) {
            erroEl.textContent = erro.message;
        }
    });
}

if (formRegistro) {
    formRegistro.addEventListener('submit', async (evento) => {
        evento.preventDefault();
        const matricula = document.getElementById('reg-matricula').value.trim();
        const nome = document.getElementById('reg-nome').value.trim();
        const email = document.getElementById('reg-email').value.trim();
        const senha = document.getElementById('reg-senha').value;
        const msgEl = document.getElementById('registro-msg');
        msgEl.textContent = '';
        msgEl.className = 'msg';
        try {
            await apiFetch('/auth/register', {
                method: 'POST',
                body: JSON.stringify({ matricula, nome, email, senha })
            });
            msgEl.textContent = 'Cadastro realizado com sucesso! Faça login acima.';
            msgEl.className = 'msg sucesso';
            formRegistro.reset();
        } catch (erro) {
            msgEl.textContent = erro.message;
            msgEl.className = 'msg erro';
        }
    });
}
