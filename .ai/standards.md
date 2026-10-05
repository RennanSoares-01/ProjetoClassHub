# Standards — Class Hub

> Convenções confirmadas em 2026-10-04 para a implementação do MVP.

## Backend (Java 25 / Spring Boot)

### Naming conventions
- **Classes:** `PascalCase` (ex: `TarefaService`).
- **Métodos/variáveis:** `camelCase` (ex: `registrarEntrega`).
- **Constantes:** `UPPER_SNAKE_CASE`.
- **Records:** devem ser usados para DTOs e Value Objects (entrada e saída da API).
- **Interfaces:** não usar prefixo `I`; o sufixo `Impl` é desencorajado, preferir nomes semânticos.
- Pacotes em letras minúsculas, organizados por funcionalidade (ex: `com.classhub.tarefa`, `com.classhub.turma`).

### Patterns (obrigatório)
- Arquitetura em camadas: `Controller` → `Service` → `Repository`. Controllers não acessam o repositório diretamente.
- Usar **Constructor Injection** para dependências (evitar `@Autowired` em campos).
- Usar **Optional** para retornos que podem ser nulos.
- Usar **MapStruct** para conversão entre Entity e DTO (record). Não expor entidades de persistência diretamente na API.
- Exceções de domínio devem ser mapeadas em um `GlobalExceptionHandler` único, via `@ControllerAdvice`, com respostas de erro padronizadas.
- Toda rota deve ter documentação Swagger/OpenAPI (springdoc).
- Toda alteração de schema de banco deve vir com um script de migration Flyway (ver [tech-stack.md](./tech-stack.md)).

## API REST

- Recursos nomeados no plural e em minúsculas (ex: `/tarefas`, `/turmas`).
- Uso correto dos verbos HTTP (`GET`, `POST`, `PUT`, `DELETE`) e dos status codes (2xx sucesso, 4xx erro de cliente, 5xx erro de servidor).
- Respostas em JSON.

## Frontend (HTML / CSS / JavaScript)

- JavaScript: `camelCase` para variáveis e funções.
- CSS: classes em `kebab-case`.
- Indentação e formatação consistentes em todo o projeto.

## Controle de versão

- Commits seguindo o padrão [Conventional Commits](https://www.conventionalcommits.org/) (`feat:`, `fix:`, `docs:`, `refactor:`, etc.).

## Formatação e lint

- Recomenda-se adotar uma ferramenta de formatação automática para o backend (ex: Spotless ou Checkstyle) e para o frontend (ex: Prettier), a serem confirmadas pela equipe.

## Pendências

- Validar estas convenções com o time antes de aplicá-las como padrão obrigatório.
- Escolher e configurar as ferramentas de lint/formatação mencionadas acima.
