# SecureDataMasker

[English](README.md) | [Português (Brasil)](README.pt-BR.md)

A Java library to validate, normalize and mask Brazilian sensitive data (CPF, CNPJ, e-mail, phone number and Pix key), with Logback integration to reduce the exposure of that data in logs.

> **Status: under design (Phase 2).** The public API exists only as a skeleton: every method throws `UnsupportedOperationException`. There is no working code and no published release. Nothing described here can be used at this point.

## Documentation

- [Technical specification (Phase 1: Discovery)](docs/spec.md) — written in Portuguese: problem, requirements, threat model, proposed architecture, roadmap and acceptance criteria.
- [Architecture decision records](docs/adr/) (Phase 2).
- [Masking policy](docs/masking-policy.md) — the planned output per type and preset (not implemented yet).
- [Phase 2 findings](docs/phase-2-findings.md) — Logback proof of concept and tooling research.

## License

Apache-2.0 (the `LICENSE` file will be added in Phase 3).
