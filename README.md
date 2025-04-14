# 🧩 exekutor 

**A Kotlin-first, pluggable execution pipeline engine with time budgeting and early exit logic.**

`exekutor` lets you compose and run a sequence of execution steps, each capable of failing fast, triggering friction (like MFA or additional checks), or continuing onward — all within a shared time budget.

Built with real-world systems in mind: fraud checks, credit scoring, compliance gating, dynamic pipelines, or anything requiring time-aware orchestration.

---

## 🚀 Features

- ⏱ **Time Budgeting** — each step gets a dynamic slice of remaining time
- 🛑 **Early Exit** — fail or pause execution on the first blocking condition
- 🧩 **Pluggable Design** — define your own steps, outcomes, and context
- 💬 **Friction Support** — support "soft fail" use cases (e.g., identity verification required)
- ✅ **Suspending Steps** — supports asynchronous/non-blocking execution using `suspend`

---

## 🧠 When to use `exekutor`

- Decision engines (fraud, credit, KYC, etc.)
- Dynamic business rule orchestration
- Multi-step validation flows
- Feature flag-based runtime logic
- Serverless workflows with strict execution time limits
