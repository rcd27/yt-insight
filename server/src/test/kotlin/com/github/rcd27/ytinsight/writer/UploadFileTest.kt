package com.github.rcd27.ytinsight.writer

import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.File

class UploadFileTest {

    @Test
    fun testUploadFile(): Unit = runBlocking {
        val targetFile =
            File("/home/tochka2883/Workspace/AIAgents/youtube-insight/server/src/main/resources/wtNvLHIrhH8.ru.txt")
        val result: UploadFileResponse = uploadFile(targetFile)
        assert(result.id.isNotBlank())
    }

    @Test
    fun uploadAllFilesFromFolder() = runBlocking {
        val folder = File("/home/tochka2883/Workspace/AIAgents/youtube-insight/notebooks/UCSQGkViib9XLwjDfIahtLdw")
        folder.walkTopDown().filter { it.isFile }
            .filter { it.extension == "txt" }
            .forEach {
                uploadFile(it)
            }
    }
}