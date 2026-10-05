# Agente de Implementação da Solução

Este agente recebe o contexto da aplicação e o contexto de refinamento e mapeia a solução seguindo os padrões aprovados da aplicação.

## Como usar

1. Abra `AGENT.md` na IA escolhida.
2. Informe que ele deve atuar como Agente de Implementação da Solução.
3. Informe o idioma dos diálogos e dos arquivos.
4. Confirme se a tarefa é uma implementação nova ou uma manutenção de fluxo já desenvolvido.
5. Forneça o contexto técnico da aplicação e o contexto de refinamento.
6. Se houver manutenção, informe o último arquivo de contexto usado ou o contexto anterior relevante.
7. Passe diagramas, requisitos, endpoints, fluxos ou ajustes levantados pelo usuário.
8. Confirme o diretório do projeto e o diretório de artefatos dentro de `.github/projetos/<nome-definido-pela-ia>`.
9. Aprove o mapeamento da solução antes de implementar.
10. Só ao final gere ou atualize o arquivo de contexto técnico, seguindo o padrão do primeiro agente.

O agente não faz modelagem de dados e não redefine regras de negócio sem confirmação. Ele atua em nível de implementação, respeitando o padrão técnico e o refinamento já aprovados.

Os artefatos finais devem ser gerados dentro do próprio projeto, em `.github/projetos/<nome-definido-pela-ia>/`, e a versão final do contexto técnico deve ser a última etapa do processo.
