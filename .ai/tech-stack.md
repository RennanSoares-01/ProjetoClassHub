# Tech Stack — Class Hub

> Fonte: informado pelo usuário em 2026-10-04. Versões específicas marcadas como "a definir" devem ser decididas pela equipe antes do início da implementação.

| Camada | Tecnologia | Versão | Observação |
|---|---|---|---|
| Frontend | HTML / CSS / JavaScript | — | Sem framework definido (vanilla), servido como estático pelo backend |
| Backend | Java | 25 | — |
| Backend | Spring Boot | 3.5.x (ou mais recente estável compatível com Java 25) | Framework REST da aplicação |
| Backend | Spring Web (REST) | — | Exposição dos endpoints HTTP |
| Backend | Spring Data JPA / Hibernate | — | Acesso a dados |
| Backend | MapStruct | — | Conversão entre Entity e DTO |
| Backend | springdoc-openapi | — | Documentação Swagger/OpenAPI de todas as rotas (obrigatório) |
| Backend | Spring Security Crypto (BCrypt) | — | Hash de senha. MVP usa token simples em memória, sem Spring Security completo nem JWT |
| Persistência | PostgreSQL | — | Motor relacional definido |
| Persistência | Flyway | — | Scripts de migration obrigatórios (schema + dados de seed/mock) |
| Build | Maven | — | — |
| Infraestrutura | Docker / Docker Compose | — | Sobe PostgreSQL e o backend; comando único para rodar localmente |
| Controle de versão | Git | — | — |
| Integração/Entrega | CI/CD | a definir | Ferramenta específica ainda não escolhida |

## Regras para a IA

- Não sugerir bancos NoSQL para este projeto.
- Toda alteração de schema deve vir acompanhada de um script de migration Flyway.
- Não introduzir Spring Security completo/JWT sem aprovação explícita — o MVP usa token simples em memória.

## Pendências

- Definir a ferramenta de CI/CD (ex: GitHub Actions, GitLab CI, Jenkins).
- Avaliar upgrade do mecanismo de autenticação (Spring Security + JWT) em versão futura, fora do escopo do MVP.
