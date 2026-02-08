package com.github.rcd27.ytinsight.writer

import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class CreateGraphTest {

    @Test
    fun createGraphTest() = runBlocking {
        val result = createGraph("GOPster Play", "created via test environment")
        assert(result.id.isNotBlank())
    }
}