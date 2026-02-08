package com.github.rcd27.writer

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue

class GetGraphsTest {

    @Test
    fun `get graph test`() {
        runBlocking {
            val data: List<GraphData> = getGraphs().data
            assertTrue(data.isNotEmpty())
        }
    }
}