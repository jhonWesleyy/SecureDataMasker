# SecureDataMasker

[English](README.md) | [Português (Brasil)](README.pt-BR.md)

Biblioteca Java para validar, normalizar e mascarar dados sensíveis brasileiros (CPF, CNPJ, e-mail, telefone e chave Pix), com integração ao Logback para reduzir a exposição desses dados em logs.

> **Status: fundação (Fase 3).** Build, verificações de qualidade e CI estão prontos. A API pública existe só como esqueleto: todos os métodos lançam `UnsupportedOperationException`. Ainda não existe código funcional nem versão publicada. Nada descrito aqui pode ser usado por enquanto.

## Documentação

- [Especificação técnica (Fase 1: Discovery)](docs/spec.md): problema, requisitos, threat model, arquitetura proposta, roadmap e critérios de aceitação.
- [Registros de decisão de arquitetura](docs/adr/) (Fase 2, em inglês).
- [Política de mascaramento](docs/masking-policy.md): a saída planejada por tipo e preset, ainda não implementada (em inglês).
- [Achados da Fase 2](docs/phase-2-findings.md): prova de conceito com o Logback e pesquisa de ferramentas (em inglês).
- [Achados da Fase 3](docs/phase-3-findings.md): verificação do build, das ferramentas de qualidade e do CI, e o teste do Jazzer (em inglês).

## Como contribuir

- [Guia de contribuição](CONTRIBUTING.md): comandos de build, toolchains, verificações de qualidade e a regra dos dados fictícios (em inglês).
- [Changelog](CHANGELOG.md) (em inglês).

## Licença

Apache-2.0. Veja [LICENSE](LICENSE) e [NOTICE](NOTICE).
