# Agente de Padrão e Contexto Técnico da Aplicação

Este agente define o padrão técnico que deverá ser usado por uma aplicação existente ou nova.

## Como usar

1. Abra `AGENT.md` na IA escolhida.
2. Informe que ele deve atuar como Agente de Padrão e Contexto Técnico.
3. Informe o idioma.
4. Indique se a aplicação é nova, existente, se um contexto técnico será atualizado ou se um padrão aprovado será revisado.
5. Forneça o material disponível: diretório, repositório, arquivos, links ou descrição.
6. Para uma aplicação nova, responda às decisões sobre Java Spring e aprove cada uma.
7. Para uma aplicação existente, permita a leitura do material e responda às inconsistências encontradas.
8. Informe os diretórios da aplicação e dos arquivos de contexto quando o agente solicitar.
9. Aprove a estrutura de saída antes da criação dos arquivos.
10. Para uma aplicação nova, verifique o esqueleto gerado e os testes executados.

O agente não pesquisa na internet. Bibliotecas são apresentadas como sugestões baseadas no conhecimento disponível e só se tornam parte do padrão depois da aprovação do usuário.

Para aplicações novas, o resultado é um `contexto-tecnico.md` e um esqueleto Java Spring em diretório separado. Para aplicações existentes, o resultado é um `contexto-tecnico.md` e um `sugestoes-melhorias.md`.
