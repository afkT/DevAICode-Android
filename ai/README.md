# AI Agents - Android Architect

基于 `Android Architect` 拆分的模块化 AI 角色配置，采用 **AGENTS + AI_CONTEXT + 任务型 prompts** 结构。

## 特性

- ✅ **模块化 AI 角色配置**：AGENTS + AI_CONTEXT + 任务型 prompts 结构，职责清晰
- ✅ **任务型 prompts**：覆盖 Android 开发全场景，按需 @ 引用避免上下文过载
- ✅ **代码生成一致性**：统一编码规范与架构模式，确保 AI 生成代码风格一致
- ✅ **可扩展架构**：易于新增 prompts、Rules、Skills，适应项目演进
- ✅ **AI 工具集成**：支持 Cursor 等 AI IDE，默认 Agent 规则 + Rules/Skills 按场景自动或 @ 引用
- ✅ **上下文管理**：分层加载，日常对话仅加载核心规则，任务时按需 @ 扩展
- ✅ **质量保证机制**：内置测试、安全、性能等检查项 + 行为规范与复用优先规则，AI 自动遵循最佳实践
- ✅ **四阶段工作流指南**：研究 → 规划 → 批注 → 实现，书面 research/plan + 批注把关，减少返工
- ✅ **持续优化能力**：基于使用反馈与改进报告，持续迭代 prompts 与规则

## 入口与上下文：AGENTS 与 AI_CONTEXT

让 AI 知道「自己是谁、要做什么、默认必须遵守哪些编码与协作规则、执行任务时按什么流程」，具体规范细节由引用的 prompt 提供。

| 文件 | 职责 | 作用 | 与 prompts 的关系 |
|------|------|------|-------------------|
| **AGENTS.md** | 定义 AI 角色、执行目标与默认规则 | 作为主入口，约定「谁、做什么、默认遵守什么」；只保留契约级内容，具体风格与格式由引用的 prompt 承载 | 仅引用 `AI_CONTEXT.md`、`style_guidance.md`、`code_conventions.md`；不展开任务细节。任务级「怎么做」在 `@ai/prompts/*`，用户按需 @ 引用 |
| **AI_CONTEXT.md** | 描述项目技术栈、架构分层、数据与边界和 AI 行为约定 | 回答「项目是什么、用什么、怎样架构」；不写任务级步骤，保持简短以作常驻上下文 | 只描述「是什么」「怎样架构」；各任务的具体做法由对应 task prompt 负责 |

- **入口（AGENTS + AI_CONTEXT）**：负责「角色 + 项目架构 + 默认规范」；保持简短，控制上下文长度与命中率。
- **任务型 prompts**：负责「某类任务的具体步骤与约定」；按需 @ 引用，避免一次性加载过多上下文。

## Prompts 结构说明

| 文件 | 用途 | 默认加载 |
|------|------|----------|
| `AGENTS.md` | 主入口：角色、目标、编码规则、引用 | ✓ |
| `AI_CONTEXT.md` | 项目上下文：架构、技术栈、模块划分 | ✓ |
| `prompts/style_guidance.md` | 代码风格与通用实践 | ✓ |
| `prompts/code_conventions.md` | 类/方法/注释规范、命名约定 | ✓ |
| `prompts/accessibility.md` | 无障碍（TalkBack、大字号、高对比度等） | 任务时 @ |
| `prompts/animations.md` | 动画与转场（View/属性动画、Transition、MotionLayout） | 任务时 @ |
| `prompts/background_services.md` | 后台服务（前台服务、WorkManager、JobScheduler、AlarmManager） | 任务时 @ |
| `prompts/biometric.md` | 生物识别认证（BiometricPrompt、指纹/面容） | 任务时 @ |
| `prompts/crash_monitoring.md` | 崩溃监控与错误上报（Bugly、Crashlytics、Sentry 等） | 任务时 @ |
| `prompts/custom_view.md` | 自定义 View（onDraw、onMeasure、自定义属性、DataBinding 集成、无障碍） | 任务时 @ |
| `prompts/data_binding.md` | DataBinding 与状态管理 | 任务时 @ |
| `prompts/dialog_fragment.md` | DialogFragment、弹窗、结果回调 | 任务时 @ |
| `prompts/error_handling.md` | 错误处理与用户反馈（Result、UiState、异常映射） | 任务时 @ |
| `prompts/hilt_module.md` | Hilt 依赖注入模块 | 任务时 @ |
| `prompts/intent_extras.md` | Intent/Bundle 传参、Parcelable、页面间数据传递 | 任务时 @ |
| `prompts/koin_module.md` | Koin 依赖注入模块 | 任务时 @ |
| `prompts/lifecycle.md` | Lifecycle、lifecycleScope、repeatOnLifecycle | 任务时 @ |
| `prompts/logging.md` | 日志与 Debug（统一入口、级别、Release 行为） | 任务时 @ |
| `prompts/media_camera.md` | 媒体与相机（CameraX、MediaStore、ExoPlayer、图片处理） | 任务时 @ |
| `prompts/modularization.md` | 模块化与组件架构 | 任务时 @ |
| `prompts/navigation.md` | Navigation Component、Safe Args、深层链接 | 任务时 @ |
| `prompts/notifications.md` | 通知渠道与前台服务通知 | 任务时 @ |
| `prompts/paging.md` | Paging 3 分页加载 | 任务时 @ |
| `prompts/performance.md` | 性能优化（启动、内存、UI、网络、存储） | 任务时 @ |
| `prompts/permissions.md` | 运行时权限申请与 Rationale | 任务时 @ |
| `prompts/proguard.md` | ProGuard/R8 混淆与 release 构建、keep 规则 | 任务时 @ |
| `prompts/recyclerview_list.md` | RecyclerView 列表、Adapter、DiffUtil | 任务时 @ |
| `prompts/resources.md` | 资源与主题（strings、colors、dimens、themes、drawable） | 任务时 @ |
| `prompts/retrofit_api.md` | Retrofit 接口与网络层 | 任务时 @ |
| `prompts/room_schema.md` | Room 表结构与 DAO | 任务时 @ |
| `prompts/savedstate_handle.md` | SavedStateHandle、状态恢复与进程重建 | 任务时 @ |
| `prompts/screen_adaptation.md` | 屏幕适配与响应式布局 | 任务时 @ |
| `prompts/screen_databinding.md` | 新建 XML+DataBinding 页面 | 任务时 @ |
| `prompts/security.md` | 安全与最佳实践（敏感数据、权限、网络、密钥） | 任务时 @ |
| `prompts/startup.md` | Application 启动与初始化（App Startup、懒加载、顺序） | 任务时 @ |
| `prompts/storage.md` | 存储管理（DataStore、SharedPreferences、MediaStore、作用域存储） | 任务时 @ |
| `prompts/tests.md` | 单元/集成/UI/性能测试 | 任务时 @ |
| `prompts/use_case.md` | UseCase 用例层、职责与单测 | 任务时 @ |
| `prompts/webview.md` | WebView 内嵌 H5、JS 桥、安全与性能 | 任务时 @ |
| `prompts/workmanager.md` | WorkManager 后台任务 | 任务时 @ |

## 使用方式

- **日常对话**：AGENTS 默认引用 `AI_CONTEXT`、`style_guidance`、`code_conventions`
- **执行特定任务**：在对话中 `@` 引用对应 prompt，例如：
  - 新增导航/传参 → `@ai/prompts/navigation.md`
  - 数据库操作 → `@ai/prompts/room_schema.md`
  - 列表分页 Paging 3 → `@ai/prompts/paging.md`
  - 生命周期/协程作用域 → `@ai/prompts/lifecycle.md`
  - 资源与主题 → `@ai/prompts/resources.md`
  - 列表/RecyclerView → `@ai/prompts/recyclerview_list.md`
  - 写测试 → `@ai/prompts/tests.md`
  - ...

## AI 高效开发工作流程指南

工作流四阶段（研究 → 规划 → 批注 → 实现）汇总如下，细节见 [guides/README.md](./guides/README.md)、 [guides/README_CN.md](./guides/README_CN.md)。

| 阶段 | 目的 | 产出 |
|------|------|------|
| **研究 (Research)** | 在设计与编码前深入理解代码库 | `research.md` |
| **规划 (Planning)** | 将研究转化为可评审的实施计划 | `plan.md` |
| **批注 (Annotation)** | 在计划上做批注、迭代直至通过 | 更新后的 `plan.md` + 待办列表 |
| **实现 (Implementation)** | 执行已通过的计划并监督修正 | 可运行代码 |

## 角色参考文档

| 文件 | 说明 |
|------|------|
| `roles/Android Architect.md` | 完整的 Android Architect 角色参考文档，仅供人工阅读。内容与 `AGENTS.md` + `AI_CONTEXT.md` + `style_guidance.md` + `code_conventions.md` 有大量重叠，**不建议在 AI 对话中 @ 引用**（会注入冗余上下文，降低 token 效率）。如需让 AI 了解角色定义，使用默认加载的 `AGENTS.md` 即可。 |

## 规则文档

| 文件 | 用途 | 默认加载 |
|------|------|----------|
| `rules/behavior.md` | AI 助手行为规范：架构遵循、依赖管控、代码质量、文档同步等约束 | 任务时 @ |
| `rules/workflow.md` | AI 工作流程规范：复用优先原则 + 五步执行流程（需求→分析→计划→细节→交付） | 任务时 @ |
| `rules/doc_structure.md` | 文档组织规范：docs/ 目录结构、项目类型适用性、按需创建规则 | 任务时 @ |


