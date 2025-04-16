# 🧠 Exekutor

<img src="logo.png" height="200" alt="exekutor logo" style="float: none; ">

A lightweight, pluggable Kotlin engine for executing complex, interruptible
workflows — built with time budgets, fail-fast execution, and simple
extensibility in mind.

Ideal for:
- ✅ Fraud and credit risk flows
- ✅ Decision trees and scoring logic
- ✅ Compliance checks
- ✅ Any pipeline-style orchestration

---

## ✨ Features

- 🔌 Modular step architecture
- ⏱️ Time-aware execution context
- ❌ Fail-fast on errors or timeouts
- ⚠️ Friction detection support (e.g. challenge required)
- 🧩 Built-in DSL for step registration
- 📚 Auto-generated documentation with Dokka
- 🧪 Fully testable core

---

## 🚀 Getting Started

```kotlin
class FraudCheck : Step<RiskContext> {
    override val name = "FraudCheck"
    override suspend fun execute(context: RiskContext): StepResult =
        if (context.payload.contains("fraud")) StepResult(Outcome.FAIL)
        else StepResult(Outcome.PASS)
}

registerSteps {
    step { FraudCheck() }
    step { CreditCheck() }
}

val engine = StepExecutionEngine(planBuilder = defaultPlanBuilder())
val result = engine.run(RiskContext("user", 3000))
```

---

## 📚 Documentation

API docs (latest version) are available at:

👉 https://yonatankarp.github.io/exekutor

---

## 🛠 Tech Stack

- Kotlin 2.x
- JDK 21
- Gradle Kotlin DSL
- Dokka for documentation

---

## 📄 License
MIT © Yonatan Karp-Rudin
