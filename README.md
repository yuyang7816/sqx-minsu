<div align="center">

# **省钱兄 JAVA 酒店民宿项目全套开源系统（JAVA + Uni-app + Vue）**

**服务端 · 管理端 · 用户端**

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![JDK](https://img.shields.io/badge/JDK-1.8-orange.svg)](docs/服务端-功能说明.md)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.11-6DB33F.svg)](docs/服务端-功能说明.md)
[![Vue](https://img.shields.io/badge/Vue-3-42b883.svg)](jdms-admin/package.json)
[![uni-app](https://img.shields.io/badge/uni--app-用户端-2b9939.svg)](jdms-uniapp/manifest.json)

八年沉淀，分批次开源由 AI 辅助开发的系统源码。  
开源为生态，服务才是根基。欢迎各位开发者一同交流。

</div>

---
⭐ 如果觉得项目不错，请点击右上角 Star 收藏支持！您的 Star 是我们持续开源迭代的最大动力！

## **1. 项目简介**

**省钱兄酒店民宿项目全套开源系统**是一套面向酒店、民宿、钟点房预订与到店核销的三端系统，内置 **房源检索、在线预订、商家入驻、员工核销、会员优惠券**，采用前后端分离架构，服务端、管理端、用户端全套代码开源。

服务端基于 **Spring Boot 2.6.11**（**Java 8**）与 **MyBatis-Plus** 构建，负责接口、数据库，以及短信、对象存储、支付、地图等外部能力。  
管理端基于 **Vue 3 + Element Plus + Vite** 开发，供运营人员在电脑浏览器中维护酒店 / 民宿门店、房间、订单、会员、财务与系统设置。  
用户端基于 **uni-app（Vue 3）** 开发，覆盖 H5 与微信小程序。住客可按城市与日期查找房源、预订入住；商家与员工可在同一套用户端里入驻开店、管理房间与到店核销。

本仓库为服务端、管理端、用户端全套源码。线上 H5 演示见 [https://jdms.xianmaxiong.com](https://jdms.xianmaxiong.com)（与仓库用户端同源）。

- **官方网站开源计划**：[https://www.xiansqx.com](https://www.xiansqx.com)
- **开源计划**：[https://opensource.xiansqx.cn](https://opensource.xiansqx.cn)
- **官方文档**：[功能说明与运行教程](docs/服务端-运行教程.md)
- **Gitee 仓库**：[https://gitee.com/maxiaodong1996/sqx-minsu.git](https://gitee.com/maxiaodong1996/sqx-minsu.git)
- **GitHub 仓库**：[https://github.com/yuyang7816/sqx-minsu.git](https://github.com/yuyang7816/sqx-minsu.git)


## **2. 移动端界面预览**

截图来自本项目线上 H5 演示站 [https://jdms.xianmaxiong.com](https://jdms.xianmaxiong.com)（与 `jdms-uniapp` 构建产物一致），手机视口截取。

<div align="center">

| 首页 | 房型列表 | 我的 | 登录 |
| :---: | :---: | :---: | :---: |
| <img src="https://jdms.xianmaxiong.com/file/uploadPath/2026/11/05/ea73d806c4559528c8cb8359784a5db6.png" width="160" alt="首页" /> | <img src="https://jdms.xianmaxiong.com/file/uploadPath/2026/11/05/e0b9dbaf494eadc573c0af782a2dae58.png" width="160" alt="房型列表" /> | <img src="https://jdms.xianmaxiong.com/file/uploadPath/2026/11/05/9dfc4d79d83e375170cb32e08f5e4a38.png" width="160" alt="我的" /> | <img src="https://jdms.xianmaxiong.com/file/uploadPath/2026/11/05/639202a58894967b338fc48ca00b7268.png" width="160" alt="登录" /> |

</div>

## **3. 开源说明**

> **【省钱兄科技｜开源计划启动】**
>
> 八年沉淀，我们将分批次开源全套由 AI 辅助开发的系统源码。  
> 本项目采用 **Apache 2.0** 许可协议，源码供学习、技术研究、商业使用、二次开发与参考使用。  
> **开源为生态，服务才是根基；** 欢迎各位开发者一同交流。

## **4. 快速开始**

建议顺序：**先服务端，再管理端，最后用户端。** 管理端和用户端都要等服务端已经启动，才能登录和加载数据。

**说明：** 仓库里**不包含** `node_modules`（体积大、随系统生成）。克隆或下载后，在 **`jdms-admin`** 目录下**必须先执行一次** `npm install`，再启动开发命令。用户端可用 HBuilderX 打开，也可用命令行运行 H5；若 uView 等组件样式异常，可在 `jdms-uniapp` 目录执行一次 `npm install`。详细步骤见下方与 [docs](docs/管理端-运行教程.md) 中的运行教程。

### **4.1 服务端**

安装 **JDK 8**、**Maven**、**MySQL 8**。新建数据库 `jdms`，导入 [`jdms-server/db/jdms.sql`](jdms-server/db/jdms.sql)。

本地开发时，将 [`jdms-server/sqx-application/src/main/resources/application.yml`](jdms-server/sqx-application/src/main/resources/application.yml) 中的 `spring.profiles.active` 改为 **`dev`**，并按本机环境修改 [`application-dev.yml`](jdms-server/sqx-application/src/main/resources/application-dev.yml) 里的数据库账号密码（库名保持 `jdms`）。

在 `jdms-server` 目录执行 Maven 启动。默认 **端口 9129**，上下文路径 **`/sqx_fast`**。接口文档：`http://127.0.0.1:9129/sqx_fast/doc.html`（Knife4j）。

启动前请先开启本机 Redis（登录态与系统配置缓存会用到，默认 `localhost:6379`）。开发配置见 `application-dev.yml` 中的 `redisson` 段。

更完整的模块划分与接口约定见 [服务端功能说明](docs/服务端-功能说明.md)。

### **4.2 管理端**

安装 **Node.js 20+**（见 `jdms-admin/package.json` 的 `engines`）。进入 `jdms-admin` 目录，**首次运行前**安装依赖并启动：

```bash
cd jdms-admin
npm install
npm run dev
```

`npm install` 会根据 `package.json` / `package-lock.json` 在本机生成 `node_modules`，只需在依赖变更或新克隆后执行。

开发站点默认端口 **8888**。请将接口地址指向本机服务端（`http://127.0.0.1:9129`，路径 `/sqx_fast`）。

演示管理端账号：`admin` / `123456`（以库中数据为准）。

### **4.3 用户端**

**HBuilderX（推荐）：** 打开 `jdms-uniapp` 目录，运行到浏览器或微信开发者工具。应用面向酒店民宿预订，底部三个入口：**首页、订单、我的**。

**命令行 H5（若已配置对应脚本）：**

```bash
cd jdms-uniapp
npm install
```

开发时请确认 [`jdms-uniapp/common/config.js`](jdms-uniapp/common/config.js) 中的接口地址指向本机或已部署的服务端（线上演示为 `https://jdms.xianmaxiong.com/sqx_fast`）。

短信、微信、支付、对象存储、地图等密钥需自行申请，并在管理端相关配置页填写。

## **5. 文档**

| 想做的事 | 打开哪一份 |
|---|---|
| 把服务端跑起来 | [服务端运行教程](docs/服务端-运行教程.md) |
| 看服务端能做什么 | [服务端功能说明](docs/服务端-功能说明.md) |
| 把管理端跑起来 | [管理端运行教程](docs/管理端-运行教程.md) |
| 看管理端能做什么 | [管理端功能说明](docs/管理端-功能说明.md) |
| 把用户端跑起来 | [用户端运行教程](docs/用户端-运行教程.md) |
| 看用户端能做什么 | [用户端功能说明](docs/用户端-功能说明.md) |

## **6. 目录结构**

仓库根目录只放三端代码、说明和协议。编译出来的 `target`、`dist`、`unpackage`、`node_modules` 是运行时生成的，不需要改。

### **6.1 仓库根目录**

| 路径 | 是什么 |
|---|---|
| `jdms-server` | **服务端**。接口、数据库脚本、支付、短信、地图等能力都在这里 |
| `jdms-admin` | **管理端**。运营人员在电脑浏览器里打开的后台 |
| `jdms-uniapp` | **用户端**。住客 / 商家 / 员工用的手机应用，也可在浏览器 H5 预览 |
| `docs` | 功能说明、运行教程与 README 预览截图 |
| `LICENSE` | Apache License 2.0 全文 |

### **6.2 服务端 `jdms-server`**

| 路径 | 是什么 |
|---|---|
| `sqx-application` | 启动入口与应用配置。主程序在 `com.sqx.SqxApplication` |
| `sqx-modules/sqx-app` | C 端与业务接口：门店、房间、订单、会员、优惠券、核销、发票等 |
| `sqx-modules/sqx-system` | 系统模块：用户、配置、权限等实体与基础能力 |
| `sqx-common` | 公共组件：Web、MyBatis、Redis、短信、支付、文件、文档等 |
| `sqx-application/src/main/resources` | `application.yml` 与各环境配置 |
| `db/jdms.sql` | 要导入的数据库脚本，库名建议是 `jdms` |

### **6.3 管理端 `jdms-admin`**

| 路径 | 是什么 |
|---|---|
| `src/pages/Home` | 数据中心 / 运营看板 |
| `src/pages/dynamic` | 酒店列表、房间管理、商家相关 |
| `src/pages/orderCenter` | 订单中心 |
| `src/pages/userList` | C 端用户 |
| `src/pages/financeList` | 财务中心 |
| `src/pages/vipPrivilege` / `courseManagement` | 会员、优惠券等配置 |
| `src/pages/allocationList` | 系统配置 |
| `src/pages/Users` / `Role` / `Menu` | 管理员、角色、菜单 |
| `src/router` | 页面地址和菜单对应关系 |
| `src/api` / 请求封装 | 请求封装与工具；API 根地址见环境变量 / 代理 |

### **6.4 用户端 `jdms-uniapp`**

| 路径 | 是什么 |
|---|---|
| `pages/index` | 首页：国内 / 钟点房 / 民宿检索与热门推荐 |
| `pages/order` | 订单列表与详情（待支付、待核销、进行中等） |
| `pages/my` | 我的：会员、钱包、优惠券、商家入驻、设置等 |
| `pages/public` | 登录、注册、找回密码 |
| `subPackage` / `my` | 钱包、优惠券、发票、邀请、入住人等子包页面 |
| `common` | 接口地址、登录态、请求封装等公共逻辑 |
| `components` | 页面里重复使用的组件 |
| `static` | 图标与默认图片 |
| `pages.json` | 页面路由与底部导航（首页 / 订单 / 我的） |
| `manifest.json` | 应用名称、小程序 AppId、H5 等配置 |

界面组件使用 **uView**。住客订房、商家中心、员工核销均在用户端完成。

## **7. 核心特性与技术栈**

### **7.1 核心特性**

· **国内酒店、钟点房、民宿三类房源检索与预订**  
· **按城市 / 地标定位、入住离店日期、房型筛选与会员价**  
· **下单支付、优惠券抵扣、发票申请、到店二维码 / 条码核销**  
· **订单全流程：待支付、待核销、进行中、已完成、取消 / 售后**  
· **商家入驻审核、营业状态、房间上下架、员工管理**  
· **员工中心协助核销与退房**  
· **会员中心、钱包充值提现、邀请推广**  
· **管理端门店 / 房间 / 订单 / 财务 / 会员 / 系统配置**  
· **短信登录、微信登录、地图导航、在线客服**

业务细节见 [服务端功能说明](docs/服务端-功能说明.md) 与 Knife4j `doc.html`。

版本以仓库里的配置文件为准。没写死版本号的，按上文快速开始安装对应大版本即可。

### **7.2 服务端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 语言 | **Java** | **1.8** | 整个服务端。不能换成 11 或 17 |
| 框架 | **Spring Boot** | **2.6.11** | 提供网页接口，端口 `9129`，路径 `/sqx_fast` |
| 持久化 | **MyBatis-Plus** | **3.5.3.2** | 读写 MySQL |
| 构建 | **Maven** | 3.6 及以上 | 下载依赖、编译、启动 |
| 数据库 | **MySQL** | 8.0 及以上 | 业务数据。库名 `jdms`，脚本是 `jdms-server/db/jdms.sql` |
| 连接池 | **Druid** | 随工程 | 管理数据库连接 |
| 缓存 | **Redis / Redisson** | 必须 | 登录态、系统配置缓存等；开发默认 `localhost:6379` |
| 接口文档 | **Knife4j / springdoc** | 随工程 | 浏览器打开 `doc.html` |
| 登录验证码 | **Kaptcha** | 0.0.9 | 管理端登录页上的图片验证码 |
| 对象存储 | **阿里云 OSS** 等（可选） | 随工程 | 不填密钥时图片可存本机路径 |
| 支付 | 微信支付、支付宝等 | 随工程 | 订房、会员、充值等。密钥要自己申请 |
| 地图 | 定位 / 选点 / 导航 | 可配置 | 目的地选择与到店导航 |

### **7.3 管理端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 运行环境 | **Node.js** | **20+** | 安装依赖、启动开发网站 |
| 页面框架 | **Vue** | **3.5** | 后台每一个页面 |
| 构建 | **Vite** | **7.x** | 开发时热更新，默认端口 `8888` |
| 界面组件 | **Element Plus** | **2.x** | 表格、表单、弹窗、菜单 |
| 页面跳转 | **Vue Router** | **4.x** | 登录页、看板和各业务页的地址 |
| 状态 | **Pinia** | **3.x** | 记住登录等信息 |
| 请求 | **Axios** | **1.x** | 把操作发给服务端 |

### **7.4 用户端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 框架 | **uni-app** | Vue 3 | 同一套页面可运行到浏览器、微信小程序 |
| 开发工具 | **HBuilderX** 或 CLI | 正式版 | 编译并运行到 Chrome 或微信开发者工具 |
| 界面组件 | **uView** | 1.x | 按钮、输入框、轮播、弹窗等 |
| 状态 | 本地缓存 / Vuex 等 | 随工程 | 登录与业务状态 |
| 浏览器预览 | H5 | 以控制台为准 | 电脑上看用户端 |
| 微信小程序 | 微信开发者工具 | 可选 | 要自己的小程序 AppId 才能真机预览 |

## **8. 开源协议**

<div align="center">

本仓库使用 **Apache License 2.0**

全文见 [LICENSE](LICENSE)

</div>

## **9. 社区与支持**

运行、二次开发或商务合作，可以联系 **省钱兄科技**。

使用过程中如果接口起不来、页面打不开，先按本文 **4. 快速开始** 和 `docs` 里的运行教程排查。短信、微信、支付、对象存储、地图等密钥需要自行申请，并在管理端系统配置中填写。仍解决不了时，可通过官方渠道说明你的环境和报错。

当前项目正在测试中，如遇到 BUG，可自行修复或者留言反馈，我们将第一时间进行版本迭代。  
欢迎一起交流学习。开源是把源码开放出来，具体落地、定制和长期维护仍由服务来承接。

<div align="center">

<img src="docs/images/community-qr.jpg" alt="省钱兄科技二维码" width="220" />

**省钱兄科技**

</div>
