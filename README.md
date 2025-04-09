# GoGoPet
---

## 环境说明
---

### 版本
| 内容 | 版本 | 说明 |
| --- | --- | --- |
| JDK | 17.0.14 |  |
| Java | 17 |  |
| Spring Boot | 3.3.10 |  |
| MySQL | 8.0 |  |
| Node.js |  |  |
| Vue |  |  |
| 打包 | jar |  |


### 依赖
| 内容 | 版本 | 说明 |
| --- | --- | --- |
| Spring Web |  | 用于构建 RESTful API 服务，支持前后端分离，处理 Web 请求 |
| MySQL Driver   |  | 用于连接 MySQL 数据库，确保应用能够与数据库进行通信。 |
| MyBatis Framework |  | 这个依赖项用于集成 MyBatis 框架，实现 SQL 映射和数据库交互 |
| MyBatis Spring Boot Starter |  | 这个依赖项用于简化 MyBatis 与 Spring Boot 的集成，提供了自动配置的支持，免去手动配置的麻烦 |
| Spring Boot DevTools |  | 用于开发时的热部署，能在修改代码后自动重新加载应用，提升开发效率。 |
| Lombok |  | 简化 Java 代码，自动生成常用的 getter/setter、构造函数等，减少样板代码。 |
| thymeleaf |  | Java 模板引擎，用于在 Spring Boot 中动态渲染 HTML 页面。 |


### 后端结构
`**src/main/java/com/backend/**`

+ `**config/**`：用于存放所有的配置类，比如数据库连接、消息队列配置等。
+ `**controller/**`：处理 HTTP 请求的控制器类。每个控制器通常对应一个功能模块，负责处理路由、请求参数验证等。
+ `**model/**`：定义数据库实体类，用于映射数据库表。
+ `**mapper/**`：数据访问层，存放 MyBatis 的 Mapper 接口类。每个接口通常对应一个数据库表的操作，配合 XML 文件完成 SQL 映射。
+ `**service/**`：存放业务逻辑层的代码，通常会调用 `repository` 层来处理实际的数据库操作。服务层将协调多个数据库操作以实现复杂的业务逻辑。
+ `**dto/**`：数据传输对象，用于与前端进行数据交互。DTO 类通常用于传输 API 的请求和响应数据，避免直接暴露实体类。
+ `**exception/**`：全局异常处理类。用于捕捉项目中的异常并进行统一处理。
+ `**BackendApplication.java**`：项目的启动类，包含 `main` 方法。

`**src/main/resources/**`

+ `**application.properties**`：Spring Boot 配置文件，用于设置数据库、端口等配置。
+ `**static/**`：存放静态资源（例如图片、JavaScript 文件、CSS 文件等）。
+ `**templates/**`：存放 Thymeleaf 模板文件（如果用 Thymeleaf 渲染视图的话）。
+ `**mapper/**`：存放 MyBatis 的 XML 文件。每个 Mapper XML 文件通常对应一个接口，定义 SQL 查询、插入、更新、删除等操作。

