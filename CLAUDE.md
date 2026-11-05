# CLAUDE.md — SecureDataMasker

Instruções para qualquer sessão do Claude que trabalhe neste repositório. Leia este arquivo e `docs/spec.md` antes de qualquer alteração.

## O projeto

Biblioteca Java para **validação, normalização e mascaramento** de dados sensíveis brasileiros (CPF, CNPJ numérico e alfanumérico, e-mail, telefone, chave Pix), com integração ao **Logback** para reduzir a exposição desses dados em logs.

Posicionamento: a biblioteca **reduz** a exposição acidental, não a elimina. Os limites estão na seção 8.1 de `docs/spec.md` e precisam aparecer na documentação pública.

## Estado atual

- **Fase 1 (Discovery): concluída.** A especificação está em `docs/spec.md`.
- **Fase 2 (Architecture): próxima.** Nenhum código implementado ainda.

Trabalhe uma fase por vez (seção 15 de `docs/spec.md`). Não avance para a fase seguinte sem que o gate da anterior tenha sido aprovado pelo mantenedor.

## Decisões

| ID | Decisão | Status |
| --- | --- | --- |
| D1 | Java mínimo **21**; desenvolvimento no **JDK 25** com `--release 21`; CI com 21 e 25 | **Aprovada** |
| D6 | `groupId` `io.github.jhonwesleyy`; pacote base `io.github.jhonwesleyy.securedatamasker` | **Aprovada** |
| D2 | Maven | **Aprovada** |
| D3 | Dois módulos publicados: `securedatamasker-core` (zero dependências em runtime) e `securedatamasker-logback` | **Aprovada** |
| D4 | Detecção: formatos com máscara → `SHAPE`; sequências só de dígitos/alfanuméricas → `VALIDATED`; telefone só com formatação ou `+55` | **Aprovada** |
| D5 | Preset de máscara padrão `BALANCED`, com máscara de comprimento fixo | **Aprovada** |
| D7 | Licença Apache-2.0 | **Aprovada** |
| D8 | `MaskingPatternLayoutEncoder` como integração padrão; `%mask(...)` como opção avançada | **Aprovada** |
| D9 | JSpecify + NullAway | **Aprovada** |
| D10 | Nome SecureDataMasker, `artifactId` `securedatamasker-*` (único dentro do namespace `io.github.jhonwesleyy`) | **Aprovada** |

Todas as decisões D1 a D10 foram aprovadas em 04/11/2026. Qualquer alteração precisa da aprovação do mantenedor e deve ser registrada nesta tabela e num ADR.

## Regras obrigatórias

1. Não implemente antes de a especificação da fase estar aprovada.
2. Não invente requisitos; requisito novo entra em `docs/spec.md` com justificativa.
3. Justifique decisões arquiteturais importantes em ADRs (`docs/adr/`).
4. Priorize simplicidade sem sacrificar segurança.
5. Não adicione dependências por conveniência. Antes de adicionar uma, verifique se o Java resolve sozinho. O `core` não tem dependências em runtime.
6. Não ignore nem esconda limitações técnicas ou problemas encontrados.
7. Uma implementação só está concluída com testes.
8. Não documente funcionalidades que não existem.
9. Não afirme que o projeto é seguro sem análise fundamentada.
10. **Use apenas dados fictícios** em testes, exemplos e documentação: CPFs e CNPJs gerados pelo algoritmo, e-mails em `example.com`/`example.org`. Nunca use dados de pessoa ou empresa real.
11. A biblioteca **nunca** inclui o valor de entrada em exceções, mensagens de status do Logback ou `toString()`.
12. Em caso de falha no mascaramento, a saída é *fail-closed*: um marcador, nunca o texto original.
13. Nada de regex com backtracking no caminho quente de detecção; a detecção é O(n).
14. Preserve a compatibilidade da API pública ou documente claramente a quebra.
15. Ao modificar arquivos existentes, leia o contexto primeiro e preserve as decisões válidas.

## Convenções de trabalho

- Cada fase em um branch próprio, entregue por pull request.
- Mensagens de commit no imperativo, em português ou inglês, de forma consistente dentro do PR.
- Comandos de build e de qualidade serão definidos na Fase 3. Quando existirem, documente-os aqui.
