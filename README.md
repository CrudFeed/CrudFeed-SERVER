<p align="center">
  <img width="120" height="120" alt="CrudFeed" src="https://github.com/user-attachments/assets/7607ccf0-92f6-4408-9c79-d774413c3c7a">
</p>

<h1 align="center">CrudFeed Server</h1>

<p align="center">
  Request dispatcher and orchestrator for CrudFeed ecosystem.<br>
  Handles all client connections and routes to Core services.
</p>

<br>

<p align="center">
  <a href="https://github.com/CrudFeed/CrudFeed-CORE"><img src="https://img.shields.io/badge/Dependency-CrudFeed_Core-brightgreen?style=for-the-badge&logo=git&logoColor=white" alt="Core Version"><a>
  <img src="https://img.shields.io/badge/Version-2.0-blue?style=for-the-badge&logo=git&logoColor=white" alt="CLI Version">
</p>

<br>

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white" alt="Maven">
  <img src="https://img.shields.io/badge/IntelliJ_IDEA-000000?style=flat-square&logo=intellijidea&logoColor=white" alt="IntelliJ IDEA">
  <img src="https://img.shields.io/badge/Linux-FCC624?style=flat-square&logo=linux&logoColor=black" alt="Linux">
</p>

---

## 🔌 What It Does

The Server is a stateless request dispatcher:

- **Spring Boot Server** — Exposes server endpoints and delegates business operations to `CORE`
- **Command Parsing** — Deserializes incoming requests and payloads into `CORE` DTOs
- **Service Routing** — Routes requests to `CORE` services (`Auth`, `Message`, `Mod`, `Post`, `User`)
- **Response Mapping** — Maps `CORE` results and DTOs into client-ready responses
- **Connection & Session Handling** — Manages client connections, sessions, and clean disconnects

---

## 🏗️ Architecture

```
              ┌────────┐
              │ Client │
              └───┬────┘
                  │
                  ▼
              ┌────────┐
              │ Server │
              └───┬────┘
                  │
                  ▼
            ┌─────────────┐
            │ Controllers │
            └──────┬──────┘
                   │
                   ▼
             ┌───────────┐
             │   Core    │
             └─────┬─────┘
                   │
          ┌────────┴────────┐
          ▼                 ▼
   ┌────────────┐    ┌──────────┐
   │ PostgreSQL │    │ Response │
   └────────────┘    └────┬─────┘
                          │
                          ▼
                     ┌────────┐
                     │ Client │
                     └────────┘

```

No business logic here — just orchestration and formatting.

---

## 📦 Getting Started

### Prerequisites

- Java 26+
- Maven 4.0+
- CrudFeed <a href="https://github.com/CrudFeed/CrudFeed-CORE">Core</a> library
- PostgreSQL running with CrudFeed database configured

### Build

```bash
git clone https://github.com/CrudFeed/CrudFeed-Server.git
cd CrudFeed-Server
mvn clean install
```

Run the server:

```bash
java -jar target/"name_of_java_jar_file".jar
```

---

## 📂 Module Structure

```

server/src/main/java/org/crudfeed/server/
├── mapper/                        # Serializers formatting domain data into raw strings for CLI UI builders
│   ├── AccountResponseMapper.java
│   ├── MessageResponseMapper.java
│   └── PostResponseMapper.java
└── CrudFeedServer.java            # Main class (socket acceptor & virtual thread dispatcher)

```

---

## 💡 Design Principles

- **Single Responsibility** — Spring handles routing, Mappers shape responses, Core runs domain logic and data access
- **Core Agnostic** — Server stays decoupled from internal entities and passes DTOs/data across boundaries

---

## 🚧 Status

Active development.

---

## 📄 License

This project is licensed under the GNU General Public License v3.0 — see the [LICENSE](LICENSE) file for details.
