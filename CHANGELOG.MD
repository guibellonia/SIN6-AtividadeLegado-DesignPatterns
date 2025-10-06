# Changelog

Todas as mudanças notáveis deste projeto serão documentadas aqui.

Este projeto segue **[Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/)** e **[SemVer](https://semver.org/lang/pt-BR/)**.

> **Como usar**
> - Mantenha a seção `[Unreleased]` no topo.
> - Para cada release, crie uma nova seção com **data**, **resumo** e **categorias**.
> - Prefira descrições orientadas a comportamento/impacto, não apenas lista de commits.

---

## [Unreleased]

### Added
-

### Changed
-

### Fixed
-

### Deprecated
-

### Removed
-

### Security
-

### Tests
-

---

## [1.1.0] - 2025-09-30
**Refactor: notificação por eventos + Strategy/Factory**

### Added
- **Evento de domínio** `OrderCreatedEvent` para sinalizar criação de pedidos.
- **Listener assíncrono e transacional** `NotifyOnOrderCreatedListener` usando `@TransactionalEventListener` + `@Async`.
- **Orquestrador** `NotificationOrchestrator` para compor o contexto e delegar o envio ao canal correto.
- **Padrão Strategy** com `NotificationStrategy` e implementações:
  - `EmailNotificationStrategy` (usa `EmailGateway`).
  - `SmsNotificationStrategy` (usa `SmsGateway`).
  - `NullNotificationStrategy` (no-op) para cenários desabilitados.
- **Factory** `NotificationStrategyFactory` para resolver a estratégia conforme preferências/config do usuário.
- **Adapters (Gateway/Adapter)** para integrações externas:
  - `EmailGateway` (ex.: SMTP/SDK).
  - `SmsGateway` (ex.: provedor de SMS).
- **Estrutura de pastas** (clean/hexagonal light): `domain/`, `application/`, `infrastructure/`.

### Changed
- `OrderService.place(...)` agora **publica `OrderCreatedEvent`** ao invés de disparar notificações diretamente.
- **Isolamento de SDKs externos**: chamadas a provedores de e-mail/SMS saíram do domínio e foram movidas para adapters em `infrastructure`.
- **Redução de acoplamento e condicionais** (`if/else` por canal) substituídos por Strategy + Factory.

### Fixed
- **Testabilidade e previsibilidade** no fluxo de notificação após criação do pedido (reação após commit da transação).
- Correção de cenários onde falhas de um canal impactavam o fluxo principal (cada estratégia é isolada e substituível).

### Tests
- **Unitários**: `NotificationStrategyFactoryTest`, `EmailNotificationStrategyTest`, `SmsNotificationStrategyTest` (gateways mockados).
- **Integração leve**: publicação de `OrderCreatedEvent` verifica que o orquestrador foi acionado e que os gateways foram chamados (via spy/mocks).

### Migration / Breaking Changes
- Serviços anteriores de notificação direta foram substituídos por **`NotificationOrchestrator` + strategies**.
- Se havia **chaves de configuração** de canais em outro local, consolidar em `application.yml`/`application.properties`.

### Docs / Chore
- README atualizado com diagrama de fluxo (evento → listener → orchestrator → strategy → gateway) e instruções de testes locais.

### Pull Request
- PR: **#1** — _Refactor: notificação por eventos + Strategy/Factory_.
- Autores: @guibellonia @MauricioHansen.

---

## [1.0.0] - 2025-09-xx
**PoC inicial**: implementação mínima para notificar usuário após criação de pedido, acoplada diretamente ao serviço e SDKs externos (sem eventos/strategies).

---

## Referências
- Utilize **Conventional Commits** no histórico para facilitar leitura no changelog.
- Links de comparação (atualize `<org>/<repo>` e tags):
  - `[Unreleased]`: `.../compare/v1.1.0...HEAD`
  - `[1.1.0]`: `.../compare/v1.0.0...v1.1.0`

[Unreleased]: https://github.com/<org>/<repo>/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/<org>/<repo>/compare/v1.0.0...v1.1.0
