# 文档组织规范

本文件定义项目文档的目录结构与适用规则。AI 在生成或更新文档时，按此结构组织。

## 适用性说明

根据项目类型选择需要的子集，不要创建与项目无关的空文档：

| 项目类型 | 不需要的目录/文件 |
|----------|------------------|
| **前端 / 移动端 / 桌面端**（Android、iOS、Web 前端、Electron 等） | `api/`、`design/database-design.md`、`deployment/` |
| **后端 / 全栈** | 全部适用 |
| **SDK / 库** | `deployment/`、`user-guide/`，但需要 `api/` |
| **CLI / 工具** | `user-guide/`、`deployment/`，但需要 `api/`（命令行参数与配置项文档） |

## 文档目录结构

```
docs/
├── README.md                   # 文档索引和导航
├── architecture/               # 架构相关文档
│   ├── README.md               # 架构文档索引
│   ├── system-architecture.md  # 系统架构设计
│   ├── component-design.md     # 组件设计
│   └── technology-stack.md     # 技术栈说明
├── design/                     # 设计文档
│   ├── README.md               # 设计文档索引
│   ├── detailed-design.md      # 详细设计
│   ├── database-design.md      # 数据库设计（仅后端/全栈）
│   └── api-design.md           # API 设计（可选，可与 api/ 合并）
├── api/                        # API 文档（仅后端/全栈/SDK/CLI）
│   ├── README.md               # API 文档索引
│   ├── rest-api.md             # REST API 文档
│   ├── examples/               # API 示例
│   │   ├── curl-examples.md
│   │   └── code-examples.md
│   └── changelog.md            # API 变更日志
├── research/                   # AI 工作流：研究阶段产出
│   └── 001-xxx.md              # 编号命名，记录代码库研究与发现
├── plans/                      # AI 工作流：规划阶段产出
│   └── 001-xxx.md              # 编号命名，记录实施计划与任务拆解
├── development/                # 开发相关文档
│   ├── README.md               # 开发文档索引
│   ├── setup.md                # 环境搭建
│   ├── coding-standards.md     # 编码规范
│   └── troubleshooting.md      # 问题排查
├── deployment/                 # 部署相关文档（仅后端/全栈）
│   ├── README.md               # 部署文档索引
│   ├── installation.md         # 安装指南
│   ├── configuration.md        # 配置说明
│   └── operations.md           # 运维手册
├── user-guide/                 # 用户手册（按需）
│   ├── README.md
│   └── user-manual.md
└── assets/                     # 文档资源
    ├── images/                 # 图片资源
    ├── diagrams/               # 架构图、流程图
    └── examples/               # 示例文件
```

## 规则

- **按需创建**：只创建项目实际需要的文档，不要生成空白占位文件
- **索引优先**：每个子目录的 `README.md` 作为该分类的索引和导航
- **文档同步**：修改代码后，检查是否影响相关文档（架构、API、设计），如有影响务必同步更新
- **研究与计划**：`research/` 和 `plans/` 用于 AI 工作流产出（见 `ai/guides/`），编号命名作为设计决策的演进记录
