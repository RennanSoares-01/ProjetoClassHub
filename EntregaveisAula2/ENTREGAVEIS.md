# Entregáveis — Prática "Implementando a sua Arquitetura" (Aula 2)

Este documento organiza os dois prompts pedidos como entregável da atividade, indicando qual agente customizado cada um aciona. Os arquivos completos e reutilizáveis ficam em [`.github/prompts/`](.github/prompts/); os agentes que eles acionam ficam em [`.github/agents/`](.github/agents/).

## Agentes utilizados no projeto

| Agente | Arquivo | Papel |
|---|---|---|
| `refinamento` | `.github/agents/refinamento.md` | Levanta o escopo de negócio e aprova as regras/domínio antes de qualquer decisão técnica. Usado em conversa para consolidar o `business-rules.md`. |
| `padrao-aplicacao` | `.github/agents/padrao-aplicacao.agent.md` | Gera a pasta `.ai/` (contexto técnico) a partir do padrão aprovado e do refinamento de negócio. |
| `implementacao` | `.github/agents/implementacao.agent.md` | Lê a pasta `.ai/` como fonte de verdade e implementa o MVP (código da aplicação). |

## 1. Prompt de geração de contexto

- **Arquivo**: [`.github/prompts/01-geracao-contexto.prompt.md`](.github/prompts/01-geracao-contexto.prompt.md)
- **Agente acionado**: `padrao-aplicacao`
- **O que gera**: a estrutura `.ai/` (`standards.md`, `architecture.md`, `tech-stack.md`, `business-rules.md`) na raiz da aplicação.

```prompt
---
description: "Gera a estrutura de contexto .ai/ (standards, architecture, tech-stack, business-rules) para uma aplicação, a partir do padrão técnico aprovado e do refinamento de negócio já existente."
agent: "padrao-aplicacao"
argument-hint: "/01-geracao-contexto class-hub, usando o refinamento já aprovado em .ai/business-rules.md"
---
Gere a estrutura de contexto técnico `.ai/` na raiz desta aplicação, seguindo exatamente este formato:

.ai/
├── standards.md        # Convenções de código e estilo
├── architecture.md     # Decisões de alto nível (ADRs)
├── tech-stack.md       # Versões e libs permitidas
└── business-rules.md   # Lógica de negócio e domínio

Antes de gerar qualquer arquivo:

1. Confirme o idioma a ser usado nos documentos.
2. Confirme o diretório raiz da aplicação onde a pasta `.ai/` será criada.
3. Verifique se já existe um contexto de refinamento de negócio aprovado (ex: business-rules.md ou os artefatos do agente de refinamento). Esse conteúdo deve alimentar `business-rules.md` — não invente regra de negócio nova.
4. Levante as decisões técnicas necessárias (linguagem, framework, banco de dados, arquitetura, convenções) fazendo perguntas agrupadas ao usuário, sempre que a informação não estiver disponível. Não assuma tecnologia sem aprovação.
5. Classifique cada decisão técnica como Obrigatório, Recomendado ou Exceção aprovada.
6. Apresente a estrutura de arquivos proposta e peça aprovação explícita antes de escrever qualquer arquivo.

Somente após a aprovação, gere os 4 arquivos de `.ai/`, cada um autocontido e consumível por outro agente de desenvolvimento sem depender desta conversa.
```

## 2. Prompt de implementação

- **Arquivo**: [`.github/prompts/02-implementacao.prompt.md`](.github/prompts/02-implementacao.prompt.md)
- **Agente acionado**: `implementacao`
- **O que gera**: o código do MVP (backend + frontend), com dados mockados, a partir da pasta `.ai/`.

```prompt
---
description: "Implementa um MVP executável localmente, com dados mockados, a partir da estrutura de contexto .ai/ (standards, architecture, tech-stack, business-rules)."
agent: "implementacao"
argument-hint: "Escopo a implementar (ex: todas as partes do MVP, ou um recurso específico)"
---
Implemente um MVP executável localmente para esta aplicação, usando dados mockados (sem necessidade de infraestrutura externa real), seguindo estritamente:

- .ai/business-rules.md — lógica de negócio e domínio (não redefina regras de negócio sem aprovação);
- .ai/architecture.md — arquitetura, camadas e endpoints aprovados;
- .ai/tech-stack.md — tecnologias, versões e libs permitidas;
- .ai/standards.md — convenções de código e estilo a seguir.

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
```

## Ordem de uso neste projeto

1. **`refinamento`** (conversa) → consolidou o escopo de negócio do Class Hub e aprovou as regras registradas em [`.ai/business-rules.md`](.ai/business-rules.md).
2. **Prompt de geração de contexto** (`padrao-aplicacao`) → gerou os 4 arquivos de [`.ai/`](.ai/).
3. **Prompt de implementação** (`implementacao`) → gerou o MVP em [`backend/`](backend/) e [`frontend/`](frontend/), documentado em [`.github/projetos/class-hub/notas-implementacao.md`](.github/projetos/class-hub/notas-implementacao.md).
