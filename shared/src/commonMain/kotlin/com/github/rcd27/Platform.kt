package com.github.rcd27

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform