package com.github.rcd27.ytinsight.youtube.channel

fun extractChannelHint(url: String): ChannelHint {
    val clean = url
        .removePrefix("https://")
        .removePrefix("http://")
        .removePrefix("www.")
        .removePrefix("youtube.com/")
        .removePrefix("m.youtube.com/")
        .removeSuffix("/")

    val additionalClean =  when {
        clean.startsWith("channel/") -> clean.removePrefix("channel/")
        clean.startsWith("@") -> clean.removePrefix("@")
        clean.startsWith("c/") -> clean.removePrefix("c/")
        clean.startsWith("user/") -> clean.removePrefix("user/")
        else -> clean
    }

    return ChannelHint(additionalClean)
}

@JvmInline
value class ChannelHint(val value: String)