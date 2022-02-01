package it.unicam.cs.hyperledgerfabricwizard.utils

/**
 * L'annotazione definisce le proprietà all'interno delle [Folder]
 * il cui contenuto deve essere inserito in un file col [filename] specificato.
 *
 * Al file viene associato il path della [Folder] in cui si trova.
 *
 * @see [Folder.folderFiles]
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class ToFile(val filename: String)