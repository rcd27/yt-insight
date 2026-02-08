package com.github.rcd27

import io.github.cdimascio.dotenv.dotenv

object Config {
    private val dotenv = dotenv { }

    val WRITER_AI_API_KEY: String = dotenv["WRITER_AI_API_KEY"] ?: error("No BOT_TOKEN in .env file")
}