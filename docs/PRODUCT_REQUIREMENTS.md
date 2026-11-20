# NEXUS AI — Product Requirements

**Status:** Phase 1 (planning). This document describes what NEXUS AI **will** do. No feature described here is implemented yet. Every item is **Planned** until it is built, tested and verified.

**Related documents:** `docs/ARCHITECTURE.md` (technical design). If this document and the architecture ever disagree, resolve it through the Decision Log in the architecture document before building.

**How to read requirement IDs:** `FR-` = functional requirement, `NFR-` = non-functional requirement, `AC-` = acceptance criterion. Priority words: **Must** (required for the release), **Should** (important, may slip), **Could** (nice to have).

---

## 1. Product Vision

NEXUS AI is an Android platform that lets individuals and organizations run multiple businesses and educational institutions from one intelligent system: customers, finances, social channels, online madrasa operations and automated workflows, assisted by AI that proposes actions and humans who approve the important ones.

**Long-term ambition:** become a trusted global technology brand for small and medium organizations.

## 2. Goals and Non-Goals

### 2.1 Goals
1. Give an owner one place to see and manage all of their businesses.
2. Reduce repetitive administrative work using AI assistance with human control.
3. Keep every business's data strictly separate and secure.
4. Work reliably on mid-range Android phones and unstable networks.
5. Operate within a free-first cost policy: no paid APIs or subscriptions without explicit owner approval.
6. Be usable in multiple languages and currencies from the start.

### 2.2 Non-Goals (for the first releases)
See Section 11 (Out of Scope).

## 3. Target Users

| Persona | Description | Main needs |
|---|---|---|
| **Multi-business owner** | Runs several small businesses or an organization | One dashboard, quick overview of money and customers, delegation |
| **Business manager** | Manages daily operations for one or more workspaces | Customer follow-up, tasks, reports |
| **Madrasa administrator** | Runs an online madrasa or learning institution | Student records, classes, attendance, fees, communication |
| **Teacher / staff member** | Works inside one workspace with limited permissions | Simple screens for assigned tasks (attendance, notes) |
| **Social media / marketing staff** | Prepares content and replies | Drafts, scheduling, approval, message inbox |
| **Read-only stakeholder** | Accountant, advisor or partner | View reports without changing data |

## 4. Core Problems

1. Owners juggle many apps (chat apps, spreadsheets, social pages) and lose track of customers and money.
2. Information about different businesses gets mixed up or is not available on the phone.
3. Social and customer messages are answered late or inconsistently.
4. Madrasa records (students, attendance, fees) are kept on paper or in scattered files.
5. AI tools are powerful but risky when they act without oversight, and many are costly.
6. Small organizations have unreliable internet and limited budgets.

## 5. Concepts and Terms

- **Account:** a person's sign-in identity.
- **Workspace:** one business or institution. All business data belongs to exactly one workspace.
- **Member:** an account that belongs to a workspace with a role.
- **Proposed action:** something AI wants to do, waiting for human review.
- **Approval:** a recorded human decision (approve or reject) on a proposed action.
- **Adapter:** replaceable component that connects to an AI provider or social platform.

## 6. Release Plan (MVP vs Later)

The release grouping below is a **proposal for owner approval**. Development phases (1–12) are defined in `docs/ARCHITECTURE.md`.

| Module | Release 1 (MVP) | Release 2 | Release 3+ |
|---|---|---|---|
| 1. AI Command Center | Text assistant, drafts, summaries, proposed actions with approval | Richer actions across modules | Voice input, proactive suggestions |
| 2. Multi-Business Management | Create/switch workspaces, members, roles, workspace profile | Cross-business overview reports | Templates per business type |
| 3. Social Media Manager | Telegram connection (receive/reply with approval); draft content | Facebook Pages publishing and inbox | Instagram, WhatsApp, website chat widget |
| 4. CRM | Contacts, notes, tags, pipeline stages, follow-up tasks | Import/export, segments | Automated follow-up sequences |
| 5. Finance and Analytics | Income/expense records, categories, multi-currency amounts, basic reports | Invoices, budgets, recurring items | Advanced analytics, forecasts |
| 6. Online Madrasa Management | Students, classes, teachers, attendance, fee records | Exams/results, announcements to guardians | Guardian/student portal (pending decision) |
| 7. Workflow Automation | — (not in MVP) | Simple rule-based triggers with approval steps | Advanced multi-step workflows |
| 8. AI Knowledge Base | Text notes per workspace, AI answers with source references | File upload (via R2) | Website content import |
| 9. Notifications and Reports | In-app and push notifications, basic summary report | Scheduled reports, export to PDF/CSV | Custom reports |
| 10. Account and Security | Sign-in, sessions, roles, audit log, data export request | Two-step verification | Advanced admin controls |

Facebook Pages, Instagram and WhatsApp depend on platform approval processes and possible costs (see Section 12). Telegram is proposed for the MVP because its bot API is simpler; this must be confirmed by the owner.

---

## 7. Functional Requirements by Module

### 7.1 AI Command Center
- **FR-AI-01 (Must, R1):** Users can type a request in natural language within the current workspace (for example "summarize this week's customer notes").
- **FR-AI-02 (Must, R1):** AI responses are generated only through the backend AI Gateway, never directly from the app.
- **FR-AI-03 (Must, R1):** AI can create **proposed actions** (for example "create follow-up task for contact X"). Proposed actions are shown with a clear summary and approve/reject buttons.
- **FR-AI-04 (Must, R1):** AI uses only data from the user's current workspace and only data the user is permitted to see.
- **FR-AI-05 (Must, R1):** Show clear messages when AI is unavailable or the usage limit is reached.
- **FR-AI-06 (Should, R1):** Conversation history per user per workspace, deletable by the user.
- **FR-AI-07 (Should, R1):** Mark AI-generated content visibly as AI-generated.
- **FR-AI-08 (Could, R3):** Voice input.

### 7.2 Multi-Business Management
- **FR-BIZ-01 (Must, R1):** A user can create a workspace with name, type, country, base currency, time zone and default language.
- **FR-BIZ-02 (Must, R1):** A user can belong to multiple workspaces and switch between them; the active workspace is always visible.
- **FR-BIZ-03 (Must, R1):** Owners/Admins can invite members by a secure invitation and assign roles.
- **FR-BIZ-04 (Must, R1):** Owners/Admins can change a member's role or remove a member; removed members lose access immediately.
- **FR-BIZ-05 (Should, R1):** Workspace profile (logo, contact info, business details).
- **FR-BIZ-06 (Should, R2):** Overview dashboard across workspaces the user can access, without mixing data inside any workspace's records.

### 7.3 Social Media Manager
- **FR-SOC-01 (Must, R1):** Connect a Telegram bot to a workspace using official Telegram APIs; credentials are stored only on the server, encrypted.
- **FR-SOC-02 (Must, R1):** Incoming Telegram messages appear in an inbox for that workspace; each message is linked to a CRM contact where possible.
- **FR-SOC-03 (Must, R1):** Replies are sent only after a human sends them or approves an AI-drafted reply. (Final default per risk level is an open decision; see Section 12.)
- **FR-SOC-04 (Should, R1):** Draft posts/messages with AI help and save drafts.
- **FR-SOC-05 (Must, R2):** Connect Facebook Pages; publish and schedule posts after approval; read comments/messages.
- **FR-SOC-06 (Could, R3):** Instagram, WhatsApp, website chat widget through official APIs.
- **FR-SOC-07 (Must, all releases):** Incoming external messages are treated as **untrusted input**; they can never trigger actions by themselves.
- **FR-SOC-08 (Must, all releases):** Users can disconnect a channel; tokens are deleted on disconnect.

### 7.4 CRM
- **FR-CRM-01 (Must, R1):** Create, view, edit, archive contacts (name, phone, email, tags, notes, source).
- **FR-CRM-02 (Must, R1):** Pipeline stages per workspace (customizable names).
- **FR-CRM-03 (Must, R1):** Follow-up tasks with due date and assignee.
- **FR-CRM-04 (Should, R1):** Search and filter by name, tag, stage.
- **FR-CRM-05 (Should, R1):** Activity timeline per contact (notes, messages, tasks).
- **FR-CRM-06 (Should, R2):** Import and export contacts (CSV).
- **FR-CRM-07 (Must, all releases):** Deleting a contact follows the privacy and deletion rules in Section 9.

### 7.5 Finance and Analytics
- **FR-FIN-01 (Must, R1):** Record income and expenses with amount, currency, category, date, note.
- **FR-FIN-02 (Must, R1):** Money is stored in minor units with an ISO 4217 currency code; each workspace has a base currency.
- **FR-FIN-03 (Must, R1):** Editing or deleting a financial record is audit-logged (who, when, old/new values).
- **FR-FIN-04 (Must, R1):** Basic reports: totals by period, by category, income vs expense.
- **FR-FIN-05 (Should, R1):** Manual entry of exchange rates with date when recording a foreign-currency amount.
- **FR-FIN-06 (Should, R2):** Invoices, budgets, recurring entries.
- **FR-FIN-07 (Must, all releases):** AI may suggest or draft financial entries, but creating, changing or deleting financial data through AI always requires explicit human approval.
- **FR-FIN-08 (Not in scope):** NEXUS AI does not process payments or hold funds (see Section 11).

### 7.6 Online Madrasa Management
- **FR-MAD-01 (Must, R1):** Manage students (name, guardian contact, enrollment date, status, notes).
- **FR-MAD-02 (Must, R1):** Manage classes/courses and assign teachers and students.
- **FR-MAD-03 (Must, R1):** Record attendance per class session; works offline and syncs later.
- **FR-MAD-04 (Must, R1):** Record fees due and payments received (records only, not payment processing).
- **FR-MAD-05 (Should, R1):** Simple attendance and fee summary reports.
- **FR-MAD-06 (Should, R2):** Exams and results; announcements to guardians through connected channels after approval.
- **FR-MAD-07 (Could, R3):** Guardian/student portal (subject to the open decision on external user types).
- **FR-MAD-08 (Must, all releases):** Student data is treated as sensitive data about minors and follows Section 9.

### 7.7 Workflow Automation (Release 2+)
- **FR-AUT-01 (Must, R2):** Rule-based automations: trigger → condition → action, created and enabled by Owners/Admins.
- **FR-AUT-02 (Must, R2):** Any step that is High risk includes a mandatory human approval step.
- **FR-AUT-03 (Must, R2):** Automation runs are logged with outcome and errors.
- **FR-AUT-04 (Must, R2):** A visible switch to pause or disable any automation instantly.
- **FR-AUT-05 (Must, R2):** Limits on run frequency to prevent loops and runaway usage.

### 7.8 AI Knowledge Base
- **FR-KB-01 (Must, R1):** Add, edit and delete text notes/articles per workspace.
- **FR-KB-02 (Must, R1):** AI answers can use the workspace knowledge base and show which entries were used as sources.
- **FR-KB-03 (Must, R1):** Knowledge base content is never used for another workspace.
- **FR-KB-04 (Should, R2):** File uploads stored in R2 under a workspace-specific prefix.

### 7.9 Notifications and Reports
- **FR-NOT-01 (Must, R1):** In-app notification list (new messages, approvals waiting, due tasks).
- **FR-NOT-02 (Should, R1):** Push notifications, with user-controlled settings per type.
- **FR-NOT-03 (Should, R1):** A basic summary report (customers, money, attendance) viewable in the app.
- **FR-NOT-04 (Should, R2):** Scheduled reports and export (PDF/CSV).
- **FR-NOT-05 (Must, R1):** Notifications never expose sensitive details on the lock screen unless the user allows it.

### 7.10 Account and Security Management
- **FR-ACC-01 (Must, R1):** Account registration and sign-in (methods to be confirmed; see Section 12).
- **FR-ACC-02 (Must, R1):** Secure session management with sign-out and a list of active sessions with remote sign-out.
- **FR-ACC-03 (Must, R1):** Role-based access enforced on the server for every request.
- **FR-ACC-04 (Must, R1):** Audit log of sensitive events, viewable by Owner/Admin.
- **FR-ACC-05 (Must, R1):** Users can request export of their data and deletion of their account (Section 9).
- **FR-ACC-06 (Should, R2):** Two-step verification.

---

## 8. User Roles and Permissions

Roles follow `docs/ARCHITECTURE.md`: Owner, Admin, Manager, Staff, Viewer. Permissions are checked on the **server**. The table below is the initial proposal.

Legend: **F** = full (create/edit/delete), **E** = create/edit own or assigned, **R** = read only, **A** = approve, **—** = no access.

| Capability | Owner | Admin | Manager | Staff | Viewer |
|---|---|---|---|---|---|
| Delete workspace, transfer ownership | F | — | — | — | — |
| Manage members and roles | F | F | — | — | — |
| Workspace settings | F | F | R | — | — |
| Connect/disconnect social channels | F | F | — | — | — |
| Social inbox and drafts | F | F | F | E | R |
| Approve High-risk AI actions | A | A | Configurable | — | — |
| CRM contacts and tasks | F | F | F | E | R |
| Finance records | F | F | F | — (default) | R |
| Finance reports | F | F | R | — | R |
| Madrasa students, classes, fees | F | F | F | E (assigned classes) | R |
| Madrasa attendance | F | F | F | E (assigned classes) | R |
| Knowledge base | F | F | F | E | R |
| Automations (R2) | F | F | — | — | — |
| AI Command Center use | F | F | F | E | R |
| View audit log | R | R | — | — | — |
| Export workspace data | F | F | — | — | — |

Rules:
- A user's permissions apply **per workspace**. Being an Owner in one workspace gives no access to another.
- At least one Owner must exist per workspace.
- Permissions for Staff are limited to assigned items where noted.
- Finer permissions (for example "teacher can only take attendance") may be added later without changing the five base roles.
- Whether Staff may view finance records is configurable per workspace (default: no).

---

## 9. Privacy and Data Rules

- **Data minimization:** collect only data needed for a feature.
- **Isolation:** every business-data record belongs to exactly one workspace and is never visible outside it (see `docs/ARCHITECTURE.md`, section 6).
- **Sensitive data:** student records (including minors), contact details, financial records and connected-account tokens are sensitive. Tokens are encrypted before storage.
- **Export and deletion:** users can request an export of their data. Account and workspace deletion follows a documented process; some records may be kept for a limited time where law or audit needs require it. The exact retention periods are an open decision.
- **Logs:** logs and audit entries must not contain passwords, tokens or full message contents unless required and protected.
- **Third parties:** data sent to AI providers or social platforms is limited to what the feature needs. Provider terms on data retention and training must be reviewed before a provider is enabled.
- **Legal compliance:** applicable laws (for example data protection laws in the countries where users live) must be reviewed with qualified legal advice before launch. This document is not legal advice.
- **Privacy policy and terms:** required before Google Play release (Phase 12).

---

## 10. Main User Journeys

### 10.1 First-time setup
1. User installs the app and creates an account.
2. User creates the first workspace (name, type, country, currency, language).
3. App shows an empty dashboard with clear next-step suggestions.
4. User optionally invites members.

### 10.2 Switching between businesses
1. User opens the workspace switcher.
2. User selects another workspace.
3. All screens now show only that workspace's data; the workspace name stays visible.

### 10.3 Handling a customer message (Telegram, R1)
1. A customer message arrives and appears in the inbox and a notification is sent.
2. The message is linked to (or creates) a CRM contact.
3. User asks AI to draft a reply.
4. AI creates a proposed reply; the user edits, approves and sends it.
5. The action is recorded in the contact timeline and audit log.

### 10.4 Approving a sensitive AI action
1. The user asks AI for something that changes data or contacts customers.
2. AI creates a proposed action with a plain-language summary.
3. The user (with sufficient role) reviews and approves or rejects.
4. On approval the server executes the action; on rejection nothing happens.
5. The proposal, decision, executor and result are audit-logged. Unanswered proposals expire.

### 10.5 Taking madrasa attendance offline
1. Teacher opens the class and marks attendance with no internet.
2. App shows that changes are waiting to sync.
3. When online, changes sync automatically; conflicts are shown to the user if they cannot be merged safely.

### 10.6 Recording and reviewing finances
1. User records an income or expense in the workspace currency (or another currency with an entered exchange rate).
2. User opens reports and sees totals by period and category.

---

## 11. Out of Scope

Not planned for the first releases (may be reconsidered later by owner decision):
- iOS app and a full web dashboard.
- Payment processing, holding funds, or acting as a financial institution.
- Tax filing, payroll and legal accounting certification.
- E-commerce storefronts or marketplace features.
- Voice or video calling.
- Training or fine-tuning AI models on customer data.
- Bulk unsolicited messaging or any spam-like feature.
- AI taking sensitive actions without human approval.
- White-labeling and on-premise hosting.
- Guardian/student portals in Release 1.

---

## 12. Unresolved Decisions

| # | Question | Needed by |
|---|---|---|
| 1 | Final list of supported languages (proposed start: Bengali, English; possible Arabic with right-to-left) and currencies | Phase 3 |
| 2 | Sign-in methods (email/password only, or also Google sign-in) and password hashing method | Phase 4 |
| 3 | Which free AI providers to support first, based on current free-tier limits | Phase 5 |
| 4 | Whether the MVP social channel is Telegram (proposed) and the order of Facebook, Instagram, WhatsApp | Phase 8 |
| 5 | Meta app review, business verification and WhatsApp messaging costs: approval to proceed | Phase 8 |
| 6 | Whether madrasa guardians/students need their own logins (external user types) | Phase 9 |
| 7 | Whether Staff may view finance records by default (proposed: no) | Phase 7 |
| 8 | Default approval level for sending replies to customers (always approve vs. configurable) | Phase 5 |
| 9 | Data retention periods and deletion process | Phase 11 |
| 10 | Minimum supported Android version | Phase 2 |
| 11 | Business model for the product itself (free, paid plans) — requires owner approval; no paid services are added without it | Before Phase 12 |
| 12 | Backup storage location and schedule | Phase 11 |
| 13 | Data location and legal requirements per target country | Before Phase 12 |
| 14 | Final brand and name availability checks | Before Phase 12 |

---

## 13. Non-Functional Requirements

Numeric targets below are **initial targets** and must be validated by measurement on real mid-range devices and the real backend.

### 13.1 Security
- **NFR-SEC-01:** All traffic over HTTPS; no secrets in the app or in the repository.
- **NFR-SEC-02:** Every business-data table includes `workspace_id`; all queries go through the single data-access layer that applies it.
- **NFR-SEC-03:** Role and workspace membership are verified on the server for every request.
- **NFR-SEC-04:** Passwords stored only as strong salted hashes; access tokens are short-lived; refresh tokens rotate.
- **NFR-SEC-05:** Sensitive fields (connected-account tokens) are encrypted before storage.
- **NFR-SEC-06:** All input is validated on the server; invalid input is rejected with a clear error.
- **NFR-SEC-07:** Audit logs are append-only and cover sign-in, role changes, connections, approvals, financial changes and data export/deletion.
- **NFR-SEC-08:** Rate limiting on sign-in, AI and messaging endpoints.
- **NFR-SEC-09:** Webhooks from external platforms are signature-verified before processing.

### 13.2 Privacy
- See Section 9. **NFR-PRI-01:** Data export and deletion requests are processed through documented procedures.

### 13.3 Performance (initial targets)
- **NFR-PERF-01:** Cold app start to usable dashboard (cached data) within 3 seconds on a mid-range device.
- **NFR-PERF-02:** 95% of ordinary API requests (non-AI) respond within 800 ms under normal load.
- **NFR-PERF-03:** Lists use pagination; no screen loads unbounded data.
- **NFR-PERF-04:** AI responses show progress feedback; requests time out with a clear message instead of hanging.

### 13.4 Reliability
- **NFR-REL-01:** Network calls use timeouts and safe retries; user actions are not lost on failure.
- **NFR-REL-02:** A shared error model with clear, friendly messages and a retry option.
- **NFR-REL-03:** Regular database exports (backup), a documented restore procedure, and a periodic restore test.
- **NFR-REL-04:** Crash and error reporting without personal data or secrets.
- **NFR-REL-05:** Offline-capable features (cached reading, low-risk queued changes) sync without duplicates.

### 13.5 Accessibility
- **NFR-ACC-01:** Text scales with the system font size; layouts remain usable at large sizes.
- **NFR-ACC-02:** Color contrast meets WCAG 2.1 AA for text.
- **NFR-ACC-03:** Interactive elements have a minimum touch target of 48dp and screen-reader labels.
- **NFR-ACC-04:** Information is never conveyed by color alone.

### 13.6 Localization
- **NFR-LOC-01:** No hardcoded user-facing text; all text in string resources.
- **NFR-LOC-02:** Initial languages as decided in Section 12; layout supports right-to-left languages.
- **NFR-LOC-03:** Money in minor units with ISO 4217 codes; dates stored in UTC and displayed in the user's time zone and locale.

### 13.7 Scalability and Maintainability
- **NFR-SCA-01:** Start with a single backend and database with clean boundaries; add complexity only after measured need.
- **NFR-SCA-02:** Indexes on frequently filtered columns, always including `workspace_id`.
- **NFR-SCA-03:** Provider and platform integrations sit behind adapters so they can be replaced.
- **NFR-SCA-04:** Free-tier limits of the infrastructure are monitored; the app degrades gracefully when limits are reached.
- **NFR-MNT-01:** Automated tests run on every change through GitHub Actions; a feature is "Done" only with passing tests and a verified build.

---

## 14. AI Usage Rules

1. All AI calls go through the backend AI Gateway and a provider adapter. The app never calls AI providers directly.
2. API keys are stored only in server-side secret storage. They are never hardcoded or committed.
3. Providers are configurable and replaceable; a fallback order may be configured.
4. Only free or already-approved providers are used. A paid provider requires explicit owner approval. Free tiers are never assumed to be unlimited.
5. Usage is tracked per workspace; quotas apply and the user sees a clear message when a limit is reached.
6. AI may only use data from the **current** workspace and only data the requesting user is allowed to see.
7. AI output is marked as AI-generated and can be wrong; users review before relying on it.
8. Content from customers, social platforms or files is **untrusted**. It cannot instruct AI to perform actions or change permissions.
9. AI cannot change roles, permissions, connections, secrets or billing settings.
10. Prompts and responses are not logged in full by default; if logged for debugging, they are protected and time-limited.

### 14.1 Human Approval Requirements

| Risk | Examples | Requirement |
|---|---|---|
| **Low** | Drafts, summaries, read-only answers | No approval |
| **Medium** | Creating/editing CRM records, tasks, schedules | Approval according to workspace setting |
| **High** | Publishing posts, sending messages to customers, creating/changing/deleting financial data, deleting data, changing roles or connections | **Always** explicit human approval by an authorized role |

- Every proposed action shows a plain-language summary of exactly what will happen.
- Proposals expire after a set time and cannot be approved after expiry.
- Approvals are limited to authorized roles (see Section 8).
- Proposals, decisions and results are recorded in the audit log.

---

## 15. Acceptance Criteria for MVP (Release 1) Features

A feature counts as **Done** only when its criteria pass in automated tests or documented manual tests, with evidence from a successful build.

### 15.1 Account and Security
- **AC-ACC-01:** A new user can register, sign in and sign out; invalid input is rejected with a clear message.
- **AC-ACC-02:** After sign-out or remote sign-out, the old session token is rejected by the server on the next request.
- **AC-ACC-03:** Access tokens expire within the configured short lifetime and refresh tokens rotate (verified by automated test).
- **AC-ACC-04:** Sign-in rate limiting blocks repeated failed attempts (verified by test).
- **AC-ACC-05:** Audit log records sign-in, role change, connection change, approval and data-export events (one test per event type).

### 15.2 Workspace Isolation (cross-cutting, required for every module)
- **AC-ISO-01:** For every API endpoint that returns or changes business data, an automated test with a user from Workspace A attempting to access Workspace B data fails (access denied) in 100% of tested endpoints.
- **AC-ISO-02:** A user removed from a workspace loses access on the next request.
- **AC-ISO-03:** AI context for a request contains no records from any other workspace (verified by test with seeded data).
- **AC-ISO-04:** Files in R2 are not retrievable by users from another workspace.

### 15.3 Multi-Business Management
- **AC-BIZ-01:** A user can create a workspace and see it in the switcher in under 10 seconds on a normal connection.
- **AC-BIZ-02:** After switching workspaces, no data from the previous workspace remains on screen.
- **AC-BIZ-03:** An Owner/Admin can invite a member and assign a role; the invited member only sees what that role permits (verified by permission matrix tests).
- **AC-BIZ-04:** The last Owner of a workspace cannot be removed or demoted.

### 15.4 AI Command Center
- **AC-AI-01:** A text request returns a response or a clear error within the timeout; no request hangs indefinitely.
- **AC-AI-02:** Changing the AI provider configuration (adapter) requires no change in app code (verified by switching to a test adapter).
- **AC-AI-03:** When a quota or provider failure occurs, the user sees a friendly message and can retry.
- **AC-AI-04:** A Medium/High-risk proposed action is never executed before approval (verified by test).
- **AC-AI-05:** A user without the required role cannot approve a High-risk action.
- **AC-AI-06:** An expired proposal cannot be approved.
- **AC-AI-07:** Text containing instructions inside an incoming customer message does not cause any action to execute (prompt-injection test cases).

### 15.5 Social (Telegram)
- **AC-SOC-01:** A Telegram bot can be connected and disconnected; after disconnect, stored credentials are deleted.
- **AC-SOC-02:** Incoming messages appear in the correct workspace's inbox only.
- **AC-SOC-03:** Webhook requests with an invalid signature or secret are rejected.
- **AC-SOC-04:** A reply is sent only after human send or approval.

### 15.6 CRM
- **AC-CRM-01:** Create, edit, archive and search contacts work; a search across 1,000 contacts returns results within 1 second on the backend.
- **AC-CRM-02:** A follow-up task appears in the assignee's notifications when due.
- **AC-CRM-03:** Staff users can only edit contacts/tasks assigned to them (permission tests).

### 15.7 Finance
- **AC-FIN-01:** Amounts are stored as integer minor units with a currency code; tests confirm no rounding errors for sample values (including currencies with 0, 2 and 3 decimal places).
- **AC-FIN-02:** Totals in reports equal the sum of the underlying records in automated tests.
- **AC-FIN-03:** Editing or deleting a record creates an audit entry with the old and new values.
- **AC-FIN-04:** AI cannot create or change a financial record without an approved proposal.

### 15.8 Online Madrasa Management
- **AC-MAD-01:** Students, classes, teacher assignment and fee records can be created and edited.
- **AC-MAD-02:** Attendance recorded offline syncs after reconnect with no duplicate or lost entries in 100% of the test scenarios (including repeated sync and app restart).
- **AC-MAD-03:** A Staff user can access only assigned classes.
- **AC-MAD-04:** Attendance and fee summary reports match underlying records in tests.

### 15.9 AI Knowledge Base
- **AC-KB-01:** Notes can be created, edited and deleted; deleted notes are no longer used by AI.
- **AC-KB-02:** An AI answer that used knowledge-base entries lists the entries as sources.
- **AC-KB-03:** Knowledge-base content from Workspace A never appears in AI answers for Workspace B.

### 15.10 Notifications and Reports
- **AC-NOT-01:** Waiting approvals, new messages and due tasks produce in-app notifications.
- **AC-NOT-02:** Users can turn each push notification type on or off, and the setting is respected.
- **AC-NOT-03:** The summary report loads within 3 seconds for a workspace with the test dataset (1,000 contacts, 5,000 finance records).

### 15.11 Cross-Cutting (UI, Offline, Localization, Accessibility)
- **AC-UI-01:** Every screen implemented in the MVP has designed loading, empty and error states.
- **AC-OFF-01:** With no connection, previously viewed data is shown with a visible "offline / may be out of date" indicator.
- **AC-LOC-01:** Switching language changes all MVP screens with no untranslated hardcoded text found by an automated lint check.
- **AC-LOC-02:** Money, dates and numbers display correctly in at least two currencies and two locales.
- **AC-A11Y-01:** MVP screens pass an automated accessibility check (labels, touch targets, contrast) and a manual screen-reader review of the main flows.
- **AC-TEST-01:** The automated test suite runs in GitHub Actions on every change and the release build is blocked if tests fail.

---

## 16. Document Control

| Item | Value |
|---|---|
| Owner | Project owner |
| Status | Draft for owner review |
| Next review | End of Phase 1 |

Changes to this document should be recorded with a short note in the Git commit message. Major scope changes require owner approval and, if technical, an entry in the Decision Log in `docs/ARCHITECTURE.md`.