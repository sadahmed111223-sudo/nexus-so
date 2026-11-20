# NEXUS AI — Architecture

**Status:** Phase 1 (planning), revision 2. Nothing in this document is implemented yet. Everything below is a plan unless marked otherwise.

**Related documents:** `docs/PRODUCT_REQUIREMENTS.md` (what the product must do, release plan and acceptance criteria). If the two documents disagree, resolve it through the Decision Log (Section 15) before building.

## 1. Purpose

NEXUS AI is an Android platform that lets individuals and organizations manage multiple businesses, customers, finances, social channels, online madrasa operations and automated workflows from one intelligent system.

## 2. Guiding Principles

1. **Security first.** Business data is isolated per workspace, and secrets never live in the app.
2. **Zero-Cost First (mandatory).** Development, testing, hosting and operation use free and open-source options. Nothing that costs money is used without the owner's prior explicit approval. See Section 2.1.
3. **Human in control.** AI proposes; people approve sensitive actions.
4. **Simple before clever.** Avoid unnecessary complexity, technologies and dependencies. Add structure only when a real need appears.
5. **Replaceable parts.** AI providers, social platforms and storage sit behind interfaces so they can be swapped.
6. **Honest status.** Every feature is marked Planned, In progress or Done. "Done" requires evidence (passing tests and a successful build).

### 2.1 Zero-Cost First Policy and Cost Control

This policy is binding for every phase and release.

**Spending rules**
1. No paid API, subscription, hosting, domain, store fee, paid trial, usage-based billing or any other paid or possibly paid service may be used without the owner's **prior, explicit, item-by-item approval**.
2. No exception is automatic. An earlier approval for one item does not cover another item.
3. When something might cost money (for example a service that asks for a payment method even for a free plan, or a free trial that converts to a paid plan), the engineer must stop and ask the owner first.
4. When a free-tier limit is reached, the system must **never upgrade itself to a paid plan**. Paid plans and pay-as-you-go options stay disabled unless the owner approves them.
5. Payment methods are not attached to cloud accounts unless the owner approves it.
6. Any proposed paid item is recorded in the Decision Log with its cost, free alternatives considered, and approval status.
7. Free tiers are never described as unlimited. Free-tier terms can change, so limits are re-verified before each release.

**Behavior when a free limit is reached**
- The app shows a clear, friendly message; it never fails silently.
- Non-essential work stops or slows down; low-risk changes may be queued where safe.
- Data is never deleted or lost because a limit was reached.
- AI requests stop with a clear message when the AI quota is reached. The system does not switch to a paid provider.
- The owner is informed (see monitoring below).

**Monitoring**
- Usage counters are kept for the infrastructure and per workspace for AI usage.
- A warning is raised before a limit is reached. The warning level is an **initial, configurable threshold** (initial proposal: 80% of a limit). It is not a final or mandatory rule and can be changed per service when real usage data is available.

**Free Tier Limits Register**
A table maintained in the repository (to be created in Phase 2) lists, for each service: what it is used for, the current free limit, the official source, the date it was verified, and what happens at the limit. No figure is entered from memory; each figure comes from the service's current official documentation.

| Service | Used for | Free limit | Verified on | Behavior at limit |
|---|---|---|---|---|
| Cloudflare Workers | Backend API | Not yet verified | — | To be verified |
| Cloudflare D1 | Database | Not yet verified | — | To be verified |
| Cloudflare R2 (only if needed) | Files | Not yet verified | — | To be verified |
| GitHub (repository, Actions) | Source code, automated builds and tests | Not yet verified | — | To be verified |
| AI providers (to be chosen) | AI Gateway | Not yet verified | — | Stop with clear message |
| Telegram Bot API | Telegram channel | Rate limits apply; not yet verified | — | Retry later, show status |
| Meta platforms (R2 and later) | Facebook and other Meta channels | Not yet verified (review process and possible costs) | — | Owner approval required first |
| Push notification service (undecided) | Push notifications (R1.1) | Not yet verified | — | To be decided |
| Google Play publishing | Store release | **Not approved.** Possible fee; see Section 14.3 | — | Owner approval required first |

**Dependencies:** only open-source or free libraries with acceptable licenses are used, and only when needed. No paid SDKs.

## 3. System Overview

```
 Android App  ──HTTPS──►  Backend API (Cloudflare Workers)
 (Kotlin, Compose)            │
                              ├──► D1 database (business data)
                              ├──► R2 storage (files, only when needed)
                              ├──► AI Gateway ──► AI provider adapters
                              └──► Integration adapters
                                   (Telegram in R1; Facebook Pages in R2;
                                    other Meta platforms and websites later)
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
- `feature:*` — one module per product area: command-center, business, social, crm, finance, madrasa, automation (R2), knowledge, notifications, account

**UI foundation:** light theme, bottom navigation, consistent spacing, accessible text sizes and contrast, and designed empty, loading and error states on every screen.

**Builds:** because the owner develops from a phone, builds run in the cloud (Section 12.3).

## 5. Backend

**Stack:** Cloudflare Workers (API), D1 (SQL database), R2 (files, only when needed). All are used on their free plans (Section 2.1).

**API principles**
- Versioned routes (`/v1/...`).
- Every request is authenticated and validated.
- Input is validated against a schema; invalid input is rejected with a clear error.
- Consistent error format and request IDs for tracing.
- Rate limiting on sensitive, AI and messaging endpoints.

**Free-plan limits:** Workers, D1 and R2 free plans have request, storage and CPU limits. Their exact values and what happens when a limit is reached must be verified from current official documentation and recorded in the Free Tier Limits Register in Phase 2. The backend and app must handle limit errors gracefully (Section 2.1). No paid plan is enabled without the owner's approval.

## 6. Multi-Tenant Security and Workspace Isolation

A **workspace** is one business or institution. Users can belong to several workspaces. Data of one workspace must never be visible to another.

**Rules**
1. Every business-data table has a `workspace_id` column. No exceptions.
2. The workspace is determined on the **server** from the user's verified membership (or, for webhooks, from the server-side connection record), never from the app's or sender's claim alone.
3. All database access goes through one data-access layer that always applies the workspace filter. Direct ad-hoc queries in feature code are not allowed. (D1 has no built-in row-level security, so this enforcement is our responsibility in code.)
4. **Endpoint inventory and cross-tenant tests:** a maintained list covers every API endpoint that reads or changes business data. Each listed endpoint has an automated test in which a user from one workspace is denied access to another workspace's data. The automated checks fail if an endpoint is missing from the list or lacks its test.
5. Files in R2 (when used) are stored under a workspace-specific prefix and served only after a permission check.
6. Audit logs record who did what, when, in which workspace. Logs are append-only.
7. AI requests include only data from the current workspace that the requesting user may see (Section 7).

**Roles:** Owner, Admin, Manager, Staff, Viewer. The detailed permission matrix is in `docs/PRODUCT_REQUIREMENTS.md`, Section 8. Permissions are checked on the server for every action. Only an Owner can assign or change the Owner role, and each workspace always keeps at least one Owner.

**Authentication and sessions**
- Short-lived access tokens and rotating refresh tokens.
- Passwords stored only as strong salted hashes (algorithm to be confirmed in Phase 4).
- Sign-out invalidates the session on the server (R1). Active-session list and remote sign-out follow in R1.1.
- Secure token storage on the device.

**Secrets and encryption**
- API keys, bot tokens, webhook secrets and platform tokens live only in server-side secret storage or are stored encrypted. They are never in the app or the repository.
- All traffic uses HTTPS.

**Webhook security:** each platform's webhooks are verified using that platform's own official mechanism (see Section 13). Webhook verification never replaces workspace isolation checks.

**Privacy:** data export and deletion requests, retention and handling of external contacts follow `docs/PRODUCT_REQUIREMENTS.md`, Section 9.

**Backup and recovery:** regular D1 exports to protected storage, a documented restore procedure, and a periodic restore test. Details are planned for Phase 11. Backup storage must also follow the Zero-Cost policy.

## 7. AI Provider Adapter

All AI calls go through an **AI Gateway** on the backend.

- A common interface (for example: generate text, summarize, classify) that every provider adapter implements.
- Each provider is a small adapter. Adding or replacing a provider does not change app or feature code.
- Only free providers, or providers the owner has explicitly approved, are enabled. Provider terms (including data retention and use for training) are reviewed before a provider is enabled.
- Keys are never hardcoded; they are configured per environment and stored as server-side secrets.
- A fallback order may be configured, but **only among approved free providers**. The gateway never falls back to a paid provider.
- Usage is tracked per workspace (request counts, token estimates, errors). Quotas and rate limits protect against free-tier exhaustion. When a quota or provider limit is reached, the request is refused with a clear message.
- The mobile application does not depend on the developer's personal AI subscription.
- Prompts and responses are not logged in full by default; any debug logging is protected and time-limited.
- Content from customers, social platforms or files is untrusted. It cannot instruct the AI to run actions or change permissions.
- AI cannot change roles, permissions, connections, secrets or billing settings.

## 8. Human Approval System

AI must not perform sensitive actions on its own.

**Flow:** AI creates a *proposed action* → the user sees a plain-language summary → an authorized user approves or rejects → only then does the server execute → the result is audit-logged.

**Risk levels** (aligned with `docs/PRODUCT_REQUIREMENTS.md`, Section 14.1)
- **Low:** drafts, summaries, read-only answers. No approval needed.
- **Medium:** creating or editing CRM records and tasks. Approval follows the workspace setting and is given by a user who is allowed to perform that action.
- **High:** sending messages to customers on behalf of AI, publishing to social channels, creating, changing or deleting financial data, deleting data, changing roles or connections. Always requires explicit approval by an authorized role.

Additional rules:
- Proposals expire after a set time and cannot be approved after expiry.
- Every proposal, approval and rejection is recorded in the audit log.
- A reply a person writes and sends themselves is a human action. An AI-drafted reply counts as approved only when the user explicitly sends it. There are no automatic AI replies in R1.
- Automations (R2 and later) include a mandatory approval step before any High-risk action.

## 9. Offline Support (where practical)

Offline support is a **Should** for R1, not a launch blocker (see `docs/PRODUCT_REQUIREMENTS.md`).

- **Read:** recently viewed data is cached locally (Room database) and shown when offline, with a clear "may be out of date" indicator.
- **Write:** simple, low-risk changes (for example attendance entries) are queued and synced later using WorkManager, without duplicates or lost entries.
- **Not offline:** AI requests, approvals of High-risk actions, sending messages and publishing require a connection.
- **Conflicts:** the server is the source of truth. Conflict rules are defined per feature; when automatic merging is unsafe, the user is asked.

## 10. Localization and Currencies

- All user-facing text lives in string resources. No hardcoded text in screens.
- Initial languages are proposed as Bengali and English, with right-to-left support planned for languages such as Arabic if the owner approves them. The final list is an open decision.
- Money is stored as whole minor units (for example cents or paisa) with an ISO 4217 currency code. Floating-point numbers are never used for money.
- Each business has a base currency. In R1, reports are totaled per currency without conversion. Manual exchange rates, stored with their dates, and base-currency totals arrive in R1.1.
- Dates and times are stored in UTC and shown in the user's time zone and locale. Number and date formats follow the selected locale.

## 11. Testing and Error Handling

**Testing**
- Unit tests for domain logic and use cases.
- Backend tests for API validation, permissions, webhook verification and **cross-workspace isolation** (Section 6, rule 4).
- Repository and database tests.
- UI tests for critical flows (sign-in, switching workspace, approvals).
- Automated checks run through GitHub Actions on every change. A feature is "Done" only when its tests pass. Test and build usage must stay within the free limits (Section 2.1).

**Error handling**
- A single shared error model (network, validation, permission, server, limit reached, unknown).
- Every screen handles loading, empty and error states with clear, friendly messages and a retry option.
- Network calls use timeouts and safe retries.
- Errors are logged without exposing secrets or personal data.

## 12. Scalability and Delivery

### 12.1 Scalability Without Over-Engineering
- Start with one backend and one database; keep boundaries clean so parts can be separated later if load requires.
- Use pagination for all lists, indexes on frequently filtered columns (always including `workspace_id`), and background jobs for slow work.
- Do not add microservices, extra frameworks or libraries until a measured need exists.
- Major architecture changes require the owner's approval and a Decision Log entry.

### 12.2 Environments
- Development and testing environments use free plans only.
- Secrets differ per environment and are never stored in the repository.

### 12.3 Free Build and Delivery Pipeline
- Android builds and automated tests run in GitHub Actions, so the app can be built without Android Studio on a phone.
- The first deliverable is a debug or test **APK** built for free, used for development and testing with a small group of test users.
- Publishing to the Google Play Store is governed by Section 14.3 and needs the owner's explicit permission if any payment is required.
- GitHub Actions free-usage limits must be verified and recorded in the Free Tier Limits Register.

## 13. Integrations

All integrations use official APIs only. Each platform is an adapter behind a common interface. Connected-account tokens and webhook secrets are encrypted and stored server-side only. For every incoming webhook, the workspace is determined from the server-side connection record and never from message content.

### 13.1 Telegram (R1)

The R1 integration uses the **official Telegram Bot API**. A workspace connects its own Telegram bot, created by the owner through Telegram's official bot-creation process, which provides a bot token.

**What it can do:** receive messages that people send to the connected bot, and reply in those same chats.

**Limitations that the product must show to users**
1. It cannot access anyone's personal Telegram account, inbox, contacts or other chats. It only sees what is sent to the bot.
2. It cannot start a conversation with a person who has not first messaged the bot.
3. It cannot see messages sent before the bot was connected.
4. A person can block the bot; sends to that chat then fail and the failure must be shown.
5. Replies appear as coming from the bot, not from the owner's personal account.
6. Bot conversations are not end-to-end encrypted.
7. Telegram enforces rate limits and file-size limits and may change its API or terms; current values are checked in the official documentation during Phase 8.
8. R1 supports text messages in private chats only. Groups, channels and media are later work (see `docs/PRODUCT_REQUIREMENTS.md`).

**Webhook security (official mechanism)**
- The webhook is registered with Telegram using the official `secret_token` option. Each connection gets its own random secret token.
- Telegram sends that value in the `X-Telegram-Bot-Api-Secret-Token` header. The server must compare it with the stored value using a **secure secret comparison that does not leak information through timing** (for example, no early exit at the first mismatching character), and must reject missing or wrong values before processing anything.
- The specific implementation of the secure comparison is chosen in Phase 8. Cloudflare's current official documentation describes a helper for this purpose (`crypto.subtle.timingSafeEqual`, with its documented conditions such as equal-length inputs). Its availability and usage must be re-confirmed in the current official documentation before it is adopted. No specific method is mandated by this document; the requirement is the secure comparison itself, and it is covered by automated tests.
- This is shared-secret validation. It is **not** a cryptographic signature of the request body, and it is not described as one.
- The webhook endpoint uses HTTPS. The token and secret are stored encrypted. On disconnect, both are deleted and the webhook registration is removed.
- Duplicate deliveries of the same update are processed once (idempotent handling using the update identifier).
- Sending replies is a separate server-side step that follows the approval rules in Section 8.

### 13.2 Meta Platforms (Facebook Pages in R2; others later)
- Meta webhooks are verified using Meta's official mechanisms: the subscription verification handshake when the webhook is set up, and, on each delivery, verification of the **`X-Hub-Signature-256`** header. The signature is an **HMAC-SHA256** calculated with the app secret over the **raw request body** (the exact bytes received, before any parsing or modification). Requests with a missing or invalid signature are rejected before processing.
- The computed and received signatures are compared using the same kind of secure secret comparison described in Section 13.1.
- These details must be confirmed against Meta's current official documentation in Phase 8 before implementation, and are covered by automated tests (valid, missing and invalid signature).
- Meta platforms may require app review and business verification, and some features (for example WhatsApp messaging) can carry costs. Under the Zero-Cost policy, none of this proceeds without the owner's explicit approval.

### 13.3 Websites
- Website chat or content integration is later work (R3+) and will be designed when approved.

## 14. Development Phases and Release Milestones

**Phases and releases are different things.** A *phase* is a block of engineering work. A *release* (R1, R1.1, R2, R3+) is a milestone: the version users receive. R1 and R2 are release milestones, not development phases.

### 14.1 Development Phases

| Phase | Goal | Status |
|---|---|---|
| 1 | Requirements and architecture | In progress |
| 2 | Android project foundation and free cloud build | Planned |
| 3 | Design system and dashboard | Planned |
| 4 | Authentication and workspaces | Planned |
| 5 | AI Command Center | Planned |
| 6 | Business management | Planned |
| 7 | Finance and CRM | Planned |
| 8 | Social integrations (Telegram for R1; Meta work later, see open questions) | Planned |
| 9 | Madrasa management | Planned |
| 10A | Simple automation (R2) | Planned |
| 10B | Advanced automation (R3+) | Planned |
| 11 | Security, testing and optimization | Planned |
| 12 | Google Play preparation | Planned |

**Order of work for R1:** Phases 2–9, then 11 and 12. Completing Phases 2–9, 11 and 12 is sufficient to release R1. Phases 10A and 10B do not block R1. Later releases repeat the relevant security and testing work (Phase 11 scope) for the features they add.

### 14.2 Release Milestones

| Release | Meaning | Contents (summary) |
|---|---|---|
| **R1** | First Google Play release (see 14.3) | Accounts and sessions, workspaces and roles, AI Command Center with approvals, Telegram bot (text, private chats), CRM basics, Finance basics, Madrasa basics, Knowledge Base notes, in-app notifications, audit log. **No automation.** |
| **R1.1** | First incremental updates | Push notifications, conversation history, workspace profile, activity timeline, exchange rates, madrasa summaries, summary report, session list |
| **R2** | Second milestone | **Simple automation (Phase 10A)**, Facebook Pages, import/export, invoices, exams and announcements, file uploads, two-step verification |
| **R3+** | Later milestones | **Advanced automation (Phase 10B)**, Instagram, WhatsApp, website chat, guardian/student portal, advanced analytics, voice input |

The exact feature lists, priorities and acceptance criteria are in `docs/PRODUCT_REQUIREMENTS.md`, Sections 6, 7 and 15.

### 14.3 Launch Rules for R1 and Google Play

1. R1 is released when the launch requirements in `docs/PRODUCT_REQUIREMENTS.md`, Section 6.2 are met, including passing workspace isolation tests, security requirements and a successful automated build.
2. **Free path first:** development, testing and APK builds are done at no cost. A test APK can be shared with test users without publishing to a store.
3. **No money is spent now.** The Google Play developer account and any related fee, and any other paid service, are **not approved**. No Zero-Cost exception is automatic.
4. Before publishing to the Play Store, the engineer must tell the owner exactly what payment, if any, is required and why, list free alternatives, and obtain the owner's explicit written permission. Without permission, Play Store publishing does not proceed.

## 15. Decision Log

| # | Decision | Reason |
|---|---|---|
| 1 | App → Backend → AI; the app never calls AI or social platforms directly | Secret safety, central permissions, cost control |
| 2 | Cloudflare Workers + D1 (+ R2 when needed), free plans | Zero-Cost First, simple to operate |
| 3 | Workspace isolation enforced on the server in one data-access layer, with an endpoint inventory and cross-tenant tests | Prevent data leaks between businesses |
| 4 | AI providers behind adapters | Easy to change provider or fall back |
| 5 | Human approval for sensitive AI actions | Safety and trust |
| 6 | Builds run in GitHub Actions | Owner develops from a phone; Android Studio is not available |
| 7 | Zero-Cost First is a mandatory principle; nothing paid without prior explicit owner approval | Owner decision |
| 8 | Free-tier limits never trigger automatic upgrades; the system degrades gracefully instead | Owner decision; prevents surprise costs |
| 9 | R1, R1.1, R2 and R3+ are release milestones, not development phases | Owner decision; avoids confusion |
| 10 | R1 is the first Play Store release; Phases 2–9, 11 and 12 are sufficient for R1 | Owner decision |
| 11 | Phase 10 is split into 10A Simple Automation (R2) and 10B Advanced Automation (R3+); neither blocks R1 | Owner decision |
| 12 | Webhook verification follows each platform's official mechanism: Telegram secret token with secure secret comparison; Meta handshake and `X-Hub-Signature-256` HMAC-SHA256 over the raw body | Correct and secure integration |
| 13 | No Google Play developer fee or any paid service is approved now; free development, testing and APK builds come first; payment for publishing needs explicit owner permission | Owner decision |
| 14 | The 80% usage warning is an initial configurable threshold, not a fixed rule | Owner decision; real usage data will guide the final value |

## 16. Open Questions

- Final list of supported languages and currencies.
- Password hashing method and sign-in options (email only, or also Google sign-in).
- Which free AI providers to support first (to be researched against current free limits and terms).
- Whether workspace owners may use their own AI provider keys at their own cost (not assumed; needs an owner decision).
- Backup storage location and schedule within the Zero-Cost policy.
- Push notification delivery mechanism (free-first review required, before R1.1).
- Meta app review, business verification and any costs: approval to proceed (before R2).
- Whether Telegram groups and media are added in R2.
- Whether madrasa guardians or students need their own logins.
- Minimum supported Android version.
- Data location and legal requirements per target country.
- How later Meta work is labeled in the phase plan (proposal: it continues Phase 8 as separate increments for R2).
- Exact free-tier limits and limit behavior of each service, to be verified and recorded in Phase 2.
- The exact secure secret comparison implementation for webhooks (chosen in Phase 8 after checking current Cloudflare documentation).
- Whether Play Store publishing requires any payment at Phase 12, and the owner's decision at that time.