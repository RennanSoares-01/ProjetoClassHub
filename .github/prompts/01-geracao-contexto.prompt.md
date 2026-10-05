---
description: "Gera a estrutura de contexto .ai/ (standards, architecture, tech-stack, business-rules) para uma aplicação, a partir do padrão técnico aprovado e do refinamento de negócio já existente."
agent: "padrao-aplicacao"
argument-hint: "/01-geracao-contexto class-hub, usando o refinamento já aprovado em .ai/business-rules.md"
---
Gere a estrutura de contexto técnico `.ai/` na raiz desta aplicação, seguindo exatamente este formato:

```text
.ai/
├── standards.md        # Convenções de código e estilo
├── architecture.md     # Decisões de alto nível (ADRs)
├── tech-stack.md       # Versões e libs permitidas
└── business-rules.md   # Lógica de negócio e domínio
```

Antes de gerar qualquer arquivo:

1. Confirme o idioma a ser usado nos documentos.
2. Confirme o diretório raiz da aplicação onde a pasta `.ai/` será criada.
3. Verifique se já existe um contexto de refinamento de negócio aprovado (ex: [business-rules.md](../../.ai/business-rules.md) ou os artefatos do agente de refinamento). Esse conteúdo deve alimentar `business-rules.md` — não invente regra de negócio nova.
4. Levante as decisões técnicas necessárias (linguagem, framework, banco de dados, arquitetura, convenções) fazendo perguntas agrupadas ao usuário, sempre que a informação não estiver disponível. Não assuma tecnologia sem aprovação.
5. Classifique cada decisão técnica como `Obrigatório`, `Recomendado` ou `Exceção aprovada`.
6. Apresente a estrutura de arquivos proposta e peça aprovação explícita antes de escrever qualquer arquivo.

Somente após a aprovação, gere os 4 arquivos de `.ai/`, cada um autocontido e consumível por outro agente de desenvolvimento sem depender desta conversa.
