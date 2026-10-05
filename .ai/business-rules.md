# Business Rules — Class Hub

> Fonte: refinamento aprovado em 2026-10-04. Este arquivo é autocontido: qualquer agente de implementação deve conseguir desenvolver a lógica de domínio a partir daqui, sem acesso à conversa original.

## 1. Visão geral do domínio

Class Hub é uma plataforma web de gestão de projetos acadêmicos. Professores criam turmas e tarefas (individuais ou em grupo) e avaliam entregas. Alunos entram em turmas, consultam tarefas, enviam entregas e acompanham notas e feedbacks. A plataforma cobre: autenticação, turmas, tarefas, entregas, avaliação/feedback, notificações e dashboards.

## 2. Papéis (roles)

| Papel | Descrição |
|---|---|
| `aluno` | Usuário matriculado em turmas, realiza entregas. |
| `professor` | Cria turmas e tarefas, avalia entregas, registra feedback. |
| `admin` | Gerencia usuários, acessos, turmas (matrícula de alunos) e relatórios institucionais. |

Cada usuário possui uma **matrícula** única (chave de login) e uma **senha**. Uma matrícula pertence a exatamente um usuário — não pode haver duas contas com a mesma matrícula.

## 3. Entidades de domínio

- **Usuário**: matrícula, senha (hash), nome, role (`aluno` | `professor` | `admin`).
- **Turma**: agrupamento de alunos que compartilham tarefas vinculadas. Possui professor(es) responsável(is) e lista de alunos matriculados.
- **Tarefa**: unidade de trabalho acadêmico criada pelo professor e vinculada a uma turma. Pode ser individual ou em grupo. Possui data limite (prazo).
  > Decisão de modelagem: os termos "projeto acadêmico" e "tarefa" citados no escopo original representam o **mesmo conceito de domínio** e foram unificados nesta única entidade. Não existe entidade "Projeto" separada.
- **Entrega**: submissão de um aluno (ou grupo de alunos) para uma Tarefa. Contém conteúdo, anexos, comentários, lista de participantes (quando em grupo), data de criação e data de atualização.
  > Decisão de modelagem: não existe entidade persistente "Equipe". Em tarefas de grupo, os participantes são apenas uma lista de matrículas vinculada diretamente à Entrega, registrada no momento do envio.
- **Feedback**: avaliação de uma Entrega pelo professor — nota e comentário/parecer.
- **Notificação**: mensagem enviada a um usuário sobre um evento relevante (entrega avaliada, prazo próximo).

## 4. Regras de negócio

### Autenticação e contas
- Login é feito com matrícula + senha.
- O cadastro público (`auth/register`) **sempre cria o usuário com role `aluno`**, independentemente do que for enviado no corpo da requisição — isso evita escalonamento de privilégio.
- Contas com role `professor` ou `admin` só podem ser criadas por um **administrador autenticado**, via endpoint restrito.
- Tentativa de cadastro com matrícula já existente deve ser rejeitada.

### Turmas
- Turma é criada pelo professor.
- A matrícula de um aluno em uma turma (vínculo aluno-turma) é realizada pelo **Administrador**.
- Tarefas vinculadas a uma turma só são visíveis e acessíveis para os alunos matriculados nela.

### Tarefas
- Criada pelo professor, vinculada a uma turma, com data limite definida.
- Pode ser do tipo individual ou em grupo.

### Entregas
- Um aluno pode enviar uma entrega para uma tarefa aberta (antes da data limite).
- A entrega pode conter anexos e comentários do aluno.
- Enquanto a tarefa estiver aberta (antes do prazo), o aluno pode atualizar sua entrega.
- **Regra de overwriting**: a atualização substitui o conteúdo anterior da entrega. A `data_criacao` original (primeira submissão) é preservada; a `data_atualizacao` reflete o último reenvio. Não há versionamento/histórico de conteúdos anteriores.
- Após a data limite da tarefa, qualquer tentativa de reenvio deve ser bloqueada, com mensagem de prazo encerrado.
- Em tarefas de grupo, o aluno autor da entrega pode incluir outros alunos como participantes, informando as matrículas de cada um. As matrículas informadas devem ser válidas (existir no sistema) e os alunos vinculados passam a ser coautores visíveis da entrega.

### Avaliação e feedback
- O professor visualiza o conteúdo, anexos, comentários e participantes de uma entrega.
- O professor registra nota e feedback para a entrega.
- O professor pode aprovar ou reprovar a entrega (status geral da entrega como resultado da avaliação — não existem sub-etapas/milestones dentro de uma tarefa).
- Após a avaliação, o aluno deve conseguir visualizar a nota e o feedback recebido.

### Notificações
- Eventos que geram notificação: entrega avaliada; prazo de tarefa próximo do vencimento.
- Canal de entrega: **in-app** (consultável na plataforma) **e e-mail**.
- A notificação deve chegar ao destinatário correto (o aluno dono/participante da entrega).

### Dashboards
- **Aluno**: turma, próximas atividades, atividades entregues, atividades atrasadas, notas recebidas.
- **Professor**: turmas, quantidade de alunos por turma, atividades abertas, atividades encerradas, entregas pendentes de avaliação.
- **Admin**: relatórios institucionais — contagem de usuários, contagem de turmas, contagem de entregas.

## 5. Permissões por papel

**Aluno**
- Realizar entregas
- Anexar arquivos à entrega
- Comentar na entrega
- Atualizar a própria entrega antes do prazo
- Incluir participantes (matrículas) em tarefas de grupo
- Visualizar seu próprio dashboard, notas e feedbacks

**Professor**
- Criar/editar/excluir turmas e tarefas
- Visualizar entregas da sua turma/tarefa
- Avaliar entregas (nota, feedback, aprovação/reprovação)
- Acompanhar evolução via dashboard próprio

**Administrador**
- Gerenciar usuários (criar professor/admin, editar acessos)
- Matricular/desmatricular alunos em turmas
- Gerenciar turmas/cursos
- Consultar tarefas/entregas
- Gerar relatórios institucionais via dashboard próprio

## 6. Critérios de aceite

- Dado que o usuário possui matrícula e senha válidas, quando ele acessa a plataforma, então deve conseguir entrar no sistema e visualizar seu painel (dashboard do seu papel).
- Dado que um usuário se cadastra via `auth/register`, quando o cadastro é concluído, então a conta deve ser criada com role `aluno`, independentemente do valor enviado no payload.
- Dado que um administrador autenticado cria um usuário via endpoint restrito, quando informa o role `professor` ou `admin`, então o novo usuário deve ser criado com o role solicitado.
- Dado que uma matrícula já está vinculada a um usuário, quando outra pessoa tentar se cadastrar com a mesma matrícula, então o cadastro deve ser rejeitado.
- Dado que um administrador matricula um aluno em uma turma, quando o vínculo é salvo, então o aluno deve passar a visualizar as tarefas daquela turma.
- Dado que o aluno está vinculado a uma tarefa aberta, quando ele envia uma entrega com anexo e comentário, então a entrega deve ser registrada com sucesso.
- Dado que a tarefa ainda está em aberto, quando o aluno altera a entrega, então o conteúdo deve ser substituído e a `data_atualizacao` deve ser registrada, mantendo a `data_criacao` original.
- Dado que a tarefa passou da data limite, quando o aluno tentar reenviar a entrega, então o sistema deve bloquear a ação e exibir mensagem de prazo encerrado.
- Dado que a tarefa é de grupo, quando o aluno incluir participantes com matrícula válida, então os alunos vinculados devem ser associados à entrega e visíveis para o professor.
- Dado que a entrega foi submetida, quando o professor acessa a tarefa, então ele deve visualizar o conteúdo, anexos, comentários e participantes.
- Dado que a entrega foi analisada, quando o professor registra nota e feedback, então o aluno deve conseguir visualizar a avaliação recebida.
- Dado que uma entrega é avaliada ou um prazo está próximo do vencimento, quando o evento ocorre, então uma notificação deve ser enviada ao destinatário correto, tanto in-app quanto por e-mail.
- Dado que uma tarefa é vinculada a uma turma, quando um aluno não matriculado nessa turma tenta acessá-la, então o acesso deve ser negado.
- Dado que o aluno acessa seu dashboard, então deve visualizar sua turma, próximas atividades, atividades entregues, atividades atrasadas e notas recebidas.
- Dado que o professor acessa seu dashboard, então deve visualizar suas turmas, quantidade de alunos, atividades abertas/encerradas e entregas pendentes de avaliação.
- Dado que o administrador acessa seu dashboard, então deve visualizar contagem de usuários, turmas e entregas.

## 7. Pendências não bloqueantes

- Tipos de arquivo e tamanho máximo permitido para anexos de entrega: não definidos.
- Confirmação/marcação de leitura de notificação in-app: não definida.
- Provedor técnico de envio de e-mail: é decisão de implementação, fora do escopo de negócio deste documento (ver `tech-stack.md`).
