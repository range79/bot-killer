# Environment (.env) Configuration Guide

This project uses environment variables for configuration.  
Before running the bot, you must enable Developer Mode in Discord and collect required IDs.

---

## Enable Developer Mode on Discord

You must enable Developer Mode to copy IDs.

### Steps:

1. Open Discord **User Settings**
2. Go to **Advanced**
3. Enable **Developer Mode**

Official Guide:  
https://support.discord.com/hc/en-us/articles/206346498

---

## How to Get Server (Guild) ID

After enabling Developer Mode:

1. Right-click on your **Server Name**
2. Click **Copy Server ID**

More Info:  
https://www.remote.tools/remote-work/how-to-find-discord-id

---

## How to Get Channel ID

1. Right-click on a **Text Channel**
2. Click **Copy Channel ID**

More Info:  
https://www.remote.tools/remote-work/how-to-find-discord-id

---

## How to Get Bot Token

1. Open Discord Developer Portal  
   https://discord.com/developers/applications

2. Select your application
3. Go to **Bot** section
4. Click **Reset Token** (if needed)
5. Copy your **Bot Token**

Never share your token publicly.

Official Docs:  
https://discord.com/developers/docs/getting-started

---

# Example `.env` File

Create a file named `.env` in the project root.

Fill it like this:

```env
# Bot token from Discord Developer Portal
BOT_TOKEN=your_bot_token_here

# Server (Guild) ID where the bot will work
SERVER_ID=your_server_id_here

# Channel ID for logging (optional but recommended)
DISCORD_LOG_CHANNEL=your_channel_id_here

# ==============================
# Optional Settings
# ==============================

# Time interval for media messages (seconds)
# Default: 30
# MESSAGE_INTERVAL=30

# Max number of messages allowed in interval
# Default: 8
# MESSAGE_LIMIT=8

# Mute/Ban duration (minutes)
# Default: 600 (10 hours)
# BAN_MINUTES=600

# Number of images to delete when limit exceeded
# Default: 50
# IMAGE_DELETE_LIMIT=50