# WORDY: gRPC Multiplayer Word Game


---

## 📝 Project Description
**Wordy**  multiplayer word game designed with a robust client-server architecture using **gRPC**.

## Quick Start

1. Create and seed the MySQL schema with `src/main/java/server/db/game.sql`.
2. Set database credentials in `src/main/resources/db.properties`.
3. Build and run tests.
4. Start the server.
5. Start one or more player clients and optionally the admin client.

## Build And Test

```powershell
mvn clean test
```

## Run Server

```powershell
mvn exec:java -Dexec.mainClass="server.GrpcServer"
```

## Run Player Client

```powershell
mvn exec:java -Dexec.mainClass="client.player.PlayerApp"
```

## Run Admin Client

```powershell
mvn exec:java -Dexec.mainClass="client.admin.AdminApp"
```

## Default Accounts

- Admin: `admin / admin123`
- Players: `player1 / password1` through `player5 / password5`

---

## 🏗 System Architecture

### 1. Player Client (`/client/player`)

- MVC Swing UI.
- Uses only `PlayerServiceGrpc.PlayerServiceBlockingStub`.

### 2. Admin Client (`/client/admin`)

- MVC Swing UI.
- Uses only `AdminServiceGrpc.AdminServiceBlockingStub`.

### 3. gRPC Server (`/server`)

- Hosts `PlayerServiceImpl` and `AdminServiceImpl`.
- Runs game sessions, lobby management, and dictionary validation.


---

## 🛠 Tech Stack
* **Language:** Java
* **Communication:** gRPC / Protocol Buffers (proto3)
* **Build Tool:** Maven
* **Database:** SQL (JDBC)
* **Pattern:** MVC (Model-View-Controller)
