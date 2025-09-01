# KillerBot

KillerBot is a **Discord moderation bot** built with **Spring Boot**, **JDA (Java Discord API)**, and **Redis**.
It is designed to run in **only one allowed server** and provides **anti-spam protection** for media messages (images & links).

## ✨ Features

* ✅ Restricts the bot to a single allowed server (leaves others automatically).
* 🚫 Detects **spam images/links** within a short time window.
* 🔇 Automatically **timeouts** (mutes) spam users for a configurable duration.
* 🧹 Deletes all recent spam media messages from the muted user.
* 📝 Logs spam events to a configurable log channel.
* ⚡ Uses **Redis** for fast spam detection with TTL-based counters.

## 🛠 Tech Stack

* **Java 21+**
* **Spring Boot**
* **JDA (Java Discord API)**
* **Redis**

## ⚙️ Configuration

Edit your `application.yml` or `application.properties` file:

```properties
spring.application.name=killer-bot
killerbot.botToken=
discord.serverid=
discord.log.channel-id=
spring.data.redis.host=
spring.data.redis.port=


```

### Environment Variables (Optional)

Instead of hardcoding values, you can also use environment variables:

```bash
export KILLERBOT_BOTTOKEN=your_token
export DISCORD_SERVERID=your_server_id
export DISCORD_LOG_CHANNELID=your_log_channel
```

## 🚀 Running the Bot

1. Clone the repository:

   ```bash
   git clone https://github.com/range79/bot-killer
   cd killerbot
   ```

2. Start Redis (if not already running):

   ```bash
   docker run -d -p 6379:6379 redis
   ```

3. Build & run with Maven/Gradle:

   ```bash
   ./gradlew bootRun
   ```


## 🙌 Contributors

For the amazing people who helped make this project possible, see the full list of [Contributors](Contributors.md).


## 🔒 Anti-Spam Logic

* Each media message (image/link) increases a counter in Redis.
* If a user exceeds **8 messages within 30 seconds**, they are muted for **10 hours**.
* The bot deletes recent spam messages from all text channels.

## 📜 License

MIT License. Free to use and modify.
