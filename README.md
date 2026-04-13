<p align="center">
	<img alt="DevAICode" src="/android/logo.webp" width="20%"/>
</p>

<h1 align="center">DevAICode-Android</h1>

<div align="center">

[![GitHub Profile](https://img.shields.io/badge/GitHub-afkT-orange.svg?style=for-the-badge)](https://github.com/afkT)
[![GitHub License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=for-the-badge)](https://github.com/afkT/DevAICode-Android/blob/main/LICENSE)
[![Maven](https://img.shields.io/badge/Maven-Dev-5776E0.svg?style=for-the-badge)](https://search.maven.org/search?q=io.github.afkt)

</div>


<p align="center">
	面向深度 AI 编程工作流的 Android 模板项目
	<br>
	以 <a href="https://github.com/afkT/DevUtils">DevUtils</a> 系列库为底座，内置模块化 AI 角色配置（AGENTS + AI_CONTEXT + 任务型 Prompts）
	<br>
	覆盖 30+ Android 开发场景，提供从研究、规划到实现的全流程 AI 辅助开发体验
</p>


## What is DevAICode-Android?

DevAICode-Android is an **AI-driven Android project template** designed for deep AI-assisted programming workflows. Instead of relying on generic AI prompts, it provides a structured system of **modular AI agents, project context, and 30+ task-specific prompts** that guide AI tools (like [Cursor](https://cursor.com)) to generate high-quality, architecturally consistent Android code.

**Key idea:** Tell the AI *who it is*, *what the project looks like*, and *how to do specific tasks* — so every generated line of code follows your architecture, conventions, and best practices.


## Features

- **Modular AI Role System** — AGENTS + AI_CONTEXT + task prompts with clear separation of concerns
- **30+ Task-Specific Prompts** — Covering the full spectrum of Android development scenarios
- **Code Generation Consistency** — Unified coding standards and architectural patterns ensure consistent AI output
- **Four-Phase Workflow** — Research → Planning → Annotation → Implementation, reducing rework
- **Context Management** — Layered loading: core rules for daily chat, task prompts on-demand via `@` reference
- **Built-in Quality Gates** — Testing, security, performance checks + behavior rules that AI follows automatically
- **Extensible Architecture** — Easy to add new prompts, rules, and skills as the project evolves
- **AI IDE Integration** — Optimized for Cursor; compatible with other AI-powered IDEs


## How It Works

### Context Loading Strategy

| File | Role | Loaded |
|------|------|--------|
| `AGENTS.md` | AI identity, goals, coding rules | Auto |
| `AI_CONTEXT.md` | Project architecture & tech stack | Auto |
| `prompts/style_guidance.md` | Code style & best practices | Auto |
| `prompts/code_conventions.md` | Naming & structure conventions | Auto |
| `prompts/*.md` (others) | Task-specific guidance | On-demand via `@` |

### Using Task Prompts

When working on a specific task, reference the relevant prompt in your AI conversation:

| Task | Prompt |
|------|--------|
| Add navigation / page routing | `@ai/prompts/navigation.md` |
| Database operations | `@ai/prompts/room_schema.md` |
| List with pagination | `@ai/prompts/paging.md` |
| Network API layer | `@ai/prompts/retrofit_api.md` |
| Lifecycle & coroutine scopes | `@ai/prompts/lifecycle.md` |
| New screen with DataBinding | `@ai/prompts/screen_databinding.md` |
| Write tests | `@ai/prompts/tests.md` |
| Performance optimization | `@ai/prompts/performance.md` |

### AI Development Workflow

| Phase | Purpose | Output |
|-------|---------|--------|
| **Research** | Understand the codebase before designing | `research.md` |
| **Planning** | Turn research into a reviewable plan | `plan.md` |
| **Annotation** | Review, iterate, and approve the plan | Updated `plan.md` + task list |
| **Implementation** | Execute the approved plan | Working code |

## Tech Stack

| Category | Technology |
|----------|-----------|
| Language | Kotlin 2.3 |
| UI | XML Layouts + DataBinding (View-based, no Compose) |
| Architecture | MVVM + Repository + Use Cases |
| DI | Hilt |
| Async | Coroutines + Flow + StateFlow + LiveData |
| Navigation | Navigation Component + Safe Args |
| Persistence | Room |
| Networking | Retrofit |
| Base Libraries | [DevUtils](https://github.com/afkT/DevUtils) Series |


## Related Projects

- [DevUtils](https://github.com/afkT/DevUtils) — Android / Java utility library with 300+ tools


---

<h2 id="中文说明">中文说明</h2>

### 这是什么？

DevAICode-Android 是一个**面向深度 AI 编程工作流的 Android 模板项目**。它不依赖泛泛的 AI 提示词，而是提供一套结构化的**模块化 AI 角色配置、项目上下文和 30+ 任务型 Prompts**，引导 AI 工具（如 [Cursor](https://cursor.com)）生成高质量、架构一致的 Android 代码。

### 核心特性

- **模块化 AI 角色配置**：AGENTS + AI_CONTEXT + 任务型 Prompts，职责清晰
- **30+ 任务型 Prompts**：覆盖 Android 开发全场景，按需 `@` 引用避免上下文过载
- **代码生成一致性**：统一编码规范与架构模式，确保 AI 生成代码风格一致
- **四阶段工作流**：研究 → 规划 → 批注 → 实现，书面 research/plan + 批注把关，减少返工
- **上下文管理**：分层加载，日常对话仅加载核心规则，任务时按需 `@` 扩展
- **质量保证机制**：内置测试、安全、性能等检查项 + 行为规范，AI 自动遵循最佳实践
- **可扩展架构**：易于新增 Prompts、Rules、Skills，适应项目演进
- **AI 工具集成**：为 Cursor 优化，兼容其他 AI IDE


## License

    Copyright 2026 afkT

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

