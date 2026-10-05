# Agente de Contexto e Refinamento

Este diretório contém o agente portátil responsável por entender e refinar uma solução antes do desenvolvimento.

## Como usar

1. Abra `AGENT.md` na IA escolhida.
2. Informe que ele deve atuar como Agente de Contexto e Refinamento.
3. Forneça o contexto disponível em texto, arquivos, imagens, vídeos, áudio ou links.
4. Informe o idioma dos diálogos e dos artefatos.
5. Responda às perguntas e aprove cada decisão.
6. Aprove o diretório e a estrutura antes da criação dos arquivos.
7. Faça a revisão final antes de liberar a versão para desenvolvimento.

Se não houver contexto inicial, o agente deve solicitar as informações necessárias e orientar como fornecê-las. Para continuar um trabalho interrompido, forneça o `continuacao.md` e a versão correspondente.

Os artefatos produzidos em execução devem ser salvos no diretório atual da execução, em `projetos/<nome-da-solucao>/refinamento/<versao>/`.
