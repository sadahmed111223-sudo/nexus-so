# NEXUS AI — Architecture

**Status:** Phase 1 (planning). Nothing in this document is implemented yet. Everything below is a plan unless marked otherwise.

## 1. Purpose

NEXUS AI is an Android platform that lets individuals and organizations manage multiple businesses, customers, finances, social channels, online madrasa operations and automated workflows from one intelligent system.

## 2. Guiding Principles

1. **Security first.** Business data is isolated per workspace, and secrets never live in the app.
2. **Free-first.** Prefer free and open-source tools. No paid API or subscription without explicit owner approval. Free tiers have limits and are never assumed to be unlimited.
3. **Human in control.** AI proposes; people approve sensitive actions.
4. **Simple before clever.** Avoid unnecessary complexity and dependencies. Add structure only when a real need appears.
5. **Replaceable parts.** AI providers, social platforms and storage sit behind interfaces so they can be swapped.
6. **Honest status.** Every feature is marked Planned, In progress or Done, and "Done" requires evidence (tests and a successful build).

## 3. System Overview

```
 Android App  ──HTTPS──►  Backend API (Cloudflare Workers)
 (Kotlin, Compose)            │
                              ├──► D1 database (business data)
                              ├──► R2 storage (files, when needed)
                              ├──► AI Gateway ──► AI provider adapters
                              └──► Integration adapters
                                   (Facebook, Instagram, WhatsApp,
                                    Telegram, Websites)
```

Rule: the app never talks to AI providers or social platforms directly. It only talks to our backend. This keeps secrets safe, allows central permission checks, and makes cost control possible.

## 4. Android App

**Stack:** Kotlin, Jetpack Compose, Material 3, Coroutines and Flow, MVVM, Clean Architecture, Repository pattern.

**Layers**
- **Presentation:** screens (Compose) and ViewModels. Holds UI state only.
- **Domain:** use cases and business rules. Pure Kotlin, no Android dependencies, easy to test.
- **Data:** repositories, local database, network client. Hides where data comes from.

**Planned module layout** (kept small at first, split later if needed)
- `app` — entry point, navigation, dependency wiring
- `core:design` — theme, colors, typography, shared components
- `core:common` — shared utilities, error types
- `core:network` — API client, session handling
- `core:database` — local cache
- `feature:*` — one module per product area: command-center, business, social, crm, finance, madrasa, automation, knowledge, notifications, account

**UI foundation:** light theme, bottom navigation, consistent spacing, accessible text sizes and contrast, and designed empty, loading and error states on every screen.

## 5. Backend

**Stack:** Cloudflare Workers (API), D1 (SQL database), R2 (files, only when needed).

**API principles**
- Versioned routes (`/v1/...`).
- Every request is authenticated and validated.
- Input is validated against a schema; invalid input is rejected with a clear error.
- Consistent error format and request IDs for tracing.
- Rate limiting on sensitive and AI endpoints.

**Known limits to plan around:** free tiers of Workers, D1 and R2 have request, storage and CPU limits. We will monitor usage and design for graceful degradation. Limits must be checked against current Cloudflare documentation before launch.

## 6. Multi-Tenant Security and Workspace Isolation

A **workspace** is one business or institution. Users can belong to several workspaces. Data of one workspace must never be visible to another.

**Rules**
1. Every business-data table has a `workspace_id` column. No exceptions.
2. The workspace is determined on the **server** from the user's verified membership, never trusted from the app alone.
3. All database access goes through one data-access layer that always applies the workspace filter. Direct ad-hoc queries in feature code are not allowed. (D1 has no built-in row-level security, so this enforcement is our responsibility in code.)
4. Cross-tenant tests are mandatory: automated tests that try to read or change another workspace's data and must fail.
5. Files in R2 are stored under a workspace-specific prefix and served only after a permission check.
6. Audit logs record who did what, when, in which workspace. Logs are append-only.

**Roles (initial):** Owner, Admin, Manager, Staff, Viewer. Permissions are checked on the server for every action.

**Authentication and sessions**
- Short-lived access tokens and rotating refresh tokens.
- Passwords stored only as strong salted hashes (algorithm to be confirmed during Phase 4).
- Session list, remote sign-out and secure token storage on the device.

**Secrets and encryption**
- API keys and platform tokens live only in server-side secret storage.
- Sensitive fields (for example connected-account tokens) are encrypted before being stored.
- All traffic uses HTTPS.

**Backup and recovery:** regular D1 exports to protected storage, a documented restore procedure, and a periodic restore test. Details planned for Phase 11.

## 7. AI Provider Adapter

All AI calls go through an **AI Gateway** on the backend.

- A common interface (for example: generate text, summarize, classify) that every provider adapter implements.
- Each provider is a small adapter. Adding or replacing a provider does not change app or feature code.
- Providers are configured per environment and, where allowed, per workspace. Keys are never hardcoded.
- Fallback order can be configured (for example free provider first, backup second).
- Usage tracking per workspace: request counts, token estimates, errors.
- Quotas and rate limits protect against free-tier exhaustion. When a limit is reached, the user sees a clear message.
- The mobile app does not depend on the developer's personal AI subscription.
- Prompts and outputs avoid including data from other workspaces. Only the current workspace's data may be used as context.

## 8. Human Approval System

AI must not perform sensitive actions on its own.

**Flow:** AI creates a *proposed action* → the user sees a clear summary → the user approves or rejects → only then does the server execute → the result is audit-logged.

**Risk levels (initial proposal)**
- **Low:** read-only suggestions, drafts. No approval needed.
- **Medium:** creating or editing records, scheduling. Approval according to workspace settings.
- **High:** publishing to social channels, sending messages to customers, financial changes, deleting data, changing roles or connections. Always requires explicit approval.

Proposals expire after a set time. Approvals can be limited to specific roles (for example only Owner or Admin for financial actions). Every proposal, approval and rejection is recorded.

## 9. Offline Support (where practical)

- **Read:** recently viewed data is cached locally (Room database) and shown when offline.
- **Write:** simple, low-risk changes (notes, drafts, attendance entries) are queued and synced later using WorkManager.
- **Not offline:** AI requests, approvals of high-risk actions, payments and publishing require a connection.
- **Conflicts:** the server is the source of truth. Conflict rules are defined per feature; when automatic merging is unsafe, the user is asked.
- The UI always shows clearly when data may be out of date or changes are waiting to sync.

## 10. Localization and Currencies

- All user-facing text lives in string resources. No hardcoded text in screens.
- Initial languages: Bengali and English. More languages are added later, including right-to-left support (for example Arabic) where needed for madrasa use. The final language list is to be confirmed by the owner.
- Money is stored as whole minor units (for example cents or paisa) with an ISO 4217 currency code. Floating-point numbers are never used for money.
- Each business has a base currency. Multi-currency amounts keep their original currency; exchange rates and their dates are stored explicitly.
- Dates and times are stored in UTC and shown in the user's time zone and locale. Number and date formats follow the selected locale.

## 11. Testing and Error Handling

**Testing**
- Unit tests for domain logic and use cases.
- Backend tests for API validation, permissions and **cross-workspace isolation**.
- Repository and database tests.
- UI tests for critical flows (sign-in, switching workspace, approvals).
- Automated checks run through GitHub Actions on every change. A feature is "Done" only when its tests pass.

**Error handling**
- A single shared error model (network, validation, permission, server, limit reached, unknown).
- Every screen handles loading, empty and error states with clear, friendly messages and a retry option.
- Network calls use timeouts and safe retries.
- Errors are logged without exposing secrets or personal data.

## 12. Scalability Without Over-Engineering

- Start with one backend and one database; keep boundaries clean so parts can be separated later if load requires.
- Use pagination for all lists, indexes on frequently filtered columns (always including `workspace_id`), and background jobs for slow work.
- Do not add microservices, extra frameworks or libraries until a measured need exists.
- Major architecture changes require the owner's approval and a decision-log entry.

## 13. Integrations

Platforms: Facebook Pages, Instagram, WhatsApp, Telegram, and websites, using official APIs only.

- Each platform is an adapter behind a common interface.
- Connected-account tokens are encrypted and stored server-side only.
- Webhooks are verified (signature checks) before processing.
- Meta platforms may require app review and business verification, and WhatsApp messaging can carry costs. These must be checked and approved by the owner before use.

## 14. Development Phases

| Phase | Goal | Status |
|---|---|---|
| 1 | Requirements and architecture | In progress |
| 2 | Android project foundation and cloud build | Planned |
| 3 | Design system and dashboard | Planned |
| 4 | Authentication and workspaces | Planned |
| 5 | AI Command Center | Planned |
| 6 | Business management | Planned |
| 7 | Finance and CRM | Planned |
| 8 | Social integrations | Planned |
| 9 | Madrasa management | Planned |
| 10 | Automation | Planned |
| 11 | Security, testing, optimization | Planned |
| 12 | Google Play preparation | Planned |

## 15. Decision Log

| # | Decision | Reason |
|---|---|---|
| 1 | App → Backend → AI; the app never calls AI or social platforms directly | Secret safety, central permissions, cost control |
| 2 | Cloudflare Workers + D1 (+ R2 when needed) | Free-first, simple to operate |
| 3 | Workspace isolation enforced on the server in one data-access layer | Prevent data leaks between businesses |
| 4 | AI providers behind adapters | Easy to change provider or fall back |
| 5 | Human approval for sensitive AI actions | Safety and trust |
| 6 | Builds run in GitHub Actions | Owner develops from a phone; Android Studio is not available |

## 16. Open Questions

- Final list of supported languages and currencies.
- Password hashing method and sign-in options (email only, or also Google sign-in).
- Which free AI providers to support first (to be researched against current limits).
- Backup storage location and schedule.