package it.unicam.cs.hyperledgerfabricwizard.utils

import java.io.File
import java.nio.file.Path
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible


fun emptyFolder(folder: Folder, name: String) = object : Folder(folder, name) {}

/**
 * Rappresenta una cartella che verrà creata
 */
abstract class Folder(parentPath: String, val name: String) {
    /**
     * Path della cartella superiore
     */
    val parentPath = Path.of(parentPath).toString()

    /**
     * Path completo
     */
    val path: String

    init {
        this.path = Path.of(this.parentPath, this.name).toString()
    }

    constructor(folder: Folder, name: String) : this(folder.path, name)

    fun buildPath(filename: String): String = Path.of(this.path, filename).toString()

    fun fileRelativePath(folder: Folder, filename: String) = Path.of(this.relativePathTo(folder), filename).toString()

    /**
     * Esegue la funzione [action] su ogni cartella a partire da quella che invoca il metodo
     * e le sue eventuali sotto cartelle ovvero le proprietà di tipo [Folder] marcate con le annotazioni
     * [Directory] o [Directories]
     */
    fun walkTree(action: (Folder) -> Unit) {
        action(this)
        this.javaClass.allFields.forEach { field ->
            if (field.isAnnotationPresent(Directories::class.java)) when {
                Map::class.java.isAssignableFrom(field.type) -> {
                    field.isAccessible = true
                    val map = field.get(this) as Map<*, *>
                    map.values.forEach {
                        if (it is Folder) {
                            it.walkTree(action)
                        }
                    }
                }
                Collection::class.java.isAssignableFrom(field.type) -> {
                    field.isAccessible = true
                    val list = field.get(this) as Collection<*>
                    list.forEach {
                        if (it is Folder) {
                            it.walkTree(action)
                        }
                    }
                }
            }
            else if (field.isAnnotationPresent(Directory::class.java)) {
                if (Folder::class.java.isAssignableFrom(field.type)) {
                    field.isAccessible = true
                    (field.get(this) as Folder).apply {
                        this.walkTree(action)
                    }
                }
            }
        }
    }

    /**
     * Restituisce i file di una [Folder] ovvero i membri della classe marcati con l'annotazione
     * [ToFile] e li passa come parametri alla funzione [action]
     *
     * Alla funzione viene passato il [File] da creare e il contenuto del file estrapolato come [toString] del membro su cui
     * è applicata l'annotazione.
     */
    fun folderFiles(action: (file: File, content: String) -> Unit) {
        this::class.members.forEach { member ->
            val annotation = member.findAnnotation<ToFile>()
            if (annotation != null) {
                member.isAccessible = true
                val content = (member.call(this) as Any).toString()
                val filename = annotation.filename
                action(File(buildPath(filename)), content)
            }
        }
    }

    /**
     * Restituisce il percorso relativo da una cartella ad un'altra
     * Es:
     * Da /home/luca/Scaricati a /home/luca/Documenti = ../Documenti
     * @param to cartella destinazione
     */
    fun relativePathTo(to: Folder): String {
        return this.relativePathTo(to.path)
    }
    fun relativePathTo(to: String): String {
        if (to == this.path) {
            return "."
        } else {
            val a = this.path.split("/")
            val b = to.split("/")
            if (to.contains(("^" + this.path).toRegex()))
                return "./" + b.subList(a.size, b.size).joinToString(separator = "/")
            else {
                var j = 0
                var y: List<String> = listOf()
                val x = minOf(a.size, b.size)
                for (i in 1 until x) {
                    if (a[i] == b[i]) {
                        j++
                    } else {
                        y = b.subList(i, b.size)
                        break
                    }
                }
                var string = ""
                for (z in 0 until a.size - j - 1) {
                    string += "../"
                }
                string += y.joinToString(separator = "/")
                return string
            }
        }
    }

    /**
     * Crea una cartella
     */
    fun createFolder() {
        File(this.path).mkdir()
    }

    override fun toString(): String {
        return path
    }

    fun toFile(): File {
        return File(this.path)
    }
}

