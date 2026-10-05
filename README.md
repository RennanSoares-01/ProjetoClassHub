# class-hub

Plataforma web de gestão de projetos acadêmicos (turmas, tarefas, entregas, avaliações, notificações e dashboards).

Entregas Aula 1 - PDF-Diagrama

Entregas Aula 2 - EntregaveisAula2

## Contexto do projeto

- `.ai/` — contexto técnico e de negócio (standards, architecture, tech-stack, business-rules) que orienta qualquer agente de implementação.
- `.github/prompts/` — prompts reutilizáveis: geração do contexto `.ai/` e implementação do MVP a partir dele.
- `backend/` — API REST em Java 25 + Spring Boot, com PostgreSQL via Flyway.
- `frontend/` — interface web estática (HTML/CSS/JS), servida pelo próprio backend.

## Como rodar o MVP localmente

Pré-requisitos: Docker e Docker Compose.

```bash
docker compose up --build
```

- Aplicação (frontend + API): http://localhost:8080
- Documentação da API (Swagger UI): http://localhost:8080/swagger-ui.html

A base de dados é populada automaticamente com usuários e dados de demonstração (ver `backend/src/main/java/com/classhub/common/seed/DataSeeder.java`):

| Papel | Matrícula | Senha |
|---|---|---|
| Admin | `admin001` | `admin123` |
| Professor | `prof001` | `prof123` |
| Aluno | `aluno001` | `aluno123` |

> Build local sem Docker exige Java 25, Maven e um PostgreSQL rodando (ver variáveis `DB_HOST`/`DB_PORT`/`DB_NAME`/`DB_USER`/`DB_PASSWORD` em `backend/src/main/resources/application.yml`).
