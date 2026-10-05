---
description: "Implementa um MVP executável localmente, com dados mockados, a partir da estrutura de contexto .ai/ (standards, architecture, tech-stack, business-rules)."
agent: "implementacao"
argument-hint: "Escopo a implementar (ex: todas as partes do MVP, ou um recurso específico)"
---
Implemente um MVP executável localmente para esta aplicação, usando **dados mockados** (sem necessidade de infraestrutura externa real), seguindo estritamente:

- [.ai/business-rules.md](../../.ai/business-rules.md) — lógica de negócio e domínio (não redefina regras de negócio sem aprovação);
- [.ai/architecture.md](../../.ai/architecture.md) — arquitetura, camadas e endpoints aprovados;
- [.ai/tech-stack.md](../../.ai/tech-stack.md) — tecnologias, versões e libs permitidas;
- [.ai/standards.md](../../.ai/standards.md) — convenções de código e estilo a seguir.

Antes de implementar:

1. Confirme o diretório raiz onde o código da aplicação será criado.
2. Confirme se é uma implementação nova ou manutenção de um fluxo já existente (verifique notas de implementação anteriores em `.github/projetos/<nome>/`, se houver).
3. Resuma o escopo que será implementado nesta rodada e peça aprovação explícita antes de criar ou alterar arquivos de código.
4. Aponte qualquer incompatibilidade encontrada entre `.ai/` e o pedido do usuário, pedindo decisão antes de prosseguir.

Depois da aprovação:

- Gere o código do MVP respeitando a arquitetura, as entidades e os endpoints definidos em `.ai/architecture.md`.
- Use dados mockados (em memória ou arquivos locais) no lugar de integrações externas reais.
- Garanta que o projeto rode localmente com um único comando documentado (ex: build/start).
- Ao final, registre as notas de implementação (`.github/projetos/<nome>/notas-implementacao.md`), sem alterar os arquivos de `.ai/` sem aprovação explícita do usuário.
