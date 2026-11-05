# CLAUDE.md — SecureDataMasker

Instruções para qualquer sessão do Claude que trabalhe neste repositório. Leia este arquivo e `docs/spec.md` antes de qualquer alteração.

## O projeto

Biblioteca Java para **validação, normalização e mascaramento** de dados sensíveis brasileiros (CPF, CNPJ numérico e alfanumérico, e-mail, telefone, chave Pix), com integração ao **Logback** para reduzir a exposição desses dados em logs.

Posicionamento: a biblioteca **reduz** a exposição acidental, não a elimina. Os limites estão na seção 8.1 de `docs/spec.md` e precisam aparecer na documentação pública.

## Estado atual

- **Fase 1 (Discovery): concluída.** A especificação está em `docs/spec.md`.
- **Fase 2 (Architecture): concluída** (PR #1, merged; gate aprovado em 05/11/2026). ADRs 001 a 008, esqueleto da API pública (todos os métodos lançam `UnsupportedOperationException("Not implemented yet")`), `docs/masking-policy.md`, PoC com Logback puro e `docs/phase-2-findings.md`.
- **Fase 3 (Foundation): entregue no branch `phase-3-foundation`, por pull request; aguarda o gate** (CI verde em toda a matriz). Conteúdo:
  - Maven Wrapper (Maven 3.9.16, com `distributionSha256Sum` e `wrapperSha256Sum`), build reproduzível (`project.build.outputTimestamp`), plugins com versão fixa;
  - toolchains: compila uma vez no JDK 25 com `--release 21`; testes no JDK 21 ou 25 via `-Dtest.jdk` (Surefire `jdkToolchain`);
  - Spotless + palantir-java-format (ADR-009), Error Prone + NullAway em modo JSpecify, SpotBugs + Find Security Bugs (`config/spotbugs-exclude.xml`), JaCoCo (só relatórios; o gate da seção 13.2 é ligado na Fase 4), Javadoc com `doclint=all` e `failOnWarnings`;
  - Jazzer 0.30.0 funciona no JUnit 6.1.3 e foi mantido (D16): um `@FuzzTest` de exemplo em modo de regressão;
  - `.github/`: `ci.yml`, `codeql.yml`, `scorecard.yml`, `dependabot.yml` e `logback-matrix/` (versões "latest" da matriz);
  - `LICENSE`, `NOTICE`, `CHANGELOG.md`, `CONTRIBUTING.md`, `.editorconfig`;
  - `docs/phase-3-findings.md`, com as verificações, as exceções às ferramentas e as **perguntas abertas** (seção 8: `requires static transitive org.jspecify`; aprovação do ADR-009).
- **Fase 4 (Core Implementation): não iniciada.** Ao começar: remover as exclusões e supressões marcadas "Phase 3 only"/"Phase 4", religar o `DoNotCallSuggester` e o gate do JaCoCo (`jacoco.check.skip=false`), e adicionar o PIT.

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
- Commits só de formatação (`spotless:apply`) ficam separados dos demais.

### Comandos de build e teste (Fase 3)

Use sempre o wrapper (`./mvnw`). O build compila com o JDK 25 do `~/.m2/toolchains.xml` (`--release 21`) e roda todos os gates (Spotless, Error Prone/NullAway, testes, SpotBugs, Javadoc, relatório do JaCoCo). Por padrão, testa no JDK 25 e contra o Logback mínimo, 1.5.19:

```bash
./mvnw -B clean verify
```

Testes no JDK 21 (a compilação continua no JDK 25):

```bash
./mvnw -B clean verify -Dtest.jdk=21
```

Contra outra versão do Logback (as "latest" da matriz estão em `.github/logback-matrix/*/pom.xml`):

```bash
./mvnw -B clean verify -Dlogback.version=1.5.38
./mvnw -B clean verify -Dlogback.version=1.6.6
```

Formatação (rode antes de commitar):

```bash
./mvnw spotless:apply
```

Ausência de dependências em runtime no `core` (deve listar só o próprio `core`):

```bash
./mvnw -B dependency:tree -pl securedatamasker-core -Dscope=runtime
```

Gate de cobertura (desligado até a Fase 4; hoje falha contra os stubs, como esperado):

```bash
./mvnw -B verify -Djacoco.check.skip=false
```

Fuzzing local com o Jazzer (o build normal só roda o modo de regressão):

```bash
JAZZER_FUZZ=1 JAZZER_MAX_DURATION=60s ./mvnw test -pl securedatamasker-core -Dtest=LongestDigitRunFuzzTest
```

Observações do ambiente:

- O `/etc/mavenrc` seleciona o JDK 25 para rodar o Maven. Com o Maven no JDK 25, o `MAVEN_OPTS` do ambiente (`--sun-misc-unsafe-memory-access=allow`) não atrapalha: os JVMs de teste (JDK 21 ou 25) não o herdam. Só se o próprio Maven rodar no JDK 21 é preciso zerá-lo: `JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 MAVEN_SKIP_RC=true MAVEN_OPTS= ./mvnw -B clean verify -Dtest.jdk=21`.
- O `~/.m2/toolchains.xml` precisa declarar os JDKs 21 e 25; no CI, o `actions/setup-java` o gera.
- O Maven Central pode responder HTTP 429 (limite de requisições) neste ambiente; nesse caso, espere e tente de novo. Não desligue a verificação de checksum do wrapper.
- O `maven-javadoc-plugin` precisa de `quiet=false`: com `quiet=true`, o `failOnWarnings` deixa de funcionar (`docs/phase-3-findings.md`, seção 5).
