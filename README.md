<table align="center">
  <tr>
    <td align="center">
      <img width="100" height="100" alt="Image" src="https://github.com/user-attachments/assets/7607ccf0-92f6-4408-9c79-d774413c3c7a" /><br>
      <sub><b>CrudFeed Server</b></sub>
    </td>
  </tr>
</table>

<br>

<p align="center">
  Request dispatcher and orchestrator for CrudFeed ecosystem.<br>
  Handles all client connections and routes to Core services.
</p>

<br>

<p align="center">
  <a href="https://github.com/CrudFeed/CrudFeed-CORE"><img src="https://img.shields.io/badge/Dependency-CrudFeed_Core-brightgreen?style=for-the-badge&logo=git&logoColor=white" alt="Core Version"><a>
  <img src="https://img.shields.io/badge/Version-1.0-blue?style=for-the-badge&logo=git&logoColor=white" alt="CLI Version">
</p>

<p align="center">
  <sub>🔌 Bridge between CLI (in the future WEB) clients and Core domain logic.</sub>
</p>

<br>

<p align="center">
  <font size="7"><b>Powered by</b></font>
</p>

<p align="center">
  <img width="40" src="https://img.shields.io/badge/%20-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java">
  <img width="40" src="https://img.shields.io/badge/%20-4169E1?style=flat-square&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img width="40" src="https://img.shields.io/badge/%20-59666C?style=flat-square&logo=hibernate&logoColor=white" alt="Hibernate">
  <img width="40" src="https://img.shields.io/badge/%20-C71A36?style=flat-square&logo=apachemaven&logoColor=white" alt="Maven">
  <img width="40" src="https://img.shields.io/badge/%20-000000?style=flat-square&logo=intellijidea&logoColor=white" alt="IDEA">
  <img width="40" src="https://img.shields.io/badge/%20-FCC624?style=flat-square&logo=linux&logoColor=black" alt="Linux">
</p>

---

## 🔌 What It Does

The Server is a stateless request dispatcher:

- **SSL/TLS Server** — Listens on port 7777, encrypts all traffic
- **Virtual Threads** — Each client connection handled by lightweight thread
- **Command Parsing** — Deserializes pipe-delimited UTF-8 commands
- **Service Routing** — Calls appropriate Core services based on command
- **Response Mapping** — Formats service results back to client-friendly format
- **Connection Management** — Handles client connect/disconnect gracefully

---

## 🏗️ Architecture

```
    CLI / (Web in the future) Client
              │
              ▼ (SSL/TLS Connection)
       CrudFeed Server (localhost:7777)
              │
      ┌───────┴───────┐
      ▼               ▼
Command Parser  Response Mappers (formats data for CLI UI)
      │
      ▼
CrudFeed Core (Services, Repositories, Entities)
      │
      ▼
  PostgreSQL

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

Server will start listening on `localhost:7777` with SSL/TLS.

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

## 🔒 Protocol

**Command Format:** `COMMAND|arg1|arg2|...`

Examples:

```

signUp|username|password
signIn|username|password
createPost|userId|postContent
writeMessage|senderId|receiverUsername|messageText
viewFeed
likePost|userId|postId
Exit

```

**Response Format:** UTF-8 strings

```

SUCCESS|data
Error! Message here

```

---

## 💡 Design Principles

- **Stateless** — Each request is independent, no session state in Server
- **Single Responsibility** — Server routes, Mappers format, Core handles logic
- **Non-Blocking** — Virtual threads handle concurrent clients efficiently
- **Secure** — SSL/TLS for all traffic, passwords never logged
- **Simple Protocol** — Pipe-delimited strings, easy to parse and test
- **Core Agnostic** — Server doesn't know about entities, just passes data

---

## 🚧 Status

Active development. Stable request routing, mapper improvements ongoing.

---

## 📄 License

Same as parent CrudFeed organization.
