Markdown# KillerBot

KillerBot is a simple Discord moderation bot focused on anti-spam protection for media messages (images, links, attachments).  
It runs **only in one specified server** and uses Spring Boot, JDA, and Redis.

## Why Docker & Docker Compose in 2026?

In 2026, almost every production-grade or semi-serious bot/project uses containers.  
Reasons we still use Docker Compose here:

- One command (`docker compose up`) starts both the bot and Redis together — no manual Redis setup, no separate terminals.
- Consistent environment: same behavior on your laptop, VPS, or any cloud — no "it works on my machine" excuses.
- Easy environment variable management (token, server ID, log channel) without editing files every time.
- Automatic restarts if the bot crashes.
- Clean shutdown and resource cleanup with `docker compose down`.
- Zero dependency hell: no need to install Java 21, Maven/Gradle wrappers, Redis server manually on every machine.
- Even if you hate Docker, in practice it's faster and less error-prone than bare-metal deployment for this kind of project.

If you really want to run without Docker, just build the JAR and run `java -jar` with Redis running separately — but then you're on your own for setup issues.

## Why Spring Boot? (yes, we got tired of newing everything)
In short: we were lazy/tired of repeating the same setup code over and over.  
Spring Boot lets you focus on "delete spam → timeout user → log it" instead of "how do I make Redis work again".



# How I Write My .env File
**[Env Guide](Env.md)**