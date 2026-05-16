# 一刻记账（YiKe Ledger）

简洁优雅的个人记账应用，基于 Kotlin + Jetpack Compose + Material3 构建。

## 功能特性

- **流水管理** — 添加、编辑、删除收支记录，支持分类、账户、日期时间
- **报表统计** — 日/周/月/年周期统计，折线图可视化收入/支出/净额趋势
- **账户管理** — 现金、银行卡、支付宝、微信等多账户支持
- **分类管理** — 自定义收入/支出分类，支持增删改
- **深色模式** — 支持浅色/深色/跟随系统三档切换
- **Lottie 动画** — 空状态、加载态、保存成功均有精致动画反馈
- **微交互** — 卡片按压缩放、列表交错入场、底部导航选中动画

## 截图

| 流水页 | 添加交易 | 报表页 | 设置页 |
|:---:|:---:|:---:|:---:|
| 流水 | 交易 | 报表 | 设置 |

## 技术栈

- **语言**：Kotlin
- **UI**：Jetpack Compose + Material3
- **动画**：Lottie Compose + Compose Animation API
- **架构**：MVVM（ViewModel + Repository）
- **构建**：Gradle Kotlin DSL + Version Catalog

## 下载安装

从 [Releases](https://github.com/luminzon-ops/YiKeLedger/releases) 页面下载最新 APK。

## 开发环境

- Android Studio Hedgehog 及以上
- JDK 17
- Gradle 9.1
- Android SDK 34
- 最低支持 Android 8.0 (API 26)

## 构建

```bash
./gradlew assembleDebug
```

APK 输出路径：`app/build/outputs/apk/debug/app-debug.apk`

## 项目结构

```
app/src/main/java/com/yike/yikeledger/
├── data/            # 数据模型与 Repository
├── ui/
│   ├── components/  # 共享 UI 组件
│   ├── screens/     # 页面（流水、报表、设置、添加交易等）
│   ├── theme/       # 主题颜色、字体、间距
│   └── viewmodel/   # ViewModel
└── MainActivity.kt  # 入口
```

## 许可证

MIT License
