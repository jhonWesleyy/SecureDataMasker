# SecureDataMasker

[English](README.md) | [Português (Brasil)](README.pt-BR.md)

Biblioteca Java para validar, normalizar e mascarar dados sensíveis brasileiros (CPF, CNPJ, e-mail, telefone e chave Pix), com integração ao Logback para reduzir a exposição desses dados em logs.

> **Status: em design (Fase 2).** A API pública existe só como esqueleto: todos os métodos lançam `UnsupportedOperationException`. Ainda não existe código funcional nem versão publicada. Nada descrito aqui pode ser usado por enquanto.

## Documentação

- [Especificação técnica (Fase 1: Discovery)](docs/spec.md): problema, requisitos, threat model, arquitetura proposta, roadmap e critérios de aceitação.
- [Registros de decisão de arquitetura](docs/adr/) (Fase 2, em inglês).
- [Política de mascaramento](docs/masking-policy.md): a saída planejada por tipo e preset, ainda não implementada (em inglês).
- [Achados da Fase 2](docs/phase-2-findings.md): prova de conceito com o Logback e pesquisa de ferramentas (em inglês).

## Licença

Apache-2.0 (o arquivo `LICENSE` será adicionado na Fase 3).
