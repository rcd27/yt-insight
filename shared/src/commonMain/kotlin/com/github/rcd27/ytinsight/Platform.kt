package com.github.rcd27.ytinsight

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform