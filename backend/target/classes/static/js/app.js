const usuario = getUsuario();
if (!usuario || !getToken()) {
    window.location.href = 'index.html';
}

document.getElementById('usuario-nome').textContent = usuario.nome + ' (' + usuario.matricula + ')';
document.getElementById('usuario-role').textContent = usuario.role;

document.getElementById('btn-sair').addEventListener('click', () => {
    limparSessao();
    window.location.href = 'index.html';
});

// --- Navegação entre seções ---
const nav = document.getElementById('app-nav');
const secoes = document.querySelectorAll('.secao');
const carregadores = {
    dashboard: carregarDashboard,
    turmas: carregarTurmas,
    tarefas: carregarTarefas,
    notificacoes: carregarNotificacoes,
    'admin-usuarios': carregarUsuarios
};

if (usuario.role !== 'ADMIN') {
    document.getElementById('nav-admin-usuarios').remove();
}

nav.addEventListener('click', (evento) => {
    const botao = evento.target.closest('button[data-secao]');
    if (!botao) return;
    mostrarSecao(botao.dataset.secao);
});

function mostrarSecao(nome) {
    secoes.forEach((s) => s.classList.toggle('visivel', s.id === 'sec-' + nome));
    nav.querySelectorAll('button').forEach((b) => b.classList.toggle('ativo', b.dataset.secao === nome));
    const carregar = carregadores[nome];
    if (carregar) carregar();
}

mostrarSecao('dashboard');

// --- Dashboard ---
async function carregarDashboard() {
    const container = document.getElementById('dashboard-conteudo');
    container.innerHTML = '<p>Carregando...</p>';
    try {
        const dados = await apiFetch('/dashboard/' + usuario.role.toLowerCase());
        container.innerHTML = renderDashboard(usuario.role, dados);
    } catch (erro) {
        container.innerHTML = '<p class="msg erro">' + erro.message + '</p>';
    }
}

function renderDashboard(role, dados) {
    if (role === 'ALUNO') {
        return [
            cartaoLista('Minhas turmas', dados.turmas.map((t) => t.nome)),
            cartaoLista('Próximas atividades', dados.proximasAtividades.map((t) => t.titulo + ' — ' + t.turmaNome + ' (até ' + formatarData(t.dataLimite) + ')')),
            cartaoLista('Atividades entregues', dados.atividadesEntregues.map((t) => t.titulo)),
            cartaoLista('Atividades atrasadas', dados.atividadesAtrasadas.map((t) => t.titulo)),
            cartaoLista('Notas recebidas', dados.notasRecebidas.map((n) => n.tarefaTitulo + ': ' + (n.nota ?? '-') + ' (' + n.status + ')'))
        ].join('');
    }
    if (role === 'PROFESSOR') {
        return [
            cartaoLista('Minhas turmas', dados.turmas.map((t) => t.nome + ' — ' + t.quantidadeAlunos + ' aluno(s)')),
            cartaoNumero('Atividades abertas', dados.atividadesAbertas),
            cartaoNumero('Atividades encerradas', dados.atividadesEncerradas),
            cartaoNumero('Entregas pendentes de avaliação', dados.entregasPendentesAvaliacao)
        ].join('');
    }
    return [
        cartaoNumero('Usuários cadastrados', dados.totalUsuarios),
        cartaoNumero('Turmas', dados.totalTurmas),
        cartaoNumero('Entregas', dados.totalEntregas)
    ].join('');
}

function cartaoLista(titulo, itens) {
    const lista = itens.length ? itens.map((i) => '<li>' + escaparHtml(i) + '</li>').join('') : '<li>Nenhum registro.</li>';
    return '<div class="cartao"><h3>' + titulo + '</h3><ul>' + lista + '</ul></div>';
}

function cartaoNumero(titulo, numero) {
    return '<div class="cartao"><h3>' + titulo + '</h3><p style="font-size:2rem;margin:0">' + numero + '</p></div>';
}

// --- Turmas ---
let turmasCache = [];

async function carregarTurmas() {
    const lista = document.getElementById('lista-turmas');
    document.getElementById('form-nova-turma-wrapper').style.display = usuario.role === 'PROFESSOR' ? 'block' : 'none';
    lista.innerHTML = '<p>Carregando...</p>';
    try {
        turmasCache = await apiFetch('/turmas');
        lista.innerHTML = turmasCache.map(renderTurma).join('') || '<p>Nenhuma turma encontrada.</p>';
    } catch (erro) {
        lista.innerHTML = '<p class="msg erro">' + erro.message + '</p>';
    }
}

function renderTurma(turma) {
    const alunos = turma.alunos.map((a) => a.nome + ' (' + a.matricula + ')').join(', ') || 'Nenhum aluno matriculado.';
    let acoesAdmin = '';
    if (usuario.role === 'ADMIN') {
        acoesAdmin = '<div class="formulario" style="flex-direction:row;gap:6px;margin-top:8px">'
            + '<input type="text" placeholder="matrícula do aluno" id="matricula-turma-' + turma.id + '" />'
            + '<button type="button" onclick="matricularAluno(' + turma.id + ')">Matricular</button>'
            + '</div>';
    }
    return '<div class="cartao">'
        + '<h3>' + escaparHtml(turma.nome) + '</h3>'
        + '<p>Professor: ' + escaparHtml(turma.professor.nome) + '</p>'
        + '<p>Alunos: ' + escaparHtml(alunos) + '</p>'
        + acoesAdmin
        + '</div>';
}

document.getElementById('form-nova-turma').addEventListener('submit', async (evento) => {
    evento.preventDefault();
    const nome = document.getElementById('turma-nome').value.trim();
    try {
        await apiFetch('/turmas', { method: 'POST', body: JSON.stringify({ nome }) });
        document.getElementById('turma-nome').value = '';
        carregarTurmas();
    } catch (erro) {
        alert(erro.message);
    }
});

async function matricularAluno(turmaId) {
    const input = document.getElementById('matricula-turma-' + turmaId);
    const matricula = input.value.trim();
    if (!matricula) return;
    try {
        await apiFetch('/turmas/' + turmaId + '/alunos/' + encodeURIComponent(matricula), { method: 'POST' });
        carregarTurmas();
    } catch (erro) {
        alert(erro.message);
    }
}

// --- Tarefas ---
async function carregarTarefas() {
    document.getElementById('form-nova-tarefa-wrapper').style.display = usuario.role === 'PROFESSOR' ? 'block' : 'none';
    if (usuario.role === 'PROFESSOR') {
        await popularSelectTurmas();
    }
    const lista = document.getElementById('lista-tarefas');
    lista.innerHTML = '<p>Carregando...</p>';
    try {
        const tarefas = await apiFetch('/tarefas');
        lista.innerHTML = tarefas.map(renderTarefa).join('') || '<p>Nenhuma tarefa encontrada.</p>';
    } catch (erro) {
        lista.innerHTML = '<p class="msg erro">' + erro.message + '</p>';
    }
}

async function popularSelectTurmas() {
    const select = document.getElementById('tarefa-turma');
    try {
        const turmas = await apiFetch('/turmas');
        select.innerHTML = turmas.map((t) => '<option value="' + t.id + '">' + escaparHtml(t.nome) + '</option>').join('');
    } catch (erro) {
        select.innerHTML = '';
    }
}

function renderTarefa(tarefa) {
    const status = tarefa.aberta ? '<span class="tag">aberta</span>' : '<span class="tag REPROVADA">encerrada</span>';
    let acoes = '';
    if (usuario.role === 'ALUNO') {
        acoes = '<button type="button" onclick="abrirFormularioEntrega(' + tarefa.id + ', \'' + tarefa.tipo + '\')">Enviar / atualizar entrega</button>'
            + '<div id="form-entrega-' + tarefa.id + '"></div>';
    } else {
        acoes = '<button type="button" onclick="verEntregas(' + tarefa.id + ')">Ver entregas</button>'
            + '<div id="entregas-' + tarefa.id + '"></div>';
    }
    return '<div class="cartao">'
        + '<h3>' + escaparHtml(tarefa.titulo) + ' ' + status + '</h3>'
        + '<p>' + escaparHtml(tarefa.turmaNome) + ' — tipo: ' + tarefa.tipo + ' — prazo: ' + formatarData(tarefa.dataLimite) + '</p>'
        + '<p>' + escaparHtml(tarefa.descricao || '') + '</p>'
        + acoes
        + '</div>';
}

document.getElementById('form-nova-tarefa').addEventListener('submit', async (evento) => {
    evento.preventDefault();
    const payload = {
        turmaId: Number(document.getElementById('tarefa-turma').value),
        titulo: document.getElementById('tarefa-titulo').value.trim(),
        descricao: document.getElementById('tarefa-descricao').value.trim(),
        tipo: document.getElementById('tarefa-tipo').value,
        dataLimite: document.getElementById('tarefa-data-limite').value
    };
    try {
        await apiFetch('/tarefas', { method: 'POST', body: JSON.stringify(payload) });
        evento.target.reset();
        carregarTarefas();
    } catch (erro) {
        alert(erro.message);
    }
});

function abrirFormularioEntrega(tarefaId, tipo) {
    const container = document.getElementById('form-entrega-' + tarefaId);
    if (container.dataset.aberto === 'true') {
        container.innerHTML = '';
        container.dataset.aberto = 'false';
        return;
    }
    container.dataset.aberto = 'true';
    container.innerHTML = '<form class="formulario" onsubmit="return enviarEntrega(event, ' + tarefaId + ')">'
        + '<label>Conteúdo <textarea id="entrega-conteudo-' + tarefaId + '" required></textarea></label>'
        + '<label>Comentário <input type="text" id="entrega-comentario-' + tarefaId + '" /></label>'
        + '<label>Anexo — nome do arquivo <input type="text" id="entrega-anexo-nome-' + tarefaId + '" /></label>'
        + '<label>Anexo — URL <input type="text" id="entrega-anexo-url-' + tarefaId + '" /></label>'
        + (tipo === 'GRUPO' ? '<label>Participantes (matrículas separadas por vírgula) <input type="text" id="entrega-participantes-' + tarefaId + '" /></label>' : '')
        + '<p id="entrega-msg-' + tarefaId + '" class="msg"></p>'
        + '<button type="submit">Enviar entrega</button>'
        + '</form>';
}

async function enviarEntrega(evento, tarefaId) {
    evento.preventDefault();
    const conteudo = document.getElementById('entrega-conteudo-' + tarefaId).value.trim();
    const comentario = document.getElementById('entrega-comentario-' + tarefaId).value.trim();
    const nomeArquivo = document.getElementById('entrega-anexo-nome-' + tarefaId).value.trim();
    const url = document.getElementById('entrega-anexo-url-' + tarefaId).value.trim();
    const participantesEl = document.getElementById('entrega-participantes-' + tarefaId);
    const participantesMatriculas = participantesEl
        ? participantesEl.value.split(',').map((m) => m.trim()).filter(Boolean)
        : [];
    const anexos = nomeArquivo && url ? [{ nomeArquivo, url }] : [];
    const msgEl = document.getElementById('entrega-msg-' + tarefaId);
    try {
        await apiFetch('/entregas', {
            method: 'POST',
            body: JSON.stringify({ tarefaId, conteudo, comentario, anexos, participantesMatriculas })
        });
        msgEl.className = 'msg sucesso';
        msgEl.textContent = 'Entrega enviada/atualizada com sucesso!';
    } catch (erro) {
        msgEl.className = 'msg erro';
        msgEl.textContent = erro.message;
    }
    return false;
}

async function verEntregas(tarefaId) {
    const container = document.getElementById('entregas-' + tarefaId);
    if (container.dataset.aberto === 'true') {
        container.innerHTML = '';
        container.dataset.aberto = 'false';
        return;
    }
    container.dataset.aberto = 'true';
    container.innerHTML = '<p>Carregando...</p>';
    try {
        const entregas = await apiFetch('/tarefas/' + tarefaId + '/entregas');
        container.innerHTML = entregas.map(renderEntregaProfessor).join('') || '<p>Nenhuma entrega recebida ainda.</p>';
    } catch (erro) {
        container.innerHTML = '<p class="msg erro">' + erro.message + '</p>';
    }
}

function renderEntregaProfessor(entrega) {
    const participantes = entrega.participantes.map((p) => p.nome).join(', ');
    const anexos = entrega.anexos.map((a) => '<a href="' + a.url + '" target="_blank">' + escaparHtml(a.nomeArquivo) + '</a>').join(', ');
    return '<div class="cartao">'
        + '<p><strong>' + escaparHtml(entrega.autor.nome) + '</strong> (' + entrega.autor.matricula + ') '
        + '<span class="tag ' + entrega.status + '">' + entrega.status + '</span></p>'
        + (participantes ? '<p>Participantes: ' + escaparHtml(participantes) + '</p>' : '')
        + '<p>' + escaparHtml(entrega.conteudo) + '</p>'
        + (entrega.comentario ? '<p><em>Comentário: ' + escaparHtml(entrega.comentario) + '</em></p>' : '')
        + (anexos ? '<p>Anexos: ' + anexos + '</p>' : '')
        + '<form class="formulario" style="flex-direction:row;gap:6px" onsubmit="return avaliarEntrega(event, ' + entrega.id + ')">'
        + '<input type="number" min="0" max="10" step="0.1" placeholder="nota" id="nota-' + entrega.id + '" required style="width:80px" />'
        + '<input type="text" placeholder="comentário" id="comentario-feedback-' + entrega.id + '" />'
        + '<select id="status-feedback-' + entrega.id + '"><option value="APROVADA">Aprovar</option><option value="REPROVADA">Reprovar</option></select>'
        + '<button type="submit">Avaliar</button>'
        + '</form>'
        + '</div>';
}

async function avaliarEntrega(evento, entregaId) {
    evento.preventDefault();
    const nota = Number(document.getElementById('nota-' + entregaId).value);
    const comentario = document.getElementById('comentario-feedback-' + entregaId).value.trim();
    const status = document.getElementById('status-feedback-' + entregaId).value;
    try {
        await apiFetch('/feedbacks', { method: 'POST', body: JSON.stringify({ entregaId, nota, comentario, status }) });
        alert('Avaliação registrada!');
    } catch (erro) {
        alert(erro.message);
    }
    return false;
}

// --- Notificações ---
async function carregarNotificacoes() {
    const lista = document.getElementById('lista-notificacoes');
    lista.innerHTML = '<p>Carregando...</p>';
    try {
        const notificacoes = await apiFetch('/notificacoes');
        lista.innerHTML = notificacoes.map((n) => '<div class="cartao"><span class="tag">' + n.tipo + '</span> '
            + '<p>' + escaparHtml(n.mensagem) + '</p>'
            + '<small>' + formatarData(n.criadoEm) + '</small></div>').join('') || '<p>Nenhuma notificação.</p>';
    } catch (erro) {
        lista.innerHTML = '<p class="msg erro">' + erro.message + '</p>';
    }
}

// --- Admin: usuários ---
async function carregarUsuarios() {
    const lista = document.getElementById('lista-usuarios');
    lista.innerHTML = '<p>Carregando...</p>';
    try {
        const usuarios = await apiFetch('/usuarios');
        lista.innerHTML = '<table><thead><tr><th>Matrícula</th><th>Nome</th><th>E-mail</th><th>Role</th></tr></thead><tbody>'
            + usuarios.map((u) => '<tr><td>' + u.matricula + '</td><td>' + escaparHtml(u.nome) + '</td><td>' + u.email + '</td><td>' + u.role + '</td></tr>').join('')
            + '</tbody></table>';
    } catch (erro) {
        lista.innerHTML = '<p class="msg erro">' + erro.message + '</p>';
    }
}

const formNovoUsuario = document.getElementById('form-novo-usuario');
if (formNovoUsuario) {
    formNovoUsuario.addEventListener('submit', async (evento) => {
        evento.preventDefault();
        const payload = {
            matricula: document.getElementById('usuario-matricula').value.trim(),
            nome: document.getElementById('usuario-nome-campo').value.trim(),
            email: document.getElementById('usuario-email').value.trim(),
            senha: document.getElementById('usuario-senha').value,
            role: document.getElementById('usuario-role-campo').value
        };
        const msgEl = document.getElementById('novo-usuario-msg');
        try {
            await apiFetch('/auth/usuarios', { method: 'POST', body: JSON.stringify(payload) });
            msgEl.className = 'msg sucesso';
            msgEl.textContent = 'Usuário criado com sucesso!';
            formNovoUsuario.reset();
            carregarUsuarios();
        } catch (erro) {
            msgEl.className = 'msg erro';
            msgEl.textContent = erro.message;
        }
    });
}

// --- Utilitários ---
function escaparHtml(texto) {
    const div = document.createElement('div');
    div.textContent = texto == null ? '' : String(texto);
    return div.innerHTML;
}

function formatarData(valor) {
    if (!valor) return '-';
    const data = new Date(valor);
    return isNaN(data.getTime()) ? valor : data.toLocaleString('pt-BR');
}
