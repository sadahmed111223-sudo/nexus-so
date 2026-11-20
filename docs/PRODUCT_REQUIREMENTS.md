# NEXUS AI — Product Requirements

**Status:** Phase 1 (planning), revision 2. This document describes what NEXUS AI **will** do. No feature described here is implemented yet. Every item is **Planned** until it is built, tested and verified.

**Related documents:** `docs/ARCHITECTURE.md` (technical design). If this document and the architecture ever disagree, resolve it through the Decision Log in the architecture document before building. Known follow-up edits to the architecture document are listed in Section 16.

**How to read this document**
- `FR-` = functional requirement, `NFR-` = non-functional requirement, `AC-` = acceptance criterion.
- **Must** = required for the release it is labeled with; the release cannot ship without it.
- **Should** = targeted for that release but not launch-blocking; if it slips, it moves to the next incremental update.
- **Could** = optional.
- Release labels: **R1** (Launch), **R1.1** (first incremental updates after launch), **R2**, **R3+**. See Section 6.

---

## 1. Product Vision

NEXUS AI is an Android platform that lets individuals and organizations run multiple businesses and educational institutions from one intelligent system: customers, finances, social channels, online madrasa operations and automated workflows, assisted by AI that proposes actions while humans approve the important ones.

**Long-term ambition:** become a trusted global technology brand for small and medium organizations.

The full vision and all ten modules remain the product direction. Delivery is split into small, verifiable releases (Section 6) so that the first release is achievable and secure.

## 2. Goals and Non-Goals

### 2.1 Goals
1. Give an owner one place to see and manage all of their businesses.
2. Reduce repetitive administrative work using AI assistance with human control.
3. Keep every business's data strictly separate and secure.
4. Work reliably on mid-range Android phones and unstable networks.
5. Operate within a free-first cost policy: no paid APIs or subscriptions without explicit owner approval.
6. Be usable in multiple languages and currencies from the start.

### 2.2 Non-Goals
See Section 11 (Out of Scope).

## 3. Target Users

| Persona | Description | Main needs |
|---|---|---|
| **Multi-business owner** | Runs several small businesses or an organization | One app for all businesses, quick overview of money and customers, delegation |
| **Business manager** | Manages daily operations in one or more workspaces | Customer follow-up, tasks, reports |
| **Madrasa administrator** | Runs an online madrasa or learning institution | Student records, classes, attendance, fees, communication |
| **Teacher / staff member** | Works inside one workspace with limited permissions | Simple screens for assigned work (for example attendance) |
| **Social media / marketing staff** | Prepares content and replies | Drafts, replies, approval workflow |
| **Read-only stakeholder** | Accountant, advisor or partner | View reports without changing data |

## 4. Core Problems

1. Owners juggle many apps (chat apps, spreadsheets, social pages) and lose track of customers and money.
2. Information about different businesses gets mixed up or is unavailable on the phone.
3. Customer messages are answered late or inconsistently.
4. Madrasa records (students, attendance, fees) are kept on paper or in scattered files.
5. AI tools are powerful but risky when they act without oversight, and many are costly.
6. Small organizations have unreliable internet and limited budgets.

## 5. Concepts and Terms

- **Account:** a person's sign-in identity.
- **Workspace:** one business or institution. All business data belongs to exactly one workspace.
- **Member:** an account that belongs to a workspace with a role.
- **Contact:** a customer, guardian or other external person stored in a workspace's CRM. Contacts are not NEXUS AI users.
- **Proposed action:** something AI wants to do, waiting for human review.
- **Approval:** a recorded human decision (approve or reject) on a proposed action.
- **Adapter:** a replaceable component that connects to an AI provider or social platform.
- **Release:** a delivered version of the app (R1, R1.1, ...). Not the same as a development *phase* (Phases 1–12 in `docs/ARCHITECTURE.md`).

---

## 6. Release Plan

The release grouping below is a **proposal for owner approval**. The 12 development phases in `docs/ARCHITECTURE.md` define the order of engineering work; releases define what users receive. See open decision 17 in Section 12 about how Phase 10 (Automation) relates to the first release.

### 6.1 Module delivery by release

| Module | R1 (Launch) | R1.1 (first updates) | R2 | R3+ |
|---|---|---|---|---|
| 1. AI Command Center | Text assistant, drafts, small set of proposed-action types with approval, usage limits | Conversation history | Broader actions across modules | Voice input, proactive suggestions |
| 2. Multi-Business Management | Create/switch workspaces, invitations, roles, dashboard for the active workspace | Workspace profile | Cross-business overview | Templates per business type |
| 3. Social Media Manager | Telegram bot connection (text messages in private chats): inbox, human replies, AI-drafted replies | Fixes and hardening from real use | Facebook Pages publishing and inbox; Telegram media and groups (if approved) | Instagram, WhatsApp, website chat widget |
| 4. CRM | Contacts, tags, pipeline stages, follow-up tasks, search, link a Telegram chat to a contact | Activity timeline | Import/export, segments | Automated follow-up sequences |
| 5. Finance and Analytics | Income/expense records, categories, amounts with currency codes, totals per currency | Manual exchange rates, base-currency totals | Invoices, budgets, recurring entries | Advanced analytics, forecasts |
| 6. Online Madrasa Management | Students, classes, teachers, attendance, fee records | Attendance and fee summaries | Exams/results, announcements to guardians | Guardian/student portal (pending decision) |
| 7. Workflow Automation | — | — | Simple rule-based automations with approval steps | Advanced multi-step workflows |
| 8. AI Knowledge Base | Text notes per workspace, AI answers with sources | — | File upload (R2 storage) | Website content import |
| 9. Notifications and Reports | In-app notifications | Push notifications, basic summary report | Scheduled reports, PDF/CSV export | Custom reports |
| 10. Account and Security | Sign-in, sign-out and session invalidation, roles, audit log, in-app data export and deletion requests | Active-session list with remote sign-out | Two-step verification | Advanced admin controls |
| Cross-cutting | Localization, accessibility, error/empty/loading states, automated tests | — | — | — |
| Cross-cutting (Should) | Cached offline reading; offline attendance entry with later sync | — | — | — |

### 6.2 Essential launch requirements (R1)

Release 1 may be published only when **all** of the following are true:
1. Every requirement labeled **Must, R1** in Section 7 is built and meets its acceptance criteria in Section 15.
2. Workspace isolation tests (AC-ISO-*) pass for every endpoint that handles business data.
3. Security requirements (NFR-SEC-*) and privacy rules (Section 9) that apply to R1 features are met.
4. The automated test suite runs in GitHub Actions and passes, and a release build is produced successfully.
5. Required store and legal documents (privacy policy, terms, account deletion path) are ready (Phase 12).
6. The release has been checked with a small group of real test users before public release.

**Should, R1** items are good to have at launch but do **not** block it. Anything not finished moves to R1.1.

### 6.3 Delivered by later incremental updates

R1.1, R2 and R3+ items in the table above are intentionally **not** launch requirements. Each later release gets its own acceptance criteria before its development begins. Removing a feature from R1 never removes the security, isolation and data protection rules that apply to it.

---

## 7. Functional Requirements by Module

### 7.1 AI Command Center
- **FR-AI-01 (Must, R1):** Users can type a request in natural language within the current workspace (for example "summarize my notes about this customer").
- **FR-AI-02 (Must, R1):** AI responses are generated only through the backend AI Gateway, never directly from the app.
- **FR-AI-03 (Must, R1):** AI can create **proposed actions** from a small initial set of action types (defined in Phase 5; for example create a CRM task, draft a reply, draft a finance entry). Each proposed action shows a clear summary with approve/reject buttons.
- **FR-AI-04 (Must, R1):** AI uses only data from the user's current workspace and only data the user is permitted to see.
- **FR-AI-05 (Must, R1):** Show clear messages when AI is unavailable or a usage limit is reached.
- **FR-AI-06 (Must, R1):** AI-generated content is visibly marked as AI-generated.
- **FR-AI-09 (Must, R1):** AI usage is counted per workspace and quotas are enforced.
- **FR-AI-07 (Should, R1.1):** Conversation history per user per workspace, deletable by the user. In R1, conversations exist only for the current session and are not stored long term.
- **FR-AI-08 (Could, R3+):** Voice input.

### 7.2 Multi-Business Management
- **FR-BIZ-01 (Must, R1):** A user can create a workspace with name, type, country, base currency, time zone and default language.
- **FR-BIZ-02 (Must, R1):** A user can belong to multiple workspaces and switch between them; the active workspace is always visible.
- **FR-BIZ-03 (Must, R1):** Owners and Admins can invite members by a secure invitation and assign roles.
- **FR-BIZ-04 (Must, R1):** Owners and Admins can change a member's role or remove a member; removed members lose access immediately. Only an Owner can assign or change the Owner role.
- **FR-BIZ-07 (Should, R1):** A dashboard for the active workspace showing waiting approvals, due tasks and shortcuts to modules.
- **FR-BIZ-05 (Should, R1.1):** Workspace profile (logo, contact info, business details).
- **FR-BIZ-06 (Should, R2):** Overview across all workspaces the user can access, without mixing data inside any workspace's records.

### 7.3 Social Media Manager

#### 7.3.1 Telegram in R1: what it is and what it is not

The R1 Telegram integration uses the **official Telegram Bot API**. A workspace connects its own Telegram **bot** (created by the owner through Telegram's official bot-creation process, which provides a bot token). The integration works with **conversations with that bot only**.

What it can do:
- Receive messages that people send to the connected bot.
- Reply in the same chats where people have messaged the bot.

What it cannot do (limitations that the product must state clearly to users):
1. It **cannot** access an owner's or anyone's personal Telegram account, personal inbox, contacts or other chats. It only sees what is sent to the bot.
2. It **cannot** start a conversation with a person who has not first messaged (started) the bot.
3. It **cannot** see messages that were sent before the bot was connected or outside the bot's chats.
4. A person can block the bot; sending to that chat then fails and the failure must be shown.
5. Replies appear as coming from the **bot**, not from the owner's personal Telegram account.
6. Bot conversations are not end-to-end encrypted. Customers should be advised not to send highly sensitive information through the bot.
7. Telegram enforces rate limits and file-size limits and may change its API or terms; exact values must be checked in current official documentation during Phase 8.
8. In R1, only **text messages in private chats** are supported. Group chats, channels, photos, voice, files and other media are not supported; such messages appear as an "unsupported message type" placeholder. (Groups also have bot privacy rules that limit which messages a bot can see; this is considered for R2.)

#### 7.3.2 Requirements
- **FR-SOC-01 (Must, R1):** A workspace can connect one Telegram bot using the official Bot API. The bot token is entered by an Owner/Admin, sent over HTTPS, stored only on the server and encrypted.
- **FR-SOC-02 (Must, R1):** Text messages sent to the connected bot (delivered by Telegram to the backend through a webhook, proposed) appear in that workspace's inbox only. The workspace for an incoming message is determined from the server-side connection record, never from message content.
- **FR-SOC-03 (Must, R1):** Users reply to a chat from the inbox. Human-written replies are sent by the human; AI-drafted replies are sent only after the user reviews and sends or approves them. There are **no automatic AI replies** in R1.
- **FR-SOC-05 (Must, R1):** The connect screen and inbox state the limitations in Section 7.3.1 in plain language.
- **FR-SOC-06 (Must, R1):** Incoming Telegram webhook requests are accepted only if they carry the correct **webhook secret token** (Telegram's official `secret_token` mechanism, sent in the `X-Telegram-Bot-Api-Secret-Token` header) for that connection. This is shared-secret validation, not a cryptographic signature of the message body. Requests with a missing or wrong token are rejected and not processed.
- **FR-SOC-07 (Must, all releases):** Incoming external messages are **untrusted input**; they can never trigger actions by themselves.
- **FR-SOC-08 (Must, R1):** Users can disconnect Telegram. On disconnect the stored bot token and webhook secret are deleted and the webhook registration is removed.
- **FR-SOC-09 (Must, R1):** Duplicate deliveries of the same Telegram update are processed only once.
- **FR-SOC-10 (Must, R1):** Failed sends (for example blocked bot, rate limit, network error) show a clear status and can be retried without creating duplicate messages.
- **FR-SOC-04 (Should, R1):** AI can draft a reply to a Telegram message for the user to review.
- **FR-SOC-11 (Must, R2):** Connect Facebook Pages; publish and schedule posts after approval; read comments and messages (subject to Meta's requirements; see Section 12).
- **FR-SOC-12 (Could, R2):** Telegram media and group chats.
- **FR-SOC-13 (Could, R3+):** Instagram, WhatsApp and a website chat widget through official APIs.

### 7.4 CRM
- **FR-CRM-01 (Must, R1):** Create, view, edit and archive contacts (name, phone, email, tags, notes, source).
- **FR-CRM-02 (Must, R1):** Pipeline stages per workspace (customizable names).
- **FR-CRM-03 (Must, R1):** Follow-up tasks with due date and assignee.
- **FR-CRM-04 (Should, R1):** Search and filter by name, tag and stage.
- **FR-CRM-08 (Should, R1):** Link a Telegram chat to a contact (select an existing contact or create one from the Telegram display name).
- **FR-CRM-05 (Should, R1.1):** Activity timeline per contact (notes, messages, tasks).
- **FR-CRM-06 (Should, R2):** Import and export contacts (CSV).
- **FR-CRM-07 (Must, all releases):** Deleting a contact follows the privacy and deletion rules in Section 9.

### 7.5 Finance and Analytics
- **FR-FIN-01 (Must, R1):** Record income and expenses with amount, currency, category, date and note.
- **FR-FIN-02 (Must, R1):** Money is stored in minor units with an ISO 4217 currency code; each workspace has a base currency.
- **FR-FIN-03 (Must, R1):** Editing or deleting a financial record is audit-logged (who, when, old and new values).
- **FR-FIN-04 (Must, R1):** Basic reports: totals by period, by category, and income vs expense, calculated **per currency** (no currency conversion in R1).
- **FR-FIN-05 (Should, R1.1):** Manual entry of exchange rates (with date) and totals in the workspace base currency.
- **FR-FIN-06 (Should, R2):** Invoices, budgets, recurring entries.
- **FR-FIN-07 (Must, all releases):** AI may suggest or draft financial entries, but creating, changing or deleting financial data through AI always requires explicit human approval.
- **FR-FIN-08 (Out of scope):** NEXUS AI does not process payments or hold funds (Section 11).

### 7.6 Online Madrasa Management
- **FR-MAD-01 (Must, R1):** Manage students (name, guardian contact, enrollment date, status, notes).
- **FR-MAD-02 (Must, R1):** Manage classes/courses and assign teachers and students.
- **FR-MAD-03 (Must, R1):** Record attendance per class session (with an internet connection).
- **FR-MAD-04 (Must, R1):** Record fees due and payments received (records only, not payment processing).
- **FR-MAD-09 (Should, R1):** Offline attendance entry that syncs later (see Section 13.4).
- **FR-MAD-05 (Should, R1.1):** Attendance and fee summary reports.
- **FR-MAD-06 (Should, R2):** Exams and results; announcements to guardians through connected channels after approval.
- **FR-MAD-07 (Could, R3+):** Guardian/student portal (depends on the open decision about external user types).
- **FR-MAD-08 (Must, all releases):** Student data is treated as sensitive data about minors and follows Section 9.

### 7.7 Workflow Automation (R2)
- **FR-AUT-01 (Must, R2):** Rule-based automations: trigger → condition → action, created and enabled by Owners/Admins.
- **FR-AUT-02 (Must, R2):** Any High-risk step includes a mandatory human approval step.
- **FR-AUT-03 (Must, R2):** Automation runs are logged with outcome and errors.
- **FR-AUT-04 (Must, R2):** A visible control to pause or disable any automation instantly.
- **FR-AUT-05 (Must, R2):** Limits on run frequency to prevent loops and runaway usage.

### 7.8 AI Knowledge Base
- **FR-KB-01 (Must, R1):** Add, edit and delete text notes/articles per workspace.
- **FR-KB-02 (Must, R1):** AI answers can use the workspace knowledge base and show which entries were used as sources.
- **FR-KB-03 (Must, R1):** Knowledge base content is never used for another workspace.
- **FR-KB-04 (Should, R2):** File uploads stored in R2 under a workspace-specific prefix.

### 7.9 Notifications and Reports
- **FR-NOT-01 (Must, R1):** In-app notification list (new messages, approvals waiting, due tasks).
- **FR-NOT-02 (Should, R1.1):** Push notifications, with user-controlled settings per type. The delivery mechanism is an open decision (Section 12).
- **FR-NOT-05 (Must, R1.1, applies when push ships):** Notifications do not show sensitive details on the lock screen unless the user allows it.
- **FR-NOT-03 (Should, R1.1):** A basic summary report (customers, money, attendance) viewable in the app.
- **FR-NOT-04 (Should, R2):** Scheduled reports and export (PDF/CSV).

### 7.10 Account and Security Management
- **FR-ACC-01 (Must, R1):** Account registration and sign-in (methods to be confirmed; see Section 12).
- **FR-ACC-02 (Must, R1):** Secure session management: sign-out invalidates the session on the server.
- **FR-ACC-03 (Must, R1):** Role-based access enforced on the server for every request.
- **FR-ACC-04 (Must, R1):** Audit log of sensitive events, viewable by Owner/Admin.
- **FR-ACC-05 (Must, R1):** Users can request, inside the app, export of their personal account data and deletion of their account (Section 9). In R1 these requests may be fulfilled through a documented process rather than fully automated.
- **FR-ACC-07 (Should, R1.1):** List of active sessions with remote sign-out.
- **FR-ACC-06 (Should, R2):** Two-step verification.

---

## 8. User Roles and Permissions

Roles follow `docs/ARCHITECTURE.md`: Owner, Admin, Manager, Staff, Viewer. Permissions are checked on the **server**. The table is the initial proposal.

Legend: **F** = full (create, edit, delete), **E** = create/edit within own or assigned items, **R** = read only, **A** = may approve, **—** = no access.

| Capability | Owner | Admin | Manager | Staff | Viewer |
|---|---|---|---|---|---|
| Delete workspace, transfer ownership | F | — | — | — | — |
| Manage members and roles (only Owner changes Owners) | F | F | — | — | — |
| Workspace settings | F | F | R | — | — |
| Connect/disconnect social channels | F | F | — | — | — |
| Social inbox and replies | F | F | F | E | — |
| Approve High-risk AI actions | A | A | Configurable (default: no) | — | — |
| Approve Medium-risk AI actions (only for actions within the approver's own permissions) | A | A | A | A | — |
| Use AI Command Center | F | F | F | E | R (read-only answers; no proposals) |
| CRM contacts and tasks | F | F | F | E | R |
| Finance records | F | F | F | — (default, configurable) | R |
| Finance reports | F | F | R | — | R |
| Madrasa students and classes | F | F | F | R (assigned classes) | R |
| Madrasa attendance | F | F | F | E (assigned classes) | R |
| Madrasa fees | F | F | F | — | R |
| Knowledge base | F | F | F | E | R |
| Automations (R2) | F | F | — | — | — |
| View audit log | R | R | — | — | — |
| Export workspace data | F | — | — | — | — |

Rules:
- Permissions apply **per workspace**. Being an Owner in one workspace gives no access to another.
- At least one Owner must exist per workspace.
- "Assigned" means assigned to that user (for example a class, a contact or a task).
- Finer permissions (for example "teacher can only take attendance") may be added later without changing the five base roles.
- Whether Staff may view finance records is configurable per workspace (default: no).
- Exporting **workspace** data is an Owner action. Exporting a user's own **account** data is available to that user (FR-ACC-05).

---

## 9. Privacy and Data Rules

- **Data minimization:** collect only data needed for a feature.
- **Isolation:** every business-data record belongs to exactly one workspace and is never visible outside it (see `docs/ARCHITECTURE.md`, section 6).
- **Sensitive data:** student records (including minors), contact details, financial records, customer messages and connected-account tokens are sensitive. Tokens and secrets are encrypted before storage.
- **External contacts:** people who message a connected bot are not NEXUS AI users. Store only what is needed (for example chat identifier, display name and message content). Workspace owners are responsible for informing their customers about how messages are handled; the app provides guidance text. Deleting a contact also removes or anonymizes their stored messages according to the deletion process.
- **Export and deletion:** users can request an export of their personal data and deletion of their account. Account and workspace deletion follows a documented process; some records may be kept for a limited time where law or audit needs require it. Retention periods are an open decision.
- **Logs:** logs and audit entries must not contain passwords, tokens or full message contents unless required and protected.
- **Third parties:** data sent to AI providers or social platforms is limited to what the feature needs. Provider terms on data retention and training must be reviewed before a provider is enabled.
- **Legal compliance:** applicable laws (for example data protection laws where users live) must be reviewed with qualified legal advice before launch. This document is not legal advice.
- **Privacy policy and terms:** required before Google Play release (Phase 12), together with any account-deletion requirements of the store.

---

## 10. Main User Journeys

### 10.1 First-time setup (R1)
1. User installs the app and creates an account.
2. User creates the first workspace (name, type, country, currency, language).
3. App shows a dashboard with clear next-step suggestions and designed empty states.
4. User optionally invites members.

### 10.2 Switching between businesses (R1)
1. User opens the workspace switcher.
2. User selects another workspace.
3. All screens show only that workspace's data; the workspace name stays visible.

### 10.3 Handling a customer message through the Telegram bot (R1)
1. The Owner/Admin connects the workspace's Telegram bot and reads the limitations shown on screen.
2. A customer sends a text message to the bot (the customer must start the chat first).
3. The message appears in that workspace's inbox and an in-app notification is created.
4. The user links the chat to a CRM contact (existing or new).
5. The user writes a reply, or asks AI for a draft.
6. The user reviews and sends the reply. The send result (success or failure) is shown.
7. Sensitive events (connection, disconnect) are recorded in the audit log.

### 10.4 Approving a sensitive AI action (R1)
1. The user asks AI for something that changes data or contacts customers.
2. AI creates a proposed action with a plain-language summary.
3. A user with a sufficient role reviews and approves or rejects it.
4. On approval the server executes the action; on rejection nothing happens.
5. The proposal, decision, executor and result are audit-logged. Unanswered proposals expire.

### 10.5 Taking madrasa attendance (R1; offline entry is Should, R1)
1. A teacher opens the class and marks attendance.
2. With a connection, the record is saved immediately.
3. If offline entry is available and there is no connection, the app shows that changes are waiting to sync and syncs them automatically later. If changes cannot be merged safely, the user is asked.

### 10.6 Recording and reviewing finances (R1)
1. User records an income or expense with an amount and currency.
2. User opens reports and sees totals by period and category, per currency.

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
- Access to a person's personal Telegram, WhatsApp or other personal accounts or inboxes.
- White-labeling and on-premise hosting.
- Guardian/student portals in R1.

---

## 12. Unresolved Decisions

| # | Question | Needed by |
|---|---|---|
| 1 | Final list of supported languages (proposed start: Bengali, English; possible Arabic with right-to-left) and currencies | Phase 3 |
| 2 | Sign-in methods (email/password only, or also Google sign-in) and password hashing method | Phase 4 |
| 3 | Which free AI providers to support first, based on current free-tier limits | Phase 5 |
| 4 | Confirm that R1 social support is Telegram bot only, and that each workspace connects its own bot; order of Facebook, Instagram, WhatsApp afterwards | Phase 8 |
| 5 | Meta app review, business verification and WhatsApp messaging costs: approval to proceed | Phase 8 |
| 6 | Whether Telegram groups and media are added in R2 | Before R2 |
| 7 | Whether madrasa guardians/students need their own logins (external user types) | Phase 9 |
| 8 | Whether Staff may view finance records by default (proposed: no) | Phase 7 |
| 9 | Default approval level for customer replies and Medium-risk actions; whether a requester may approve their own High-risk proposal | Phase 5 |
| 10 | Data retention periods and deletion process | Phase 11 |
| 11 | Minimum supported Android version | Phase 2 |
| 12 | Business model for the product itself (free, paid plans). Requires owner approval; no paid services are added without it | Before Phase 12 |
| 13 | Backup storage location and schedule | Phase 11 |
| 14 | Data location and legal requirements per target country | Before Phase 12 |
| 15 | Final brand and name availability checks | Before Phase 12 |
| 16 | Push notification delivery mechanism (free-first review required) | Before R1.1 |
| 17 | Whether R1 is published after Phases 2–9, 11 and 12 with Phase 10 (Automation) delivered as an update, which needs a note in the phase plan in `docs/ARCHITECTURE.md` | Before Phase 10 |

---

## 13. Non-Functional Requirements

Numeric targets below are **initial targets** and must be validated by measurement on real mid-range devices and the real backend.

### 13.1 Security
- **NFR-SEC-01:** All traffic over HTTPS; no secrets in the app or in the repository.
- **NFR-SEC-02:** Every business-data table includes `workspace_id`; all queries go through the single data-access layer that applies it.
- **NFR-SEC-03:** Role and workspace membership are verified on the server for every request.
- **NFR-SEC-04:** Passwords stored only as strong salted hashes; access tokens are short-lived; refresh tokens rotate.
- **NFR-SEC-05:** Sensitive fields (for example connected-account tokens and webhook secrets) are encrypted before storage.
- **NFR-SEC-06:** All input is validated on the server; invalid input is rejected with a clear error.
- **NFR-SEC-07:** Audit logs are append-only and cover sign-in, role changes, connections, approvals, financial changes and data export/deletion requests.
- **NFR-SEC-08:** Rate limiting on sign-in, AI and messaging endpoints.
- **NFR-SEC-09:** Incoming webhooks are verified **before processing, using the mechanism each platform officially supports** (Telegram: webhook secret token; Meta platforms: their signed-request mechanism, to be confirmed in Phase 8). Webhook verification never replaces workspace isolation checks.

### 13.2 Privacy
- **NFR-PRI-01:** Data export and deletion requests are handled through documented procedures (Section 9).

### 13.3 Performance (initial targets)
- **NFR-PERF-01:** Cold app start to usable dashboard within 3 seconds on a mid-range device (with cached data).
- **NFR-PERF-02:** 95% of ordinary (non-AI) API requests respond within 800 ms under normal load.
- **NFR-PERF-03:** Lists use pagination; no screen loads unbounded data.
- **NFR-PERF-04:** AI requests show progress feedback and time out with a clear message instead of hanging.

### 13.4 Reliability
- **NFR-REL-01:** Network calls use timeouts and safe retries; user actions are not lost on failure.
- **NFR-REL-02:** A shared error model with clear, friendly messages and a retry option.
- **NFR-REL-03:** Regular database exports (backup), a documented restore procedure, and a periodic restore test.
- **NFR-REL-04:** Crash and error reporting without personal data or secrets.
- **NFR-REL-05:** Where offline features ship (cached reading, offline attendance), they sync without duplicates or lost entries. Offline support covers only low-risk features; AI requests, approvals of High-risk actions and sending messages require a connection.

### 13.5 Accessibility
- **NFR-ACC-01:** Text scales with the system font size; layouts remain usable at large sizes.
- **NFR-ACC-02:** Color contrast meets WCAG 2.1 AA for text.
- **NFR-ACC-03:** Interactive elements have a minimum touch target of 48dp and screen-reader labels.
- **NFR-ACC-04:** Information is never conveyed by color alone.

### 13.6 Localization
- **NFR-LOC-01:** No hardcoded user-facing text; all text in string resources.
- **NFR-LOC-02:** Initial languages as decided in Section 12; layouts support right-to-left languages.
- **NFR-LOC-03:** Money in minor units with ISO 4217 codes; dates stored in UTC and displayed in the user's time zone and locale.

### 13.7 Scalability and Maintainability
- **NFR-SCA-01:** Start with a single backend and database with clean boundaries; add complexity only after a measured need.
- **NFR-SCA-02:** Indexes on frequently filtered columns, always including `workspace_id`.
- **NFR-SCA-03:** AI provider and platform integrations sit behind adapters so they can be replaced.
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
7. AI output is marked as AI-generated and can be wrong; users review it before relying on it.
8. Content from customers, social platforms or files is **untrusted**. It cannot instruct AI to perform actions or change permissions.
9. AI cannot change roles, permissions, connections, secrets or billing settings.
10. Prompts and responses are not logged in full by default; if logged for debugging, they are protected and time-limited.

### 14.1 Human Approval Requirements

| Risk | Examples | Requirement |
|---|---|---|
| **Low** | Drafts, summaries, read-only answers | No approval |
| **Medium** | Creating/editing CRM records, tasks | Approval according to workspace setting, by a user allowed to perform that action |
| **High** | Sending messages to customers on behalf of AI, publishing posts, creating/changing/deleting financial data, deleting data, changing roles or connections | **Always** explicit human approval by an authorized role (Section 8) |

- Every proposed action shows a plain-language summary of exactly what will happen.
- Proposals expire after a set time and cannot be approved after expiry.
- Proposals, decisions and results are recorded in the audit log.
- A reply a person writes and sends themselves is a human action, not an AI action. An AI-drafted reply counts as approved only when the user explicitly sends it.

---

## 15. Acceptance Criteria

A feature counts as **Done** only when its criteria pass in automated tests or documented manual tests, with evidence from a successful build. Criteria marked **(Should)** apply to Should items; the rest cover Must items. Criteria for R1.1, R2 and R3+ features are written before their development begins.

### 15.1 Account and Security (R1)
- **AC-ACC-01:** A new user can register, sign in and sign out; invalid input is rejected with a clear message.
- **AC-ACC-02:** After sign-out, the old session token is rejected by the server on the next request.
- **AC-ACC-03:** Access tokens expire within the configured short lifetime and refresh tokens rotate (automated test).
- **AC-ACC-04:** Repeated failed sign-in attempts are rate-limited (automated test).
- **AC-ACC-05:** The audit log records sign-in, role change, connection change, approval and data export/deletion request events (one test per event type).
- **AC-ACC-06:** A user can submit an account data export request and an account deletion request from inside the app, and each creates a tracked request record.

### 15.2 Workspace Isolation (cross-cutting, required for every module)
- **AC-ISO-01:** A maintained inventory lists every API endpoint that reads or changes business data. Each has an automated test where a user from Workspace A is denied access to Workspace B data. CI fails if an endpoint is missing from the inventory or lacks a test. Result required: 100% of inventoried endpoints pass.
- **AC-ISO-02:** A user removed from a workspace is denied access on the next request.
- **AC-ISO-03:** The context sent to AI for a request contains no records from any other workspace (test with seeded data in two workspaces).
- **AC-ISO-04:** Webhook-delivered data is stored only in the workspace that owns the connection record, regardless of message content.

### 15.3 Multi-Business Management (R1)
- **AC-BIZ-01:** A user can create a workspace and see it in the switcher within 10 seconds on a normal connection.
- **AC-BIZ-02:** After switching workspaces, no data from the previous workspace remains on screen or in cached lists.
- **AC-BIZ-03:** An Owner/Admin can invite a member and assign a role; the invited member can do only what the permission matrix allows (matrix-driven automated tests).
- **AC-BIZ-04:** The last Owner of a workspace cannot be removed or demoted; an Admin cannot change an Owner.
- **AC-BIZ-05 (Should):** The dashboard's waiting-approval and due-task counts match the test dataset.

### 15.4 AI Command Center (R1)
- **AC-AI-01:** A text request returns a response or a clear error within the configured timeout; no request hangs indefinitely.
- **AC-AI-02:** Switching to a different (test) provider adapter requires no change in app code.
- **AC-AI-03:** When a quota or provider failure occurs, the user sees a friendly message and can retry.
- **AC-AI-04:** A Medium or High-risk proposed action is never executed before approval (automated test).
- **AC-AI-05:** A user without the required role cannot approve a High-risk action.
- **AC-AI-06:** An expired proposal cannot be approved.
- **AC-AI-07:** Instructions inside an incoming customer message do not cause any action to execute (prompt-injection test cases).
- **AC-AI-08:** Per-workspace usage counters increase with each AI request, and requests are refused with a clear message once the configured quota is reached (automated test).
- **AC-AI-09:** AI-generated text is visibly labeled as AI-generated in the UI.

### 15.5 Social: Telegram Bot (R1)
- **AC-SOC-01:** A bot can be connected and disconnected. After disconnect, the stored bot token and webhook secret no longer exist, and the webhook registration is removed.
- **AC-SOC-02:** Incoming text messages appear only in the inbox of the workspace that owns the connection.
- **AC-SOC-03:** A webhook request is processed only if the `X-Telegram-Bot-Api-Secret-Token` header matches the secret token configured for that connection through Telegram's official `secret_token` option. Automated tests cover a missing token, a wrong token and a correct token; missing or wrong tokens are rejected and no data is stored. The comparison is constant-time. This is shared-secret validation, not signature verification of the request body.
- **AC-SOC-04:** A reply is sent only after a human sends it or reviews and sends an AI-drafted reply; no automatic AI replies exist.
- **AC-SOC-05:** The connect screen and inbox display the bot limitations from Section 7.3.1.
- **AC-SOC-06:** A message that is not plain text, or comes from a group chat, is shown as an unsupported placeholder (or ignored) without errors or crashes.
- **AC-SOC-07:** A failed send shows a clear status; retrying does not create a duplicate message.
- **AC-SOC-08:** Delivering the same Telegram update twice results in one stored message.
- **AC-SOC-09 (Should):** An AI-drafted reply is created for a selected message, and the draft is never sent without user action.

### 15.6 CRM (R1)
- **AC-CRM-01:** Create, edit and archive contacts work; the follow-up task and pipeline stage features work as specified.
- **AC-CRM-02:** A follow-up task creates an in-app notification for the assignee when due.
- **AC-CRM-03:** Staff users can edit only contacts and tasks assigned to them (permission tests).
- **AC-CRM-04 (Should):** A search across 1,000 test contacts returns results within 1 second on the backend.
- **AC-CRM-05 (Should):** A Telegram chat can be linked to an existing or new contact.

### 15.7 Finance (R1)
- **AC-FIN-01:** Amounts are stored as integer minor units with a currency code; tests confirm correct values for currencies with 0, 2 and 3 decimal places.
- **AC-FIN-02:** Report totals equal the sum of the underlying records in automated tests, per currency.
- **AC-FIN-03:** Editing or deleting a record creates an audit entry with old and new values.
- **AC-FIN-04:** AI cannot create or change a financial record without an approved proposal.

### 15.8 Online Madrasa Management (R1)
- **AC-MAD-01:** Students, classes, teacher assignment and fee records can be created and edited.
- **AC-MAD-02:** Attendance can be recorded and reviewed per class session.
- **AC-MAD-03:** A Staff user can access only assigned classes and cannot view fee records.
- **AC-MAD-04 (Should):** Attendance recorded offline syncs after reconnect with no duplicate or lost entries in all defined test scenarios (repeated sync, app restart, connection loss during sync).

### 15.9 AI Knowledge Base (R1)
- **AC-KB-01:** Notes can be created, edited and deleted; deleted notes are no longer used by AI.
- **AC-KB-02:** An AI answer that used knowledge-base entries lists those entries as sources.
- **AC-KB-03:** Knowledge-base content from Workspace A never appears in AI answers for Workspace B.

### 15.10 Notifications (R1)
- **AC-NOT-01:** Waiting approvals, new inbox messages and due tasks produce in-app notifications for the right users only.

### 15.11 Cross-Cutting (R1)
- **AC-UI-01:** Every screen in R1 has designed loading, empty and error states.
- **AC-LOC-01:** Switching language changes all R1 screens, and an automated lint check finds no hardcoded user-facing text.
- **AC-LOC-02:** Money, dates and numbers display correctly in at least two currencies and two locales.
- **AC-A11Y-01:** R1 screens pass an automated accessibility check (labels, touch targets, contrast) and a manual screen-reader review of the main flows.
- **AC-TEST-01:** The automated test suite runs in GitHub Actions on every change and blocks the release build when tests fail.
- **AC-OFF-01 (Should):** With no connection, previously viewed data is shown with a visible "offline / may be out of date" indicator.

---

## 16. Document Control

| Item | Value |
|---|---|
| Owner | Project owner |
| Status | Draft for owner review (revision 2) |
| Next review | End of Phase 1 |

**Follow-up edits needed in `docs/ARCHITECTURE.md` to stay consistent with this document**
1. Section 13 (Integrations): replace "Webhooks are verified (signature checks) before processing" with wording that each webhook is verified using the mechanism the platform officially supports (for example Telegram's webhook secret token; signed requests for Meta platforms).
2. Section 14 (Development Phases): add a note that releases (R1, R1.1, R2, R3+) are delivery milestones, and record the outcome of open decision 17.

Changes to this document should be recorded with a short note in the Git commit message. Major scope changes require owner approval and, if technical, an entry in the Decision Log in `docs/ARCHITECTURE.md`.