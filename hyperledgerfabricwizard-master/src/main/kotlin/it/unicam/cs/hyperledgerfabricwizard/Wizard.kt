package it.unicam.cs.hyperledgerfabricwizard

import it.unicam.cs.hyperledgerfabricwizard.client.Network
import it.unicam.cs.hyperledgerfabricwizard.fabric.folder.NetworkFolder
import it.unicam.cs.hyperledgerfabricwizard.utils.zipFiles
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.util.zip.ZipOutputStream

object Wizard {
    fun submit(network: Network): ResponseEntity<ByteArray> {
        val headers = HttpHeaders()
        headers.accessControlExposeHeaders = listOf(HttpHeaders.CONTENT_DISPOSITION)
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + network.name + ".zip")
        val zip = zip(createTempFolder(network))
        return ResponseEntity(zip, headers, HttpStatus.OK)
    }
}

fun createTempFolder(network: Network): File {
    val rootFolder = NetworkFolder(Files.createTempDirectory(network.name).toString(), network)
    rootFolder.walkTree {
        println(it)
        it.createFolder()
        it.folderFiles { file, content ->
            println(file.path)
            file.writeText(content)
        }
    }
    val fileName = rootFolder.parentPath
    var file = File( fileName)
    file.createNewFile()
    return file
    //return File(rootFolder.parentPath)
}

fun zip(directory: File): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    ZipOutputStream(BufferedOutputStream(byteArrayOutputStream)).use {
        zipFiles(it, directory, "")
    }
    return byteArrayOutputStream.toByteArray()
}
