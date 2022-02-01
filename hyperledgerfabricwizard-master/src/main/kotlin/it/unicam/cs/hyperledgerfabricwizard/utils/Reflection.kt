package it.unicam.cs.hyperledgerfabricwizard.utils

import java.lang.reflect.Field

/**
 * Restituisce tutte le proprietà di una classe comprese quelle di superclassi
 */
val <T : Any> Class<T>.allFields: List<Field>
    get() {
        val fields = mutableListOf<Field>()
        var clazz: Class<*> = this
        while (clazz != Any::class.java) {
            fields.addAll(clazz.declaredFields)
            clazz = clazz.superclass
        }
        return fields
    }