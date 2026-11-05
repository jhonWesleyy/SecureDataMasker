# CLAUDE.md — SecureDataMasker

Instruções para qualquer sessão do Claude que trabalhe neste repositório. Leia este arquivo e `docs/spec.md` antes de qualquer alteração.

## O projeto

Biblioteca Java para **validação, normalização e mascaramento** de dados sensíveis brasileiros (CPF, CNPJ numérico e alfanumérico, e-mail, telefone, chave Pix), com integração ao **Logback** para reduzir a exposição desses dados em logs.

Posicionamento: a biblioteca **reduz** a exposição acidental, não a elimina. Os limites estão na seção 8.1 de `docs/spec.md` e precisam aparecer na documentação pública.

## Estado atual

- **Fase 1 (Discovery): concluída.** A especificação está em `docs/spec.md`.
- **Fase 2 (Architecture): entregue no branch `phase-2-architecture` (PR #1), gate revisado em 05/11/2026.** Conteúdo:
  - ADRs 001 a 008 em `docs/adr/`;
  - esqueleto da API pública de `securedatamasker-core` e `securedatamasker-logback`: todos os métodos lançam `UnsupportedOperationException("Not implemented yet")`, sem lógica de validação, detecção ou mascaramento;
  - `docs/masking-policy.md` com a tabela final de máscaras;
  - PoC com Logback puro em `securedatamasker-logback/src/test/.../poc/`;
  - `docs/phase-2-findings.md`, com resultados e fontes.
- **Perguntas da Fase 2 respondidas** na revisão do gate (D14 a D18; seção 5 de `docs/phase-2-findings.md`). Falta só o merge do PR #1 pelo mantenedor.
- **Fase 3 (Foundation): não iniciada.** Os POMs atuais são mínimos de propósito: sem plugins de qualidade, CI, assinatura ou publicação.

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
| D11 | Objetivo em etapas: projeto de portfólio na série 0.x; biblioteca mantida para terceiros a partir da 1.0, com compatibilidade rigorosa (japicmp) desde então | **Aprovada** |
| D12 | MVP sem adaptador JSON: só a integração via PatternLayout; RF21/RF22 continuam como funcionalidades futuras | **Aprovada** |
| D13 | Idioma: código, Javadoc e documentação (`docs/`, CHANGELOG, CONTRIBUTING) em **inglês**. README bilíngue: `README.md` em inglês e `README.pt-BR.md` em português, com link entre eles no topo; no `README.pt-BR.md`, os exemplos de código são **idênticos** aos do inglês e só o texto ao redor é traduzido | **Aprovada** |
| D14 | Logback suportado: **1.5.19+, incluindo 1.6.x**; matriz do CI com 1.5.19, a última 1.5.x e a última 1.6.x (ADR-007) | **Aprovada** |
| D15 | jqwik **não** adotado: testes de propriedade com geradores próprios de seed fixa em `@ParameterizedTest` do JUnit | **Aprovada** |
| D16 | Jazzer só depois de um `@FuzzTest` real rodar no JUnit 6 (Fase 3 ou 4); sem voltar o JUnit para a 5.x | **Aprovada** |
| D17 | RF15 (detectores customizados) adiado; manter ou cortar é decidido antes da 1.0 (ADR-006) | **Aprovada** |
| D18 | `TextMasker.detect(...)` fora da API pública; `Match` é interno (ADR-002) | **Aprovada** |

As decisões D1 a D13 foram aprovadas em 04/11/2026 e D14 a D18 em 05/11/2026, na revisão do gate da Fase 2. Qualquer alteração precisa da aprovação do mantenedor e deve ser registrada nesta tabela e num ADR.

Exceções de idioma: este `CLAUDE.md` (instruções internas) e `docs/spec.md` (registro da Fase 1) continuam em português. Todo documento criado a partir da Fase 2 segue a D13.

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
- Comandos de qualidade (Spotless, Error Prone, SpotBugs, JaCoCo, PIT) serão definidos na Fase 3. Quando existirem, documente-os aqui.

### Comandos de build e teste (Fase 2)

Build com o JDK 25 (`--release 21` vem do POM). Por padrão, compila contra o Logback mínimo, 1.5.19:

```bash
mvn -v                     # deve mostrar "Java version: 25..."
mvn -B clean verify
```

PoC e esqueleto contra outra versão do Logback:

```bash
mvn -B clean verify -Dlogback.version=1.5.38
mvn -B clean verify -Dlogback.version=1.6.6
```

Conferência com o JDK 21. O `MAVEN_OPTS` do ambiente traz `--sun-misc-unsafe-memory-access=allow`, que o JDK 21 não aceita, por isso é zerado:

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 MAVEN_SKIP_RC=true MAVEN_OPTS= mvn -B clean verify
```

Ausência de dependências em runtime no `core`:

```bash
mvn -B dependency:tree -pl securedatamasker-core
```

Observações do ambiente:

- O `mvn` usa o `JAVA_HOME`, não o `update-alternatives`. O JDK 25 é selecionado por `/etc/mavenrc` (`export JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64`).
- O `~/.m2/toolchains.xml` tem os JDKs 21 e 25, mas o `maven-toolchains-plugin` não está configurado nos POMs: isso é da Fase 3.
