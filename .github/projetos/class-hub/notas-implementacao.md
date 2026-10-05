# Notas de implementação — Class Hub MVP

**Versão**: v1-2026-10-04
**Tipo**: implementação nova (primeira versão do MVP)
**Base utilizada**: `.ai/business-rules.md`, `.ai/architecture.md`, `.ai/tech-stack.md`, `.ai/standards.md` (todos na raiz do repositório).

## O que foi implementado

MVP completo, ponta a ponta, com dados mockados (seed automático no startup):

- **Autenticação**: login por matrícula/senha, registro público sempre como `ALUNO`, criação de `PROFESSOR`/`ADMIN` restrita a um admin autenticado. Token opaco em memória (sem Spring Security/JWT), conforme ADR-008.
- **Usuários**: listagem e atualização (admin).
- **Turmas**: CRUD (professor cria/edita/exclui a própria turma; admin tudo), matrícula/remoção de aluno restrita ao admin.
- **Tarefas**: CRUD vinculado a uma turma, tipos `INDIVIDUAL`/`GRUPO`, visibilidade por papel.
- **Entregas**: envio e atualização com overwrite (mantém `dataCriacao`, atualiza `dataAtualizacao`), anexos, comentário, participantes de grupo (validados contra a turma), bloqueio após o prazo.
- **Feedback/Avaliação**: nota + comentário + status (`APROVADA`/`REPROVADA`), dispara notificação.
- **Notificações**: in-app (consulta via API) + e-mail simulado (log). Evento de prazo próximo gerado por um scheduler (`PrazoProximoScheduler`, a cada 10 min, janela de 48h).
- **Dashboards**: aluno, professor e admin, com o conteúdo exato aprovado na conversa de refinamento.
- **Frontend**: HTML/CSS/JS vanilla, uma página de login/cadastro e uma página autenticada com navegação por seção, servido pelo próprio backend.

## Decisões técnicas assumidas durante a implementação

- **Campo `email` em Usuário**: não estava no escopo de negócio original, mas é necessário para o canal de notificação por e-mail já aprovado. Adicionado como campo obrigatório de cadastro.
- **Anexos e participantes como sub-recursos relacionais** (`entrega_anexos`, `entrega_participantes`, `turma_alunos`) em vez de colunas serializadas — decisão técnica de modelagem, não altera a regra de negócio.
- **Seed de dados via `ApplicationRunner`** (não via script SQL `V2__seed.sql`): evita depender de um hash BCrypt calculado manualmentes; os hashes são gerados em tempo de execução com o `PasswordEncoder` real da aplicação.
- **Entrega "entregue" sem feedback ainda**: fica com `status = PENDENTE` até o professor avaliar.

## Validação realizada

O ambiente local usado nesta sessão **não possui Maven, Docker nem Java 25** instalados (apenas Java 8) — não foi possível compilar, subir os containers ou rodar testes automatizados aqui. O código foi revisado manualmente (tipos, assinaturas, navegação de propriedades JPA, imports), mas **a primeira execução real (`docker compose up --build`) ainda precisa ser feita em um ambiente com essas ferramentas** antes da gravação do vídeo de demonstração.

## Pendências conhecidas

- Validar o build real (`docker compose up --build`) e corrigir eventuais erros de compilação não detectáveis sem o toolchain.
- Definir ferramenta de CI/CD (ver `.ai/tech-stack.md`).
- Upgrade futuro de autenticação para Spring Security + JWT, caso o projeto avance além do MVP (ADR-008).
- Tipos/tamanho máximo de anexo e marcação de notificação como lida seguem como pendência de negócio (ver `.ai/business-rules.md`).
